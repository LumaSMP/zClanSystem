package com.zero.zClanSystem.gui.main.MyClan.MyClanInvites;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.MyClan.MyClanInvites.Search.MyClanInvitesSearch;
import com.zero.zClanSystem.gui.main.MyClan.MyClanMenuGUI;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class MyClanInvitesGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public MyClanInvitesGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.MY_CLAN_INVITES) return;

        GUIHolder holder = (GUIHolder) top.getHolder();
        Clan clan = holder.getClan();
        int page = holder.getPage();

        int slot = e.getRawSlot();

        if (slot >= 45 && slot <= 53) {
            e.setCancelled(true);
        }

        if (slot == 49) {
            e.setCancelled(true);
            player.closeInventory();
            new MyClanMenuGUI(clanManager, clan).open(player);
            return;
        }

        if (slot == 45 && page > 0) {
            e.setCancelled(true);
            player.closeInventory();
            new MyClanInvitesGUI(clanManager, clan).open(player, page - 1);
            return;
        }

        if (slot == 53) {
            e.setCancelled(true);
            player.closeInventory();
            new MyClanInvitesGUI(clanManager, clan).open(player, page + 1);
            return;
        }

        if (slot == 47) {
            e.setCancelled(true);

            UUID uuid = player.getUniqueId();

            if (MyClanInvitesSearch.searchMap.containsKey(uuid)) {
                MyClanInvitesSearch.clearSearch(uuid);
                player.sendMessage("§eSearch cleared.");
                player.closeInventory();
                new MyClanInvitesGUI(clanManager, clan).open(player, 0);
                return;
            }

            player.closeInventory();
            MyClanInvitesSearch.startSearch(uuid);
            player.sendMessage("§bPlease enter your search query:");
            return;
        }

        if (slot == 8) {
            e.setCancelled(true);

            boolean isOwner = clan.getOwner().equals(player.getUniqueId());
            boolean isCoOwner = clan.getCoOwners().contains(player.getUniqueId());

            if (!isOwner && !isCoOwner) {
                player.sendMessage("§cYou are not allowed to create invites.");
                return;
            }

            player.sendMessage("§aInvite creation not implemented yet.");
            return;
        }

        if (slot < 45) {

            ItemStack clicked = top.getItem(slot);
            if (clicked == null || clicked.getType() != Material.PLAYER_HEAD) {
                return;
            }

            SkullMeta meta = (SkullMeta) clicked.getItemMeta();
            OfflinePlayer op = meta.getOwningPlayer();

            if (op == null || op.getName() == null) {
                player.sendMessage("§cCould not identify player.");
                return;
            }

            UUID targetUUID = op.getUniqueId();

            boolean isOwner = clan.getOwner().equals(player.getUniqueId());
            boolean isCoOwner = clan.getCoOwners().contains(player.getUniqueId());

            if (!isOwner && !isCoOwner) {
                player.sendMessage("§cYou are not allowed to cancel invites.");
                return;
            }

            if (!clanManager.hasValidInvite(targetUUID, clan.getTag())) {
                clanManager.removeInvite(clan, targetUUID);
                player.sendMessage("§cThis invite has expired.");
                player.closeInventory();
                new MyClanInvitesGUI(clanManager, clan).open(player, page);
                return;
            }

            clanManager.removeInvite(clan, targetUUID);
            player.sendMessage("§aInvite cancelled for §e" + op.getName());

            player.closeInventory();
            new MyClanInvitesGUI(clanManager, clan).open(player, page);
        }
    }
}
