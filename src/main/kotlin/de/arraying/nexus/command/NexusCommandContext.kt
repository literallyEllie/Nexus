package de.arraying.nexus.command

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

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
@Suppress("unused", "MemberVisibilityCanPrivate")
class NexusCommandContext internal constructor(
        private val locale: NexusCommandLocalization,
        val sender: CommandSender,
        val args: Array<out String>
) {

    /**
     * Replies to the command sender.
     */
    @JvmOverloads
    fun reply(message: String, prefix: Boolean = true) {
        sender.sendMessage("${if(prefix) locale.prefix else ""}${ChatColor.GRAY}$message")
    }

}