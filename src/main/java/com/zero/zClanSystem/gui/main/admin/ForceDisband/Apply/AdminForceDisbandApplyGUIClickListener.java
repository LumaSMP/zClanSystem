package com.zero.zClanSystem.gui.main.admin.ForceDisband.Apply;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.admin.ForceDisband.AdminForceDisbandGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AdminForceDisbandApplyGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public AdminForceDisbandApplyGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.ADMIN_FORCE_DISBAND_APPLY) return;

        String title = e.getView().getTitle();
        if (!title.startsWith(AdminForceDisbandApplyGUI.TITLE_PREFIX)) return;

        int slot = e.getRawSlot();

        if (slot == 18) {
            player.closeInventory();
            new AdminForceDisbandGUI(clanManager).open(player);
            return;
        }

        if (slot == 13) {

            String clanName = title.substring(title.indexOf("Disband ") + 8, title.indexOf(" ("));
            Clan clan = clanManager.getClanByName(clanName);

            if (clan == null) {
                player.sendMessage("§cError: Clan not found.");
                player.closeInventory();
                return;
            }

            clanManager.forceDisband(clan);

            player.sendMessage("§cClan §f" + clanName + " §cwas permanently disbanded.");
            player.closeInventory();
        }
    }
}