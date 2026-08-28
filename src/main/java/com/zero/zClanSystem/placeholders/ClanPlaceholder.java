package com.zero.zClanSystem.placeholders;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClanPlaceholder extends PlaceholderExpansion {

    private final ClanManager clanManager;

    public ClanPlaceholder(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "clan";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Zero";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, @NotNull String id) {

        if (p == null) return "";

        Clan clan = clanManager.getClanOf(p.getUniqueId());

        switch (id.toLowerCase()) {

            case "suffix":
                return clan != null ? " " + "§7[§r" + clan.getTagColor() + clan.getTag() + "§7]§r" : "";

            case "tag":
                return clan != null ? clan.getTag() : "";

            case "name":
                return clan != null ? clan.getName() : "";
        }

        return null;
    }
}
