package de.arraying.nexus;

/**
 * Copyright 2018 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class NexusInstance {

    private static Nexus instance;

    /**
     * Sets the Nexus instance.
     * @param instance The instance.
     */
    public static void setInstance(Nexus instance) {
        NexusInstance.instance = instance;
    }

    /**
     * Gets the instance of the nexus plugin.
     * @return The instance.
     */
    public static Nexus get() {
        return instance;
    }

}
