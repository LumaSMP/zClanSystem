package com.zero.zClanSystem.gui.main.MyClan.MyClanMembers;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.MyClan.MyClanMembers.Search.MyClanMembersSearch;
import com.zero.zClanSystem.gui.main.MyClan.MyClanMenuGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class MyClanMembersGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public MyClanMembersGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.MY_CLAN_MEMBERS_LIST) return;

        GUIHolder holder = (GUIHolder) top.getHolder();
        Clan clan = holder.getClan();
        int page = holder.getPage();

        int slot = e.getRawSlot();

        // Orientation slots are not clickable
        if (slot == 0 || slot == 8 || slot == 9 || slot == 17 || slot == 18 ||
                slot == 26 || slot == 27 || slot == 35 || slot == 36 || slot == 44) {
            return;
        }

        // Back
        if (slot == 49) {
            player.closeInventory();
            new MyClanMenuGUI(clanManager, clan).open(player);
            return;
        }

        // Prev Page
        if (slot == 45 && page > 0) {
            player.closeInventory();
            new MyClanMembersGUI(clanManager, clan).open(player, page - 1);
            return;
        }

        // Next Page
        if (slot == 53) {
            player.closeInventory();
            new MyClanMembersGUI(clanManager, clan).open(player, page + 1);
            return;
        }

        // Search
        if (slot == 47) {

            UUID uuid = player.getUniqueId();

            if (MyClanMembersSearch.searchMap.containsKey(uuid)) {
                MyClanMembersSearch.clearSearch(uuid);
                player.sendMessage("§eSearch cleared.");
                player.closeInventory();
                new MyClanMembersGUI(clanManager, clan).open(player, 0);
                return;
            }

            player.closeInventory();
            MyClanMembersSearch.startSearch(uuid);
            player.sendMessage("§bPlease enter your search query:");
            return;
        }

        // Player heads: no action, intentionally not clickable (overview only)
    }
}