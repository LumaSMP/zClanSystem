package com.zero.zClanSystem.clan;

import com.zero.zClanSystem.files.FileManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ClanManager {

    private final FileManager fileManager;
    private final JavaPlugin plugin;

    private final Map<String, Clan> clansByName = new HashMap<>();
    private final Map<String, Clan> clansByTag = new HashMap<>();
    private final Map<UUID, Clan> clansByPlayer = new HashMap<>();
    private final Map<UUID, InviteData> pendingInvites = new HashMap<>();

    public ClanManager(FileManager fileManager, JavaPlugin plugin) {
        this.fileManager = fileManager;
        this.plugin = plugin;

        loadCreateCosts();
        loadClans();
    }

    public int getMaxTagLength() {
        return plugin.getConfig().getInt("max-tag-length", 3);
    }

    public ClanCreateCostType getCostType() {
        String mode = plugin.getConfig().getString("clan-create-cost", "NONE").toUpperCase();
        try {
            return ClanCreateCostType.valueOf(mode);
        } catch (Exception e) {
            return ClanCreateCostType.NONE;
        }
    }

    public List<ItemStack> getItemCost() {
        List<ItemStack> cost = new ArrayList<>();

        if (getCostType() != ClanCreateCostType.ITEM) {
            return cost;
        }

        List<?> list = plugin.getConfig().getList("clan-create-item-cost");
        if (list != null) {
            for (Object o : list) {
                if (o instanceof Map<?, ?> map) {

                    String matStr = (String) map.get("material");
                    Object amountObj = map.get("amount");
                    int amount = 1;

                    if (amountObj instanceof Number num) {
                        amount = num.intValue();
                    }

                    Material mat = Material.matchMaterial(matStr);
                    if (mat != null && amount > 0) {
                        cost.add(new ItemStack(mat, amount));
                    }
                }
            }
        }

        return cost;
    }

    private void loadCreateCosts() {}

    private boolean hasRequiredItems(Player player) {
        for (ItemStack req : getItemCost()) {
            if (!player.getInventory().containsAtLeast(req, req.getAmount())) {
                return false;
            }
        }
        return true;
    }

    private void removeRequiredItems(Player player) {
        for (ItemStack req : getItemCost()) {
            player.getInventory().removeItem(req);
        }
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public boolean clanExistsByName(String name) {
        return clansByName.containsKey(name.toLowerCase());
    }

    public boolean clanExistsByTag(String tag) {
        return clansByTag.containsKey(tag.toLowerCase());
    }

    public Clan getClanOf(UUID uuid) {
        return clansByPlayer.get(uuid);
    }

    public Clan getClanByTag(String tag) {
        return clansByTag.get(tag.toLowerCase());
    }

    public Collection<Clan> getAllClans() {
        return clansByName.values();
    }

    public List<String> getAllClanTags() {
        return new ArrayList<>(clansByTag.keySet());
    }

    public List<String> getAllClanNames() {
        return new ArrayList<>(clansByName.keySet());
    }

    public void log(String message) {
        Bukkit.getLogger().info("[ClanSystem] " + message);
    }

    public boolean clanNameExists(String name) { return clansByName.containsKey(name.toLowerCase()); }

    public boolean clanTagExists(String tag) { return clansByTag.containsKey(tag.toLowerCase()); }

    public Clan getClanByName(String name) {
        if (name == null) return null;
        return clansByName.get(name.toLowerCase());
    }

    public void broadcastToClan(Clan clan, String message) {
        for (UUID uuid : clan.getAllPlayers()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null && p.isOnline()) {
                p.sendMessage(message);
            }
        }
    }

    public void playSoundToClan(Clan clan, Sound sound, float volume, float pitch) {
        for (UUID uuid : clan.getAllPlayers()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null && p.isOnline()) {
                p.playSound(p.getLocation(), sound, volume, pitch);
            }
        }
    }

    public void addMember(Clan clan, UUID uuid) {
        clan.addMember(uuid);

        clan.removeInvited(uuid);
        pendingInvites.remove(uuid);

        clansByPlayer.put(uuid, clan);
        saveClans();

        Player p = Bukkit.getPlayer(uuid);
        if (p != null) {
            applyClanSuffix(p);
            updateTablist(p);
        }
    }

    public void removeMember(Clan clan, UUID uuid) {
        clan.removeMember(uuid);

        clan.removeInvited(uuid);
        pendingInvites.remove(uuid);

        clansByPlayer.remove(uuid);
        saveClans();

        Player p = Bukkit.getPlayer(uuid);
        if (p != null) {
            applyClanSuffix(p);
            updateTablist(p);
        }
    }

    public void kickPlayer(Clan clan, UUID uuid, Player admin) {

        OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);

        removeMember(clan, uuid);

        if (admin != null) {
            admin.sendMessage("§aPlayer §f" + target.getName() + " §ahas been kicked.");
        }

        Player online = Bukkit.getPlayer(uuid);
        if (online != null) {
            online.sendMessage("§cYou have been kicked from your clan.");
        }

        log("Admin " + admin.getName() + " kicked " + target.getName() + " from clan " + clan.getName());
    }

    public void invitePlayer(Clan clan, UUID uuid) {

        boolean expireEnabled = plugin.getConfig().getBoolean("Invite-Expire");
        int expireMinutes = plugin.getConfig().getInt("Invite-Expire-Time");

        long expires;

        if (expireEnabled) {
            expires = System.currentTimeMillis() + (expireMinutes * 60_000L);
        } else {
            expires = Long.MAX_VALUE;
        }

        pendingInvites.put(uuid, new InviteData(clan.getTag().toLowerCase(), expires));
        clan.addInvited(uuid);

        saveClans();
    }

    public void startInviteExpirationTask() {

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            Iterator<Map.Entry<UUID, InviteData>> it = pendingInvites.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<UUID, InviteData> entry = it.next();
                UUID uuid = entry.getKey();
                InviteData data = entry.getValue();

                if (data.isExpired()) {

                    Clan clan = clansByTag.get(data.getClanTag());
                    if (clan != null) {
                        clan.removeInvited(uuid);
                    }

                    it.remove();
                }
            }

            saveClans();

        }, 20L, 20L * 30);
    }

    public boolean hasValidInvite(UUID uuid, String tag) {
        InviteData data = pendingInvites.get(uuid);
        if (data == null) return false;

        if (data.isExpired()) {
            pendingInvites.remove(uuid);
            return false;
        }

        return data.getClanTag().equalsIgnoreCase(tag);
    }

    public void clearInvite(UUID uuid) {
        pendingInvites.remove(uuid);
    }

    public int getPendingInvitesCount(Clan clan) {
        return clan.getInvited().size();
    }

    public List<UUID> getPendingInvites(Clan clan) {
        return new ArrayList<>(clan.getInvited());
    }

    public Map<UUID, InviteData> getPendingInviteMap() {
        return pendingInvites;
    }

    public void removeInvite(Clan clan, UUID uuid) {
        clan.removeInvited(uuid);
        pendingInvites.remove(uuid);
        saveClans();
    }

    public void clearPendingInvites(Clan clan) {
        for (UUID uuid : clan.getInvited()) {
            pendingInvites.remove(uuid);
        }
        clan.clearInvited();
        saveClans();
    }

    public Clan createClan(String name, String tag, UUID owner) {
        Clan clan = new Clan(name, tag, owner);

        clansByName.put(name.toLowerCase(), clan);
        clansByTag.put(tag.toLowerCase(), clan);
        clansByPlayer.put(owner, clan);

        saveClans();
        return clan;
    }

    public void createClanFull(Player player, String name, String tag) {

        if (getCostType() == ClanCreateCostType.ITEM) {

            if (!hasRequiredItems(player)) {
                player.sendMessage("§cYou don't have the required items to create a clan.");
                return;
            }

            removeRequiredItems(player);
        }

        UUID uuid = player.getUniqueId();

        Clan clan = createClan(name, tag, uuid);

        applyClanSuffix(player);
        updateTablist(player);

        player.sendMessage("§aClan created successfully!");
        player.sendMessage("§7Name: §f" + clan.getName());
        player.sendMessage("§7Tag: §f" + clan.getTag());

        broadcastToClan(clan, "§a" + player.getName() + " created the clan!");

        log(player.getName() + " created clan " + clan.getName() + " (" + clan.getTag() + ")");
    }

    public void disbandClan(Clan clan) {
        for (UUID uuid : clan.getInvited()) {
            pendingInvites.remove(uuid);
        }
        clan.clearInvited();

        clansByName.remove(clan.getName().toLowerCase());
        clansByTag.remove(clan.getTag().toLowerCase());

        for (UUID uuid : clan.getAllPlayers()) {
            clansByPlayer.remove(uuid);

            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                applyClanSuffix(p);
                updateTablist(p);
            }
        }

        var config = fileManager.getClansConfig();
        config.set("clans." + clan.getName(), null);
        fileManager.saveClansConfig();
    }

    public void forceDisband(Clan clan) {

        broadcastToClan(clan, "§cThe clan §f" + clan.getName() + " §chas been force-disbanded by an administrator.");

        disbandClan(clan);
    }

    public void saveClans() {
        var config = fileManager.getClansConfig();
        config.set("clans", null);

        for (Clan clan : clansByName.values()) {
            String path = "clans." + clan.getName();

            config.set(path + ".tag", clan.getTag());
            config.set(path + ".owner", clan.getOwner().toString());

            List<String> members = clan.getMembers().stream()
                    .map(UUID::toString)
                    .toList();
            config.set(path + ".members", members);

            List<String> coOwners = clan.getCoOwners().stream()
                    .map(UUID::toString)
                    .toList();
            config.set(path + ".coOwners", coOwners);

            List<String> invited = clan.getInvited().stream()
                    .map(UUID::toString)
                    .toList();
            config.set(path + ".invited", invited);

            config.set(path + ".friendlyFire", clan.isFriendlyFire());
            config.set(path + ".tagColor", clan.getTagColor());
            config.set(path + ".teamkills", clan.getTeamkills());
            config.set(path + ".extra-points", clan.getExtraPoints());
            config.set(path + ".points", calculateClanScore(clan));

            if (clan.getHome() != null) {
                Location loc = clan.getHome();
                config.set(path + ".home.world", loc.getWorld().getName());
                config.set(path + ".home.x", loc.getX());
                config.set(path + ".home.y", loc.getY());
                config.set(path + ".home.z", loc.getZ());
                config.set(path + ".home.yaw", loc.getYaw());
                config.set(path + ".home.pitch", loc.getPitch());
            }
        }

        fileManager.saveClansConfig();
    }

    public void loadClans() {
        var config = fileManager.getClansConfig();

        if (!config.contains("clans")) return;

        ConfigurationSection section = config.getConfigurationSection("clans");
        if (section == null) return;

        for (String name : section.getKeys(false)) {

            String tag = config.getString("clans." + name + ".tag");
            UUID owner = UUID.fromString(config.getString("clans." + name + ".owner"));

            Clan clan = new Clan(name, tag, owner);

            clansByPlayer.put(owner, clan);

            List<String> members = config.getStringList("clans." + name + ".members");
            for (String uuidStr : members) {
                UUID uuid = UUID.fromString(uuidStr);
                clan.addMember(uuid);

                clansByPlayer.put(uuid, clan);
            }

            List<String> coOwners = config.getStringList("clans." + name + ".coOwners");
            for (String uuidStr : coOwners) {
                UUID uuid = UUID.fromString(uuidStr);
                clan.addCoOwner(uuid);

                clansByPlayer.put(uuid, clan);
            }

            List<String> invited = config.getStringList("clans." + name + ".invited");
            for (String uuidStr : invited) {
                UUID uuid = UUID.fromString(uuidStr);
                clan.addInvited(uuid);
            }

            clan.setFriendlyFire(config.getBoolean("clans." + name + ".friendlyFire"));

            if (config.contains("clans." + name + ".tagColor")) {
                clan.setTagColor(config.getString("clans." + name + ".tagColor"));
            }

            clan.setTeamkills(config.getInt("clans." + name + ".teamkills", 0));
            clan.setExtraPoints(config.getInt("clans." + name + ".extra-points", 0));

            if (config.contains("clans." + name + ".home")) {
                String worldName = config.getString("clans." + name + ".home.world");
                World world = Bukkit.getWorld(worldName);

                double x = config.getDouble("clans." + name + ".home.x");
                double y = config.getDouble("clans." + name + ".home.y");
                double z = config.getDouble("clans." + name + ".home.z");
                float yaw = (float) config.getDouble("clans." + name + ".home.yaw");
                float pitch = (float) config.getDouble("clans." + name + ".home.pitch");

                if (world != null) {
                    clan.setHome(new Location(world, x, y, z, yaw, pitch));
                }
            }

            // Clan registrieren
            clansByName.put(name.toLowerCase(), clan);
            clansByTag.put(tag.toLowerCase(), clan);
        }
    }

    public void reloadClans() {
        fileManager.reloadClansConfig();
        clansByName.clear();
        clansByTag.clear();
        clansByPlayer.clear();
        loadClans();

        var scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        for (var team : scoreboard.getTeams()) {
            if (team.getName().startsWith("clan_suffix_")) {
                team.unregister();
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            applyClanSuffix(p);
            updateTablist(p);
        }
    }

    public void reloadConfigFile() {
        plugin.reloadConfig();
    }

    public void applyClanSuffix(Player player) {
        Clan clan = getClanOf(player.getUniqueId());
        var scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        String teamName = "clan_suffix_" + player.getName().toLowerCase();

        var team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }

        team.setPrefix("");

        if (clan != null) {
            team.setSuffix(" " + "§7[§r" + clan.getTagColor() + clan.getTag() + "§7]§r");
        } else {
            team.setSuffix("");
        }

        if (!team.hasEntry(player.getName())) {
            team.addEntry(player.getName());
        }
    }

    public void updateTablist(Player player) {
        Clan clan = getClanOf(player.getUniqueId());

        if (clan == null) {
            player.setPlayerListName(player.getName());
            return;
        }

        player.setPlayerListName(
                player.getName() + " " + "§7[§r" + clan.getTagColor() + clan.getTag() + "§7]§r"
        );
    }

    public void renameClanName(Clan clan, String newName) {

        String oldName = clan.getName();

        if (oldName.equalsIgnoreCase(newName)) {
            return;
        }
        clansByName.remove(oldName.toLowerCase());
        clan.setNameInternal(newName);
        clansByName.put(newName.toLowerCase(), clan);
        saveClans();
        for (UUID uuid : clan.getAllPlayers()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                applyClanSuffix(p);
                updateTablist(p);
            }
        }
        broadcastToClan(clan, "§aClan name changed to §f" + newName + "§a.");
    }

    public void renameClanTag(Clan clan, String newTag) {

        String oldTag = clan.getTag();

        if (oldTag.equalsIgnoreCase(newTag)) {
            return;
        }
        clansByTag.remove(oldTag.toLowerCase());
        clan.setTagInternal(newTag);
        clansByTag.put(newTag.toLowerCase(), clan);
        saveClans();

        for (UUID uuid : clan.getAllPlayers()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                applyClanSuffix(p);
                updateTablist(p);
            }
        }
        broadcastToClan(clan, "§aClan tag changed to §f" + newTag + "§a.");
    }

    public void setClanColor(Clan clan, String colorCode) {
        clan.setTagColor(colorCode);
        saveClans();

        for (UUID uuid : clan.getAllPlayers()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                applyClanSuffix(p);
                updateTablist(p);
            }
        }
    }

    public int calculateClanScore(Clan clan) {

        int score = 0;

        for (UUID uuid : clan.getAllPlayers()) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);

            if (!op.hasPlayedBefore()) continue;

            int kills = op.getStatistic(Statistic.PLAYER_KILLS);
            int deaths = op.getStatistic(Statistic.DEATHS);

            score += plugin.getConfig().getInt("clan-score.base-points", 4);
            score += kills * plugin.getConfig().getInt("clan-score.kill-points", 6);
            score += deaths * plugin.getConfig().getInt("clan-score.death-points", -2);
        }

        score += clan.getTeamkills() * plugin.getConfig().getInt("clan-score.teamkill-points", -4);

        if (clan.getTagColor() != null && !clan.getTagColor().equals("§7")) {
            score += plugin.getConfig().getInt("clan-score.tagcolor-bonus", 30);
        }

        score += clan.getExtraPoints();

        return score;
    }

    public void setClanHome(Clan clan, Location loc) {
        clan.setHome(loc);
        saveClans();
    }

    public void deleteClanHome(Clan clan) {
        clan.setHome(null);
        saveClans();
    }

    public void transferOwnership(Clan clan, UUID newOwner, Player executor) {

        UUID oldOwner = clan.getOwner();

        clan.removeCoOwner(newOwner);
        clan.removeMember(newOwner);

        clan.addMember(oldOwner);

        clan.setOwnerInternal(newOwner);

        saveClans();

        Player p = Bukkit.getPlayer(newOwner);
        if (p != null) {
            applyClanSuffix(p);
            updateTablist(p);
            p.sendMessage("§aYou are now the clan owner!");
        }

        if (executor != null) {
            executor.sendMessage("§aOwnership transferred.");
        }

        broadcastToClan(clan, "§e" + Bukkit.getOfflinePlayer(newOwner).getName() + " is now the clan owner.");
    }

    public void promoteToCoOwner(Clan clan, UUID uuid, Player executor) {
        if (clan.getOwner().equals(uuid)) {
            if (executor != null) executor.sendMessage("§cYou cannot promote the clan owner.");
            return;
        }
        if (clan.getCoOwners().contains(uuid)) {
            if (executor != null) executor.sendMessage("§eThis player is already a Co‑Owner.");
            return;
        }

        clan.addCoOwner(uuid);
        clan.removeMember(uuid);

        saveClans();

        Player p = Bukkit.getPlayer(uuid);
        if (p != null) {
            applyClanSuffix(p);
            updateTablist(p);
            p.sendMessage("§aYou have been promoted to Co‑Owner!");
        }

        if (executor != null) {
            executor.sendMessage("§aPlayer promoted to Co‑Owner.");
        }

        broadcastToClan(clan, "§a" + Bukkit.getOfflinePlayer(uuid).getName() + " has been promoted to Co‑Owner.");
    }

    public void demoteToMember(Clan clan, UUID uuid, Player executor) {
        if (clan.getOwner().equals(uuid)) {
            if (executor != null) executor.sendMessage("§cYou cannot demote the clan owner.");
            return;
        }
        if (!clan.getCoOwners().contains(uuid)) {
            if (executor != null) executor.sendMessage("§eThis player is not a Co‑Owner.");
            return;
        }

        clan.removeCoOwner(uuid);
        clan.addMember(uuid);

        saveClans();

        Player p = Bukkit.getPlayer(uuid);
        if (p != null) {
            applyClanSuffix(p);
            updateTablist(p);
            p.sendMessage("§cYou have been demoted to Member.");
        }

        if (executor != null) {
            executor.sendMessage("§cPlayer demoted to Member.");
        }

        broadcastToClan(clan, "§e" + Bukkit.getOfflinePlayer(uuid).getName() + " has been demoted to Member.");
    }
}
