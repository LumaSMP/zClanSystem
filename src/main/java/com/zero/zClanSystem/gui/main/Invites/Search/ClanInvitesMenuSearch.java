package com.zero.zClanSystem.gui.main.Invites.Search;

import java.util.*;

public class ClanInvitesMenuSearch {

    public static final Set<UUID> waitingForSearch = new HashSet<>();
    public static final Map<UUID, String> searchMap = new HashMap<>();

    public static void startSearch(UUID uuid) {
        waitingForSearch.add(uuid);
    }

    public static void clearSearch(UUID uuid) {
        waitingForSearch.remove(uuid);
        searchMap.remove(uuid);
    }
}
