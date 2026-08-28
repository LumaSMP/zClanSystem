package com.zero.zClanSystem.gui.main.admin.ClanSelect.Search;

import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanSelectModifyGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class ClanSelectModifySearchListener implements Listener {

    private final ClanManager clanManager;

    public ClanSelectModifySearchListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        // Only continue if the player is currently waiting for search input
        if (!ClanSelectModifySearch.waitingForSearch.remove(uuid)) return;

        e.setCancelled(true);
        String query = e.getMessage().toLowerCase();

        Bukkit.getScheduler().runTask(clanManager.getPlugin(), () -> {

            ClanSelectModifySearch.searchMap.put(uuid, query);

            player.sendMessage("§aSearch set to: §f" + query);

            new ClanSelectModifyGUI(clanManager).open(player, 0);
        });
    }
}
