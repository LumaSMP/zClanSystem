package com.zero.zClanSystem.listeners;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ClanDamageListener implements Listener {

    private final ClanManager clanManager;

    public ClanDamageListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClanDamage(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        Clan victimClan = clanManager.getClanOf(victim.getUniqueId());
        Clan attackerClan = clanManager.getClanOf(attacker.getUniqueId());

        if (victimClan == null || attackerClan == null) return;

        if (!victimClan.equals(attackerClan)) return;

        if (victimClan.isFriendlyFire()) return;

        event.setCancelled(true);

        attacker.sendMessage("§cYou cannot damage members of your clan.");
    }
}
