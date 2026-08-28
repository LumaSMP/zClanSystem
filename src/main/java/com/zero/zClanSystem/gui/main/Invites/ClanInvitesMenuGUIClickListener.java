package com.zero.zClanSystem.gui.main.Invites;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.Invites.Search.ClanInvitesMenuSearch;
import com.zero.zClanSystem.gui.main.TopClans.ClanStats.TopClansStatsGUI;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class ClanInvitesMenuGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public ClanInvitesMenuGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.CLAN_INVITES_MENU) return;

        int slot = e.getRawSlot();
        UUID uuid = player.getUniqueId();

        if (slot >= 45 && slot <= 53) {
            e.setCancelled(true);
        }

        if (slot == 49) {
            e.setCancelled(true);
            player.closeInventory();
            return;
        }

        if (slot == 47) {
            e.setCancelled(true);

            if (ClanInvitesMenuSearch.searchMap.containsKey(uuid)) {
                ClanInvitesMenuSearch.clearSearch(uuid);
                player.sendMessage("§eSearch cleared.");
                player.closeInventory();
                new ClanInvitesMenuGUI(clanManager).open(player, 0);
                return;
            }

            player.closeInventory();
            ClanInvitesMenuSearch.startSearch(uuid);
            player.sendMessage("§bPlease enter your search query:");
            return;
        }

        if (slot < 45) {

            ItemStack clicked = top.getItem(slot);
            if (clicked == null || clicked.getType() != Material.PLAYER_HEAD) return;

            SkullMeta meta = (SkullMeta) clicked.getItemMeta();
            OfflinePlayer owner = meta.getOwningPlayer();
            if (owner == null) return;

            Clan clan = clanManager.getClanOf(owner.getUniqueId());
            if (clan == null) return;

            // Accept invite
            if (e.isLeftClick()) {
                clanManager.addMember(clan, uuid);
                player.sendMessage("§aYou joined clan §e" + clan.getName());
                player.closeInventory();
                return;
            }

            // Decline invite
            if (e.isRightClick()) {
                clanManager.removeInvite(clan, uuid);
                player.sendMessage("§cInvite declined.");
                player.closeInventory();
                new ClanInvitesMenuGUI(clanManager).open(player, 0);
                return;
            }

            // Clan Information
            if (e.getClick() == ClickType.DOUBLE_CLICK) {
                player.closeInventory();
                new TopClansStatsGUI(clanManager, clan).open(player);
                return;
            }
        }
    }
}
