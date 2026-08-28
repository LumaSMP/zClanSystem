package com.zero.zClanSystem.gui.main.admin.ForceDisband.Apply;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AdminForceDisbandApplyGUI {

    public static final String TITLE_PREFIX = "§8Disband ";

    private final ClanManager clanManager;
    private final Clan clan;

    public AdminForceDisbandApplyGUI(ClanManager clanManager, Clan clan) {
        this.clanManager = clanManager;
        this.clan = clan;
    }

    public void open(Player player) {

        String title = TITLE_PREFIX + clan.getName() + " (" + clan.getTag() + ")";
        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.ADMIN_FORCE_DISBAND_APPLY),
                27,
                title
        );

        inv.setItem(13, GUIUtils.item(
                Material.BARRIER,
                "§cForce Disband",
                "§7Click to permanently delete this clan"
        ));

        inv.setItem(18, GUIUtils.item(
                Material.ARROW,
                "§eBack",
                "§7Return to previous menu"
        ));

        player.openInventory(inv);
    }
}