package com.condor.phantommenace.gui;

import java.util.ArrayList;

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
import com.condor.phantommenace.item.simpleitems.DefenderToken;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.item.legendaryitems.CreeperFirework;
import com.condor.phantommenace.item.legendaryitems.SuperPick;
import com.condor.phantommenace.sql.SQLLinker;
import com.condor.phantommenace.main.PhantomMain;

public class PhantomShopGUI {

  private static ItemStack INSOMNIA_POTION = CustomItemGenerator.getInsomniaPotion();

  private static CustomItem[] itemsForSale = {
    CustomItemManager.getItemByType(CustomItemType.FANCY_PANTS),
    CustomItemManager.getItemByType(CustomItemType.ENDER_BLADE),
    CustomItemManager.getItemByType(CustomItemType.PRIDE_SHEARS),
    CustomItemManager.getItemByType(CustomItemType.CREEPER_BOW),
    CustomItemManager.getItemByType(CustomItemType.LAVA_WALKERS),
    CustomItemManager.getItemByType(CustomItemType.CREEPER_FIREWORK),
    CustomItemManager.getItemByType(CustomItemType.SUPER_PICK),
    CustomItemManager.getItemByType(CustomItemType.SLAYER_SWORD),
    CustomItemManager.getItemByType(CustomItemType.FLIGHT_POTION),
    CustomItemManager.getItemByType(CustomItemType.ALMOND_CAKE),
    CustomItemManager.getItemByType(CustomItemType.BEDROCK_BREAKER),
    CustomItemManager.getItemByType(CustomItemType.ZOMBIE_EGG),
    CustomItemManager.getItemByType(CustomItemType.FOX_EGG),
    CustomItemManager.getItemByType(CustomItemType.SPIDER_EGG),
    CustomItemManager.getItemByType(CustomItemType.SLIME_EGG),
    CustomItemManager.getItemByType(CustomItemType.COW_EGG),
    CustomItemManager.getItemByType(CustomItemType.ZOMBIE_PIGLIN_EGG),
    CustomItemManager.getItemByType(CustomItemType.REPLANTER_HOE),
    CustomItemManager.getItemByType(CustomItemType.FOLIAGE_AXE)
  };

  private static boolean canAfford(Player player, int price) {
    int amt = 0;
    for (ItemStack item : player.getInventory().getContents()) {
      if (DefenderToken.isDefenderToken(item)) {
        amt += item.getAmount();
      }
    }
    return amt >= price;
  }

  private static void handleGenericPurchase(PaginatedGui gui, Player player, ItemStack item, CustomItemType type, int price) {
    boolean purchased = false;

    if (canAfford(player, price)) {
      chargeAmount(player, price);
      player.getInventory().addItem(item);
      purchased = true;
    }

    if (!purchased) {
      player.sendMessage("You can't afford that.");
    } else {
      Bukkit.getScheduler().runTaskAsynchronously(PhantomMain.getPlugin(), new Runnable() {
        @Override
        public void run() {
          SQLLinker.pushToDB(player, type, price, System.currentTimeMillis());
        }
      });
      gui.close(player);
    }
  }

  private static void chargeAmount(Player player, int price) {
    int amtCharged = 0;
    for (ItemStack item : player.getInventory().getContents()) {
      if (DefenderToken.isDefenderToken(item)) {
        if ((price - amtCharged) <= item.getAmount()) {
          item.setAmount(item.getAmount() - (price - amtCharged));
          break;
        } else {
          amtCharged += item.getAmount();
          item.setAmount(0);
        }
      }
    }
  }

  public static void displayShopGUI(Player player) {
    // Gui gui = new Gui(3, "Phantom Shop Menu");
    PaginatedGui gui = Gui.paginated().title(Component.text("Phantom Shop Menu")).rows(4).pageSize(36).create();
  	gui.setDefaultClickAction(event -> {
  		event.setCancelled(true);
  	});

    // gui.getFiller().fill(new GuiItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));

    GuiItem insomniaPotionItem = new GuiItem(INSOMNIA_POTION, event -> {
        player.getInventory().addItem(INSOMNIA_POTION);
        player.sendMessage("Enjoy! It goes great with cookies.");
        gui.close(player);
  	});

    gui.addItem(insomniaPotionItem);

    for (CustomItem shopItem : itemsForSale) {
      GuiItem guiItem = new GuiItem(shopItem.getInstance(), event -> {
          handleGenericPurchase(gui, player, shopItem.getInstance(), shopItem.getType(), shopItem.getPrice());
      });
      gui.addItem(guiItem);
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
