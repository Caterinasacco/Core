
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

package it.water.core.api.model;

import it.water.core.api.security.Authenticable;

/**
 * @Author Aristide Cittadino.
 * Interface for defining the concept of user.
 */
public interface User extends Authenticable {
    /**
     * Identifies an user uniquely
     * @return
     */
    long getId();

    /**
     * Gets the User name
     *
     * @return a string that represents User name
     */
    String getName();

    /**
     * Gets the HUser lastname
     *
     * @return a string that represents User lastname
     */
    String getLastname();

    /**
     * Gets the HUser email
     *
     * @return a string that represents User email
     */
    String getEmail();

    /**
     * Gets the HUser username
     *
     * @return a string that represents User username
     */
    String getUsername();


    /**
     * Gets if User is administrator
     *
     * @return true if User is administrator
     */
    boolean isAdmin();

    /**
     *
     * @return
     */
    @Override
    default String getScreenName() {
        return this.getUsername();
    }

    /**
     *
     * @return
     */
    @Override
    default String getScreenNameFieldName() {
        return "username";
    }

    /**
     *
     * @return
     */
    @Override
    default boolean isActive() {
        return false;
    }
}
