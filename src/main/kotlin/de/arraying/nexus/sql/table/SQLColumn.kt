package de.arraying.nexus.sql.table

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
class SQLColumn @JvmOverloads constructor(
        private val name: String,
        private val type: Type,
        private var length: Int? = null,
        private val notNull: Boolean = false,
        private val primary: Boolean = false
) {

    /**
     * Initializes the column
     */
    init {
        if(type == Type.STRING
            && length == null) {
            length = 256
        }
    }

    /**
     * Converts the column into a string.
     */
    override fun toString(): String {
        return "`$name` ${type.identifier}${if(type == Type.STRING) if(length != null) "($length)" else "${256}" else ""} ${if(notNull) "NOT NULL" else ""} ${if(primary) "PRIMARY KEY" else ""}"
    }

    enum class Type(val identifier: String) {

        /**
         * A string.
         */
        STRING("VARCHAR"),

        /**
         * An integer.
         */
        INT("INT"),

        /**
         * A long.
         */
        BIGINT("BIGINT"),

        /**
         * A boolean.
         */
        BOOL("BOOL")

    }

}