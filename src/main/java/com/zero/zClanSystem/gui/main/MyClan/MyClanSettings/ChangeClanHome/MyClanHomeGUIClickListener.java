package com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.ChangeClanHome;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.MyClanSettingsGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MyClanHomeGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public MyClanHomeGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.MY_CLAN_HOME) return;

        Clan clan = ((GUIHolder) top.getHolder()).getClan();
        if (clan == null) return;

        int slot = e.getRawSlot();

        switch (slot) {

            case 12 -> { // Set Home
                clanManager.setClanHome(clan, player.getLocation());
                player.sendMessage("§aClan home set.");
                player.closeInventory();
                new MyClanHomeGUI(clanManager, clan).open(player);
                return;
            }

            case 14 -> { // Delete Home
                clanManager.deleteClanHome(clan);
                player.sendMessage("§cClan home deleted.");
                player.closeInventory();
                new MyClanHomeGUI(clanManager, clan).open(player);
                return;
            }

            case 18 -> { // Back
                player.closeInventory();
                new MyClanSettingsGUI(clanManager, clan).open(player);
            }
        }
    }
}
