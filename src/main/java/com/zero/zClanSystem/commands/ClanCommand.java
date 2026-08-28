package com.zero.zClanSystem.commands;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.main.ClanMainMenuGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.ArrayList;

public class ClanCommand implements CommandExecutor {

    private final ClanManager clanManager;
    private final Set<UUID> teleportingPlayers = new HashSet<>();
    private final boolean useGui;

    public ClanCommand(ClanManager clanManager) {
        this.clanManager = clanManager;
        this.useGui = clanManager.getPlugin().getConfig().getString("command-system", "COMMAND")
                .equalsIgnoreCase("GUI");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        if (useGui) {

            // /clan -> GUI
            if (args.length == 0) {
                new ClanMainMenuGUI(clanManager).open(player);
                return true;
            }

            if (args.length >= 1) {
                switch (args[0].toLowerCase()) {
                    case "home" -> {
                        handleHome(player);
                        return true;
                    }
                    case "sethome" -> {
                        handleSetHome(player);
                        return true;
                    }
                    case "delhome" -> {
                        handleDelHome(player);
                        return true;
                    }
                }
            }

            return true;
        }

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create" -> handleCreate(player, args);
            case "disband" -> handleDisband(player, args);
            case "sethome" -> handleSetHome(player);
            case "home" -> handleHome(player);
            case "ff" -> handleFriendlyFire(player, args);
            case "promote" -> handlePromote(player, args);
            case "demote" -> handleDemote(player, args);
            case "leave" -> handleLeave(player);
            case "kick" -> handleKick(player, args);
            case "invite" -> handleInvite(player, args);
            case "accept" -> handleAccept(player, args);
            case "rename" -> handleRename(player, args);
            case "info" -> handleInfo(player, args);
            case "color" -> handleColor(player, args);
            case "top" -> handleTop(player, args);
            default -> sendHelp(player);
        }

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage("§8§m------------------------------");
        player.sendMessage("§7§lClan Commands");
        player.sendMessage("§7/clan create <name> <tag>");
        player.sendMessage("§7/clan disband");
        player.sendMessage("§7/clan sethome");
        player.sendMessage("§7/clan home");
        player.sendMessage("§7/clan ff <on/off>");
        player.sendMessage("§7/clan info (<tag>)");
        player.sendMessage("§7/clan promote <player>");
        player.sendMessage("§7/clan demote <player>");
        player.sendMessage("§7/clan invite <player>");
        player.sendMessage("§7/clan accept <tag>");
        player.sendMessage("§7/clan kick <player>");
        player.sendMessage("§7/clan top (<tag>)");
        player.sendMessage("§7/clan color <tag> <color>");
        player.sendMessage("§7/clan leave");
        player.sendMessage("§8§m------------------------------");
    }

    private void handleCreate(Player player, String[] args) {

        if (args.length < 3) {
            player.sendMessage("§cUsage: /clan create <name> <tag>");
            return;
        }

        String name = args[1];
        String tag = args[2];

        int max = clanManager.getMaxTagLength();
        if (tag.length() > max) {
            player.sendMessage("§cClan tags can only be up to " + max + " characters long.");
            return;
        }

        if (clanManager.getClanOf(player.getUniqueId()) != null) {
            player.sendMessage("§cYou are already in a clan.");
            return;
        }

        if (clanManager.clanExistsByName(name)) {
            player.sendMessage("§cA clan with this name already exists.");
            return;
        }

        if (clanManager.clanExistsByTag(tag)) {
            player.sendMessage("§cA clan with this tag already exists.");
            return;
        }

        Clan clan = clanManager.createClan(name, tag, player.getUniqueId());

        player.sendMessage("§aClan created successfully!");

        // Update suffix and tablist for the owner
        clanManager.applyClanSuffix(player);
        clanManager.updateTablist(player);

        player.sendMessage("§7Name: §f" + clan.getName());
        player.sendMessage("§7Tag: §f" + clan.getTag());

        clanManager.broadcastToClan(clan, "§a" + player.getName() + " created the clan!");
        clanManager.log(player.getName() + " created clan " + clan.getName() + " (" + clan.getTag() + ")");
    }

    private void handleDisband(Player player, String[] args) {

        // OP disband by tag
        if (player.isOp() && args.length >= 2 && args[0].equalsIgnoreCase("disband")) {

            String tag = args[1].toLowerCase();
            Clan targetClan = clanManager.getClanByTag(tag);

            if (targetClan == null) {
                player.sendMessage("§cNo clan with this tag exists.");
                return;
            }

            clanManager.broadcastToClan(targetClan, "§cYour clan has been force-disbanded by an operator.");
            clanManager.disbandClan(targetClan);

            player.sendMessage("§cClan §f" + targetClan.getName() + " §cwas disbanded by an operator.");
            clanManager.log("OP " + player.getName() + " force-disbanded clan " + targetClan.getName());
            return;
        }

        Clan clan = clanManager.getClanOf(player.getUniqueId());

        if (clan == null) {
            player.sendMessage("§cYou are not in a clan.");
            return;
        }

        if (!clan.isOwner(player.getUniqueId())) {
            player.sendMessage("§cOnly the clan owner can disband the clan.");
            return;
        }

        clanManager.broadcastToClan(clan, "§cYour clan has been disbanded by " + player.getName() + ".");
        clanManager.disbandClan(clan);

        player.sendMessage("§cYour clan has been disbanded.");
        clanManager.log(player.getName() + " disbanded clan " + clan.getName());
    }

    private void handleSetHome(Player player) {

        Clan clan = clanManager.getClanOf(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cYou are not in a clan.");
            return;
        }

        if (!clan.isOwner(player.getUniqueId())) {
            player.sendMessage("§cOnly the clan owner can set the clan home.");
            return;
        }

        clan.setHome(player.getLocation());
        clanManager.saveClans();

        player.sendMessage("§aClan home has been set to your current location.");
    }

    private void handleDelHome(Player player) {

        var clan = clanManager.getClanOf(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cYou are not in a clan.");
            return;
        }

        if (!clan.isOwner(player.getUniqueId()) && !clan.isCoOwner(player.getUniqueId())) {
            player.sendMessage("§cOnly the clan owner or co-owner can delete the clan home.");
            return;
        }

        clanManager.deleteClanHome(clan);
        player.sendMessage("§cClan home deleted.");
    }

    private void handleHome(Player player) {

        Clan clan = clanManager.getClanOf(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cYou are not in a clan.");
            return;
        }

        if (clan.getHome() == null) {
            player.sendMessage("§cYour clan does not have a home set.");
            return;
        }

        if (teleportingPlayers.contains(player.getUniqueId())) {
            player.sendMessage("§cYou are already teleporting!");
            return;
        }

        teleportingPlayers.add(player.getUniqueId());

        player.sendMessage("§eTeleporting to clan home in §f20 seconds§e...");
        player.sendMessage("§7Do not move or sneak, or the teleport will cancel.");

        final var startLocation = player.getLocation().clone();
        final int[] taskId = new int[1];

        taskId[0] = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                clanManager.getPlugin(),
                new Runnable() {

                    int timeLeft = 20;

                    @Override
                    public void run() {

                        if (!player.isOnline()) {
                            cancel();
                            return;
                        }

                        if (player.getLocation().distanceSquared(startLocation) > 0.1) {
                            player.sendMessage("§cTeleport cancelled because you moved.");
                            cancel();
                            return;
                        }

                        if (player.isSneaking()) {
                            player.sendMessage("§cTeleport cancelled because you sneaked.");
                            cancel();
                            return;
                        }

                        player.sendActionBar("§eTeleporting in §6" + timeLeft + "§es...");
                        player.playSound(player.getLocation(), "minecraft:block.note_block.hat", 0.3f, 1.2f);

                        if (timeLeft > 0) {
                            timeLeft--;
                            return;
                        }

                        player.teleport(clan.getHome());
                        player.sendMessage("§aTeleported to clan home.");
                        cancel();
                    }

                    private void cancel() {
                        Bukkit.getScheduler().cancelTask(taskId[0]);
                        teleportingPlayers.remove(player.getUniqueId());
                    }

                },
                0L, 20L
        );
    }

    private void handleFriendlyFire(Player player, String[] args) {

        Clan clan = clanManager.getClanOf(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cYou are not in a clan.");
            return;
        }

        if (!clan.isStaff(player.getUniqueId())) {
            player.sendMessage("§cOnly the owner or co-owner can change friendly fire.");
            return;
        }

        if (args.length < 2) {
            player.sendMessage("§cUsage: /clan ff <on/off>");
            return;
        }

        switch (args[1].toLowerCase()) {
            case "on" -> {
                clan.setFriendlyFire(true);
                clanManager.saveClans();
                player.sendMessage("§aFriendly fire is now §fON§a.");
                clanManager.broadcastToClan(clan, "§eFriendly Fire was turned §cON §eby " + player.getName() + ".");
            }
            case "off" -> {
                clan.setFriendlyFire(false);
                clanManager.saveClans();
                player.sendMessage("§aFriendly fire is now §fOFF§a.");
                clanManager.broadcastToClan(clan, "§eFriendly Fire was turned §aOFF §eby " + player.getName() + ".");
            }
            default -> player.sendMessage("§cUsage: /clan ff <on/off>");
        }
    }

    private void handlePromote(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage("§cUsage: /clan promote <player>");
            return;
        }

        Clan clan = clanManager.getClanOf(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cYou are not in a clan.");
            return;
        }

        if (!clan.isOwner(player.getUniqueId())) {
            player.sendMessage("§cOnly the clan owner can promote members.");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage("§cPlayer not found.");
            return;
        }

        if (!clan.isMember(target.getUniqueId())) {
            player.sendMessage("§cThis player is not in your clan.");
            return;
        }

        if (clan.isOwner(target.getUniqueId())) {
            player.sendMessage("§cYou cannot promote the owner.");
            return;
        }

        if (clan.isCoOwner(target.getUniqueId())) {
            player.sendMessage("§cThis player is already a co-owner.");
            return;
        }

        clan.addCoOwner(target.getUniqueId());
        clanManager.saveClans();

        player.sendMessage("§aYou promoted §f" + target.getName() + " §ato co-owner.");
        target.sendMessage("§eYou have been promoted to §fCo-Owner §ein clan §f" + clan.getName());
    }

    private void handleDemote(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage("§cUsage: /clan demote <player>");
            return;
        }

        Clan clan = clanManager.getClanOf(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cYou are not in a clan.");
            return;
        }

        if (!clan.isOwner(player.getUniqueId())) {
            player.sendMessage("§cOnly the clan owner can demote co-owners.");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage("§cPlayer not found.");
            return;
        }

        if (!clan.isMember(target.getUniqueId())) {
            player.sendMessage("§cThis player is not in your clan.");
            return;
        }

        if (clan.isOwner(target.getUniqueId())) {
            player.sendMessage("§cYou cannot demote the owner.");
            return;
        }

        if (!clan.isCoOwner(target.getUniqueId())) {
            player.sendMessage("§cThis player is not a co-owner.");
            return;
        }

        clan.removeCoOwner(target.getUniqueId());
        clanManager.saveClans();

        player.sendMessage("§eYou demoted §f" + target.getName() + " §eto member.");
        target.sendMessage("§cYou have been demoted to §fMember §cin clan §f" + clan.getName());
    }

    private void handleLeave(Player player) {

        Clan clan = clanManager.getClanOf(player.getUniqueId());

        if (clan == null) {
            player.sendMessage("§cYou are not in a clan.");
            return;
        }

        if (clan.isOwner(player.getUniqueId())) {
            player.sendMessage("§cYou cannot leave your own clan. Use /clan disband instead.");
            return;
        }

        clanManager.removeMember(clan, player.getUniqueId());

        player.sendMessage("§eYou have left the clan §f" + clan.getName());
        clanManager.broadcastToClan(clan, "§e" + player.getName() + " left the clan.");
    }

    private void handleKick(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage("§cUsage: /clan kick <player>");
            return;
        }

        Clan clan = clanManager.getClanOf(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cYou are not in a clan.");
            return;
        }

        if (!clan.isStaff(player.getUniqueId())) {
            player.sendMessage("§cOnly the owner or co-owner can kick players.");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage("§cPlayer not found.");
            return;
        }

        if (!clan.isMember(target.getUniqueId())) {
            player.sendMessage("§cThis player is not in your clan.");
            return;
        }

        boolean isPlayerOwner = clan.isOwner(player.getUniqueId());
        boolean isTargetOwner = clan.isOwner(target.getUniqueId());
        boolean isTargetCoOwner = clan.isCoOwner(target.getUniqueId());

        if (isTargetOwner && !isPlayerOwner) {
            player.sendMessage("§cYou cannot kick the clan owner.");
            return;
        }

        if (isTargetCoOwner && !isPlayerOwner) {
            player.sendMessage("§cYou cannot kick another co-owner.");
            return;
        }

        clanManager.removeMember(clan, target.getUniqueId());

        player.sendMessage("§eYou kicked §f" + target.getName() + " §efrom the clan.");
        target.sendMessage("§cYou have been kicked from the clan §f" + clan.getName());

        clanManager.broadcastToClan(clan, "§c" + target.getName() + " was kicked by " + player.getName() + ".");
        clanManager.log(player.getName() + " kicked " + target.getName() + " from clan " + clan.getName());
    }

    private void handleInvite(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage("§cUsage: /clan invite <player>");
            return;
        }

        Clan clan = clanManager.getClanOf(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cYou are not in a clan.");
            return;
        }

        if (!clan.isStaff(player.getUniqueId())) {
            player.sendMessage("§cOnly the owner or co-owner can invite players.");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage("§cPlayer not found.");
            return;
        }

        if (clanManager.getClanOf(target.getUniqueId()) != null) {
            player.sendMessage("§cThis player is already in a clan.");
            return;
        }

        clanManager.invitePlayer(clan, target.getUniqueId());

        player.sendMessage("§aInvitation sent to §f" + target.getName());
        target.playSound(target.getLocation(), "entity.experience_orb.pickup", 1f, 1f);
        TextComponent base = new TextComponent("§eYou have been invited to join clan §f" + clan.getName() + "§e!");

        TextComponent accept = new TextComponent(" §8[§aACCEPT§8]");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan accept " + clan.getTag()));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§aClick to join the clan").create()));

        base.addExtra(accept);

        target.spigot().sendMessage(base);

        target.sendMessage("§7You can also type §a/clan accept " + clan.getTag() + " §7to join.");
    }

    private void handleAccept(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage("§cUsage: /clan accept <tag>");
            return;
        }

        String tag = args[1];

        if (!clanManager.hasValidInvite(player.getUniqueId(), tag)) {
            player.sendMessage("§cYou do not have a valid invite for this clan.");
            return;
        }

        Clan clan = clanManager.getClanByTag(tag);
        if (clan == null) {
            player.sendMessage("§cClan not found.");
            return;
        }

        if (clanManager.getClanOf(player.getUniqueId()) != null) {
            player.sendMessage("§cYou are already in a clan.");
            clanManager.clearInvite(player.getUniqueId());
            return;
        }

        clanManager.addMember(clan, player.getUniqueId());
        clanManager.clearInvite(player.getUniqueId());

        player.sendMessage("§aYou have joined the clan §f" + clan.getName());
        clanManager.broadcastToClan(clan, "§a" + player.getName() + " joined the clan!");
    }

    private void handleInfo(Player player, String[] args) {

        // /clan info
        if (args.length == 1) {
            player.sendMessage("§8§m------------------------------");
            player.sendMessage("§a§lAll clans:");

            for (Clan clan : clanManager.getAllClans()) {
                player.sendMessage("§7- §f" + clan.getName() + " §8[§f" + clan.getTag() + "§8]");
            }

            player.sendMessage("§8§m------------------------------");
            return;
        }

        // /clan info <tag>
        String tag = args[1];
        Clan clan = clanManager.getClanByTag(tag);

        if (clan == null) {
            player.sendMessage("§cClan not found.");
            return;
        }

        player.sendMessage("§8§m------------------------------");
        player.sendMessage("§a§lClan information");

        player.sendMessage("§7Name: §f" + clan.getName());
        player.sendMessage("§7Tag: §f" + clan.getTag());

        // Owner
        String ownerName = Bukkit.getOfflinePlayer(clan.getOwner()).getName();
        player.sendMessage("§7Owner: §f" + ownerName);

        // Co-Owners
        if (clan.getCoOwners().isEmpty()) {
            player.sendMessage("§7Co-Owners: §fNone");
        } else {
            String coList = clan.getCoOwners().stream()
                    .map(uuid -> Bukkit.getOfflinePlayer(uuid).getName())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("None");
            player.sendMessage("§7Co-Owners: §f" + coList);
        }

        // Members
        String memberList = clan.getMembers().stream()
                .filter(uuid -> !uuid.equals(clan.getOwner()) && !clan.isCoOwner(uuid))
                .map(uuid -> Bukkit.getOfflinePlayer(uuid).getName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("None");

        player.sendMessage("§7Members: §f" + memberList);

        // Friendly Fire
        player.sendMessage("§7Friendly Fire: " +
                (clan.isFriendlyFire() ? "§cON" : "§aOFF"));

        // Home
        player.sendMessage("§7Home: " +
                (clan.getHome() != null ? "§aSet" : "§cNot set"));

        player.sendMessage("§8§m------------------------------");
    }

    private void handleColor(Player player, String[] args) {

        if (!player.isOp()) {
            player.sendMessage("§cOnly server operators can use this command.");
            return;
        }

        if (args.length < 3) {
            player.sendMessage("§cUsage: /clan color <clan-tag> <color>");
            return;
        }

        String tag = args[1].toLowerCase();
        Clan clan = clanManager.getClanByTag(tag);

        if (clan == null) {
            player.sendMessage("§cClan not found.");
            return;
        }

        String colorName = args[2].toLowerCase();

        String colorCode = switch (colorName) {
            case "red" -> "§c";
            case "green" -> "§a";
            case "blue" -> "§9";
            case "yellow" -> "§e";
            case "aqua" -> "§b";
            case "white" -> "§f";
            case "gray" -> "§7";
            case "darkred" -> "§4";
            case "darkgreen" -> "§2";
            case "darkblue" -> "§1";
            default -> null;
        };

        if (colorCode == null) {
            player.sendMessage("§cInvalid color. Available: red, green, blue, yellow, aqua, white, gray, darkred, darkgreen, darkblue");
            return;
        }

        clan.setTagColor(colorCode);
        clanManager.saveClans();

        for (UUID uuid : clan.getAllPlayers()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                clanManager.applyClanSuffix(p);
                clanManager.updateTablist(p);
            }
        }

        player.sendMessage("§aClan tag color updated to " + colorCode + colorName + "§a.");
    }


    private void handleRename(Player player, String[] args) {

        if (args.length < 3) {
            player.sendMessage("§cUsage: /clan rename <name/tag> <value>");
            return;
        }

        Clan clan = clanManager.getClanOf(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cYou are not in a clan.");
            return;
        }

        if (!clan.isOwner(player.getUniqueId())) {
            player.sendMessage("§cOnly the clan owner can rename the clan.");
            return;
        }

        String type = args[1].toLowerCase();
        String newValue = args[2];

        switch (type) {

            case "name" -> {
                if (clanManager.clanExistsByName(newValue)) {
                    player.sendMessage("§cA clan with this name already exists.");
                    return;
                }

                String oldName = clan.getName();
                clanManager.renameClanName(clan, newValue);

                player.sendMessage("§aClan name changed from §f" + oldName + " §ato §f" + newValue);
            }

            case "tag" -> {
                int max = clanManager.getMaxTagLength();
                if (newValue.length() > max) {
                    player.sendMessage("§cClan tags can only be up to " + max + " characters long.");
                    return;
                }

                if (clanManager.clanExistsByTag(newValue)) {
                    player.sendMessage("§cA clan with this tag already exists.");
                    return;
                }

                String oldTag = clan.getTag();
                clanManager.renameClanTag(clan, newValue);

                player.sendMessage("§aClan tag changed from §f" + oldTag + " §ato §f" + newValue);

                for (UUID uuid : clan.getAllPlayers()) {
                    Player p = Bukkit.getPlayer(uuid);
                    if (p != null) {
                        clanManager.applyClanSuffix(p);
                        clanManager.updateTablist(p);
                    }
                }
            }

            default -> player.sendMessage("§cUsage: /clan rename <name/tag> <value>");
        }
    }

    //good thing the plugin calculates this and not me ;)

    private void handleTop(Player player, String[] args) {

        if (args.length == 2) {

            String tag = args[1].toLowerCase();
            Clan clan = clanManager.getClanByTag(tag);

            if (clan == null) {
                player.sendMessage("§cClan not found.");
                return;
            }

            player.sendMessage("§8§m------------------------------");
            player.sendMessage("§a§lClan points list");
            player.sendMessage("§7Clan: §f" + clan.getName() + " §8[" + clan.getTag() + "§8]");
            player.sendMessage("");

            int total = 0;

            for (UUID uuid : clan.getAllPlayers()) {

                OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);

                int kills = op.getStatistic(Statistic.PLAYER_KILLS);
                int deaths = op.getStatistic(Statistic.DEATHS);

                int base = 4;               // +4 per member
                int killPoints = kills * 6; // +6 per kill
                int deathPoints = deaths * -2; // -2 per death

                int contribution = base + killPoints + deathPoints;

                total += contribution;

                int killContribution = kills * 6;
                int deathContribution = deaths * -2;

                player.sendMessage(
                        "§7" + op.getName() + ": §f" + contribution +
                                " §8(+" + killContribution + "p Kills | " +
                                deathContribution + "p Deaths)"
                );
            }

            int tk = clan.getTeamkills();
            int tkPenalty = tk * -4;
            total += tkPenalty;

            player.sendMessage("");
            player.sendMessage("§7Teamkill penalty: §c" + tkPenalty);

            int extra = clan.getExtraPoints();
            total += extra;

            player.sendMessage("§7Extra points: §e" + extra);

            int tagBonus = 0;
            if (clan.getTagColor() != null && !clan.getTagColor().equals("§7")) {
                tagBonus = 30;
                total += tagBonus;
            }

            player.sendMessage("§7Tag color bonus: §a" + tagBonus);


            player.sendMessage("");
            player.sendMessage("§aTotal clan score: §f" + total);
            player.sendMessage("§8§m------------------------------");
            return;
        }

        var clans = new ArrayList<>(clanManager.getAllClans());

        clans.sort((a, b) -> Integer.compare(
                clanManager.calculateClanScore(b),
                clanManager.calculateClanScore(a)
        ));

        player.sendMessage("§8§m------------------------------");
        player.sendMessage("§a§lTop clan list");

        int rank = 1;
        int lastScore = Integer.MIN_VALUE;

        for (Clan clan : clans) {

            int score = clanManager.calculateClanScore(clan);

            if (score != lastScore) {
                if (lastScore != Integer.MIN_VALUE) {
                    rank++;
                }
            }

            player.sendMessage("§7" + rank + ". §f" + clan.getName()
                    + " §8[" + clan.getTag() + "§8]"
                    + " §7(" + score + "p)");

            lastScore = score;
        }

        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("§7Points are calculated by:");
        player.sendMessage("§7Member-Kills, Member-Deaths,");
        player.sendMessage("§7Members, Color-Tag, extra-Points,");
        player.sendMessage("§8§m------------------------------");
    }
}
