package com.zero.zClanSystem.gui.main.TopClans.ClanStats;

import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.TopClans.TopClansListGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TopClansStatsGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public TopClansStatsGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.TOP_CLAN_STATS) return;

        String title = e.getView().getTitle();
        if (!title.startsWith(TopClansStatsGUI.TITLE_PREFIX)) return;

        int slot = e.getRawSlot();

        // --- Back Button ---
        if (slot == 18) {
            player.closeInventory();
            new TopClansListGUI(clanManager).open(player);
            return;
        }
    }
}
