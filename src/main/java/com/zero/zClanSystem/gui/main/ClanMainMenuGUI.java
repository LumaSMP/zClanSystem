package com.zero.zClanSystem.gui.main;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ClanMainMenuGUI {

    public static final String TITLE = "§8Clan Menu";

    private final ClanManager clanManager;

    public ClanMainMenuGUI(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public void open(Player player) {

        Clan clan = clanManager.getClanOf(player.getUniqueId());

        if (clan == null) {
            openNoClanMenu(player);
        } else {
            openClanMenu(player, clan);
        }
    }

    private void openNoClanMenu(Player player) {

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.MAIN_MENU_NO_CLAN),
                27,
                TITLE
        );

        inv.setItem(11, GUIUtils.item(Material.EMERALD, "§aCreate Clan", "§7Create a new clan"));
        inv.setItem(13, GUIUtils.item(Material.PAPER, "§eInvites", "§7View your clan invites"));
        inv.setItem(15, GUIUtils.item(Material.DIAMOND, "§bTop Clans", "§7View the top clans"));

        if (player.hasPermission("clansystem.admin")) {
            inv.setItem(26, GUIUtils.item(Material.REDSTONE, "§cAdmin", "§7Admin tools"));
        }

        player.openInventory(inv);
    }

    private void openClanMenu(Player player, Clan clan) {

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.MAIN_MENU_CLAN),
                27,
                TITLE
        );

        inv.setItem(12, GUIUtils.item(
                Material.BOOK,
                "§aMy Clan",
                "§7Clan: §f" + clan.getName()
        ));

        inv.setItem(14, GUIUtils.item(
                Material.DIAMOND,
                "§bTop Clans",
                "§7View the top clans"
        ));

        if (player.hasPermission("clansystem.admin")) {
            inv.setItem(26, GUIUtils.item(
                    Material.REDSTONE,
                    "§cAdmin",
                    "§7Admin tools"
            ));
        }

        player.openInventory(inv);
    }
}
