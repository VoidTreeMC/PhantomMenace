package com.condor.phantommenace.npc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.condor.phantommenace.gui.BlacksmithGUI;
import com.condor.phantommenace.gui.FoodShopGUI;
import com.condor.phantommenace.gui.PhantomShopGUI;
import com.github.juliarn.npc.event.PlayerNPCInteractEvent;

public enum NPCAction {
    NONE,
    FOOD_SHOP,
    BLACKSMITH,
    ENDER_CHEST,
    PHANTOM_SHOP,
    POTION_SHOP;

    public static NPCAction fromString(String str) {
        NPCAction ret = NPCAction.NONE;

        try {
            ret = NPCAction.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Encountered unknown NPC action in npcs.yml: \"" + str + "\"");
        }

        return ret;
    }

    public static void act(NPCAction type, Player player, PlayerNPCInteractEvent event) {
        switch (type) {
            case FOOD_SHOP:
                FoodShopGUI.displayShopGUI(player);
                break;
            case BLACKSMITH:
                BlacksmithGUI.displayShopGUI(player);
                break;
            case ENDER_CHEST:
                player.openInventory(player.getEnderChest());
                break;
            case PHANTOM_SHOP:
                PhantomShopGUI.displayShopGUI(player);
                break;
            case POTION_SHOP:
                // TODO: Write the potion shop
                player.sendMessage("I don't do anything yet, but I've always dreamed of selling potions.");
                break;
            case NONE:
            default:
                break;
        }
    }
}
