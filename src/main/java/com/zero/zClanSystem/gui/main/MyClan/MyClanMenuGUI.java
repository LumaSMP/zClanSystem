package com.zero.zClanSystem.gui.main.MyClan;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MyClanMenuGUI {

    private final ClanManager clanManager;
    private final Clan clan;

    public MyClanMenuGUI(ClanManager clanManager, Clan clan) {
        this.clanManager = clanManager;
        this.clan = clan;
    }

    public void open(Player player) {

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.MY_CLAN_MENU, clan),
                27,
                "§8" + clan.getName()
        );

        // MEMBERS (slot 10)
        inv.setItem(10, GUIUtils.item(
                Material.PLAYER_HEAD,
                "§eMembers",
                "§7Total Members: §f" + clan.getMembers().size()
        ));

        // INVITES (slot 12)
        inv.setItem(12, GUIUtils.item(
                Material.PAPER,
                "§eInvites",
                "§7Pending Invites: §f" + clan.getInvited().size()
        ));

        // STATS (slot 14)
        inv.setItem(14, GUIUtils.item(
                Material.GOLD_INGOT,
                "§eStats",
                "§7View clan statistics"
        ));

        // INFO (slot 16)
        inv.setItem(16, GUIUtils.item(
                Material.BOOK,
                "§eInfo",
                "§7General clan information"
        ));

        // SETTINGS (slot 8) — Owner & Co-Owner only
        if (clan.isOwner(player.getUniqueId()) || clan.isCoOwner(player.getUniqueId())) {
            inv.setItem(8, GUIUtils.item(
                    Material.NETHER_STAR,
                    "§eSettings",
                    "§7Clan settings & management"
            ));
        }

        // BACK BUTTON (slot 18)
        inv.setItem(18, GUIUtils.item(
                Material.ARROW,
                "§eBack",
                "§7Return to main menu"
        ));

        // LEAVE CLAN (slot 26) - not owner only
        if (!clan.isOwner(player.getUniqueId())) {
            inv.setItem(26, GUIUtils.item(
                    Material.BARRIER,
                    "§cLeave Clan",
                    "§7Leave your current clan"
            ));
        }

        player.openInventory(inv);
    }
}
