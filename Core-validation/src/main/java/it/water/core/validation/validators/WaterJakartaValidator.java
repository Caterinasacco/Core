
/*
 Copyright 2019-2023 ACSoftware

 Licensed under the Apache License, Version 2.0 (the "License")
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */

package it.water.core.validation.validators;

import it.water.core.api.model.Resource;
import it.water.core.api.validation.WaterValidator;
import it.water.core.model.exceptions.ValidationException;
import it.water.core.model.validation.ValidationError;
import jakarta.validation.*;
import jakarta.validation.spi.ValidationProvider;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class WaterJakartaValidator implements WaterValidator {
    private static Logger log = LoggerFactory.getLogger(WaterJakartaValidator.class);
    private Validator instance;

    public WaterJakartaValidator() {
        ValidatorFactory factory = configureValidator().buildValidatorFactory();
        instance = factory.getValidator();
    }

    public void validate(Resource entity) {
        Set<ConstraintViolation<Resource>> validationResults = this.instance.validate(entity);
        if (validationResults != null && !validationResults.isEmpty()) {
            log.debug("System Service Validation failed for entity {}: {}, errors: {}"
                    , entity.getResourceName(), entity, validationResults);
            throw new ValidationException(generateValidationError(validationResults));
        }
    }

    /**
     * Return exceptions produced during interaction with Water platform
     *
     * @param errors violations list
     * @return List of exceptions produced
     */
    public static List<ValidationError> generateValidationError(Set<ConstraintViolation<Resource>> errors) {
        List<ValidationError> vErrors = new ArrayList<>();
        Iterator<ConstraintViolation<Resource>> it = errors.iterator();
        while (it.hasNext()) {
            ConstraintViolation<Resource> violation = it.next();
            ValidationError vError = null;
            String message = violation.getMessage();
            String path = violation.getLeafBean().getClass().getSimpleName().toLowerCase() + "-" + violation.getPropertyPath().toString().toLowerCase();
            String wrongValue = (violation.getInvalidValue() != null) ? violation.getInvalidValue().toString() : "";
            vError = new ValidationError(message, path, wrongValue);
            vErrors.add(vError);
            log.debug("Add validation message: {}", violation.getMessage());
        }
        return vErrors;
    }

    private synchronized Configuration<?> configureValidator() {
        return Validation.byDefaultProvider()
                .providerResolver(new ValidationProviderResolver()).configure()
                .messageInterpolator(new ParameterMessageInterpolator());
    }

    private class ValidationProviderResolver implements jakarta.validation.ValidationProviderResolver {
        protected Logger log = LoggerFactory.getLogger(ValidationProviderResolver.class.getName());

        @Override
        public List<ValidationProvider<?>> getValidationProviders() {
            log.debug("Returning Validation provider HibernateValidator");
            return Collections.singletonList(new HibernateValidator());
        }
    }
}
