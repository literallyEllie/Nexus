package de.arraying.nexus.sql.entity

import de.arraying.nexus.Nexus
import de.arraying.nexus.sql.SQLQuery
import de.arraying.nexus.sql.table.SQLTable
import java.lang.reflect.Field

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
@Suppress("unused")
abstract class SQLEntity @JvmOverloads constructor(
        nexus: Nexus,
        private val id: String,
        private val idColumn: String = SQLTable.Data.ID.identifier
) {

    private val log = nexus.logger
    private val sql = nexus.sql
    private val table: String?

    /**
     * Initializes the table.
     */
    init {
        val coreTables = sql.tables.values.filter {
            it.entity == this.javaClass
        }
        val core = coreTables.firstOrNull()
        table = if(core == null) {
                log.severe("Could not find corresponding table for entity $javaClass")
                null
            } else {
                core.name
            }
    }

    /**
     * Loads the data.
     */
    fun load(): SQLEntityStatus {
        table?: return SQLEntityStatus.ERROR
        var loaded = false
        if(!SQLQuery(sql, "SELECT * FROM `$table` WHERE `$idColumn` = ?;").fetch({
            if(it.next()) {
                getFields { column, field -> field.isAccessible = true; field.set(this, it.getObject(column))
                }
                loaded = true
            } else {
                loaded = false
            }
        }, id)) {
            log.severe("Could not fetch core data for entity $javaClass")
            return SQLEntityStatus.ERROR
        }
        return if(loaded) SQLEntityStatus.LOADED else SQLEntityStatus.NONE
    }

    /**
     * Saves the data.
     */
    fun save() {
        table?: return
        val queryBuilder = StringBuilder("UPDATE `$table` SET")
        val values = ArrayList<Any?>()
        getFields {
            column, field -> field.isAccessible = true; values.add(field[this]); queryBuilder.append(" `$column`=?,")
        }
        var query = queryBuilder.toString()
        query = if(query.endsWith(",")) query.substring(0, query.lastIndex) else query
        query += " WHERE `$idColumn` = ?;"
        values.add(id)
        if(!SQLQuery(sql, query).execute(*values.toArray())) {
            log.severe("Could not execute update for entity $javaClass")
        }
    }

    /**
     * Creates the data.
     */
    fun create() {
        table?: return
        val queryBuilder = StringBuilder("INSERT INTO `$table` VALUES(?,")
        val values = ArrayList<Any?>()
        values.add(id)
        getFields {
            _, field -> field.isAccessible = true; values.add(field[this]); queryBuilder.append(" ?,")
        }
        var query = queryBuilder.toString()
        query = if(query.endsWith(",")) query.substring(0, query.lastIndex) else query
        query += ");"
        if(!SQLQuery(sql, query).execute(*values.toArray())) {
            log.severe("Could not create for entity $javaClass")
        }
    }

    /**
     * Gets all fields, and takes in a consumer to handle them.
     */
    private fun getFields(consumer: (column: String, field: Field) -> Unit) {
        for(field in javaClass.declaredFields) {
            if(field.isAnnotationPresent(SQLEntityField::class.java)) {
                val annotation = field.getAnnotation(SQLEntityField::class.java)
                consumer(annotation.column, field)
            }
        }
    }

}