package com.zero.zClanSystem.gui.main.MyClan.MyClanStats;

import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.MyClan.MyClanMenuGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MyClanStatsGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public MyClanStatsGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.MY_CLAN_STATS) return;

        String title = e.getView().getTitle();
        if (!title.startsWith(MyClanStatsGUI.TITLE_PREFIX)) return;

        int slot = e.getRawSlot();

        // --- Back Button ---
        if (slot == 18) {
            player.closeInventory();

            var holder = (GUIHolder) top.getHolder();
            var clan = holder.getClan();

            new MyClanMenuGUI(clanManager, clan).open(player);
        }
    }
}
