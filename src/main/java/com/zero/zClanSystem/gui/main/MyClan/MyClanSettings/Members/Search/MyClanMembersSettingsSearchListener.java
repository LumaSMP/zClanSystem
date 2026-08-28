package com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.Search;

import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MyClanMembersSettingsGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class MyClanMembersSettingsSearchListener implements Listener {

    private final ClanManager clanManager;

    public MyClanMembersSettingsSearchListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!MyClanMembersSettingsSearch.waitingForSearch.remove(uuid)) return;

        e.setCancelled(true);
        String query = e.getMessage().toLowerCase();

        Bukkit.getScheduler().runTask(clanManager.getPlugin(), () -> {

            MyClanMembersSettingsSearch.searchMap.put(uuid, query);

            player.sendMessage("§aSearch set to: §f" + query);

            new MyClanMembersSettingsGUI(
                    clanManager,
                    clanManager.getClanOf(player.getUniqueId())
            ).open(player, 0);
        });
    }
}
