package com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MemberOptions.TransferOwnership;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MyClanMembersSettingsGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class MyClanMembersSettingsOptionsTransferOwnershipGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public MyClanMembersSettingsOptionsTransferOwnershipGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.MY_CLAN_TRANSFER_OWNERSHIP) return;

        GUIHolder holder = (GUIHolder) top.getHolder();
        Clan clan = holder.getClan();
        UUID targetUUID = holder.getTargetUUID();

        int slot = e.getRawSlot();

        // Confirm transfer
        if (slot == 15) {

            if (!clan.isOwner(player.getUniqueId())) {
                player.sendMessage("§cOnly the clan owner can transfer ownership.");
                return;
            }

            clanManager.transferOwnership(clan, targetUUID, player);

            player.closeInventory();
            new MyClanMembersSettingsGUI(clanManager, clan).open(player);
            return;
        }

        // Back
        if (slot == 18) {
            player.closeInventory();
            new MyClanMembersSettingsGUI(clanManager, clan).open(player);
        }
    }
}
