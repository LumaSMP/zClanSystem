package com.zero.zClanSystem.gui.main.admin.ClanSelect;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.admin.ClanSelect.Search.ClanSelectModifySearch;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClanSelectModifyGUI {

    public static final String TITLE = "§8Select Clan";

    private final ClanManager clanManager;

    public ClanSelectModifyGUI(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public void open(Player player) {
        open(player, 0);
    }

    public void open(Player player, int page) {

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.ADMIN_CLAN_SELECT_MODIFY),
                54,
                TITLE
        );

        GUIHolder holder = (GUIHolder) inv.getHolder();
        holder.setPage(page);

        UUID uuid = player.getUniqueId();
        String query = ClanSelectModifySearch.searchMap.get(uuid);

        List<ItemStack> entries = new ArrayList<>();

        for (Clan clan : clanManager.getAllClans()) {

            OfflinePlayer owner = Bukkit.getOfflinePlayer(clan.getOwner());

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setOwningPlayer(owner);
            meta.setDisplayName("§e" + clan.getName());
            meta.setLore(List.of(
                    "§7Clan Tag: §f" + clan.getTag(),
                    "§7Owner: §f" + (owner.getName() != null ? owner.getName() : "Unknown"),
                    "§7Members: §f" + clan.getMembers().size(),
                    "§7Click to modify this clan"
            ));

            head.setItemMeta(meta);
            entries.add(head);
        }

        if (query != null && !query.isEmpty()) {
            entries.removeIf(item -> {
                String name = item.getItemMeta().getDisplayName().toLowerCase();
                return !name.contains(query.toLowerCase());
            });
        }

        int start = page * 45;
        int end = Math.min(start + 45, entries.size());

        int slot = 0;
        for (int i = start; i < end; i++) {
            inv.setItem(slot++, entries.get(i));
        }

        // FILLER
        for (int i = 45; i < 54; i++) {
            inv.setItem(i, GUIUtils.item(Material.RED_STAINED_GLASS_PANE, "§r"));
        }

        // SEARCH DISPLAY
        if (query != null) {
            inv.setItem(46, GUIUtils.item(Material.PAPER, "§eSearch: §f" + query));
        }

        // SEARCH BUTTON
        inv.setItem(47, GUIUtils.item(Material.OAK_SIGN, "§eSearch", "§7Click to search for a clan"));

        // BACK BUTTON
        inv.setItem(49, GUIUtils.item(Material.ARROW, "§eBack", "§7Return to Admin Menu"));

        // PREV PAGE
        if (page > 0) {
            inv.setItem(45, GUIUtils.item(Material.ARROW, "§ePrev Page", "§7Go to previous page"));
        }

        // NEXT PAGE
        if (end < entries.size()) {
            inv.setItem(53, GUIUtils.item(Material.ARROW, "§eNext Page", "§7Go to next page"));
        }

        player.openInventory(inv);
    }
}
