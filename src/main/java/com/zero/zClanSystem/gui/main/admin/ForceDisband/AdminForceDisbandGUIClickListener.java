package com.zero.zClanSystem.gui.main.admin.ForceDisband;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.admin.AdminMenuGUI;
import com.zero.zClanSystem.gui.main.admin.ForceDisband.Apply.AdminForceDisbandApplyGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AdminForceDisbandGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public AdminForceDisbandGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.ADMIN_FORCE_DISBAND) return;

        if (!e.getView().getTitle().equals(AdminForceDisbandGUI.TITLE)) return;

        GUIHolder holder = (GUIHolder) top.getHolder();
        int currentPage = holder.getPage();

        int slot = e.getRawSlot();
        ItemStack item = e.getCurrentItem();
        if (item == null) return;

        // BACK BUTTON
        if (slot == 49) {
            player.closeInventory();
            AdminMenuGUI.open(player);
            return;
        }

        // PREV PAGE
        if (slot == 45) {
            player.closeInventory();
            new AdminForceDisbandGUI(clanManager).open(player, Math.max(0, currentPage - 1));
            return;
        }

        // NEXT PAGE
        if (slot == 53) {
            player.closeInventory();
            new AdminForceDisbandGUI(clanManager).open(player, currentPage + 1);
            return;
        }

        // CLAN HEAD CLICK
        if (item.getType() == Material.PLAYER_HEAD) {

            String clanName = item.getItemMeta().getDisplayName().replace("§eClan: ", "");
            Clan clan = clanManager.getClanByName(clanName);

            if (clan == null) return;

            player.closeInventory();
            new AdminForceDisbandApplyGUI(clanManager, clan).open(player);
        }
    }
}
