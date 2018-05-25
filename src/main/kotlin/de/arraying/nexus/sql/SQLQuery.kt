package de.arraying.nexus.sql

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

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
class SQLQuery(
        private val sql: SQLHandler,
        private val query: String
) {

    /**
     * Executes the query.
     */
    fun execute(vararg data: Any): Boolean {
        return prepare({
            it.execute()
        }, *data)
    }

    /**
     * Fetches the query results.
     */
    fun fetch(consumer: (ResultSet) -> Unit, vararg data: Any): Boolean {
        return prepare({
            val result = it.executeQuery()
            result?: return@prepare
            consumer(result)
        }, *data)
    }

    /**
     * Prepares the statement.
     */
    private fun prepare(consumer: (PreparedStatement) -> Unit, vararg data: Any): Boolean {
        return try {
            val statement = sql.pool.connection.prepareStatement(query)
            for(i in 0..(data.size - 1)) {
                statement.setObject(i + 1, data[i])
            }
            consumer(statement)
            statement.connection.close()
            true
        } catch(exception: SQLException) {
            exception.printStackTrace()
            false
        }
    }

}