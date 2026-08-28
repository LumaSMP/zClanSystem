package com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MemberOptions.TransferOwnership;

import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class MyClanMembersSettingsOptionsTransferOwnershipGUIBlockListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.MY_CLAN_TRANSFER_OWNERSHIP) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.MY_CLAN_TRANSFER_OWNERSHIP) return;
        e.setCancelled(true);
    }
}
