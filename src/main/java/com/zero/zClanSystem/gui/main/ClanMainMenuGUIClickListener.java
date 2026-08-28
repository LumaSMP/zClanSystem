package com.zero.zClanSystem.gui.main;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.Invites.ClanInvitesMenuGUI;
import com.zero.zClanSystem.gui.main.MyClan.MyClanMenuGUI;
import com.zero.zClanSystem.gui.main.TopClans.TopClansListGUI;
import com.zero.zClanSystem.gui.main.admin.AdminMenuGUI;
import com.zero.zClanSystem.gui.main.create.CreateClanGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClanMainMenuGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public ClanMainMenuGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        GUIType type = GUIUtils.getType(top);

        if (type != GUIType.MAIN_MENU_NO_CLAN && type != GUIType.MAIN_MENU_CLAN) return;
        if (!e.getView().getTitle().equals(ClanMainMenuGUI.TITLE)) return;

        var item = e.getCurrentItem();
        if (item == null) return;

        Material mat = item.getType();

        if (type == GUIType.MAIN_MENU_NO_CLAN) {

            switch (mat) {

                case EMERALD -> {
                    player.closeInventory();
                    CreateClanGUI.open(player, null, null, false, false);
                }

                case PAPER -> {
                    player.closeInventory();
                    new ClanInvitesMenuGUI(clanManager).open(player);
                    return;
                }

                case DIAMOND -> {
                    player.closeInventory();
                    new TopClansListGUI(clanManager).open(player);
                }

                case REDSTONE -> {
                    player.closeInventory();
                    if (!player.hasPermission("clansystem.admin")) {
                        player.sendMessage("§cYou do not have permission to access the admin menu.");
                        return;
                    }
                    AdminMenuGUI.open(player);
                }
            }

            return;
        }

        if (type == GUIType.MAIN_MENU_CLAN) {

            Clan clan = clanManager.getClanOf(player.getUniqueId());
            if (clan == null) return;

            switch (mat) {

                case BOOK -> {
                    player.closeInventory();
                    new MyClanMenuGUI(clanManager, clan).open(player);
                }

                case DIAMOND -> {
                    player.closeInventory();
                    new TopClansListGUI(clanManager).open(player);
                }

                case REDSTONE -> {
                    player.closeInventory();
                    if (!player.hasPermission("clansystem.admin")) {
                        player.sendMessage("§cYou do not have permission to access the admin menu.");
                        return;
                    }
                    AdminMenuGUI.open(player);
                }
            }
        }
    }
}
