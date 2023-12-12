
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

package it.water.core.api.interceptors;

import it.water.core.api.service.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;


/**
 * @param <A> Annotation
 *            Used for defining annotation for pre-processing fields before a method invocation
 * @Author Aristide Cittadino
 */
public interface BeforeMethodFieldInterceptor<A extends Annotation> extends BeforeMethodInterceptor<A> {
    /**
     * @param destination      Service which is going to be invoked
     * @param m               Method
     * @param annotatedFields fields annotated with annotation param
     * @param args            Method arguments
     * @param annotation      Annotation processed on the method which maps the Interceptor definition
     * @param <S>              Service Type
     */
    <S extends Service> void interceptMethod(S destination, Method m, List<Field> annotatedFields, Object[] args, A annotation);

    @Override
    default <S extends Service> void interceptMethod(S destination, Method m, Object[] args, A annotation) {
        throw new UnsupportedOperationException();
    }
}
