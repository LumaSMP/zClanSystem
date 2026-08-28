package com.zero.zClanSystem.gui.main.MyClan.MyClanSettings;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MyClanSettingsGUI {

    public static final String TITLE_PREFIX = "§8Settings ";

    private final ClanManager clanManager;
    private final Clan clan;

    public MyClanSettingsGUI(ClanManager clanManager, Clan clan) {
        this.clanManager = clanManager;
        this.clan = clan;
    }

    public void open(Player player) {

        String title = TITLE_PREFIX + "§7" + clan.getName();

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.MY_CLAN_SETTINGS, clan),
                27,
                title
        );

        // settings (middle row: slots 10–16)
        // Slot 10 = Change Name
        inv.setItem(10, GUIUtils.item(
                Material.NAME_TAG,
                "§eChange Clan Name",
                "§7Click to rename your clan"
        ));

        // Slot 11 = Change Tag
        inv.setItem(11, GUIUtils.item(
                Material.NAME_TAG,
                "§eChange Clan Tag",
                "§7Click to change your tag"
        ));

        // Slot 12 = Clan Home
        inv.setItem(12, GUIUtils.item(
                Material.RED_BED,
                "§eClan Home",
                "§7Manage clan home"
        ));

        // Slot 13 = Members
        inv.setItem(13, GUIUtils.item(
                Material.PLAYER_HEAD,
                "§eMembers",
                "§7View all clan members"
        ));

        // Slot 16 = Disband Clan
        inv.setItem(16, GUIUtils.item(
                Material.BARRIER,
                "§cDisband Clan",
                "§7Permanently delete your clan"
        ));


        // back
        inv.setItem(18, GUIUtils.item(
                Material.ARROW,
                "§eBack",
                "§7Return to previous menu"
        ));

        player.openInventory(inv);
    }
}
