package com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanModify.ModifyColor;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanSelectModifyGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClanModifyColorGUIClickListener implements Listener {

    private final ClanManager clanManager;

    public ClanModifyColorGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.ADMIN_CLAN_MODIFY_COLOR) return;

        String title = e.getView().getTitle();
        if (!title.startsWith(ClanModifyColorGUI.TITLE_PREFIX)) return;

        int slot = e.getRawSlot();

        // BACK BUTTON
        if (slot == 18) {
            player.closeInventory();
            new ClanSelectModifyGUI(clanManager).open(player);
            return;
        }

        // RESET BUTTON
        if (slot == 22) {

            GUIHolder holder = (GUIHolder) top.getHolder();
            Clan clan = holder.getClan();
            if (clan == null) return;

            clanManager.setClanColor(clan, "§7");

            player.sendMessage("§aClan color reset to §7Gray§a.");
            player.closeInventory();
            new ClanSelectModifyGUI(clanManager).open(player);
            return;
        }

        // Dye → §-Colorcode
        Map<Material, String> dyes = new LinkedHashMap<>();
        dyes.put(Material.WHITE_DYE, "§f");
        dyes.put(Material.GRAY_DYE, "§8");
        dyes.put(Material.BLACK_DYE, "§0");

        dyes.put(Material.BROWN_DYE, "§6");
        dyes.put(Material.RED_DYE, "§c");
        dyes.put(Material.ORANGE_DYE, "§6");
        dyes.put(Material.YELLOW_DYE, "§e");

        dyes.put(Material.LIME_DYE, "§a");
        dyes.put(Material.GREEN_DYE, "§2");
        dyes.put(Material.CYAN_DYE, "§3");
        dyes.put(Material.LIGHT_BLUE_DYE, "§b");

        dyes.put(Material.BLUE_DYE, "§9");
        dyes.put(Material.PURPLE_DYE, "§5");
        dyes.put(Material.PINK_DYE, "§d");

        if (!top.getItem(slot).getType().name().endsWith("_DYE")) return;

        Material clicked = top.getItem(slot).getType();
        String colorCode = dyes.get(clicked);

        GUIHolder holder = (GUIHolder) top.getHolder();
        Clan clan = holder.getClan();
        if (clan == null) return;

        clanManager.setClanColor(clan, colorCode);

        player.sendMessage("§aClan color updated to " + colorCode + clan.getTag() + "§a.");

        player.closeInventory();
        new ClanSelectModifyGUI(clanManager).open(player);
    }
}
