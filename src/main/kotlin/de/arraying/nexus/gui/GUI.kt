package de.arraying.nexus.gui

import de.arraying.nexus.Nexus
import de.arraying.nexus.gui.slot.GUISlot
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory

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
abstract class GUI(
        val name: String,
        slotCount: Int
) {

    val slots = ArrayList<GUISlot>()
    private val inventory = Bukkit.createInventory(null, slotCount, name)

    /**
     * Loads all GUI slots.
     */
    abstract fun populate()

    /**
     * Creates the GUI and returns the inventory.
     */
    fun createAndGet(): Inventory {
        populate()
        return inventory
    }

    /**
     * Refreshes the GUI.
     */
    fun refresh() {
        slots.clear()
        inventory.clear()
        populate()
    }

    /**
     * Registers a GUI slot.
     */
    fun registerSlot(slot: GUISlot) {
        if(slot !is GUISlot.Empty) {
            slots.add(slot)
            inventory.setItem(slot.slot, slot.item)
        }
    }

}