package com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.ChangeClanHome;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MyClanHomeGUI {

    public static final String TITLE_PREFIX = "§8Clan Home ";

    private final ClanManager clanManager;
    private final Clan clan;

    public MyClanHomeGUI(ClanManager clanManager, Clan clan) {
        this.clanManager = clanManager;
        this.clan = clan;
    }

    public void open(Player player) {

        String title = TITLE_PREFIX + "§7" + clan.getName();

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.MY_CLAN_HOME, clan),
                27,
                title
        );

        // Info (top right)
        if (clan.getHome() != null) {
            var loc = clan.getHome();
            inv.setItem(8, GUIUtils.item(
                    Material.PAPER,
                    "§eClan Home",
                    "§7World: §f" + loc.getWorld().getName(),
                    "§7X: §f" + loc.getX(),
                    "§7Y: §f" + loc.getY(),
                    "§7Z: §f" + loc.getZ()
            ));
        } else {
            inv.setItem(8, GUIUtils.item(
                    Material.PAPER,
                    "§eClan Home",
                    "§cNo home set."
            ));
        }

        // Set Home
        inv.setItem(12, GUIUtils.item(
                Material.LIME_CONCRETE,
                "§aSet Clan Home",
                "§7Set clan home to your current location"
        ));

        // Delete Home
        inv.setItem(14, GUIUtils.item(
                Material.RED_CONCRETE,
                "§cDelete Clan Home",
                "§7Remove the current clan home"
        ));

        // Back
        inv.setItem(18, GUIUtils.item(
                Material.ARROW,
                "§eBack",
                "§7Return to previous menu"
        ));

        player.openInventory(inv);
    }
}
