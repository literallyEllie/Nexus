package de.arraying.nexus.sql.table

import de.arraying.nexus.sql.SQLHandler
import de.arraying.nexus.sql.SQLQuery

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
class SQLTable @JvmOverloads constructor(
        val name: String,
        val entity: Class<*>? = null
) {

    private val columns = ArrayList<SQLColumn>()

    /**
     * Adds a SQL column to the table.
     */
    @Suppress("unused")
    fun column(column: SQLColumn): SQLTable {
        columns.add(column)
        return this
    }

    /**
     * Generates the table and any other applicable tables.
     */
    fun generate(sql: SQLHandler): Boolean {
        var query = "CREATE TABLE IF NOT EXISTS `$name`("
        for(i in 0..(columns.size - 1)) {
            query += columns[i]
            query += if(i + 1 != columns.size) {
                    ",\n"
                } else {
                    "\n"
                }
        }
        query += ");"
        val result = SQLQuery(sql, query).execute()
        if(result) {
            sql.tables[name] = this
        }
        return result
    }

    enum class Data(val identifier: String) {

        /**
         * The ID of the data.
         */
        ID("id"),

    }

}