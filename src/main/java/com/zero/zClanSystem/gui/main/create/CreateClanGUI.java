package com.zero.zClanSystem.gui.main.create;

import com.zero.zClanSystem.zClanSystem;
import com.zero.zClanSystem.clan.ClanCreateCostType;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIHolder;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateClanGUI {

    public static final String TITLE = "§8Create Clan";

    public static void open(Player player, String name, String tag, boolean nameValid, boolean tagValid) {

        UUID uuid = player.getUniqueId();
        ClanManager cm = zClanSystem.getInstance().getClanManager();

        ClanCreateCostType costType = cm.getCostType();
        boolean costNone = costType == ClanCreateCostType.NONE;

        boolean paymentConfirmed = CreateClanGUIClickListener.paymentConfirmed.getOrDefault(uuid, false);

        Inventory inv = Bukkit.createInventory(
                new GUIHolder(GUIType.CREATE_CLAN),
                27,
                TITLE
        );

        if (costNone) {

            // NAME
            inv.setItem(11,
                    GUIUtils.item(
                            name == null ? Material.NAME_TAG :
                                    (nameValid ? Material.GREEN_CONCRETE : Material.RED_CONCRETE),
                            "§bClan Name",
                            name == null ? "§7Click to enter clan name" :
                                    (nameValid ? "§a" + name : "§c" + name)
                    )
            );

            // TAG
            inv.setItem(13,
                    GUIUtils.item(
                            tag == null ? Material.NAME_TAG :
                                    (tagValid ? Material.GREEN_CONCRETE : Material.RED_CONCRETE),
                            "§bClan Tag",
                            tag == null ? "§7Click to enter clan tag" :
                                    (tagValid ? "§a" + tag : "§c" + tag)
                    )
            );

            // CREATE
            boolean canCreate = nameValid && tagValid;

            inv.setItem(15,
                    GUIUtils.item(
                            canCreate ? Material.GREEN_CONCRETE : Material.GRAY_CONCRETE,
                            canCreate ? "§aCreate Clan" : "§7Create Clan",
                            canCreate ? "§aClick to create clan" : "§7Name and tag must be valid"
                    )
            );

            inv.setItem(18,
                    GUIUtils.item(
                            Material.ARROW,
                            "§eBack",
                            "§7Go back to previous menu"
                    )
            );
        }

        else {

            // NAME
            inv.setItem(10,
                    GUIUtils.item(
                            name == null ? Material.NAME_TAG :
                                    (nameValid ? Material.GREEN_CONCRETE : Material.RED_CONCRETE),
                            "§bClan Name",
                            name == null ? "§7Click to enter clan name" :
                                    (nameValid ? "§a" + name : "§c" + name)
                    )
            );

            // TAG
            inv.setItem(12,
                    GUIUtils.item(
                            tag == null ? Material.NAME_TAG :
                                    (tagValid ? Material.GREEN_CONCRETE : Material.RED_CONCRETE),
                            "§bClan Tag",
                            tag == null ? "§7Click to enter clan tag" :
                                    (tagValid ? "§a" + tag : "§c" + tag)
                    )
            );

            // PAYMENT
            List<String> lore = new ArrayList<>();
            lore.add("§7Click to confirm payment.");
            lore.add("");

            for (ItemStack req : cm.getItemCost()) {

                boolean enough = player.getInventory().containsAtLeast(req, req.getAmount());

                String matName = req.getType().name().toLowerCase().replace("_", " ");
                matName = Character.toUpperCase(matName.charAt(0)) + matName.substring(1);

                lore.add(
                        (enough ? "§a" : "§c") + req.getAmount() +
                                "§7x §f" + matName
                );
            }

            inv.setItem(14,
                    GUIUtils.item(
                            paymentConfirmed ? Material.GREEN_CONCRETE : Material.GOLD_INGOT,
                            paymentConfirmed ? "§aPayment Confirmed" : "§ePayment",
                            lore.toArray(new String[0])
                    )
            );

            // CREATE
            boolean canCreate = nameValid && tagValid && paymentConfirmed;

            inv.setItem(16,
                    GUIUtils.item(
                            canCreate ? Material.GREEN_CONCRETE : Material.GRAY_CONCRETE,
                            canCreate ? "§aCreate Clan" : "§7Create Clan",
                            canCreate ? "§aClick to create clan" : "§7Name, tag and payment must be valid"
                    )
            );

            inv.setItem(18,
                    GUIUtils.item(
                            Material.YELLOW_CONCRETE,
                            "§eBack",
                            "§7Go back to previous menu"
                    )
            );
        }

        player.openInventory(inv);
    }
}
