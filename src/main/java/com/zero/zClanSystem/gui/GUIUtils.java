package com.zero.zClanSystem.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GUIUtils {

    public static ItemStack item(Material mat, String name, String... lore) {
        ItemStack i = new ItemStack(mat);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(name);

        if (lore != null && lore.length > 0) {
            m.setLore(List.of(lore));
        }

        i.setItemMeta(m);
        return i;
    }

    public static GUIType getType(org.bukkit.inventory.Inventory inv) {
        if (inv.getHolder() instanceof GUIHolder holder) {
            return holder.getType();
        }
        return null;
    }
}