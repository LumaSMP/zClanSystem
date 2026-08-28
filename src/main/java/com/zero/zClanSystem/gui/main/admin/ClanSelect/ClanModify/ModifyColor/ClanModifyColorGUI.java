package com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanModify.ModifyColor;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClanModifyColorGUI {

    public static final String TITLE_PREFIX = "§8Clan Color: ";

    private final ClanManager clanManager;
    private final Clan clan;

    public ClanModifyColorGUI(ClanManager clanManager, Clan clan) {
        this.clanManager = clanManager;
        this.clan = clan;
    }

    public void open(Player player) {

        String title = TITLE_PREFIX + "§7" + clan.getName();

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.ADMIN_CLAN_MODIFY_COLOR, clan),
                27,
                title
        );

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

        // Slots 0–8 & 10–16
        int[] slots = {
                0,1,2,3,4,5,6,7,8,
                10,11,12,13,14,15,16
        };

        int index = 0;

        for (Map.Entry<Material, String> entry : dyes.entrySet()) {

            int slot = slots[index++];

            String colorCode = entry.getValue();
            String colorName = entry.getKey().name()
                    .replace("_DYE", "")
                    .replace("_", " ")
                    .toLowerCase();

            colorName = colorName.substring(0,1).toUpperCase() + colorName.substring(1);

            inv.setItem(slot, GUIUtils.item(
                    entry.getKey(),
                    "§e" + colorName,
                    "§7Click to set clan color",
                    "§7Preview: " + colorCode + clan.getTag()
            ));
        }

        // RESET BUTTON (sets color to §7)
        inv.setItem(22, GUIUtils.item(
                Material.BARRIER,
                "§cReset Color",
                "§7Click to reset clan color",
                "§7Preview: §7" + clan.getTag()
        ));

        // BACK BUTTON
        inv.setItem(18, GUIUtils.item(
                Material.ARROW,
                "§eBack",
                "§7Return to clan selection"
        ));

        player.openInventory(inv);
    }
}
