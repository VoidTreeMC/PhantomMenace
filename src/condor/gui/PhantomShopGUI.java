package condor.gui;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import dev.triumphteam.gui.guis.PaginatedGui;
import dev.triumphteam.gui.guis.GuiItem;

import condor.item.CustomItemGenerator;
import condor.item.CustomItemManager;
import condor.item.simpleitems.DefenderToken;
import condor.item.CustomItemType;

public class PhantomShopGUI {

  private static ItemStack INSOMNIA_POTION = CustomItemGenerator.getInsomniaPotion();
  private static ItemStack FANCY_PANTS = CustomItemManager.getItemByType(CustomItemType.FANCY_PANTS).getInstance();
  private static ItemStack ENDER_BLADE = CustomItemManager.getItemByType(CustomItemType.ENDER_BLADE).getInstance();

  private static int FANCY_PANTS_PRICE = 25;
  private static int ENDER_BLADE_PRICE = 50;

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
    PaginatedGui gui = new PaginatedGui(3, 4, "Phantom Shop Menu");
  	gui.setDefaultClickAction(event -> {
  		event.setCancelled(true);
  	});

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

    gui.setItem(2, 4, enderBladeItem);
    gui.setItem(1, 5, insomniaPotionItem);
    gui.setItem(2, 6, fancyPantsItem);

  	gui.open(player);
  }
}
