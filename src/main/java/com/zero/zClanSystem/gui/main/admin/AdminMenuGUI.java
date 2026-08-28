package com.zero.zClanSystem.gui.main.admin;

import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AdminMenuGUI {

    public static final String TITLE = "§8Admin Menu";

    public static void open(Player player) {

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.ADMIN_MENU),
                27,
                TITLE
        );

        inv.setItem(11, GUIUtils.item(
                Material.BARRIER,
                "§cDisband Clan",
                "§7Force disband a clan"
        ));

        inv.setItem(13, GUIUtils.item(
                Material.IRON_SWORD,
                "§eKick Player",
                "§7Kick a player from his clan"
        ));

        inv.setItem(15, GUIUtils.item(
                Material.ANVIL,
                "§bEdit Clan",
                "§7Modify clan settings"
        ));

        // BACK BUTTON (bottom left)
        inv.setItem(18, GUIUtils.item(
                Material.ARROW,
                "§eBack",
                "§7Return to main menu"
        ));

        if (player.isOp()) {

            inv.setItem(25, GUIUtils.item( // later: need a cooldown
                    Material.BOOK,
                    "§eReload clans",
                    "§7Reload the clans.yml"
            ));

            inv.setItem(26, GUIUtils.item(  // later: need a cooldown
                    Material.PAPER,
                    "§eReload config",
                    "§7Reload the config.yml"
            ));
        }

        player.openInventory(inv);
    }
}