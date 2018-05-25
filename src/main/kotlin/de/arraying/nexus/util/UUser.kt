package de.arraying.nexus.util

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.apache.commons.io.IOUtils
import org.bukkit.Bukkit
import java.io.IOException
import java.net.URL
import java.nio.charset.Charset
import java.util.*
import java.util.regex.Pattern

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
object UUser {

    private val pattern = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})")

    /**
     * Gets a UUID from player name. If the name is invalid, null will be returned.
     * Non-online players WILL cause ratelimits!
     */
    fun fromName(name: String): UUID? {
        @Suppress("DEPRECATION")
        val player = Bukkit.getPlayer(name)
        if(player != null) {
            return player.uniqueId
        }
        return try {
            val content = IOUtils.toString(URL("https://api.mojang.com/users/profiles/minecraft/" + name), Charset.forName("utf8"))
            val uuidRaw = (JsonParser().parse(content) as JsonObject).get("id").toString().replace("\"", "")
            val uuid = pattern.matcher(uuidRaw).replaceAll("$1-$2-$3-$4-$5")
            UUID.fromString(uuid)
        } catch (exception: IOException) {
            null
        } catch (exception: ClassCastException) {
            null
        }

    }

}