package com.condor.phantommenace.gui;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Bukkit;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;

import com.condor.phantommenace.item.CustomItemGenerator;
import com.condor.phantommenace.item.CustomItemManager;
import com.condor.phantommenace.item.simpleitems.DefenderToken;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.sql.SQLLinker;
import com.condor.phantommenace.main.PhantomMain;

public class BlacksmithGUI {

  private static Random rng = new Random();

  private static ItemStack CONFIRM_PANE_RED = new ItemStack(Material.RED_STAINED_GLASS_PANE);
  private static ItemStack CONFIRM_PANE_GREEN = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
  private static ItemStack CONFIRM_PANE_YELLOW = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
  private static ItemStack CONFIRM_PANE_ORANGE = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
  private static ItemStack INSTRUCTION_BOOK = new ItemStack(Material.BOOK);

  public static ArrayList<CustomItemType> obtainableItems = new ArrayList<>();

  static {
    obtainableItems.add(CustomItemType.FANCY_PANTS);
    obtainableItems.add(CustomItemType.ENDER_BLADE);
    obtainableItems.add(CustomItemType.PRIDE_SHEARS);
    obtainableItems.add(CustomItemType.CREEPER_BOW);
    obtainableItems.add(CustomItemType.LAVA_WALKERS);
    obtainableItems.add(CustomItemType.SUPER_PICK);
    obtainableItems.add(CustomItemType.SLAYER_SWORD);
    obtainableItems.add(CustomItemType.BEDROCK_BREAKER);

    ItemMeta redPaneMeta = CONFIRM_PANE_RED.getItemMeta();
    redPaneMeta.setDisplayName("Destroy these items");
    ArrayList<String> redPaneLore = new ArrayList<>();
    redPaneLore.add("Your items will be consumed");
    redPaneLore.add("and you will receive a new item.");
    redPaneMeta.setLore(redPaneLore);
    CONFIRM_PANE_RED.setItemMeta(redPaneMeta);

    ItemMeta yellowPaneMeta = CONFIRM_PANE_YELLOW.getItemMeta();
    yellowPaneMeta.setDisplayName("Please insert items before forging");
    ArrayList<String> yellowPaneLore = new ArrayList<>();
    yellowPaneLore.add("Place two legendary items into");
    yellowPaneLore.add("the forge slots to forge them together");
    yellowPaneMeta.setLore(yellowPaneLore);
    CONFIRM_PANE_YELLOW.setItemMeta(yellowPaneMeta);

    ItemMeta orangePaneMeta = CONFIRM_PANE_ORANGE.getItemMeta();
    orangePaneMeta.setDisplayName("No possible results");
    ArrayList<String> orangePaneLore = new ArrayList<>();
    orangePaneLore.add("There are no results available");
    orangePaneLore.add("from forging these items");
    orangePaneMeta.setLore(orangePaneLore);
    CONFIRM_PANE_ORANGE.setItemMeta(orangePaneMeta);

    ItemMeta greenPaneMeta = CONFIRM_PANE_GREEN.getItemMeta();
    greenPaneMeta.setDisplayName("Forging confirmed.");
    CONFIRM_PANE_GREEN.setItemMeta(greenPaneMeta);

    ItemMeta bookMeta = INSTRUCTION_BOOK.getItemMeta();
    bookMeta.setDisplayName("Instructions");
    ArrayList<String> bookLore = new ArrayList<>();
    bookLore.add("Place one legendary item");
    bookLore.add("into each of the");
    bookLore.add("two slots above, then");
    bookLore.add("click the red button below.");
    bookLore.add("The two items will be destroyed");
    bookLore.add("and used to create a");
    bookLore.add("new legendary item.");
    bookMeta.setLore(bookLore);
    INSTRUCTION_BOOK.setItemMeta(bookMeta);
  }

