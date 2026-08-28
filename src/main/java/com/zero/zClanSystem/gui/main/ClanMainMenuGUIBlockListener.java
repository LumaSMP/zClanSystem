package com.zero.zClanSystem.gui.main;

import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class ClanMainMenuGUIBlockListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        var top = e.getView().getTopInventory();
        GUIType type = GUIUtils.getType(top);

        if (type != GUIType.MAIN_MENU_NO_CLAN && type != GUIType.MAIN_MENU_CLAN) return;
        if (!e.getView().getTitle().equals(ClanMainMenuGUI.TITLE)) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {

        var top = e.getView().getTopInventory();
        GUIType type = GUIUtils.getType(top);

        if (type != GUIType.MAIN_MENU_NO_CLAN && type != GUIType.MAIN_MENU_CLAN) return;
        if (!e.getView().getTitle().equals(ClanMainMenuGUI.TITLE)) return;

        e.setCancelled(true);
    }
}
