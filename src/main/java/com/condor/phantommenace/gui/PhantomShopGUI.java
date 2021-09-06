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
  private static CustomItem FLIGHT_POTION = CustomItemManager.getItemByType(CustomItemType.FLIGHT_POTION);

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
        handleGenericPurchase(gui, player, FANCY_PANTS.getInstance(), FANCY_PANTS.getPrice());
  	});

    GuiItem enderBladeItem = new GuiItem(ENDER_BLADE.getInstance(), event -> {
        handleGenericPurchase(gui, player, ENDER_BLADE.getInstance(), ENDER_BLADE.getPrice());
  	});

    GuiItem prideShearsItem = new GuiItem(PRIDE_SHEARS.getInstance(), event -> {
        handleGenericPurchase(gui, player, PRIDE_SHEARS.getInstance(), PRIDE_SHEARS.getPrice());
  	});

    GuiItem creeperBowItem = new GuiItem(CREEPER_BOW.getInstance(), event -> {
        handleGenericPurchase(gui, player, CREEPER_BOW.getInstance(), CREEPER_BOW.getPrice());
  	});

    GuiItem lavaWalkersItem = new GuiItem(LAVA_WALKERS.getInstance(), event -> {
        handleGenericPurchase(gui, player, LAVA_WALKERS.getInstance(), LAVA_WALKERS.getPrice());
  	});

    GuiItem creeperFireworkItem = new GuiItem(CREEPER_FIREWORK.getInstance(), event -> {
        handleGenericPurchase(gui, player, CREEPER_FIREWORK.getInstance(), CREEPER_FIREWORK.getPrice());
  	});

    GuiItem superPickItem = new GuiItem(SUPER_PICK.getInstance(), event -> {
        handleGenericPurchase(gui, player, SUPER_PICK.getInstance(), SUPER_PICK.getPrice());
  	});

    GuiItem slayerSwordItem = new GuiItem(SLAYER_SWORD.getInstance(), event -> {
        handleGenericPurchase(gui, player, SLAYER_SWORD.getInstance(), SLAYER_SWORD.getPrice());
  	});

    GuiItem flightPotionItem = new GuiItem(FLIGHT_POTION.getInstance(), event -> {
        handleGenericPurchase(gui, player, FLIGHT_POTION.getInstance(), FLIGHT_POTION.getPrice());
  	});

    gui.setItem(1, 5, insomniaPotionItem);
    gui.setItem(2, 2, creeperFireworkItem);
    gui.setItem(2, 3, lavaWalkersItem);
    gui.setItem(2, 4, enderBladeItem);
    gui.setItem(2, 5, prideShearsItem);
    gui.setItem(2, 6, fancyPantsItem);
    gui.setItem(2, 7, creeperBowItem);
    gui.setItem(2, 8, superPickItem);
    gui.setItem(3, 4, slayerSwordItem);
    gui.setItem(3, 6, flightPotionItem);


  	gui.open(player);
  }
}
