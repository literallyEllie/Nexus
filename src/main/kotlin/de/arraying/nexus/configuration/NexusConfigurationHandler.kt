package de.arraying.nexus.configuration

import de.arraying.nexus.Nexus
import de.arraying.nexus.NexusInstance
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
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
@Suppress("unused", "MemberVisibilityCanBePrivate")
object NexusConfigurationHandler {

    private const val configDefault = "config"

    /**
     * Loads a configuration file, and maps it to an object.
     */
    @JvmOverloads
    fun <T> loadConfig(clazz: Class<T>, name: String = configDefault): T? {
        if(clazz.constructors.all {
            it.parameterCount != 0
        }) {
            return null
        }
        val info = getConfig(name)
        val new = clazz.newInstance()
        if(info.created) {
            saveConfig(new, clazz, name)
        } else {
            handleConfigFields(clazz, {
                path, field -> field.isAccessible = true
                if(info.config[path] != null) {
                    field.set(new, info.config[path])
                }
            })
        }
        return new
    }

    /**
     * Saves the configuration object to file.
     */
    @JvmOverloads
    fun <T> saveConfig(instance: T, clazz: Class<T>, name: String = configDefault) {
        val info = getConfig(name)
        handleConfigFields(clazz, {
            path, field -> field.isAccessible = true; info.config[path] = field.get(instance)
        })
        info.config.save(info.file)
    }

    /**
     * Handles all configuration fields.
     * This gets all specified fields and executes the higher order function for each field.
     */
    private fun handleConfigFields(clazz: Class<*>, consumer: (path: String, field: Field) -> Unit) {
        for(field in clazz.declaredFields) {
            if(field.isAnnotationPresent(NexusConfigurationField::class.java)) {
                val annotation = field.getAnnotation(NexusConfigurationField::class.java)
                consumer(annotation.path, field)
            }
        }
    }

    /**
     * Gets the appropriate configuration object.
     */
    private fun getConfig(name: String) =
            if(name == configDefault) Information(NexusInstance.get().config, File(NexusInstance.get().dataFolder, "config.yml"), false) else {
                val file = File(NexusInstance.get().dataFolder, "$name.yml")
                val created = if(file.createNewFile()) {
                        Nexus.log.info("Created the configuration file $name.yml.")
                        true
                    } else {
                        false
                    }
                Information(YamlConfiguration.loadConfiguration(file), file, created)
            }

    private class Information(
            val config: FileConfiguration,
            val file: File,
            val created: Boolean
    )

}