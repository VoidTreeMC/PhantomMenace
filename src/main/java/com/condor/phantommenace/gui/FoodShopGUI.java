package com.condor.phantommenace.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import net.kyori.adventure.text.Component;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import dev.triumphteam.gui.builder.item.ItemBuilder;

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemGenerator;
import com.condor.phantommenace.item.CustomItemManager;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.main.PhantomMain;

public class FoodShopGUI {

  private static HashMap<Material, Double> foodMap = new HashMap<>();

  static {
    foodMap.put(Material.CARROT, 5.0);
    foodMap.put(Material.BAKED_POTATO, 5.25);
    foodMap.put(Material.CHORUS_FRUIT, 3.0);
    foodMap.put(Material.APPLE, 10.0);
    foodMap.put(Material.DRIED_KELP, 10.0);
    foodMap.put(Material.HONEYCOMB, 3.15);
    foodMap.put(Material.CAKE, 240.68);
    foodMap.put(Material.PUMPKIN_PIE, 6.21);
    foodMap.put(Material.COOKIE, 2.10);
    foodMap.put(Material.GLOW_BERRIES, 1.0);
    foodMap.put(Material.PUMPKIN_PIE, 1.0);
    foodMap.put(Material.DRIED_KELP, 3.38);
    foodMap.put(Material.TROPICAL_FISH, 6.0);
  }

  private static boolean canAfford(Player player, double price) {
    double amt = PhantomMain.getPlugin().getEconomy().getBalance(player);
    return amt >= price;
  }

  private static void chargeAmount(Player player, double price) {
    PhantomMain.getPlugin().getEconomy().withdrawPlayer(player, price);
  }

  private static void handleGenericPurchase(PaginatedGui gui, Player player, ItemStack item, double price) {
    boolean purchased = false;

    if (canAfford(player, price)) {
      chargeAmount(player, price);
      player.getInventory().addItem(item);
      purchased = true;
    }

    if (!purchased) {
      player.sendMessage("You can't afford that.");
    } else {
      gui.close(player);
    }
  }

  public static void displayShopGUI(Player player) {
    PaginatedGui gui = Gui.paginated().title(Component.text("Food Shop Menu")).rows(4).pageSize(36).create();
  	gui.setDefaultClickAction(event -> {
  		event.setCancelled(true);
  	});

    // gui.getFiller().fill(new GuiItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));

    for (Entry<Material, Double> shopItem : foodMap.entrySet()) {
      Material mat = shopItem.getKey();
      double price = shopItem.getValue();
      ItemStack theItem = new ItemStack(mat);
      ItemMeta theMeta = theItem.getItemMeta();
      ArrayList<String> lore = new ArrayList<>();
      lore.add("$" + price);
      theMeta.setLore(lore);
      theItem.setItemMeta(theMeta);
      GuiItem newItem = new GuiItem(theItem, event -> {
          handleGenericPurchase(gui, player, new ItemStack(mat), price);
    	});
      gui.addItem(newItem);
    }

    // Previous item
    gui.setItem(4, 4, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName("Previous").asGuiItem(event -> gui.previous()));
    // Page number
    gui.setItem(4, 5, ItemBuilder.from(Material.COMPASS).setName(ChatColor.GRAY + "Page: " + gui.getCurrentPageNum() + "/" + gui.getPagesNum()).asGuiItem());
    // Next item
    gui.setItem(4, 6, ItemBuilder.from(Material.RED_STAINED_GLASS_PANE).setName("Next").asGuiItem(event -> gui.next()));

  	gui.open(player);
  }
}
