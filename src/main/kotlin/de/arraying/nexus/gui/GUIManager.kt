package de.arraying.nexus.gui

import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

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
object GUIManager {

    private val current = ConcurrentHashMap<Player, GUI>()

    /**
     * Opens the GUI for the specific player.
     */
    @Suppress("unused")
    fun openGUI(player: Player, gui: GUI) {
        player.openInventory(gui.createAndGet())
        current[player] = gui
    }

    /**
     * Marks that the player's GUI was closed.
     */
    fun closeGUI(player: Player) {
        current.remove(player)
    }

    /**
     * Gets the GUI for the player.
     */
    fun getGUI(player: Player) = current[player]

}