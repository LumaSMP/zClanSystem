package com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanModify.ModifyColor;

import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class ClanModifyColorGUIBlockListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        var inv = e.getInventory();
        if (GUIUtils.getType(inv) != GUIType.ADMIN_CLAN_MODIFY_COLOR) return;

        String title = e.getView().getTitle();
        if (!title.startsWith(ClanModifyColorGUI.TITLE_PREFIX)) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {

        var inv = e.getInventory();
        if (GUIUtils.getType(inv) != GUIType.ADMIN_CLAN_MODIFY_COLOR) return;

        String title = e.getView().getTitle();
        if (!title.startsWith(ClanModifyColorGUI.TITLE_PREFIX)) return;

        e.setCancelled(true);
    }
}
