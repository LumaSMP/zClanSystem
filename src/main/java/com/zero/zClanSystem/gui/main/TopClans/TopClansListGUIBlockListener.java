package com.zero.zClanSystem.gui.main.TopClans;

import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class TopClansListGUIBlockListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        var inv = e.getInventory();
        if (GUIUtils.getType(inv) != GUIType.TOP_CLANS_LIST) return;

        if (!e.getView().getTitle().equals(TopClansListGUI.TITLE)) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {

        var inv = e.getInventory();
        if (GUIUtils.getType(inv) != GUIType.TOP_CLANS_LIST) return;

        if (!e.getView().getTitle().equals(TopClansListGUI.TITLE)) return;

        e.setCancelled(true);
    }
}
