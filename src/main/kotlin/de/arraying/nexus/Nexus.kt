package de.arraying.nexus

import de.arraying.nexus.configuration.NexusConfigurationHandler
import de.arraying.nexus.gui.GUIListener
import de.arraying.nexus.sql.SQLCredentials
import de.arraying.nexus.sql.SQLHandler
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import java.util.logging.Logger

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
abstract class Nexus: JavaPlugin() {

    lateinit var sql: SQLHandler
    lateinit var configuration: NexusConfiguration

    /**
     * Passes in a configuration that can be tweaked.
     */
    abstract fun configure(configuration: NexusConfiguration?)

    /**
     * What happens when the plugin starts.
     */
    open fun onStartup() {}

    /**
     * What happens when the plugin shuts down.
     */
    open fun onShutdown() {}

    /**
     * Enables the plugin.
     */
    override fun onEnable() {
        NexusInstance.setInstance(this)
        log = logger
        configuration = NexusConfiguration()
        configure(configuration)
        log.info("Preparing IO...")
        try {
            if(dataFolder.mkdirs()) {
                log.info("Created data folder.")
            }
        } catch(io: IOException) {
            log.severe("IO exception creating plugin folder.")
            return
        }
        log.info("Registering ${configuration.commands.size} commands...")
        try {
            val field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
            field.isAccessible = true
            val commandMap = field.get(Bukkit.getServer()) as CommandMap
            commandMap.registerAll(name, configuration.commands as List<Command>?)
        } catch(nsfe: NoSuchFieldException) {
            log.severe("Could not access the command map; no such field.")
        } catch(iare: IllegalArgumentException) {
            log.severe("Illegal argument exception while reflecting.")
        } catch(iace: IllegalAccessException) {
            log.severe("Illegal access exception while reflecting.")
        } catch(npe: NullPointerException) {
            log.severe("Null pointer exception while reflecting.")
        }
        val pluginManager = Bukkit.getPluginManager()
        configuration.listeners.add(GUIListener())
        for(listener in configuration.listeners) {
            pluginManager.registerEvents(listener, this)
        }
        if(configuration.sql) {
            log.info("SQL has been specified to be used, setting up...")
            val credentials = NexusConfigurationHandler.loadConfig(SQLCredentials::class.java, "database")
            if(credentials == null
                    || !credentials.isValid()) {
                log.severe("The SQL credentials are invalid or could not be loaded, disabling plugin.")
                Bukkit.getPluginManager().disablePlugin(this)
                return
            }
            val sql = SQLHandler(configuration.sqlConfiguration, credentials)
            if(!sql.init()) {
                log.severe("An SQL error occurred, disabling plugin.")
                Bukkit.getPluginManager().disablePlugin(this)
                return
            }
            this.sql = sql
        }
        onStartup()
        super.onEnable()
    }

    /**
     * Disables the plugin.
     */
    override fun onDisable() {
        try {
            sql.pool.shutdown()
        } catch(ignored: UninitializedPropertyAccessException) {}
        onShutdown()
        super.onDisable()
    }

    companion object {

        lateinit var log: Logger

    }

}