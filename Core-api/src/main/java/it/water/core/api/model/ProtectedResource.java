
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

package it.water.core.api.model;

/**
 * @Author Aristide Cittadino
 * Generic interface component for ProtectedResource.
 * This interface defines the method for gets the resource id of the protected entity of  platform.
 */
public interface ProtectedResource extends Resource {

    /**
     * Gets the resource id of the protected entity of  platform
     *
     * @return resource id of the protected entity
     */
    String getResourceId();
}
