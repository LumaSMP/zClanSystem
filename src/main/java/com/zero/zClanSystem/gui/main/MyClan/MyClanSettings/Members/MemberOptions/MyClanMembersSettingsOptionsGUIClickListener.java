package com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MemberOptions;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MemberOptions.TransferOwnership.MyClanMembersSettingsOptionsTransferOwnershipGUI;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MyClanMembersSettingsGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class MyClanMembersSettingsOptionsGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public MyClanMembersSettingsOptionsGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.MY_CLAN_MEMBER_OPTIONS) return;

        GUIHolder holder = (GUIHolder) top.getHolder();
        Clan clan = holder.getClan();
        UUID targetUUID = holder.getTargetUUID();

        int slot = e.getRawSlot();

        // Kick
        // Kick
        if (slot == 10) {

            boolean isOwner = clan.isOwner(player.getUniqueId());
            boolean isCoOwner = clan.isCoOwner(player.getUniqueId());
            boolean targetIsOwner = clan.isOwner(targetUUID);
            boolean targetIsCoOwner = clan.isCoOwner(targetUUID);

            // Owner cannot kick himself
            if (player.getUniqueId().equals(targetUUID)) {
                player.sendMessage("§cYou cannot kick yourself.");
                return;
            }

            if (isCoOwner && !isOwner) {

                if (targetIsOwner) {
                    player.sendMessage("§cYou cannot kick the clan owner.");
                    return;
                }

                if (targetIsCoOwner) {
                    player.sendMessage("§cYou cannot kick another Co‑Owner.");
                    return;
                }
            }

            // Owner can kick anyone except himself
            clanManager.kickPlayer(clan, targetUUID, player);
            player.sendMessage("§cPlayer kicked.");
            player.closeInventory();
            new MyClanMembersSettingsGUI(clanManager, clan).open(player);
            return;
        }

        // Promote
        if (slot == 12) {

            if (!clan.isOwner(player.getUniqueId())) {
                player.sendMessage("§cOnly the clan owner can promote players.");
                return;
            }

            clanManager.promoteToCoOwner(clan, targetUUID, player);
            player.sendMessage("§aPlayer promoted to Co‑Owner.");
            player.closeInventory();
            new MyClanMembersSettingsGUI(clanManager, clan).open(player);
            return;
        }


        // Demote
        if (slot == 14) {

            if (!clan.isOwner(player.getUniqueId())) {
                player.sendMessage("§cOnly the clan owner can demote players.");
                return;
            }

            clanManager.demoteToMember(clan, targetUUID, player);
            player.sendMessage("§8Player demoted to Member.");
            player.closeInventory();
            new MyClanMembersSettingsGUI(clanManager, clan).open(player);
            return;
        }

        // Make Owner
        if (slot == 16) {

            if (!clan.isOwner(player.getUniqueId())) {
                player.sendMessage("§cOnly the clan owner can transfer ownership.");
                return;
            }

            if (clan.isOwner(targetUUID)) {
                player.sendMessage("§cThis player is already the owner.");
                return;
            }

            player.closeInventory();
            new MyClanMembersSettingsOptionsTransferOwnershipGUI(clanManager, clan, targetUUID).open(player);
            return;
        }
    }
}
