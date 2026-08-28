package com.zero.zClanSystem.listeners;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final ClanManager clanManager;

    public PlayerDeathListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer == null) return;

        Clan victimClan = clanManager.getClanOf(victim.getUniqueId());
        Clan killerClan = clanManager.getClanOf(killer.getUniqueId());

        // Teamkill?
        if (victimClan != null && killerClan != null && victimClan == killerClan) {
            killerClan.addTeamkill();
            clanManager.saveClans();
        }
    }
}