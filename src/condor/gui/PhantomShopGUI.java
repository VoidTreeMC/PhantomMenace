package condor.gui;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import dev.triumphteam.gui.guis.PaginatedGui;
import dev.triumphteam.gui.guis.GuiItem;

import condor.item.CustomItemGenerator;

public class PhantomShopGUI {

  private static ItemStack INSOMNIA_POTION = CustomItemGenerator.getInsomniaPotion();

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

    gui.setItem(2, 5, insomniaPotionItem);

  	gui.open(player);
  }
}
