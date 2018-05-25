package de.arraying.nexus.gui

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

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
class GUIListener: Listener {

    private val sound = Sound.values().find { it.name == "CLICK" || it.name == "UI_BUTTON_CLICK" }

    /**
     * When a player clicks in an inventory.
     */
    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as Player??: return
        val gui = GUIManager.getGUI(player)?: return
        if(event.inventory.name == gui.name
                && event.currentItem != null
                && event.currentItem.type != Material.AIR) {
            event.isCancelled = true
            val slot = gui.slots.find { it.slot == event.slot }?: return
            when(event.click) {
                ClickType.LEFT -> slot.leftClick.onClick(player)
                ClickType.RIGHT -> slot.rightClick.onClick(player)
                else -> {}
            }
            if(sound != null) {
                player.playSound(player.location, sound, 30f, 1f)
            }
        }
    }

    /**
     * When the player closes an inventory.
     */
    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        val player = event.player as Player??: return
        val inventory = event.inventory
        if(inventory.name == GUIManager.getGUI(player)?.name) {
            GUIManager.closeGUI(event.player as Player)
        }
    }

}