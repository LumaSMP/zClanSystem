package com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MemberOptions.MyClanMembersSettingsOptionsGUI;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.Search.MyClanMembersSettingsSearch;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.MyClanSettingsGUI;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class MyClanMembersSettingsGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public MyClanMembersSettingsGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.MY_CLAN_MEMBERS) return;

        GUIHolder holder = (GUIHolder) top.getHolder();
        Clan clan = holder.getClan();
        int page = holder.getPage();

        int slot = e.getRawSlot();

        // Orientation slots are NOT clickable
        if (slot == 0 || slot == 8 || slot == 9 || slot == 17 || slot == 18 ||
                slot == 26 || slot == 27 || slot == 35 || slot == 36 || slot == 44) {
            e.setCancelled(true);
            return;
        }

        // Back
        if (slot == 49) {
            player.closeInventory();
            new MyClanSettingsGUI(clanManager, clan).open(player);
            return;
        }

        // Prev Page
        if (slot == 45 && page > 0) {
            player.closeInventory();
            new MyClanMembersSettingsGUI(clanManager, clan).open(player, page - 1);
            return;
        }

        // Next Page
        if (slot == 53) {
            player.closeInventory();
            new MyClanMembersSettingsGUI(clanManager, clan).open(player, page + 1);
            return;
        }

        // Search
        if (slot == 47) {

            UUID uuid = player.getUniqueId();

            if (MyClanMembersSettingsSearch.searchMap.containsKey(uuid)) {
                MyClanMembersSettingsSearch.clearSearch(uuid);
                player.sendMessage("§eSearch cleared.");
                player.closeInventory();
                new MyClanMembersSettingsGUI(clanManager, clan).open(player, 0);
                return;
            }

            player.closeInventory();
            MyClanMembersSettingsSearch.startSearch(uuid);
            player.sendMessage("§bPlease enter your search query:");
            return;
        }

        // Player head clicked
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

            player.closeInventory();
            new MyClanMembersSettingsOptionsGUI(clanManager, clan, targetUUID).open(player);
            return;
        }
    }
}
