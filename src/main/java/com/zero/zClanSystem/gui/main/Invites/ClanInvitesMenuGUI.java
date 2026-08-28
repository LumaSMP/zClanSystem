package com.zero.zClanSystem.gui.main.Invites;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.clan.InviteData;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.Invites.Search.ClanInvitesMenuSearch;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class ClanInvitesMenuGUI {

    public static final String TITLE_PREFIX = "§8Invites ";

    private final ClanManager clanManager;

    private static final int[] INVITE_SLOTS = {
            9,10,11,12,13,14,15,
            18,19,20,21,22,23,24,
            27,28,29,30,31,32,33,
            36,37,38,39,40,41,42
    };

    public ClanInvitesMenuGUI(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public void open(Player player) {
        open(player, 0);
    }

    public void open(Player player, int page) {

        String title = TITLE_PREFIX + "§7Received";

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.CLAN_INVITES_MENU, null),
                54,
                title
        );

        GUIHolder holder = (GUIHolder) inv.getHolder();
        holder.setPage(page);

        UUID uuid = player.getUniqueId();

        // Scan clans.yml for invites
        List<Clan> received = new ArrayList<>();
        for (Clan clan : clanManager.getAllClans()) {
            if (clan.getInvited().contains(uuid)) {

                // Expiration check
                InviteData data = clanManager.getPendingInviteMap().get(uuid);
                if (data != null && data.isExpired()) {
                    clanManager.removeInvite(clan, uuid);
                    continue;
                }

                received.add(clan);
            }
        }

        // Search filter
        String query = ClanInvitesMenuSearch.searchMap.get(uuid);
        if (query != null && !query.isEmpty()) {
            String q = query.toLowerCase();
            received.removeIf(clan -> !clan.getName().toLowerCase().contains(q));
        }

        int pages = (int) Math.ceil(received.size() / (double) INVITE_SLOTS.length);

        fillPage(player, inv, page, received, pages);

        player.openInventory(inv);
    }

    private void fillPage(Player player, Inventory inv, int page, List<Clan> received, int pages) {

        int perPage = INVITE_SLOTS.length;
        int start = page * perPage;
        int end = Math.min(start + perPage, received.size());

        for (int i = start; i < end; i++) {
            Clan clan = received.get(i);
            inv.setItem(INVITE_SLOTS[i - start], createInviteItem(player, clan));
        }

        fillBottom(player, inv, page, page > 0, page < (pages - 1));
    }

    private ItemStack createInviteItem(Player player, Clan clan) {

        UUID ownerUUID = clan.getOwner();
        OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerUUID);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(owner);
        meta.setDisplayName("§e" + clan.getName());

        List<String> lore = new ArrayList<>();
        lore.add("§7Tag: §f" + clan.getTag());

        // Expiration
        InviteData data = clanManager.getPendingInviteMap().get(player.getUniqueId());
        if (data == null || data.getClanTag() == null) {
            lore.add("§7Expires in: §fNever");
        } else {
            long expires = data.isExpired() ? 0 : (data.getExpiresAt() - System.currentTimeMillis());
            if (expires <= 0) {
                lore.add("§7Expires in: §fExpired");
            } else {
                long minutes = expires / 60000;
                lore.add("§7Expires in: §f" + minutes + " minutes");
            }
        }

        lore.add("§aClick to accept");
        lore.add("§cRight-Click to decline");
        lore.add("§bDouble-Click for Clan Information");

        meta.setLore(lore);
        head.setItemMeta(meta);

        return head;
    }

    private void fillBottom(Player player, Inventory inv, int page, boolean hasPrev, boolean hasNext) {

        for (int i = 45; i < 54; i++) {
            inv.setItem(i, GUIUtils.item(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "§r"));
        }

        if (hasPrev) inv.setItem(45, GUIUtils.item(Material.ARROW, "§ePrev Page"));

        inv.setItem(47, GUIUtils.item(Material.OAK_SIGN, "§eSearch", "§7Click to search"));

        inv.setItem(49, GUIUtils.item(Material.ARROW, "§eBack", "§7Return"));

        if (hasNext) inv.setItem(53, GUIUtils.item(Material.ARROW, "§eNext Page"));

        String query = ClanInvitesMenuSearch.searchMap.get(player.getUniqueId());
        if (query != null) {
            inv.setItem(46, GUIUtils.item(Material.PAPER, "§eSearch: §f" + query));
        }
    }
}
