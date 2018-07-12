package de.arraying.nexus.item

import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

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
class ItemBuilder(
        material: Material
) {

    private val item = ItemStack(material)
    private val meta = item.itemMeta

    /**
     * Sets the amount.
     */
    fun amount(amount: Int): ItemBuilder {
        item.amount = amount
        return this
    }

    /**
     * Sets the name of the item.
     */
    fun name(name: String): ItemBuilder {
        meta.displayName = name
        return this
    }

    /**
     * Sets the lore of the item.
     */
    fun lore(vararg lines: String): ItemBuilder {
        val lore = ArrayList<String>()
        lines.mapTo(lore) { "${ChatColor.GRAY}$it" }
        meta.lore = lore
        return this
    }

    /**
     * Enchants the item.
     */
    @JvmOverloads
    fun enchant(enchant: Enchantment, level: Int = 1): ItemBuilder {
        item.addUnsafeEnchantment(enchant, if (level < 1) 1 else level)
        return this
    }

    /**
     * Sets the colour of the armour.
     */
    fun colour(colour: Color): ItemBuilder {
        if (meta is LeatherArmorMeta) {
            meta.color = colour
        }
        return this
    }

    /**
     * Colours the item with a dye color.
     */
    fun colour(color: DyeColor): ItemBuilder {
        if (item.type == Material.WOOL || item.type.name.contains("GLASS")) {
            item.durability = color.woolData.toShort()
        } else if (item.type == Material.INK_SACK) {
            item.durability = color.dyeData.toShort()
        }
        return this
    }

    /**
     * Sets the item to a head.
     */
    fun head(owner: String): ItemBuilder {
        if (item.type == Material.SKULL_ITEM) {
            item.durability = 3
            (meta as SkullMeta).owner = owner
        }
        return this
    }

    /**
     * Builds the item.
     */
    fun build(): ItemStack {
        item.itemMeta = meta
        return item
    }

}