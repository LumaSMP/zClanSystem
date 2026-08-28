package com.zero.zClanSystem.clan;

import org.bukkit.Location;

import java.util.*;

public class Clan {

    private final String name;
    private final String tag;
    private UUID owner;

    private final Set<UUID> members = new HashSet<>();
    private final Set<UUID> coOwners = new HashSet<>();

    private final Set<UUID> invited = new HashSet<>();

    private boolean friendlyFire = false;
    private String tagColor = "§7";
    private Location home;
    private int teamkills = 0;

    private int extraPoints = 0;

    public int getExtraPoints() {
        return extraPoints;
    }

    public void setExtraPoints(int extraPoints) {
        this.extraPoints = extraPoints;
    }

    public Clan(String name, String tag, UUID owner) {
        this.name = name;
        this.tag = tag;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }

    public UUID getOwner() {
        return owner;
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public boolean isMember(UUID uuid) {
        return members.contains(uuid);
    }

    public void addMember(UUID uuid) {
        if (!isOwner(uuid) && !isCoOwner(uuid)) {
            members.add(uuid);
        }
    }

    public void removeMember(UUID uuid) {
        if (!isOwner(uuid)) {
            members.remove(uuid);
        }
        coOwners.remove(uuid);
        invited.remove(uuid);
    }

    public boolean isOwner(UUID uuid) {
        return owner.equals(uuid);
    }

    public boolean isCoOwner(UUID uuid) {
        return coOwners.contains(uuid);
    }

    public boolean isStaff(UUID uuid) {
        return isOwner(uuid) || isCoOwner(uuid);
    }

    public void addCoOwner(UUID uuid) {
        if (!isOwner(uuid)) {
            members.remove(uuid);
            coOwners.add(uuid);
        }
    }

    public void removeCoOwner(UUID uuid) {
        if (coOwners.remove(uuid)) {
            members.add(uuid);
        }
    }

    public Set<UUID> getCoOwners() {
        return coOwners;
    }

    public Set<UUID> getAllPlayers() {
        Set<UUID> all = new HashSet<>();
        all.add(owner);
        all.addAll(coOwners);
        all.addAll(members);
        return all;
    }

    public int getTeamkills() {
        return teamkills;
    }

    public void addTeamkill() {
        teamkills++;
    }

    public void setTeamkills(int value) {
        this.teamkills = value;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    public Set<UUID> getInvited() {
        return Collections.unmodifiableSet(invited);
    }

    public void addInvited(UUID uuid) {
        invited.add(uuid);
    }

    public void removeInvited(UUID uuid) {
        invited.remove(uuid);
    }

    public void clearInvited() {
        invited.clear();
    }

    public void setNameInternal(String newName) {
        try {
            var field = Clan.class.getDeclaredField("name");
            field.setAccessible(true);
            field.set(this, newName);
        } catch (Exception ignored) {}
    }

    public void setTagInternal(String newTag) {
        try {
            var field = Clan.class.getDeclaredField("tag");
            field.setAccessible(true);
            field.set(this, newTag);
        } catch (Exception ignored) {}
    }

    public void setOwnerInternal(UUID newOwner) {
        this.owner = newOwner;
    }
}
