package com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.ChangeClanTag;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.MyClanSettingsGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ChangeClanTagInputListener implements Listener {

    private static ClanManager clanManager;

    public ChangeClanTagInputListener(ClanManager manager) {
        clanManager = manager;
    }

    private static final Set<UUID> waiting = new HashSet<>();

    public static void startTagInput(Player player, Clan clan) {
        waiting.add(player.getUniqueId());
        player.sendMessage("§bPlease enter the new clan tag:");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!waiting.remove(uuid)) return;

        e.setCancelled(true);

        String msg = e.getMessage();

        // SWITCH TO MAIN THREAD
        Bukkit.getScheduler().runTask(clanManager.getPlugin(), () -> {

            Clan clan = clanManager.getClanOf(uuid);

            if (clan == null) {
                player.sendMessage("§cYou are not in a clan.");
                return;
            }

            boolean valid = true;
            String error = null;

            // VALIDATION
            if (msg.isEmpty()) {
                valid = false;
                error = "§cClan tag cannot be empty.";
            }

            if (valid && msg.length() > clanManager.getMaxTagLength()) {
                valid = false;
                error = "§cClan tag exceeds max length (" + clanManager.getMaxTagLength() + ").";
            }

            if (valid && !msg.matches("[A-Za-z0-9]+")) {
                valid = false;
                error = "§cClan tag may only contain letters and numbers.";
            }

            if (valid && clanManager.clanTagExists(msg)) {
                valid = false;
                error = "§cClan tag already exists.";
            }

            if (!valid) {
                player.sendMessage(error);
                new MyClanSettingsGUI(clanManager, clan).open(player);
                return;
            }

            // SUCCESS
            player.sendMessage("§aClan tag changed to: §f" + msg);

            clanManager.renameClanTag(clan, msg);

            new MyClanSettingsGUI(clanManager, clan).open(player);
        });
    }
}
