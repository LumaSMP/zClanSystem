package com.zero.zClanSystem.gui.main.TopClans.AllClansRanklist;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.TopClans.AllClansRanklist.Search.TopClansAllClansRankListSearch;
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

public class TopClansAllClansRankListGUI {

    public static final String TITLE = "§8All Clan Rankings";

    private final ClanManager clanManager;

    public TopClansAllClansRankListGUI(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public void open(Player player) {
        open(player, 0);
    }

    public void open(Player player, int page) {

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.TOP_CLANS_ALL_RANK_LIST),
                54,
                TITLE
        );

        GUIHolder holder = (GUIHolder) inv.getHolder();
        holder.setPage(page);

        UUID uuid = player.getUniqueId();
        String query = TopClansAllClansRankListSearch.searchMap.get(uuid);

        // Sort clans by score DESC
        List<Clan> clans = new ArrayList<>(clanManager.getAllClans());
        clans.sort((a, b) -> Integer.compare(
                clanManager.calculateClanScore(b),
                clanManager.calculateClanScore(a)
        ));

        // Apply search filter
        if (query != null && !query.isEmpty()) {
            clans.removeIf(clan -> !clan.getName().toLowerCase().contains(query));
        }

        int start = page * 45;
        int end = Math.min(start + 45, clans.size());

        int slot = 0;

        for (int i = start; i < end; i++) {

            Clan clan = clans.get(i);
            OfflinePlayer owner = Bukkit.getOfflinePlayer(clan.getOwner());

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setOwningPlayer(owner);
            meta.setDisplayName("§e" + clan.getName());
            meta.setLore(List.of(
                    "§7Rank: §f" + (i + 1),
                    "§7Tag: §f" + clan.getTag(),
                    "§7Owner: §f" + (owner.getName() != null ? owner.getName() : "Unknown"),
                    "§7Score: §f" + clanManager.calculateClanScore(clan)
            ));

            head.setItemMeta(meta);

            inv.setItem(slot++, head);
        }

        // FILLER
        for (int i = 45; i < 54; i++) {
            inv.setItem(i, GUIUtils.item(Material.GRAY_STAINED_GLASS_PANE, "§r"));
        }

        // SEARCH DISPLAY
        if (query != null) {
            inv.setItem(46, GUIUtils.item(Material.PAPER, "§eSearch: §f" + query));
        }

        // SEARCH BUTTON
        inv.setItem(47, GUIUtils.item(Material.OAK_SIGN, "§eSearch", "§7Click to search for a clan"));

        // BACK
        inv.setItem(49, GUIUtils.item(Material.ARROW, "§eBack", "§7Return to Top Clans Menu"));

        // PREV PAGE
        if (page > 0) {
            inv.setItem(45, GUIUtils.item(Material.ARROW, "§ePrev Page", "§7Go to previous page"));
        }

        // NEXT PAGE
        if (end < clans.size()) {
            inv.setItem(53, GUIUtils.item(Material.ARROW, "§eNext Page", "§7Go to next page"));
        }

        player.openInventory(inv);
    }
}
