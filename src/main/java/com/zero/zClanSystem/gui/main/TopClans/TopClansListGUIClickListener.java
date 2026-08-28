package com.zero.zClanSystem.gui.main.TopClans;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.ClanMainMenuGUI;
import com.zero.zClanSystem.gui.main.TopClans.AllClansRanklist.TopClansAllClansRankListGUI;
import com.zero.zClanSystem.gui.main.TopClans.ClanStats.TopClansStatsGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TopClansListGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public TopClansListGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var inv = e.getInventory();
        if (GUIUtils.getType(inv) != GUIType.TOP_CLANS_LIST) return;

        if (!e.getView().getTitle().equals(TopClansListGUI.TITLE)) return;

        int slot = e.getRawSlot();
        ItemStack item = e.getCurrentItem();
        if (item == null) return;

        // BACK BUTTON
        if (slot == 45) {
            player.closeInventory();
            new ClanMainMenuGUI(clanManager).open(player);
            return;
        }

        // ALL CLANS BUTTON
        if (slot == 52) {
            player.closeInventory();
            new TopClansAllClansRankListGUI(clanManager).open(player);
            return;
        }

        // MY CLAN BUTTON
        if (slot == 53) {

            Clan clan = clanManager.getClanOf(player.getUniqueId());
            if (clan == null) {
                return;
            }

            player.closeInventory();
            new TopClansStatsGUI(clanManager, clan).open(player);
            return;
        }

        // TOP 10 CLAN HEAD CLICK
        if (item.getType() == Material.PLAYER_HEAD && slot != 53) {

            var meta = item.getItemMeta();
            if (meta == null || meta.getLore() == null || meta.getLore().isEmpty()) return;

            String loreLine = meta.getLore().get(0);
            if (!loreLine.startsWith("§7Clan: §f")) return;

            String clanName = loreLine.replace("§7Clan: §f", "").trim();

            Clan clan = clanManager.getClanByName(clanName);
            if (clan == null) {
                player.sendMessage("§cClan not found.");
                return;
            }

            player.closeInventory();
            new TopClansStatsGUI(clanManager, clan).open(player);
            return;
        }
    }
}
