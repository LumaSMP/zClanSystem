package com.zero.zClanSystem.gui.main.MyClan.MyClanInvites.Search;

import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.main.MyClan.MyClanInvites.MyClanInvitesGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class MyClanInvitesSearchListener implements Listener {

    private final ClanManager clanManager;

    public MyClanInvitesSearchListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        // Player is not waiting for search → ignore
        if (!MyClanInvitesSearch.waitingForSearch.remove(uuid)) return;

        e.setCancelled(true);
        String query = e.getMessage().toLowerCase();

        Bukkit.getScheduler().runTask(clanManager.getPlugin(), () -> {

            MyClanInvitesSearch.searchMap.put(uuid, query);

            player.sendMessage("§aSearch set to: §f" + query);

            new MyClanInvitesGUI(
                    clanManager,
                    clanManager.getClanOf(player.getUniqueId())
            ).open(player, 0);
        });
    }
}
