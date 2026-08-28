package com.zero.zClanSystem.gui.main.TopClans.ClanStats;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class TopClansStatsGUI {

    public static final String TITLE_PREFIX = "§8Stats ";

    private final ClanManager clanManager;
    private final Clan clan;

    public TopClansStatsGUI(ClanManager clanManager, Clan clan) {
        this.clanManager = clanManager;
        this.clan = clan;
    }

    private int getTotalKills() {
        int total = 0;
        for (UUID uuid : clan.getAllPlayers()) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
            total += op.getStatistic(Statistic.PLAYER_KILLS);
        }
        return total;
    }

    private int getTotalDeaths() {
        int total = 0;
        for (UUID uuid : clan.getAllPlayers()) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
            total += op.getStatistic(Statistic.DEATHS);
        }
        return total;
    }

    private String getColorName(String colorCode) {
        return switch (colorCode) {
            case "§0" -> "Black";
            case "§1" -> "Dark Blue";
            case "§2" -> "Green";
            case "§3" -> "Cyan";
            case "§4" -> "Dark Red";
            case "§5" -> "Purple";
            case "§6" -> "Orange";
            case "§7" -> "None";
            case "§8" -> "Gray";
            case "§9" -> "Blue";
            case "§a" -> "Lime";
            case "§b" -> "Light Blue";
            case "§c" -> "Red";
            case "§d" -> "Pink";
            case "§e" -> "Yellow";
            case "§f" -> "White";
            default -> "Unknown";
        };
    }

    public void open(Player player) {

        String title = TITLE_PREFIX + "§7" + clan.getName();

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.TOP_CLAN_STATS, clan),
                27,
                title
        );

        // --- Stats ---

        inv.setItem(11, GUIUtils.item(
                Material.IRON_SWORD,
                "§eMember Kills",
                "§7" + getTotalKills() + " total kills"
        ));

        inv.setItem(12, GUIUtils.item(
                Material.SKELETON_SKULL,
                "§eMember Deaths",
                "§7" + getTotalDeaths() + " total deaths"
        ));

        inv.setItem(13, GUIUtils.item(
                Material.PLAYER_HEAD,
                "§eMembers",
                "§7" + clan.getMembers().size() + " total members"
        ));

        inv.setItem(14, GUIUtils.item(
                Material.YELLOW_DYE,
                "§eColor Tag",
                "§7Current: " + clan.getTagColor() + getColorName(clan.getTagColor())
        ));

        inv.setItem(15, GUIUtils.item(
                Material.EMERALD,
                "§eExtra Points",
                "§7" + clan.getExtraPoints() + " bonus points"
        ));

        // --- Clan Points ---
        inv.setItem(26, GUIUtils.item(
                Material.GOLD_INGOT,
                "§eClan Points",
                "§7" + clanManager.calculateClanScore(clan) + " total points"
        ));

        // --- Back Button ---
        inv.setItem(18, GUIUtils.item(
                Material.ARROW,
                "§eBack",
                "§7Return to previous menu"
        ));

        player.openInventory(inv);
    }
}
