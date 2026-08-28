package com.zero.zClanSystem.gui.main.create;

import com.zero.zClanSystem.clan.ClanCreateCostType;
import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.gui.GUIType;
import com.zero.zClanSystem.gui.GUIUtils;
import com.zero.zClanSystem.gui.main.ClanMainMenuGUI;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateClanGUIClickListener implements Listener {

    public static final Map<UUID, Boolean> paymentConfirmed = new HashMap<>();

    private final ClanManager clanManager;

    public CreateClanGUIClickListener(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        var top = e.getView().getTopInventory();
        if (GUIUtils.getType(top) != GUIType.CREATE_CLAN) return;

        if (!e.getView().getTitle().equals(CreateClanGUI.TITLE)) return;

        int slot = e.getRawSlot();
        UUID uuid = player.getUniqueId();

        boolean costNone = clanManager.getCostType() == ClanCreateCostType.NONE;

        // NAME
        if ((costNone && slot == 11) || (!costNone && slot == 10)) {
            player.closeInventory();
            CreateChatInputListener.startNameInput(player);
            return;
        }

        // TAG
        if ((costNone && slot == 13) || (!costNone && slot == 12)) {
            player.closeInventory();
            CreateChatInputListener.startTagInput(player);
            return;
        }

        // PAYMENT
        if (!costNone && slot == 14) {

            boolean allEnough = true;

            for (var req : clanManager.getItemCost()) {
                if (!player.getInventory().containsAtLeast(req, req.getAmount())) {
                    allEnough = false;
                    break;
                }
            }

            if (!allEnough) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                return;
            }

            boolean current = paymentConfirmed.getOrDefault(uuid, false);
            paymentConfirmed.put(uuid, !current);

            String name = CreateChatInputListener.nameMap.get(uuid);
            String tag = CreateChatInputListener.tagMap.get(uuid);
            boolean nameValid = CreateChatInputListener.nameValidMap.getOrDefault(uuid, false);
            boolean tagValid = CreateChatInputListener.tagValidMap.getOrDefault(uuid, false);

            CreateClanGUI.open(player, name, tag, nameValid, tagValid);
            return;
        }

        // CREATE
        if ((costNone && slot == 15) || (!costNone && slot == 16)) {

            String name = CreateChatInputListener.nameMap.get(uuid);
            String tag = CreateChatInputListener.tagMap.get(uuid);

            boolean nameValid = CreateChatInputListener.nameValidMap.getOrDefault(uuid, false);
            boolean tagValid = CreateChatInputListener.tagValidMap.getOrDefault(uuid, false);

            boolean paymentOk = costNone || paymentConfirmed.getOrDefault(uuid, false);

            if (!nameValid || !tagValid || !paymentOk) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                return;
            }

            clanManager.createClanFull(player, name, tag);
            player.closeInventory();
        }

        // BACK
        if (slot == 18) {
            player.closeInventory();
            new ClanMainMenuGUI(clanManager).open(player);
        }
    }
}
