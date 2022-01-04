package com.condor.phantommenace.gui;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.ChatColor;
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
  private static CustomItem ALMOND_CAKE = CustomItemManager.getItemByType(CustomItemType.ALMOND_CAKE);
  private static CustomItem BEDROCK_BREAKER = CustomItemManager.getItemByType(CustomItemType.BEDROCK_BREAKER);
  private static CustomItem ZOMBIE_EGG = CustomItemManager.getItemByType(CustomItemType.ZOMBIE_EGG);
  private static CustomItem FOX_EGG = CustomItemManager.getItemByType(CustomItemType.FOX_EGG);
  private static CustomItem SPIDER_EGG = CustomItemManager.getItemByType(CustomItemType.SPIDER_EGG);
  private static CustomItem SLIME_EGG = CustomItemManager.getItemByType(CustomItemType.SLIME_EGG);
  private static CustomItem COW_EGG = CustomItemManager.getItemByType(CustomItemType.COW_EGG);
  private static CustomItem ZOMBIE_PIGLIN_EGG = CustomItemManager.getItemByType(CustomItemType.ZOMBIE_PIGLIN_EGG);
  private static CustomItem REPLANTER_HOE = CustomItemManager.getItemByType(CustomItemType.REPLANTER_HOE);

  private static boolean canAfford(Player player, int price) {
    int amt = 0;
    for (ItemStack item : player.getInventory().getContents()) {
      if (DefenderToken.isDefenderToken(item)) {
        amt += item.getAmount();
      }
    }
    return amt >= price;
  }

  private static void handleGenericPurchase(PaginatedGui gui, Player player, ItemStack item, int price) {
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

    GuiItem zombieSpawnItem = new GuiItem(ZOMBIE_EGG.getInstance(), event -> {
      handleGenericPurchase(gui, player, ZOMBIE_EGG.getInstance(), ZOMBIE_EGG.getPrice());
    });

    GuiItem foxSpawnItem = new GuiItem(FOX_EGG.getInstance(), event -> {
      handleGenericPurchase(gui, player, FOX_EGG.getInstance(), FOX_EGG.getPrice());
    });

    GuiItem spiderSpawnItem = new GuiItem(SPIDER_EGG.getInstance(), event -> {
      handleGenericPurchase(gui, player, SPIDER_EGG.getInstance(), SPIDER_EGG.getPrice());
    });

    GuiItem zombiePiglinSpawnItem = new GuiItem(ZOMBIE_PIGLIN_EGG.getInstance(), event -> {
      handleGenericPurchase(gui, player, ZOMBIE_PIGLIN_EGG.getInstance(), ZOMBIE_PIGLIN_EGG.getPrice());
    });

    GuiItem slimeSpawnItem = new GuiItem(SLIME_EGG.getInstance(), event -> {
      handleGenericPurchase(gui, player, SLIME_EGG.getInstance(), SLIME_EGG.getPrice());
    });

    GuiItem cowSpawnItem = new GuiItem(COW_EGG.getInstance(), event -> {
      handleGenericPurchase(gui, player, COW_EGG.getInstance(), COW_EGG.getPrice());
    });

    GuiItem almondCakeItem = new GuiItem(ALMOND_CAKE.getInstance(), event -> {
      handleGenericPurchase(gui, player, ALMOND_CAKE.getInstance(), ALMOND_CAKE.getPrice());
    });

    GuiItem bedrockBreakerItem = new GuiItem(BEDROCK_BREAKER.getInstance(), event -> {
      handleGenericPurchase(gui, player, BEDROCK_BREAKER.getInstance(), BEDROCK_BREAKER.getPrice());
    });

    GuiItem replanterHoeItem = new GuiItem(REPLANTER_HOE.getInstance(), event -> {
      handleGenericPurchase(gui, player, REPLANTER_HOE.getInstance(), REPLANTER_HOE.getPrice());
    });

    gui.addItem(insomniaPotionItem);
    gui.addItem(creeperFireworkItem);
    gui.addItem(lavaWalkersItem);
    gui.addItem(enderBladeItem);
    gui.addItem(prideShearsItem);
    gui.addItem(fancyPantsItem);
    gui.addItem(creeperBowItem);
    gui.addItem(superPickItem);
    gui.addItem(slayerSwordItem);
    gui.addItem(flightPotionItem);
    gui.addItem(almondCakeItem);
    gui.addItem(bedrockBreakerItem);
    gui.addItem(replanterHoeItem);
    gui.addItem(zombieSpawnItem);
    gui.addItem(foxSpawnItem);
    gui.addItem(spiderSpawnItem);
    gui.addItem(zombiePiglinSpawnItem);
    gui.addItem(slimeSpawnItem);
    gui.addItem(cowSpawnItem);


    // Previous item
    gui.setItem(4, 4, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName("Previous").asGuiItem(event -> gui.previous()));
    // Page number
    gui.setItem(4, 5, ItemBuilder.from(Material.COMPASS).setName(ChatColor.GRAY + "Page: " + gui.getCurrentPageNum() + "/" + gui.getPagesNum()).asGuiItem());
    // Next item
    gui.setItem(4, 6, ItemBuilder.from(Material.RED_STAINED_GLASS_PANE).setName("Next").asGuiItem(event -> gui.next()));

  	gui.open(player);
  }
}
