package com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MemberOptions.TransferOwnership;

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

public class MyClanMembersSettingsOptionsTransferOwnershipGUI {

    private final ClanManager clanManager;
    private final Clan clan;
    private final UUID targetUUID;

    public MyClanMembersSettingsOptionsTransferOwnershipGUI(ClanManager clanManager, Clan clan, UUID targetUUID) {
        this.clanManager = clanManager;
        this.clan = clan;
        this.targetUUID = targetUUID;
    }

    public void open(Player player) {

        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUUID);

        String title = "§8Transfer Ownership §7" + target.getName();

        GUIHolder holder = new GUIHolder(GUIType.MY_CLAN_TRANSFER_OWNERSHIP, clan, targetUUID);

        Inventory inv = Bukkit.createInventory(holder, 27, title);
        holder.setInventory(inv);

        // Filler
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, GUIUtils.item(Material.GRAY_STAINED_GLASS_PANE, "§r"));
        }

        // Center: Are you sure?
        inv.setItem(13, GUIUtils.item(
                Material.BARRIER,
                "§cAre you sure?",
                "§7You are about to transfer ownership.",
                "§7This action is §cpermanent§7.",
                "§7You will become a §fMember§7.",
                "§7The selected player will become §aOwner§7."
        ));

        // Confirm (slot 15)
        inv.setItem(15, GUIUtils.item(
                Material.EMERALD_BLOCK,
                "§aConfirm Transfer",
                "§7Click to transfer clan ownership"
        ));

        // Back (slot 18)
        inv.setItem(18, GUIUtils.item(
                Material.ARROW,
                "§eBack",
                "§7Return to member options"
        ));

        player.openInventory(inv);
    }
}
