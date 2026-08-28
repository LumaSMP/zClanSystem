package com.zero.zClanSystem.listeners;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ClanChatListener implements Listener {

    private final ClanManager clanManager;

    public ClanChatListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Clan clan = clanManager.getClanOf(player.getUniqueId());

        if (clan == null) return;

        String tag = "§7[§r" + clan.getTagColor() + clan.getTag() + "§7]§r";

        event.setFormat(tag + "<%1$s> %2$s");
    }
}