  /**
   * Forges two items together and creates a new one,
   * if both of the items are legendary items
   * Returns null if one of the items is not a legendary item
   * @param  firstItem  The first legendary item to be destroyed
   * @param  secondItem The second legendary item to be destroyed
   * @param  player     The player who is reforging
   * @return            A new legendary item, or null.
   */
  public static ItemStack forgeItems(ItemStack firstItem, ItemStack secondItem, Player player) {
    CustomItemType firstType = CustomItemType.getTypeFromCustomItem(firstItem);
    CustomItemType secondType = CustomItemType.getTypeFromCustomItem(secondItem);

    if (firstType == null || secondType == null) {
      return null;
    }

    if (!CustomItemType.isLegendaryItem(firstType) || !CustomItemType.isLegendaryItem(secondType)) {
      return null;
    }

    int firstPrice = CustomItemManager.getItemByType(firstType).getPrice();
    int secondPrice = CustomItemManager.getItemByType(secondType).getPrice();
    int maxPrice = Math.max(firstPrice, secondPrice);

    CustomItemType thirdType;
    ArrayList<CustomItemType> potentials = (ArrayList<CustomItemType>) obtainableItems.clone();
    potentials.remove(firstType);
    potentials.remove(secondType);
    for (CustomItemType potential : (ArrayList<CustomItemType>) potentials.clone()) {
      if (CustomItemManager.getItemByType(potential).getPrice() > maxPrice) {
        potentials.remove(potential);
      }
    }
    if (potentials.size() <= 0) {
      return CONFIRM_PANE_ORANGE;
    }
    thirdType = potentials.get(rng.nextInt(potentials.size()));

    Bukkit.getScheduler().runTaskAsynchronously(PhantomMain.getPlugin(), new Runnable() {
      @Override
      public void run() {
        SQLLinker.pushToDB(player, firstType, secondType, thirdType, System.currentTimeMillis());
      }
    });

    return CustomItemManager.getItemByType(thirdType).getInstance();
  }

  public static void displayShopGUI(Player player) {
    Gui gui = new Gui(5, "Legendary Forge");
  	gui.setDefaultClickAction(event -> {
      ItemStack currItem = event.getCurrentItem();
      if (currItem == null || !CustomItemType.isLegendaryItem(currItem)) {
        // If they're not clicking on air with their legendary item
        if ((event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) ||
             !CustomItemType.isLegendaryItem(event.getCursor())) {
          event.setCancelled(true);
        }
      }
  	});

    // If they close the GUI early, make sure we give them their items back
    gui.setCloseGuiAction(event -> {
      Inventory guiInventory = gui.getInventory();
      // If the glass pane isn't green, refund their items
      if (guiInventory.getItem(40).getType() != Material.LIME_STAINED_GLASS_PANE) {
        // Otherwise, give them the items in the forge slots back
        ItemStack firstItem = guiInventory.getItem(11);
        ItemStack secondItem = guiInventory.getItem(15);
        if (firstItem != null) {
          player.getInventory().addItem(firstItem);
        }
        if (secondItem != null) {
          player.getInventory().addItem(secondItem);
        }
      }
    });

    gui.getFiller().fill(new GuiItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));
    GuiItem air = new GuiItem(new ItemStack(Material.AIR));

    GuiItem confirmPane = new GuiItem(CONFIRM_PANE_RED, event -> {
      gui.updateItem(5, 5, new GuiItem(CONFIRM_PANE_GREEN));
      Inventory guiInventory = gui.getInventory();
      ItemStack firstItem = guiInventory.getItem(11);
      ItemStack secondItem = guiInventory.getItem(15);
      ItemStack newItem = forgeItems(firstItem, secondItem, player);
      if (newItem == null) {
        gui.updateItem(5, 5, new GuiItem(CONFIRM_PANE_YELLOW));
      } else if (newItem == CONFIRM_PANE_ORANGE) {
        gui.updateItem(5, 5, new GuiItem(CONFIRM_PANE_ORANGE));
      } else {
        gui.updateItem(2, 3, air);
        gui.updateItem(2, 7, air);
        player.sendMessage("Your items have been reforged into a new legendary item!");
        player.sendMessage("Your legendary item is in your inventory.");
        player.getInventory().addItem(newItem);
        gui.close(player);
      }
    });

    GuiItem whitePane = new GuiItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
    GuiItem bookItem = new GuiItem(INSTRUCTION_BOOK);

    gui.setItem(1, 3, whitePane);
    gui.setItem(1, 7, whitePane);
    gui.setItem(2, 2, whitePane);
    gui.setItem(2, 6, whitePane);
    gui.setItem(2, 8, whitePane);
    gui.setItem(2, 4, whitePane);
    gui.setItem(2, 6, whitePane);
    gui.setItem(3, 3, whitePane);
    gui.setItem(3, 7, whitePane);

    gui.setItem(4, 5, bookItem);

    gui.setItem(5, 4, whitePane);
    gui.setItem(5, 6, whitePane);

    gui.setItem(2, 3, air);
    gui.setItem(2, 7, air);
    gui.setItem(5, 5, confirmPane);


  	gui.open(player);
  }
}
