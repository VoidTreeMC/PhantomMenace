package com.condor.phantommenace.gui;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.Material;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemGenerator;
import com.condor.phantommenace.item.CustomItemManager;
import com.condor.phantommenace.item.simpleitems.DefenderToken;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.item.legendaryitems.CreeperFirework;
import com.condor.phantommenace.item.legendaryitems.SuperPick;

public class PhantomShopGUI {

  private static ItemStack INSOMNIA_POTION = CustomItemGenerator.getInsomniaPotion();
  private static CustomItem FANCY_PANTS = CustomItemManager.getItemByType(CustomItemType.FANCY_PANTS);
  private static CustomItem ENDER_BLADE = CustomItemManager.getItemByType(CustomItemType.ENDER_BLADE);
  private static CustomItem PRIDE_SHEARS = CustomItemManager.getItemByType(CustomItemType.PRIDE_SHEARS);
  private static CustomItem CREEPER_BOW = CustomItemManager.getItemByType(CustomItemType.CREEPER_BOW);
  private static CustomItem LAVA_WALKERS = CustomItemManager.getItemByType(CustomItemType.LAVA_WALKERS);
  private static CustomItem CREEPER_FIREWORK = CustomItemManager.getItemByType(CustomItemType.CREEPER_FIREWORK);
  private static CustomItem SUPER_PICK = CustomItemManager.getItemByType(CustomItemType.SUPER_PICK);
  private static CustomItem SLAYER_SWORD = CustomItemManager.getItemByType(CustomItemType.SLAYER_SWORD);

  private static int FANCY_PANTS_PRICE = 25;
  private static int ENDER_BLADE_PRICE = 50;
  private static int PRIDE_SHEARS_PRICE = 10;
  private static int CREEPER_BOW_PRICE = 50;
  private static int LAVA_WALKERS_PRICE = 50;
  private static int CREEPER_FIREWORK_PRICE = 1;
  private static int SUPER_PICK_PRICE = 100;
  private static int SLAYER_SWORD_PRICE = 50;

  private static boolean canAfford(Player player, int price) {
    int amt = 0;
    for (ItemStack item : player.getInventory().getContents()) {
      if (DefenderToken.isDefenderToken(item)) {
        amt += item.getAmount();
      }
    }
    return amt >= price;
  }

  private static void handleGenericPurchase(Gui gui, Player player, ItemStack item, int price) {
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
    Gui gui = new Gui(3, "Phantom Shop Menu");
  	gui.setDefaultClickAction(event -> {
  		event.setCancelled(true);
  	});

    gui.getFiller().fill(new GuiItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));

    GuiItem insomniaPotionItem = new GuiItem(INSOMNIA_POTION, event -> {
        player.getInventory().addItem(INSOMNIA_POTION);
        player.sendMessage("Enjoy! It goes great with cookies.");
        gui.close(player);
  	});

    GuiItem fancyPantsItem = new GuiItem(FANCY_PANTS.getInstance(), event -> {
        handleGenericPurchase(gui, player, FANCY_PANTS.getInstance(), FANCY_PANTS_PRICE);
  	});

    GuiItem enderBladeItem = new GuiItem(ENDER_BLADE.getInstance(), event -> {
        handleGenericPurchase(gui, player, ENDER_BLADE.getInstance(), ENDER_BLADE_PRICE);
  	});

    GuiItem prideShearsItem = new GuiItem(PRIDE_SHEARS.getInstance(), event -> {
        handleGenericPurchase(gui, player, PRIDE_SHEARS.getInstance(), PRIDE_SHEARS_PRICE);
  	});

    GuiItem creeperBowItem = new GuiItem(CREEPER_BOW.getInstance(), event -> {
        handleGenericPurchase(gui, player, CREEPER_BOW.getInstance(), CREEPER_BOW_PRICE);
  	});

    GuiItem lavaWalkersItem = new GuiItem(LAVA_WALKERS.getInstance(), event -> {
        handleGenericPurchase(gui, player, LAVA_WALKERS.getInstance(), LAVA_WALKERS_PRICE);
  	});

    GuiItem creeperFireworkItem = new GuiItem(CREEPER_FIREWORK.getInstance(), event -> {
        handleGenericPurchase(gui, player, CREEPER_FIREWORK.getInstance(), CREEPER_FIREWORK_PRICE);
  	});

    GuiItem superPickItem = new GuiItem(SUPER_PICK.getInstance(), event -> {
        handleGenericPurchase(gui, player, SUPER_PICK.getInstance(), SUPER_PICK_PRICE);
  	});

    GuiItem slayerSwordItem = new GuiItem(SLAYER_SWORD.getInstance(), event -> {
        handleGenericPurchase(gui, player, SLAYER_SWORD.getInstance(), SLAYER_SWORD_PRICE);
  	});

    gui.setItem(1, 5, insomniaPotionItem);
    gui.setItem(2, 2, creeperFireworkItem);
    gui.setItem(2, 3, lavaWalkersItem);
    gui.setItem(2, 4, enderBladeItem);
    gui.setItem(2, 5, prideShearsItem);
    gui.setItem(2, 6, fancyPantsItem);
    gui.setItem(2, 7, creeperBowItem);
    gui.setItem(2, 8, superPickItem);
    gui.setItem(3, 5, slayerSwordItem);

  	gui.open(player);
  }
}
