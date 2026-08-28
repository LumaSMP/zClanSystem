package com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanModify;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanModify.ModifyColor.ClanModifyColorGUI;
import com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanSelectModifyGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AdminClanModifyMenuGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public AdminClanModifyMenuGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.ADMIN_CLAN_MODIFY) return;

        String title = e.getView().getTitle();
        if (!title.startsWith(AdminClanModifyMenuGUI.TITLE_PREFIX)) return;

        int slot = e.getRawSlot();

        if (slot == 18) {
            player.closeInventory();
            new ClanSelectModifyGUI(clanManager).open(player);
            return;
        }

        if (slot == 13) {

            GUIHolder holder = (GUIHolder) top.getHolder();
            Clan clan = holder.getClan();
            if (clan == null) return;

            player.closeInventory();
            new ClanModifyColorGUI(clanManager, clan).open(player);
        }
    }
}
