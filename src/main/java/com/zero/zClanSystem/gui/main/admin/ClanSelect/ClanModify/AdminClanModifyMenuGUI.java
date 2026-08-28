package com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanModify;

import com.zero.zClanSystem.clan.Clan;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AdminClanModifyMenuGUI {

    public static final String TITLE_PREFIX = "§8Modify ";

    private final ClanManager clanManager;
    private final Clan clan;

    public AdminClanModifyMenuGUI(ClanManager clanManager, Clan clan) {
        this.clanManager = clanManager;
        this.clan = clan;
    }

    public void open(Player player) {

        String title = TITLE_PREFIX + "§7" + clan.getName();

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.ADMIN_CLAN_MODIFY, clan),
                27,
                title
        );

        inv.setItem(13, GUIUtils.item(
                Material.YELLOW_DYE,
                "§eClan Color",
                "§7Edit clan tag colors"
        ));

        inv.setItem(18, GUIUtils.item(
                Material.ARROW,
                "§eBack",
                "§7Return to previous menu"
        ));

        player.openInventory(inv);
    }
}
