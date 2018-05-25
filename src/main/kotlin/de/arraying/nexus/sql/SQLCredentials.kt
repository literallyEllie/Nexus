package de.arraying.nexus.sql

import de.arraying.nexus.configuration.NexusConfigurationField

/**
 * Copyright 2018 Arraying
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class SQLCredentials {

    @NexusConfigurationField("host") private val host = "localhost"
    @NexusConfigurationField("port") private val port = 3306
    @NexusConfigurationField("database") private val database = ""
    @NexusConfigurationField("username") val username = ""
    @NexusConfigurationField("password") val password = ""

    /**
     * Whether or not the SQL credentials are valid.
     */
    fun isValid() = host.isNotEmpty() && database.isNotEmpty() && username.isNotEmpty()

    /**
     * Converts the databse to JDBC format.
     */
    fun toJDBC() = "jdbc:mysql://$host:$port/$database"

}