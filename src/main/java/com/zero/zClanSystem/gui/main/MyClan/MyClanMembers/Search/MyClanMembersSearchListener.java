package com.zero.zClanSystem.gui.main.MyClan.MyClanMembers.Search;

import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.main.MyClan.MyClanMembers.MyClanMembersGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class MyClanMembersSearchListener implements Listener {

    private final ClanManager clanManager;

    public MyClanMembersSearchListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!MyClanMembersSearch.waitingForSearch.remove(uuid)) return;

        e.setCancelled(true);
        String query = e.getMessage().toLowerCase();

        Bukkit.getScheduler().runTask(clanManager.getPlugin(), () -> {

            MyClanMembersSearch.searchMap.put(uuid, query);

            player.sendMessage("§aSearch set to: §f" + query);

            new MyClanMembersGUI(
                    clanManager,
                    clanManager.getClanOf(player.getUniqueId())
            ).open(player, 0);
        });
    }
}