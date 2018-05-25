package de.arraying.nexus.gui.slot

import de.arraying.nexus.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

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
open class GUISlot @JvmOverloads constructor(
        open var slot: Int,
        open val item: ItemStack,
        open val leftClick: GUIClick = GUIClick.Implementation(),
        open val rightClick: GUIClick = GUIClick.Implementation()
) {

    class Empty(slot: Int): GUISlot(slot, ItemBuilder(Material.AIR).build())

}