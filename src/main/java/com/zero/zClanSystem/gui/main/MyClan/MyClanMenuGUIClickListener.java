package com.zero.zClanSystem.gui.main.MyClan;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.ClanMainMenuGUI;
import com.zero.zClanSystem.gui.main.MyClan.MyClanMembers.MyClanMembersGUI;
import com.zero.zClanSystem.gui.main.MyClan.MyClanInvites.MyClanInvitesGUI;
import com.zero.zClanSystem.gui.main.MyClan.MyClanStats.MyClanStatsGUI;
import com.zero.zClanSystem.gui.main.MyClan.MyClanInfo.MyClanInfoGUI;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.MyClanSettingsGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MyClanMenuGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public MyClanMenuGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.MY_CLAN_MENU) return;

        Clan clan = ((GUIHolder) top.getHolder()).getClan();
        if (clan == null) return;

        int slot = e.getRawSlot();
        ItemStack item = e.getCurrentItem();
        if (item == null) return;

        switch (slot) {

            // MEMBERS
            case 10 -> {
                player.closeInventory();
                new MyClanMembersGUI(clanManager, clan).open(player);
                return;
            }

            // invites
            case 12 -> {
                player.closeInventory();
                new MyClanInvitesGUI(clanManager, clan).open(player);
                return;
            }

            // STATS
            case 14 -> {
                player.closeInventory();
                new MyClanStatsGUI(clanManager, clan).open(player);
                return;
            }

            // INFO
            case 16 -> {
                player.closeInventory();
                return;
            }

            // SETTINGS
            case 8 -> {
                if (!clan.isOwner(player.getUniqueId()) && !clan.isCoOwner(player.getUniqueId())) {
                    player.sendMessage("§cYou are not allowed to access clan settings.");
                    return;
                }
                player.closeInventory();
                new MyClanSettingsGUI(clanManager, clan).open(player);
                return;
            }

            // BACK
            case 18 -> {
                player.closeInventory();
                new ClanMainMenuGUI(clanManager).open(player);
            }

            // LEAVE CLAN
            case 26 -> {
                player.closeInventory();

                if (clan.isOwner(player.getUniqueId())) {
                    return;
                }

                clanManager.removeMember(clan, player.getUniqueId());
                clanManager.broadcastToClan(clan, "§e" + player.getName() + " has left the clan.");
                player.sendMessage("§cYou have left your clan.");
                return;
            }
        }
    }
}
