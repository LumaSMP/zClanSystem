package com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.Search.MyClanMembersSettingsSearch;
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

public class MyClanMembersSettingsGUI {

    public static final String TITLE_PREFIX = "§8Members ";

    private final ClanManager clanManager;
    private final Clan clan;

    private static final int[] ORIENTATION_SLOTS = {
            0, 8, 9, 17, 18, 26, 27, 35, 36, 44
    };

    private static final int[] MEMBER_SLOTS = {
            1,2,3,4,5,6,7,
            10,11,12,13,14,15,16,
            19,20,21,22,23,24,25,
            28,29,30,31,32,33,34,
            37,38,39,40,41,42,43
    };

    public MyClanMembersSettingsGUI(ClanManager clanManager, Clan clan) {
        this.clanManager = clanManager;
        this.clan = clan;
    }

    public void open(Player player) {
        open(player, 0);
    }

    public void open(Player player, int page) {

        String title = TITLE_PREFIX + "§7" + clan.getName();

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.MY_CLAN_MEMBERS, clan),
                54,
                title
        );

        GUIHolder holder = (GUIHolder) inv.getHolder();
        holder.setPage(page);

        List<UUID> coOwners = new ArrayList<>(clan.getCoOwners());
        List<UUID> members = new ArrayList<>(clan.getMembers());
        members.remove(clan.getOwner());
        members.removeAll(coOwners);

        UUID uuid = player.getUniqueId();
        String query = MyClanMembersSettingsSearch.searchMap.get(uuid);

        if (query != null && !query.isEmpty()) {

            coOwners.removeIf(id -> {
                String name = Bukkit.getOfflinePlayer(id).getName();
                return name == null || !name.toLowerCase().contains(query);
            });

            members.removeIf(id -> {
                String name = Bukkit.getOfflinePlayer(id).getName();
                return name == null || !name.toLowerCase().contains(query);
            });
        }

        int coOwnerPages = (int) Math.ceil(coOwners.size() / (double) MEMBER_SLOTS.length);
        int memberPages = (int) Math.ceil(members.size() / (double) MEMBER_SLOTS.length);

        if (page == 0) {
            fillOwnerPage(player, inv, coOwnerPages, memberPages);
        } else if (page <= coOwnerPages) {
            fillCoOwnerPage(player, inv, page, coOwners, coOwnerPages, memberPages);
        } else {
            fillMemberPage(player, inv, page, coOwnerPages, members, memberPages);
        }

        player.openInventory(inv);
    }

    private void fillOwnerPage(Player player, Inventory inv, int coOwnerPages, int memberPages) {

        UUID ownerUUID = clan.getOwner();
        OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerUUID);

        inv.setItem(22, createHead(ownerUUID, "Owner"));

        int[] around = {13,14,15,21,23,29,30,31};
        for (int slot : around) {
            inv.setItem(slot, GUIUtils.item(Material.GOLD_BLOCK, "§r"));
        }

        for (int slot : ORIENTATION_SLOTS) {
            inv.setItem(slot, GUIUtils.item(Material.GOLD_BLOCK, "§r"));
        }

        boolean hasNext = coOwnerPages > 0 || memberPages > 0;
        fillBottom(player, inv, 0, false, hasNext);
    }

    private void fillCoOwnerPage(Player player, Inventory inv, int page, List<UUID> coOwners, int coOwnerPages, int memberPages) {

        int perPage = MEMBER_SLOTS.length;
        int start = (page - 1) * perPage;
        int end = Math.min(start + perPage, coOwners.size());

        for (int i = start; i < end; i++) {
            inv.setItem(MEMBER_SLOTS[i - start], createHead(coOwners.get(i), "Co-Owner"));
        }

        for (int slot : ORIENTATION_SLOTS) {
            inv.setItem(slot, GUIUtils.item(Material.IRON_BLOCK, "§r"));
        }

        boolean hasPrev = page > 0;
        boolean hasNext = (page < coOwnerPages) || (memberPages > 0);

        fillBottom(player, inv, page, hasPrev, hasNext);
    }

    private void fillMemberPage(Player player, Inventory inv, int page, int coOwnerPages, List<UUID> members, int memberPages) {

        int memberPageIndex = page - coOwnerPages - 1;
        int perPage = MEMBER_SLOTS.length;
        int start = memberPageIndex * perPage;
        int end = Math.min(start + perPage, members.size());

        for (int i = start; i < end; i++) {
            inv.setItem(MEMBER_SLOTS[i - start], createHead(members.get(i), "Member"));
        }

        for (int slot : ORIENTATION_SLOTS) {
            inv.setItem(slot, GUIUtils.item(Material.COAL_BLOCK, "§r"));
        }

        boolean hasPrev = page > 0;
        boolean hasNext = memberPageIndex < (memberPages - 1);

        fillBottom(player, inv, page, hasPrev, hasNext);
    }

    private ItemStack createHead(UUID uuid, String rank) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(op);
        meta.setDisplayName("§e" + op.getName());
        meta.setLore(List.of("§7Rank: §f" + rank));
        head.setItemMeta(meta);

        return head;
    }

    private void fillBottom(Player player, Inventory inv, int page, boolean hasPrev, boolean hasNext) {

        for (int i = 45; i < 54; i++) {
            inv.setItem(i, GUIUtils.item(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "§r"));
        }

        if (hasPrev) inv.setItem(45, GUIUtils.item(Material.ARROW, "§ePrev Page"));

        inv.setItem(47, GUIUtils.item(Material.OAK_SIGN, "§eSearch", "§7Click to search"));

        inv.setItem(49, GUIUtils.item(Material.ARROW, "§eBack", "§7Return to Settings"));

        if (hasNext) inv.setItem(53, GUIUtils.item(Material.ARROW, "§eNext Page"));

        String query = MyClanMembersSettingsSearch.searchMap.get(player.getUniqueId());

        if (query != null) {
            inv.setItem(46, GUIUtils.item(Material.PAPER, "§eSearch: §f" + query));
        }
    }
}
