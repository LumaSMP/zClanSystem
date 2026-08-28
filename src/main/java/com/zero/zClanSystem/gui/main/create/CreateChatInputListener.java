package com.zero.zClanSystem.gui.main.create;

import com.zero.zClanSystem.clan.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class CreateChatInputListener implements Listener {

    public static final Set<UUID> waitingForName = new HashSet<>();
    public static final Set<UUID> waitingForTag = new HashSet<>();

    public static final Map<UUID, String> nameMap = new HashMap<>();
    public static final Map<UUID, String> tagMap = new HashMap<>();

    public static final Map<UUID, Boolean> nameValidMap = new HashMap<>();
    public static final Map<UUID, Boolean> tagValidMap = new HashMap<>();

    private static ClanManager clanManager;

    public CreateChatInputListener(ClanManager manager) {
        clanManager = manager;
    }

    public static void startNameInput(Player player) {
        waitingForName.add(player.getUniqueId());
        player.sendMessage("§bPlease enter your clan name:");
    }

    public static void startTagInput(Player player) {
        waitingForTag.add(player.getUniqueId());
        player.sendMessage("§bPlease enter your clan tag:");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String msg = e.getMessage();

        if (waitingForName.remove(uuid)) {
            e.setCancelled(true);

            boolean valid = true;
            String error = null;

            if (msg.isEmpty()) {
                valid = false;
                error = "§cClan name cannot be empty.";
            }

            if (valid && clanManager.clanNameExists(msg)) {
                valid = false;
                error = "§cClan name already exists.";
            }

            if (valid && clanManager.getClanOf(uuid) != null) {
                valid = false;
                error = "§cYou are already in a clan.";
            }

            nameMap.put(uuid, msg);
            nameValidMap.put(uuid, valid);

            if (!valid) {
                player.sendMessage(error);
            } else {
                player.sendMessage("§aClan name set to: §f" + msg);
            }

            reopenCreateGui(player);
            return;
        }

        if (waitingForTag.remove(uuid)) {
            e.setCancelled(true);

            boolean valid = true;
            String error = null;

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

            if (valid && clanManager.getClanOf(uuid) != null) {
                valid = false;
                error = "§cYou are already in a clan.";
            }

            tagMap.put(uuid, msg);
            tagValidMap.put(uuid, valid);

            if (!valid) {
                player.sendMessage(error);
            } else {
                player.sendMessage("§aClan tag set to: §f" + msg);
            }

            reopenCreateGui(player);
        }
    }

    private void reopenCreateGui(Player player) {
        UUID uuid = player.getUniqueId();

        String name = nameMap.get(uuid);
        String tag = tagMap.get(uuid);

        boolean nameValid = nameValidMap.getOrDefault(uuid, false);
        boolean tagValid = tagValidMap.getOrDefault(uuid, false);

        Bukkit.getScheduler().runTask(clanManager.getPlugin(), () -> {
            CreateClanGUI.open(player, name, tag, nameValid, tagValid);
        });
    }
}