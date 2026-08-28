package com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.DisbandClan;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.MyClanSettingsGUI;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MyClanSettingsDisbandClanGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public MyClanSettingsDisbandClanGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.MY_CLAN_SETTINGS_DISBAND) return;

        Clan clan = ((GUIHolder) top.getHolder()).getClan();
        if (clan == null) return;

        int slot = e.getRawSlot();

        switch (slot) {

            // CONFIRM DISBAND
            case 15 -> {
                player.closeInventory();

                clanManager.broadcastToClan(clan, "§cYour clan " + clan.getName() + "§c has been disbanded.");
                clanManager.playSoundToClan(clan, Sound.ENTITY_ENDER_DRAGON_DEATH, 1f, 1f);
                clanManager.disbandClan(clan);
                player.sendMessage("§cYour clan has been permanently disbanded.");
                return;
            }

            // BACK
            case 18 -> {
                player.closeInventory();
                new MyClanSettingsGUI(clanManager, clan).open(player);
            }
        }
    }
}
