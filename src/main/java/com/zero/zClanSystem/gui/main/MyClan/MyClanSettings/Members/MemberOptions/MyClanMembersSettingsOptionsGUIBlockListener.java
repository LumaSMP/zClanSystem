package com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MemberOptions;

import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MyClanMembersSettingsOptionsGUIBlockListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.MY_CLAN_MEMBER_OPTIONS) return;

        int slot = e.getRawSlot();

        // Block bottom inventory
        if (slot >= top.getSize()) {
            e.setCancelled(true);
            return;
        }

        // Block filler panes
        if (top.getItem(slot) != null &&
                top.getItem(slot).getType().toString().contains("STAINED_GLASS_PANE")) {
            e.setCancelled(true);
        }
    }
}
