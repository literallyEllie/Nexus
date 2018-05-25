package de.arraying.nexus.util

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

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
object UEconomy {

    val economy: Economy? = if(Bukkit.getPluginManager().isPluginEnabled("Vault")) Bukkit.getServicesManager().getRegistration(Economy::class.java).provider else null

    /**
     * Gets the balance of a player.
     */
    fun get(player: OfflinePlayer) = economy?.getBalance(player)?: 0

    /**
     * Gives a player money.
     */
    fun give(player: OfflinePlayer, amount: Double) = economy?.depositPlayer(player, amount)

    /**
     * Takes money from a player.
     */
    fun take(player: OfflinePlayer, amount: Double) = economy?.withdrawPlayer(player, amount)

}