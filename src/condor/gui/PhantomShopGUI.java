package condor.gui;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.Material;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;

import condor.item.CustomItemGenerator;
import condor.item.CustomItemManager;
import condor.item.simpleitems.DefenderToken;
import condor.item.CustomItemType;
import condor.item.legendaryitems.CreeperFirework;
import condor.item.legendaryitems.SuperPick;

public class PhantomShopGUI {

  private static ItemStack INSOMNIA_POTION = CustomItemGenerator.getInsomniaPotion();
  private static ItemStack FANCY_PANTS = CustomItemManager.getItemByType(CustomItemType.FANCY_PANTS).getInstance();
  private static ItemStack ENDER_BLADE = CustomItemManager.getItemByType(CustomItemType.ENDER_BLADE).getInstance();
  private static ItemStack PRIDE_SHEARS = CustomItemManager.getItemByType(CustomItemType.PRIDE_SHEARS).getInstance();
  private static ItemStack CREEPER_BOW = CustomItemManager.getItemByType(CustomItemType.CREEPER_BOW).getInstance();
  private static ItemStack LAVA_WALKERS = CustomItemManager.getItemByType(CustomItemType.LAVA_WALKERS).getInstance();
  private static ItemStack CREEPER_FIREWORK = CustomItemManager.getItemByType(CustomItemType.CREEPER_FIREWORK).getInstance();
  private static ItemStack SUPER_PICK = CustomItemManager.getItemByType(CustomItemType.SUPER_PICK).getInstance();

  private static int FANCY_PANTS_PRICE = 25;
  private static int ENDER_BLADE_PRICE = 50;
  private static int PRIDE_SHEARS_PRICE = 10;
  private static int CREEPER_BOW_PRICE = 50;
  private static int LAVA_WALKERS_PRICE = 50;
  private static int CREEPER_FIREWORK_PRICE = 1;
  private static int SUPER_PICK_PRICE = 100;

  private static boolean canAfford(Player player, int price) {
    int amt = 0;
    for (ItemStack item : player.getInventory().getContents()) {
      if (DefenderToken.isDefenderToken(item)) {
        amt += item.getAmount();
      }
    }
    return amt >= price;
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

    GuiItem fancyPantsItem = new GuiItem(FANCY_PANTS, event -> {
        boolean purchased = false;

        if (canAfford(player, FANCY_PANTS_PRICE)) {
          chargeAmount(player, FANCY_PANTS_PRICE);
          player.getInventory().addItem(FANCY_PANTS);
          purchased = true;
        }

        if (!purchased) {
          player.sendMessage("You can't afford that.");
        } else {
          gui.close(player);
        }
  	});

    GuiItem enderBladeItem = new GuiItem(ENDER_BLADE, event -> {
        boolean purchased = false;

        if (canAfford(player, ENDER_BLADE_PRICE)) {
          chargeAmount(player, ENDER_BLADE_PRICE);
          player.getInventory().addItem(ENDER_BLADE);
          purchased = true;
        }

        if (!purchased) {
          player.sendMessage("You can't afford that.");
        } else {
          gui.close(player);
        }
  	});

    GuiItem prideShearsItem = new GuiItem(PRIDE_SHEARS, event -> {
        boolean purchased = false;

        if (canAfford(player, PRIDE_SHEARS_PRICE)) {
          chargeAmount(player, PRIDE_SHEARS_PRICE);
          player.getInventory().addItem(PRIDE_SHEARS);
          purchased = true;
        }

        if (!purchased) {
          player.sendMessage("You can't afford that.");
        } else {
          gui.close(player);
        }
  	});

    GuiItem creeperBowItem = new GuiItem(CREEPER_BOW, event -> {
        boolean purchased = false;

        if (canAfford(player, CREEPER_BOW_PRICE)) {
          chargeAmount(player, CREEPER_BOW_PRICE);
          player.getInventory().addItem(CREEPER_BOW);
          purchased = true;
        }

        if (!purchased) {
          player.sendMessage("You can't afford that.");
        } else {
          gui.close(player);
        }
  	});

    GuiItem lavaWalkersItem = new GuiItem(LAVA_WALKERS, event -> {
        boolean purchased = false;

        if (canAfford(player, LAVA_WALKERS_PRICE)) {
          chargeAmount(player, LAVA_WALKERS_PRICE);
          player.getInventory().addItem(LAVA_WALKERS);
          purchased = true;
        }

        if (!purchased) {
          player.sendMessage("You can't afford that.");
        } else {
          gui.close(player);
        }
  	});

    GuiItem creeperFireworkItem = new GuiItem(CREEPER_FIREWORK, event -> {
        boolean purchased = false;

        if (canAfford(player, CREEPER_FIREWORK_PRICE)) {
          chargeAmount(player, CREEPER_FIREWORK_PRICE);
          player.getInventory().addItem(new CreeperFirework().getInstance());
          purchased = true;
        }

        if (!purchased) {
          player.sendMessage("You can't afford that.");
        } else {
          gui.close(player);
        }
  	});

    GuiItem superPickItem = new GuiItem(SUPER_PICK, event -> {
        boolean purchased = false;

        if (canAfford(player, SUPER_PICK_PRICE)) {
          chargeAmount(player, SUPER_PICK_PRICE);
          player.getInventory().addItem(new SuperPick().getInstance());
          purchased = true;
        }

        if (!purchased) {
          player.sendMessage("You can't afford that.");
        } else {
          gui.close(player);
        }
  	});

    gui.setItem(1, 5, insomniaPotionItem);
    gui.setItem(2, 2, creeperFireworkItem);
    gui.setItem(2, 3, lavaWalkersItem);
    gui.setItem(2, 4, enderBladeItem);
    gui.setItem(2, 5, prideShearsItem);
    gui.setItem(2, 6, fancyPantsItem);
    gui.setItem(2, 7, creeperBowItem);
    gui.setItem(2, 8, superPickItem);

  	gui.open(player);
  }
}
