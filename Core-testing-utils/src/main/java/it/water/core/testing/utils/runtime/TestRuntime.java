/*
 * Copyright 2024 Aristide Cittadino
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.water.core.testing.utils.runtime;

import it.water.core.api.bundle.Runtime;
import it.water.core.api.model.User;
import it.water.core.api.permission.SecurityContext;
import it.water.core.bundle.WaterRuntime;
import it.water.core.interceptors.annotations.FrameworkComponent;
import it.water.core.model.exceptions.ValidationException;
import it.water.core.model.validation.ValidationError;
import it.water.core.testing.utils.bundle.TestRuntimeInitializer;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * @Author Aristide Cittadino
 * During the test phase thread local may fail to associate the right security context to requests.
 * With this componente used onlu for test purpose we should overcome this problem.
 * This component is useful specially when tests are executed outside the junit thread for example karate.
 */
@FrameworkComponent(priority = 1, services = {Runtime.class})
public class TestRuntime extends WaterRuntime implements Runtime {
    private SecurityContext securityContext;

    public TestRuntime() {
        //Filling security context with admin default, so rest test can easily integrate
        this.fillSecurityContext(new SecurityContext() {
            @Override
            public String getLoggedUsername() {
                return "admin";
            }

            @Override
            public boolean isLoggedIn() {
                return true;
            }

            @Override
            public boolean isAdmin() {
                return true;
            }

            @Override
            public long getLoggedEntityId() {
                return 0;
            }
        });
    }

    @Override
    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    @Override
    public void fillSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    /**
     * Launches one operation and checks validation exception
     *
     * @param r
     * @param invalidFields
     */
    public static void assertValidationException(Runnable r, String... invalidFields) {
        List<ValidationError> errors = null;
        try {
            r.run();
        } catch (ValidationException e) {
            errors = e.getViolations();
        }
        //checking wether the expected field is contained in the violation message
        Assertions.assertTrue(errors != null && errors.stream().anyMatch(violation -> Arrays.stream(invalidFields).anyMatch(invalidField -> violation.getField().endsWith(invalidField))));
    }

    /**
     * Perform an action impersonating a specific user
     *
     * @param user
     * @param operation
     */
    public static void runAs(User user, Runnable operation) {
        Runtime runtime = TestRuntimeInitializer.getInstance().getComponentRegistry().findComponent(Runtime.class, null);
        TestRuntimeInitializer.getInstance().impersonate(user, runtime);
        operation.run();
        runtime.fillSecurityContext(null);
    }

    /**
     * Invoke a supplier function with specific user
     *
     * @param user
     * @param supplier
     * @param <R>
     * @return
     */
    public static <R> R getAs(User user, Supplier<R> supplier) {
        Runtime runtime = TestRuntimeInitializer.getInstance().getComponentRegistry().findComponent(Runtime.class, null);
        TestRuntimeInitializer.getInstance().impersonate(user, runtime);
        R returnValue = supplier.get();
        runtime.fillSecurityContext(null);
        return returnValue;
    }

}
