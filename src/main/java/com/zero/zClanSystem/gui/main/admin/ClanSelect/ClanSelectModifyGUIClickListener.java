package com.zero.zClanSystem.gui.main.admin.ClanSelect;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.admin.AdminMenuGUI;
import com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanModify.AdminClanModifyMenuGUI;
import com.zero.zClanSystem.gui.main.admin.ClanSelect.Search.ClanSelectModifySearch;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ClanSelectModifyGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public ClanSelectModifyGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.ADMIN_CLAN_SELECT_MODIFY) return;
        if (!e.getView().getTitle().equals(ClanSelectModifyGUI.TITLE)) return;

        GUIHolder holder = (GUIHolder) top.getHolder();
        int currentPage = holder.getPage();

        int slot = e.getRawSlot();
        ItemStack item = e.getCurrentItem();
        if (item == null) return;

        // BACK
        if (slot == 49) {
            player.closeInventory();
            AdminMenuGUI.open(player);
            return;
        }

        // PREV PAGE
        if (slot == 45) {
            player.closeInventory();
            new ClanSelectModifyGUI(clanManager).open(player, Math.max(0, currentPage - 1));
            return;
        }

        // NEXT PAGE
        if (slot == 53) {
            player.closeInventory();
            new ClanSelectModifyGUI(clanManager).open(player, currentPage + 1);
            return;
        }

        // SEARCH
        if (slot == 47) {

            UUID uuid = player.getUniqueId();

            if (ClanSelectModifySearch.searchMap.containsKey(uuid)) {
                ClanSelectModifySearch.clearSearch(uuid);
                player.sendMessage("§eSearch cleared.");
                player.closeInventory();
                new ClanSelectModifyGUI(clanManager).open(player, 0);
                return;
            }

            player.closeInventory();
            ClanSelectModifySearch.startSearch(uuid);
            player.sendMessage("§bPlease enter your search query:");
            return;
        }

        // CLAN CLICK
        if (slot < 45 && item.getType() == Material.PLAYER_HEAD) {

            String clanName = item.getItemMeta().getDisplayName().replace("§e", "");
            Clan clan = clanManager.getClanByName(clanName);
            if (clan == null) return;

            player.closeInventory();
            new AdminClanModifyMenuGUI(clanManager, clan).open(player);
        }
    }
}
