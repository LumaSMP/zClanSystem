package com.zero.zClanSystem.gui.main.TopClans;

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

public class TopClansListGUI {

    public static final String TITLE = "§8Top Clan List";

    private final ClanManager clanManager;

    public TopClansListGUI(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public void open(Player player) {

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.TOP_CLANS_LIST),
                54,
                TITLE
        );

        List<Clan> clans = new ArrayList<>(clanManager.getAllClans());
        clans.sort((a, b) -> Integer.compare(
                clanManager.calculateClanScore(b),
                clanManager.calculateClanScore(a)
        ));

        inv.setItem(8, GUIUtils.item(
                Material.PAPER,
                "§eInfo",
                "§7Points are calculated by:",
                "",
                "§7Member-Kills",
                "§7Member-Deaths",
                "§7Members",
                "§7Color-Tag",
                "§7extra-Points"
        ));

        inv.setItem(45, GUIUtils.item(
                Material.ARROW,
                "§eBack",
                "§7Return to clan menu"
        ));

        inv.setItem(52, GUIUtils.item(
                Material.GOLD_NUGGET,
                "§eAll Clans",
                "§7Rank list of all clans"
        ));

        Clan myClan = clanManager.getClanOf(player.getUniqueId());
        if (myClan != null) {

            int myScore = clanManager.calculateClanScore(myClan);
            int myRank = 1;

            for (Clan c : clans) {
                int score = clanManager.calculateClanScore(c);
                if (score > myScore) myRank++;
            }

            OfflinePlayer owner = Bukkit.getOfflinePlayer(myClan.getOwner());

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setOwningPlayer(owner);
            meta.setDisplayName("§eMy Clan");
            meta.setLore(List.of(
                    "§7" + myClan.getName() + " §8[" + myClan.getTag() + "§8]",
                    "§7Rank: §f" + myRank
            ));

            head.setItemMeta(meta);

            inv.setItem(53, head);

        } else {
            inv.setItem(53, GUIUtils.item(
                    Material.BARRIER,
                    "§cNo Clan",
                    "§7You are not in a clan"
            ));
        }


        int[] slots = {13,21,22,23,30,31,32,39,40,41};

        for (int i = 0; i < Math.min(10, clans.size()); i++) {

            Clan clan = clans.get(i);
            OfflinePlayer owner = Bukkit.getOfflinePlayer(clan.getOwner());

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setOwningPlayer(owner);
            meta.setDisplayName("§eTop " + (i + 1));
            meta.setLore(List.of(
                    "§7Clan: §f" + clan.getName(),
                    "§7Tag: §f" + clan.getTag(),
                    "§7Owner: §f" + (owner.getName() != null ? owner.getName() : "Unknown")
            ));

            head.setItemMeta(meta);

            inv.setItem(slots[i], head);
        }

        player.openInventory(inv);
    }
}
