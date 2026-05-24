/*
 * This file is part of FastInv, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 MrMicky
 * Contributors: Sniper_TVmc (adaptation for EssentialsX-GUI)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package fr.snipertvmc.essentialsxgui.libraries.fastinv;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

/**
 * Class to create inventory schemes for FastInv.
 *
 * @author MrMicky (original), Sniper_TVmc (adaptation for EssentialsX-GUI)
 */
public class InventoryScheme {

    private final List<String> masks = new ArrayList<>();
    private final Map<Character, ItemStack> items = new HashMap<>();
    private final Map<Character, Consumer<InventoryClickEvent>> handlers = new HashMap<>();
    private char paginationChar;

    /**
     * Add a mask to this scheme including all sorts of characters.
     * For example, "110101011"
     *
     * @param mask a 9-character mask
     * @return this scheme instance
     */
    public InventoryScheme mask(String mask) {
        Objects.requireNonNull(mask);
        this.masks.add(mask.length() > 9 ? mask.substring(0, 10) : mask);

        return this;
    }

    /**
     * Add multiple masks to this scheme, including all sorts of characters.
     * For example, "111111111", "110101011", "111111111"
     *
     * @param masks multiple 9-character masks
     * @return this scheme instance
     */
    public InventoryScheme masks(String... masks) {
        for (String mask : Objects.requireNonNull(masks)) {
            mask(mask);
        }
        return this;
    }

    /**
     * Bind character to the corresponding item in the inventory.
     *
     * @param character the associated character in the mask
     * @param item      the item to use for this character
     * @param handler   consumer for the item
     * @return this scheme instance
     */
    public InventoryScheme bindItem(char character, ItemStack item, Consumer<InventoryClickEvent> handler) {
        this.items.put(character, Objects.requireNonNull(item));

        if (handler != null) {
            this.handlers.put(character, handler);
        }
        return this;
    }

    /**
     * Bind character to the corresponding item in the inventory.
     *
     * @param character the associated character in the mask
     * @param item      the item to use for this character
     * @return this scheme instance
     */
    public InventoryScheme bindItem(char character, ItemStack item) {
        return this.bindItem(character, item, null);
    }

    /**
     * Bind character for pagination content.
     *
     * @param character the associated character in the mask
     * @return this scheme instance
     */
    public InventoryScheme bindPagination(char character) {
        this.paginationChar = character;
        return this;
    }

    /**
     * Unbind any item from this character.
     *
     * @param character the character to unbind
     * @return this scheme instance
     */
    public InventoryScheme unbindItem(char character) {
        this.items.remove(character);
        this.handlers.remove(character);
        return this;
    }

    /**
     * Apply the current inventory scheme to the FastInv instance.
     *
     * @param inv the FastInv instance to apply this scheme to
     */
    public void apply(FastInv inv) {
        List<Integer> paginationSlots = new ArrayList<>();

        for (int line = 0; line < this.masks.size(); line++) {
            String mask = this.masks.get(line);

            for (int slot = 0; slot < mask.length(); slot++) {
                char c = mask.charAt(slot);

                if (c == this.paginationChar) {
                    paginationSlots.add(9 * line + slot);
                    continue;
                }

                ItemStack item = this.items.get(c);
                Consumer<InventoryClickEvent> handler = this.handlers.get(c);

                if (item != null) {
                    inv.setItem(9 * line + slot, item, handler);
                }
            }
        }

        if (inv instanceof PaginatedFastInv && !paginationSlots.isEmpty()) {
            ((PaginatedFastInv) inv).setContentSlots(paginationSlots);
        }
    }
}
