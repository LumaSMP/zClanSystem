package com.zero.zClanSystem.gui;

import com.zero.zClanSystem.clan.Clan;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

public class GUIHolder implements InventoryHolder {

    private final GUIType type;
    private final Clan clan;
    private final UUID targetUUID;

    private Inventory inventory;

    private int page = 0;

    public GUIHolder(GUIType type) {
        this(type, null, null);
    }

    public GUIHolder(GUIType type, Clan clan) {
        this(type, clan, null);
    }

    public GUIHolder(GUIType type, Clan clan, UUID targetUUID) {
        this.type = type;
        this.clan = clan;
        this.targetUUID = targetUUID;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public GUIType getType() {
        return type;
    }

    public Clan getClan() {
        return clan;
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
