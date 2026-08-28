package com.zero.zClanSystem.gui.main.admin;

import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.ClanMainMenuGUI;
import com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanSelectModifyGUI;
import com.zero.zClanSystem.gui.main.admin.ForceDisband.AdminForceDisbandGUI;
import com.zero.zClanSystem.gui.main.admin.KickPlayer.AdminKickPlayerGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AdminMenuGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public AdminMenuGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.ADMIN_MENU) return;

        if (!e.getView().getTitle().equals(AdminMenuGUI.TITLE)) return;

        int slot = e.getRawSlot();
        ItemStack item = e.getCurrentItem();
        if (item == null) return;

        switch (slot) {

            case 11 -> {
                player.closeInventory();
                new AdminForceDisbandGUI(clanManager).open(player);
            }

            case 13 -> {
                player.closeInventory();
                new AdminKickPlayerGUI(clanManager).open(player);
            }

            case 15 -> {
                player.closeInventory();
                new ClanSelectModifyGUI(clanManager).open(player);
            }

            // BACK BUTTON
            case 18 -> {
                player.closeInventory();
                new ClanMainMenuGUI(clanManager).open(player);
            }

            case 25 -> {
                if (!player.isOp()) {
                    player.sendMessage("§cYou must be OP to use this.");
                    return;
                }

                player.closeInventory();
                clanManager.reloadClans();
                player.sendMessage("§aReloaded §fclans.yml§a successfully.");
            }

            case 26 -> {
                if (!player.isOp()) {
                    player.sendMessage("§cYou must be OP to use this.");
                    return;
                }

                player.closeInventory();
                clanManager.reloadConfigFile();
                player.sendMessage("§aReloaded §fconfig.yml§a successfully.");
            }
        }
    }
}