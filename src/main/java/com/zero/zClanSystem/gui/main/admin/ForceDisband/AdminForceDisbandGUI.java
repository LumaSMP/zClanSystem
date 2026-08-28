package com.zero.zClanSystem.gui.main.admin.ForceDisband;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminForceDisbandGUI {

    public static final String TITLE = "§8Force Disband Menu";

    private final ClanManager clanManager;

    public AdminForceDisbandGUI(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public void open(Player player) {
        open(player, 0);
    }

    public void open(Player player, int page) {

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.ADMIN_FORCE_DISBAND),
                54,
                TITLE
        );

        GUIHolder holder = (GUIHolder) inv.getHolder();
        holder.setPage(page);

        List<Clan> clans = new ArrayList<>(clanManager.getAllClans());

        int start = page * 45;
        int end = Math.min(start + 45, clans.size());

        int slot = 0;

        for (int i = start; i < end; i++) {

            Clan clan = clans.get(i);
            OfflinePlayer owner = Bukkit.getOfflinePlayer(clan.getOwner());

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setOwningPlayer(owner);
            meta.setDisplayName("§eClan: " + clan.getName());
            meta.setLore(List.of(
                    "§7Clan Tag: §f" + clan.getTag(),
                    "§7Owner: §f" + (owner.getName() != null ? owner.getName() : "Unknown"),
                    "§7Members: §f" + clan.getMembers().size()
            ));

            head.setItemMeta(meta);
            inv.setItem(slot++, head);
        }

        // FILLER
        for (int i = 45; i < 54; i++) {
            inv.setItem(i, GUIUtils.item(Material.RED_STAINED_GLASS_PANE, "§r"));
        }

        // BACK BUTTON
        inv.setItem(49, GUIUtils.item(Material.ARROW, "§eBack", "§7Return to Admin Menu"));

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
