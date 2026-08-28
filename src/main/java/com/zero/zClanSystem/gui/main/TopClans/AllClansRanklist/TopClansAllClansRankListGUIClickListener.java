package com.zero.zClanSystem.gui.main.TopClans.AllClansRanklist;

import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.TopClans.AllClansRanklist.Search.TopClansAllClansRankListSearch;
import com.zero.zClanSystem.gui.main.TopClans.ClanStats.TopClansStatsGUI;
import com.zero.zClanSystem.gui.main.TopClans.TopClansListGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class TopClansAllClansRankListGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public TopClansAllClansRankListGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.TOP_CLANS_ALL_RANK_LIST) return;
        if (!e.getView().getTitle().equals(TopClansAllClansRankListGUI.TITLE)) return;

        GUIHolder holder = (GUIHolder) top.getHolder();
        int currentPage = holder.getPage();

        int slot = e.getRawSlot();
        ItemStack item = e.getCurrentItem();
        if (item == null) return;

        // BACK
        if (slot == 49) {
            player.closeInventory();
            new TopClansListGUI(clanManager).open(player);
            return;
        }

        // PREV PAGE
        if (slot == 45) {
            player.closeInventory();
            new TopClansAllClansRankListGUI(clanManager).open(player, Math.max(0, currentPage - 1));
            return;
        }

        // NEXT PAGE
        if (slot == 53) {
            player.closeInventory();
            new TopClansAllClansRankListGUI(clanManager).open(player, currentPage + 1);
            return;
        }

        // SEARCH
        if (slot == 47) {

            UUID uuid = player.getUniqueId();

            if (TopClansAllClansRankListSearch.searchMap.containsKey(uuid)) {
                TopClansAllClansRankListSearch.clearSearch(uuid);
                player.sendMessage("§eSearch cleared.");
                player.closeInventory();
                new TopClansAllClansRankListGUI(clanManager).open(player, 0);
                return;
            }

            player.closeInventory();
            TopClansAllClansRankListSearch.startSearch(uuid);
            player.sendMessage("§bPlease enter your search query:");
            return;
        }

        // CLAN CLICK
        if (item.getType() == Material.PLAYER_HEAD) {

            String rawName = item.getItemMeta().getDisplayName();
            String clanName = rawName.replace("§e", "");

            var clan = clanManager.getClanByName(clanName);
            if (clan == null) {
                player.sendMessage("§cClan not found.");
                return;
            }

            player.closeInventory();
            new TopClansStatsGUI(clanManager, clan).open(player);
            return;
        }
    }
}
