package com.zero.zClanSystem.gui.main.admin.KickPlayer.Search;

import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.main.admin.KickPlayer.AdminKickPlayerGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class AdminKickPlayerSearchListener implements Listener {

    private final ClanManager clanManager;

    public AdminKickPlayerSearchListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!AdminKickPlayerSearch.waitingForSearch.remove(uuid)) return;

        e.setCancelled(true);
        String query = e.getMessage().toLowerCase();

        Bukkit.getScheduler().runTask(clanManager.getPlugin(), () -> {

            AdminKickPlayerSearch.searchMap.put(uuid, query);

            player.sendMessage("§aSearch set to: §f" + query);

            new AdminKickPlayerGUI(clanManager).open(player, 0);
        });
    }
}