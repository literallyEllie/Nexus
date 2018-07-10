package de.arraying.nexus.gui.impl

import de.arraying.nexus.gui.GUI
import de.arraying.nexus.gui.slot.GUIClick
import de.arraying.nexus.gui.slot.GUISlot
import de.arraying.nexus.item.ItemBuilder
import org.bukkit.ChatColor
import org.bukkit.Material
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
@Suppress("unused")
open class PaginatedGUI @JvmOverloads constructor(
        name: String,
        private val slotCount: Int,
        private val persistentSlots: List<GUISlot>,
        pageSlots: List<GUISlot>,
        sortingAlgorithm: SortingAlgorithm = SortingAlgorithm.Implementation()
): GUI(name, slotCount) {

    private val pages = HashMap<Int, MutableList<GUISlot>>()
    private var pageNumber = 0

    init {
        var index = 0
        pages[index] = ArrayList()
        var list = sortingAlgorithm.getSlots()
        val items = pageSlots.toMutableList()
        if(list.isNotEmpty()
                && items.isNotEmpty()) {
            while(list.isNotEmpty()) {
                val slot = list.removeAt(0)
                val item = items.removeAt(0)
                item.slot = slot
                pages[index]!!.add(item)
                if(items.isEmpty()) {
                    break
                }
                if(list.isEmpty()) {
                    list = sortingAlgorithm.getSlots()
                    index++
                    pages[index] = ArrayList()
                }
            }
        }
    }

    /**
     * Populates the GUI with the slots.
     */
    override fun populate() {
        registerSlot(getPrevious())
        registerSlot(getNext())
        for(persistent in persistentSlots) {
            registerSlot(persistent)
        }
        for(entry in pages[pageNumber]!!) {
            registerSlot(entry)
        }
    }

    /**
     * Gets the slot that will go to the previous page.
     */
    open fun getPrevious() = GUISlot(
            slotCount - 9,
            ItemBuilder(Material.SIGN)
                    .name("${if(hasPrevious()) ChatColor.GREEN else ChatColor.RED}${ChatColor.BOLD}Previous")
                    .lore(if(hasPrevious()) "${ChatColor.GRAY}Goes to the prior page." else "${ChatColor.GRAY}Not available on first page.")
                    .build(),
            PageChanger(this, -1),
            PageChanger(this, -1)
    )

    /**
     * Gets the slot that will go to the next page.
     */
    open fun getNext() = GUISlot(
            slotCount - 1,
            ItemBuilder(Material.SIGN)
                    .name("${if(hasNext()) ChatColor.GREEN else ChatColor.RED}${ChatColor.BOLD}Next")
                    .lore(if(hasNext()) "${ChatColor.GRAY}Goes to the succeeding page." else "${ChatColor.GRAY}Not available on last page.")
                    .build(),
            PageChanger(this, 1),
            PageChanger(this, 1)
    )

    /**
     * Moves the current page by the specified offset.
     * Positive number is next page, negative is previous.
     */
    fun move(offset: Int) {
        assert(offset != 0)
        if((pageNumber <= 0
            && offset < 0)
            || (pageNumber >= pages.size - 1
            && offset > 0)) {
            return
        }
        pageNumber += offset
        refresh()
    }

    /**
     * Whether or not the GUI has a previous page.
     */
    private fun hasPrevious() = pageNumber > 0

    /**
     * Whether or not the GUI has a next page.
     */
    private fun hasNext() = pageNumber < pages.size - 1

    interface SortingAlgorithm {

        /**
         * Gets all slots as a list of integer.
         */
        fun getSlots(): MutableList<Int>

        class Implementation: SortingAlgorithm {

            /**
             * The slot numbers that should be occupied by the data.
             */
            override fun getSlots(): MutableList<Int> {
                return (10 until 17).toMutableList()
            }

        }

    }

    private class PageChanger(
            private val gui: PaginatedGUI,
            private val offset: Int
    ): GUIClick {

        /**
         * Changes the page.
         */
        override fun onClick(entity: Player) {
            gui.move(offset)
        }

    }
}