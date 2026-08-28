package com.zero.zClanSystem.commands;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClanTabCompleter implements TabCompleter {

    private final ClanManager clanManager;
    private final boolean useGui;

    private static final List<String> SUBCOMMANDS_COMMAND = Arrays.asList(
            "create", "disband", "sethome", "delhome", "home", "ff", "promote", "demote",
            "invite", "kick", "leave", "accept", "rename", "info", "top", "color"
    );

    private static final List<String> SUBCOMMANDS_GUI = Arrays.asList(
            "home", "sethome", "delhome"
    );

    public ClanTabCompleter(ClanManager clanManager) {
        this.clanManager = clanManager;
        this.useGui = clanManager.getPlugin().getConfig().getString("command-system", "COMMAND")
                .equalsIgnoreCase("GUI");
    }

    private List<String> filter(List<String> list, String input) {
        List<String> out = new ArrayList<>();
        String lower = input.toLowerCase();
        for (String s : list) if (s.toLowerCase().startsWith(lower)) out.add(s);
        return out;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!(sender instanceof Player p)) return Collections.emptyList();

        if (useGui) {

            if (args.length == 1) {

                List<String> out = new ArrayList<>(SUBCOMMANDS_GUI);

                Clan clan = clanManager.getClanOf(p.getUniqueId());

                if (clan == null || (!clan.isOwner(p.getUniqueId()) && !clan.isCoOwner(p.getUniqueId()))) {
                    out.remove("sethome");
                    out.remove("delhome");
                }

                return filter(out, args[0]);
            }

            return Collections.emptyList();
        }

        if (args.length == 1) return filter(SUBCOMMANDS_COMMAND, args[0]);

        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("info"))
                return filter(clanManager.getAllClanTags(), args[1]);

            if (args[0].equalsIgnoreCase("disband") && sender.isOp())
                return filter(clanManager.getAllClanTags(), args[1]);

            if (args[0].equalsIgnoreCase("color") && sender.isOp())
                return filter(clanManager.getAllClanTags(), args[1]);

            if (args[0].equalsIgnoreCase("top"))
                return filter(clanManager.getAllClanTags(), args[1]);

            switch (args[0].toLowerCase()) {
                case "ff":
                    return filter(Arrays.asList("on", "off"), args[1]);

                case "promote":
                case "demote":
                case "kick":
                case "invite":
                    List<String> players = Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName).toList();
                    return filter(players, args[1]);

                case "create":
                    return filter(Collections.singletonList("<name>"), args[1]);

                case "accept":
                    return filter(Collections.singletonList("<tag>"), args[1]);

                case "rename":
                    return filter(Arrays.asList("name", "tag"), args[1]);
            }
        }

        if (args[0].equalsIgnoreCase("info") && args.length >= 3)
            return Collections.emptyList();

        if (args.length == 3 && args[0].equalsIgnoreCase("create"))
            return filter(Collections.singletonList("<tag>"), args[2]);

        if (args.length == 3 && args[0].equalsIgnoreCase("rename")) {
            if (args[1].equalsIgnoreCase("name"))
                return filter(Collections.singletonList("<new-name>"), args[2]);
            if (args[1].equalsIgnoreCase("tag"))
                return filter(Collections.singletonList("<new-tag>"), args[2]);
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("color") && sender.isOp())
            return filter(Arrays.asList("red", "green", "blue", "yellow", "aqua", "white", "gray",
                    "darkred", "darkgreen", "darkblue"), args[2]);

        return Collections.emptyList();
    }
}
