package com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MemberOptions;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class MyClanMembersSettingsOptionsGUI {

    public static final String TITLE_PREFIX = "§8";

    private final ClanManager clanManager;
    private final Clan clan;
    private final UUID targetUUID;

    public MyClanMembersSettingsOptionsGUI(ClanManager clanManager, Clan clan, UUID targetUUID) {
        this.clanManager = clanManager;
        this.clan = clan;
        this.targetUUID = targetUUID;
    }

    public void open(Player player) {

        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUUID);

        String title = TITLE_PREFIX + clan.getName() + " §7" + target.getName() + " Options";

        GUIHolder holder = new GUIHolder(GUIType.MY_CLAN_MEMBER_OPTIONS, clan, targetUUID);

        Inventory inv = Bukkit.createInventory(
                holder,
                27,
                title
        );

        holder.setInventory(inv);

        // Filler
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, GUIUtils.item(Material.GRAY_STAINED_GLASS_PANE, "§r"));
        }

        // Kick
        inv.setItem(10, GUIUtils.item(
                Material.IRON_SWORD,
                "§cKick Player",
                "§7Remove this player from the clan"
        ));

        // Promote
        inv.setItem(12, GUIUtils.item(
                Material.EMERALD_BLOCK,
                "§aPromote",
                "§7Promote this player to Co‑Owner"
        ));

        // Demote
        inv.setItem(14, GUIUtils.item(
                Material.COAL_BLOCK,
                "§8Demote",
                "§7Demote this player to Member"
        ));


        // Make Owner
        inv.setItem(16, GUIUtils.item(
                Material.GOLDEN_HELMET,
                "§bMake Owner",
                "§7Transfer clan ownership to this player"
        ));

        // Back
        inv.setItem(22, GUIUtils.item(
                Material.ARROW,
                "§eBack",
                "§7Return to Members"
        ));

        player.openInventory(inv);
    }
}
