
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

package it.water.core.api.registry.filter;

public interface FilterImplementation {
    default String transform(ComponentFilter filter) {
        if (filter instanceof ComponentFilterAndCondition filterAndCondition)
            return transform(filterAndCondition);
        else if (filter instanceof ComponentFilterOrCondition filterOrCondition)
            return transform(filterOrCondition);
        else if (filter instanceof ComponentPropertyFilter componentPropertyFilter)
            return transform(componentPropertyFilter);
        else
            throw new IllegalArgumentException("Invalid filter");
    }

    String transform(ComponentFilterAndCondition andCondition);

    String transform(ComponentFilterOrCondition orCondition);

    String transform(ComponentPropertyFilter propertyFilter);
}
