package de.arraying.nexus.sql

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.pool.HikariPool
import de.arraying.nexus.sql.table.SQLTable

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
class SQLHandler(
        private val config: SQLConfiguration,
        private val credentials: SQLCredentials
) {

    lateinit var pool: HikariPool
    val tables = HashMap<String, SQLTable>()

    fun init(): Boolean {
        val config = HikariConfig()
        config.poolName = "nexus_cp"
        config.leakDetectionThreshold = 3000
        config.idleTimeout = 20000
        config.minimumIdle = 2
        config.maximumPoolSize = 5
        config.jdbcUrl = credentials.toJDBC()
        config.username = credentials.username
        if(credentials.password.isNotEmpty()) {
            config.password = credentials.password
        }
        this.config.preprocess(config)
        pool = HikariPool(config)
        return this.config.tables.all {
            it.generate(this)
        }
    }

}