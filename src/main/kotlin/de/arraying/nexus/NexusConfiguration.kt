package de.arraying.nexus

import de.arraying.nexus.command.NexusCommand
import de.arraying.nexus.command.NexusCommandLocalization
import de.arraying.nexus.sql.SQLConfiguration
import org.bukkit.event.Listener

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
class NexusConfiguration {

    /**
     * The locale of the commands.
     */
    var locale = NexusCommandLocalization(
            "",
            "You must be a player in-game to execute this command.",
            "This command can only be executed by the console.",
            "You do not have the required permission to execute this command.",
            "A sub-command with that name does not exist."
    )

    /**
     * Whether or not SQL should be initialized.
     */
    internal var sql = false

    /**
     * The SQL configuration.
     */
    var sqlConfiguration = SQLConfiguration()
    set(value) {
        field = value
        sql = true
    }

    /**
     * A list of commands to register.
     */
    val commands = ArrayList<NexusCommand>()

    /**
     * A list of listeners to register.
     */
    val listeners = ArrayList<Listener>()

}