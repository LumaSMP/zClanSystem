package com.zero.zClanSystem.gui.main.MyClan.MyClanSettings;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.MyClan.MyClanMenuGUI;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.ChangeClanHome.MyClanHomeGUI;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.ChangeClanName.ChangeClanNameInputListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.ChangeClanTag.ChangeClanTagInputListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.DisbandClan.MyClanSettingsDisbandClanGUI;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MyClanMembersSettingsGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MyClanSettingsGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public MyClanSettingsGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.MY_CLAN_SETTINGS) return;

        Clan clan = ((GUIHolder) top.getHolder()).getClan();
        if (clan == null) return;

        int slot = e.getRawSlot();

        switch (slot) {

            // name
            case 10 -> {
                player.closeInventory();
                ChangeClanNameInputListener.startNameInput(player, clan);
                return;
            }

            // tag
            case 11 -> {
                player.closeInventory();
                ChangeClanTagInputListener.startTagInput(player, clan);
                return;
            }

            // home
            case 12 -> {
                player.closeInventory();
                new MyClanHomeGUI(clanManager, clan).open(player);
                return;
            }

            // members
            case 13 -> {
                player.closeInventory();
                new MyClanMembersSettingsGUI(clanManager, clan).open(player);
                return;
            }

            // disband
            case 16 -> {
                if (!clan.isOwner(player.getUniqueId())) {
                    player.sendMessage("§cOnly the clan owner can disband the clan.");
                    return;
                }

                player.closeInventory();
                new MyClanSettingsDisbandClanGUI(clanManager, clan).open(player);
                return;
            }

            // Back
            case 18 -> {
                player.closeInventory();
                new MyClanMenuGUI(clanManager, clan).open(player);
            }
        }
    }
}
