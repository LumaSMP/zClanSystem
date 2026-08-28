package com.zero.zClanSystem.gui.main.admin.KickPlayer;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.admin.AdminMenuGUI;
import com.zero.zClanSystem.gui.main.admin.KickPlayer.Search.AdminKickPlayerSearch;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class AdminKickPlayerGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public AdminKickPlayerGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.ADMIN_KICK_PLAYER) return;
        if (!e.getView().getTitle().equals(AdminKickPlayerGUI.TITLE)) return;

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
            new AdminKickPlayerGUI(clanManager).open(player, Math.max(0, currentPage - 1));
            return;
        }

        // NEXT PAGE
        if (slot == 53) {
            player.closeInventory();
            new AdminKickPlayerGUI(clanManager).open(player, currentPage + 1);
            return;
        }

        // SEARCH
        if (slot == 47) {

            UUID uuid = player.getUniqueId();

            if (AdminKickPlayerSearch.searchMap.containsKey(uuid)) {
                AdminKickPlayerSearch.clearSearch(uuid);
                player.sendMessage("§eSearch cleared.");
                player.closeInventory();
                new AdminKickPlayerGUI(clanManager).open(player, 0);
                return;
            }

            player.closeInventory();
            AdminKickPlayerSearch.startSearch(uuid);
            player.sendMessage("§bPlease enter your search query:");
            return;
        }

        // PLAYER HEAD CLICK
        if (item.getType() == Material.PLAYER_HEAD && slot < 45) {

            String playerName = item.getItemMeta().getDisplayName().replace("§e", "");
            OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
            if (target == null) return;

            Clan clan = clanManager.getClanOf(target.getUniqueId());
            if (clan == null) return;

            // Owner cannot be kicked
            if (clan.getOwner().equals(target.getUniqueId())) {
                player.sendMessage("§cYou cannot kick the clan owner.");
                return;
            }

            // DOUBLE CLICK = KICK
            if (e.getClick() == ClickType.DOUBLE_CLICK) {

                clanManager.kickPlayer(clan, target.getUniqueId(), player);

                player.closeInventory();
                new AdminKickPlayerGUI(clanManager).open(player, currentPage);
                return;
            }
        }
    }
}
