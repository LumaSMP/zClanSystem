package com.zero.zClanSystem.listeners;

import com.zero.zClanSystem.clan.ClanManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ClanJoinListener implements Listener {

    private final ClanManager clanManager;

    public ClanJoinListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        clanManager.applyClanSuffix(player);
        clanManager.updateTablist(player);
    }
}
