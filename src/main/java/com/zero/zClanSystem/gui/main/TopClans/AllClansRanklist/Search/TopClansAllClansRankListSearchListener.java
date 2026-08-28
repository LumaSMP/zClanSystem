package com.zero.zClanSystem.gui.main.TopClans.AllClansRanklist.Search;

import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.main.TopClans.AllClansRanklist.TopClansAllClansRankListGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class TopClansAllClansRankListSearchListener implements Listener {

    private final ClanManager clanManager;

    public TopClansAllClansRankListSearchListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        // Only continue if the player is currently waiting for search input
        if (!TopClansAllClansRankListSearch.waitingForSearch.remove(uuid)) return;

        e.setCancelled(true);
        String query = e.getMessage().toLowerCase();

        // MUST run on main thread
        Bukkit.getScheduler().runTask(clanManager.getPlugin(), () -> {

            TopClansAllClansRankListSearch.searchMap.put(uuid, query);

            player.sendMessage("§aSearch set to: §f" + query);

            new TopClansAllClansRankListGUI(clanManager).open(player, 0);
        });
    }
}
