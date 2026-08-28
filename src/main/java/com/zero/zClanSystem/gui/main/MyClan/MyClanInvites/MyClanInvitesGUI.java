package com.zero.zClanSystem.gui.main.MyClan.MyClanInvites;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.MyClan.MyClanInvites.Search.MyClanInvitesSearch;
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

public class MyClanInvitesGUI {

    public static final String TITLE_PREFIX = "§8Invites ";

    private final ClanManager clanManager;
    private final Clan clan;

    private static final int[] INVITE_SLOTS = {
            9,10,11,12,13,14,15,
            18,19,20,21,22,23,24,
            27,28,29,30,31,32,33,
            36,37,38,39,40,41,42
    };

    public MyClanInvitesGUI(ClanManager clanManager, Clan clan) {
        this.clanManager = clanManager;
        this.clan = clan;
    }

    public void open(Player player) {
        open(player, 0);
    }

    public void open(Player player, int page) {

        String title = TITLE_PREFIX + "§7" + clan.getName();

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.MY_CLAN_INVITES, clan),
                54,
                title
        );

        GUIHolder holder = (GUIHolder) inv.getHolder();
        holder.setPage(page);

        // Clean expired invites before showing GUI
        for (UUID id : new ArrayList<>(clan.getInvited())) {
            if (!clanManager.hasValidInvite(id, clan.getTag())) {
                clanManager.removeInvite(clan, id);
            }
        }

        List<UUID> invites = new ArrayList<>(clan.getInvited());

        String query = MyClanInvitesSearch.searchMap.get(player.getUniqueId());
        if (query != null && !query.isEmpty()) {
            String q = query.toLowerCase();
            invites.removeIf(id -> {
                OfflinePlayer op = Bukkit.getOfflinePlayer(id);
                String name = op.getName();
                return name == null || !name.toLowerCase().contains(q);
            });
        }

        int pages = (int) Math.ceil(invites.size() / (double) INVITE_SLOTS.length);

        fillInvitePage(player, inv, page, invites, pages);

        player.openInventory(inv);
    }

    private void fillInvitePage(Player player, Inventory inv, int page, List<UUID> invites, int pages) {

        int perPage = INVITE_SLOTS.length;
        int start = page * perPage;
        int end = Math.min(start + perPage, invites.size());

        for (int i = start; i < end; i++) {
            UUID target = invites.get(i);
            inv.setItem(INVITE_SLOTS[i - start], createInviteHead(player, target));
        }

        boolean isOwner = clan.getOwner().equals(player.getUniqueId());
        boolean isCoOwner = clan.getCoOwners().contains(player.getUniqueId());

        if (isOwner || isCoOwner) {
            inv.setItem(8, GUIUtils.item(
                    Material.EMERALD,
                    "§aCreate Invite",
                    "§7Click to invite a player"
            ));
        }

        fillBottom(player, inv, page, page > 0, page < (pages - 1));
    }

    private ItemStack createInviteHead(Player viewer, UUID targetUUID) {

        OfflinePlayer op = Bukkit.getOfflinePlayer(targetUUID);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(op);
        meta.setDisplayName("§e" + op.getName());

        List<String> lore = new ArrayList<>();
        lore.add("§7Invite pending");

        boolean isOwner = clan.getOwner().equals(viewer.getUniqueId());
        boolean isCoOwner = clan.getCoOwners().contains(viewer.getUniqueId());

        if (isOwner || isCoOwner) {
            lore.add("§cDouble-Click to cancel invite");
        }

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

        inv.setItem(49, GUIUtils.item(Material.ARROW, "§eBack", "§7Return to MyClan Menu"));

        if (hasNext) inv.setItem(53, GUIUtils.item(Material.ARROW, "§eNext Page"));

        String query = MyClanInvitesSearch.searchMap.get(player.getUniqueId());
        if (query != null) {
            inv.setItem(46, GUIUtils.item(Material.PAPER, "§eSearch: §f" + query));
        }
    }
}
