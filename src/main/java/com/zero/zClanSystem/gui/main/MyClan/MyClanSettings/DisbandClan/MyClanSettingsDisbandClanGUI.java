package com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.DisbandClan;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MyClanSettingsDisbandClanGUI {

    private final ClanManager clanManager;
    private final Clan clan;

    public MyClanSettingsDisbandClanGUI(ClanManager clanManager, Clan clan) {
        this.clanManager = clanManager;
        this.clan = clan;
    }

    public void open(Player player) {

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.MY_CLAN_SETTINGS_DISBAND, clan),
                27,
                "§8Disband Clan"
        );

        // CENTER — Are you sure?
        inv.setItem(13, GUIUtils.item(
                Material.BARRIER,
                "§cAre you sure?",
                "§7This action is §cpermanent§7.",
                "§7Your clan will be §4deleted§7.",
                "§7This cannot be undone."
        ));

        // CONFIRM BUTTON (slot 15)
        inv.setItem(15, GUIUtils.item(
                Material.REDSTONE_BLOCK,
                "§cDisband Clan",
                "§7Click to permanently delete your clan"
        ));

        // BACK BUTTON (slot 18)
        inv.setItem(18, GUIUtils.item(
                Material.ARROW,
                "§eBack",
                "§7Return to settings"
        ));

        player.openInventory(inv);
    }
}
