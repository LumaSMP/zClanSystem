package com.zero.zClanSystem.gui.main.MyClan.MyClanStats;

import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class MyClanStatsGUIBlockListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.MY_CLAN_STATS) return;

        String title = e.getView().getTitle();
        if (!title.startsWith(MyClanStatsGUI.TITLE_PREFIX)) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.MY_CLAN_STATS) return;

        String title = e.getView().getTitle();
        if (!title.startsWith(MyClanStatsGUI.TITLE_PREFIX)) return;

        e.setCancelled(true);
    }
}
