package de.arraying.nexus.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

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
abstract class NexusCommand @JvmOverloads constructor(
        name: String,
        private val perm: String,
        private val target: Target = Target.BOTH,
        private val subCommands: List<NexusCommand> = listOf(),
        vararg aliases: String
): Command(name, "", "", listOf(*aliases)) {

    internal lateinit var locale: NexusCommandLocalization

    /**
     * The method that gets invoked when the command is executed.
     */
    abstract fun onCommand(context: NexusCommandContext)

    /**
     * Executes the command, and does any required preprocessing.
     */
    override fun execute(sender: CommandSender?, commandLabel: String?, args: Array<out String>?): Boolean {
        if(sender == null
                || args == null) {
            throw IllegalStateException("Sender or args are null.")
        }
        val context = NexusCommandContext(locale, sender, args)
        if(target == Target.PLAYER && sender !is Player) {
            context.reply(locale.mustBePlayer)
            return true
        }
        if(target == Target.CONSOLE && sender !is ConsoleCommandSender) {
            context.reply(locale.mustBeConsole)
            return true
        }
        if(perm.isNotEmpty() && !sender.hasPermission(perm)) {
            context.reply(locale.noPermission)
            return true
        }
        if(subCommands.isNotEmpty()
                && args.isNotEmpty()) {
            val command = args[0].toLowerCase()
            val subCommand = subCommands.firstOrNull { it.name == command || it.aliases.contains(command) }
            subCommand?.execute(sender, commandLabel, if(args.size == 1) emptyArray() else args.copyOfRange(1, args.size-1))?:
                    context.reply(locale.noSubCommand)
            return true
        }
        onCommand(context)
        return true
    }

    /**
     * Tab completes the command.
     * Returns null to signal no completion.
     */
    override fun tabComplete(sender: CommandSender?, alias: String?, args: Array<out String>?): MutableList<String>? {
        return null
    }

    /**
     * The target executor for the specified command.
     */
    enum class Target {

        /**
         * The target for this command is a player.
         */
        PLAYER,

        /**
         * The target for this command is a console.
         */
        CONSOLE,

        /**
         * The target for this command can be a player or a console.
         */
        BOTH

    }

}