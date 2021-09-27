package com.condor.phantommenace.item.legendaryitems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Entity;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Event;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.World;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.BookMeta;

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.runnable.DoEnderBladeTeleport;
import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.runnable.DisplayCooldown;
import com.condor.phantommenace.item.CustomItemManager;

public class GoldVoucher extends CustomItem {

  private static final String NAME = "Gold Voucher";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static TreeMap<UUID, Long> mapOfTimesUsed = new TreeMap<>();

  private static Random rng = new Random();

  private static ArrayList<CustomItemType> legendaryItemTypes = new ArrayList<>();

  static {
    legendaryItemTypes.add(CustomItemType.FANCY_PANTS);
    legendaryItemTypes.add(CustomItemType.ENDER_BLADE);
    legendaryItemTypes.add(CustomItemType.CREEPER_BOW);
    legendaryItemTypes.add(CustomItemType.LAVA_WALKERS);
    legendaryItemTypes.add(CustomItemType.SUPER_PICK);
    legendaryItemTypes.add(CustomItemType.SLAYER_SWORD);
  }

  static {
    loreList.add("Gold Voucher");
    loreList.add("Right-click with this to receive");
    loreList.add("your reward.");
    loreList.add("");

    triggerList.add(PlayerInteractEvent.class);
  }

  public GoldVoucher() {
    super(NAME, loreList, triggerList, CustomItemType.GOLD_VOUCHER, 0);
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.PAPER, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName(NAME);
    meta.setLore(loreList);
    // meta.setUnbreakable(true);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    boolean ret = false;
    if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      // If they right-clicked
      if ((pie.getAction() == Action.RIGHT_CLICK_AIR ||
          pie.getAction() == Action.RIGHT_CLICK_BLOCK)) {
        // If they're holding a gold voucher
        if (isGoldVoucher(player.getItemInHand())) {
          ret = true;
        }
      }
    }
    return ret;
  }

  public void execute(Event event) {
    if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      player.getItemInHand().setAmount(0);
      player.getInventory().addItem(getShulker(player));
    }
  }

  public boolean isGoldVoucher(ItemStack item) {
    return (item != null) && (item.getType() == Material.PAPER) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.GOLD_VOUCHER);
  }

  private ItemStack getShulker(Player player) {
    ItemStack is = new ItemStack(Material.YELLOW_SHULKER_BOX);
    BlockStateMeta meta = (BlockStateMeta) is.getItemMeta();
    meta.setDisplayName("Gold Contributor");
    ShulkerBox box = (ShulkerBox) meta.getBlockState();
    Inventory inventory = box.getInventory();
    ItemStack sixteenVoidCoins = CustomItemManager.getItemByType(CustomItemType.DEFENDER_TOKEN).getInstance();
    sixteenVoidCoins.setAmount(16);
    ItemStack sixtyFourGold = new ItemStack(Material.GOLD_INGOT, 64);
    ItemStack prideShears = CustomItemManager.getItemByType(CustomItemType.PRIDE_SHEARS).getInstance();
    ItemStack thankYouBook = new ItemStack(Material.WRITTEN_BOOK);
    ItemStack randomLegendaryItem = CustomItemManager.getItemByType(legendaryItemTypes.get(rng.nextInt(legendaryItemTypes.size()))).getInstance();
    BookMeta bookMeta = (BookMeta) thankYouBook.getItemMeta();
    bookMeta.setTitle("Thank you!");
    bookMeta.setAuthor(ChatColor.RED + "Void" + ChatColor.GRAY + "Tree");
    bookMeta.addPage("Thank you for contributing to " + ChatColor.RED + "Void" + ChatColor.GRAY + "Tree" + ChatColor.RESET + ", " + player.getDisplayName());
    thankYouBook.setItemMeta(bookMeta);
    inventory.setItem(0, sixteenVoidCoins);
    inventory.setItem(1, sixtyFourGold);
    inventory.setItem(2, sixteenVoidCoins);
    inventory.setItem(3, sixtyFourGold);
    inventory.setItem(4, randomLegendaryItem);
    inventory.setItem(5, sixtyFourGold);
    inventory.setItem(6, sixteenVoidCoins);
    inventory.setItem(7, sixtyFourGold);
    inventory.setItem(8, sixteenVoidCoins);
    inventory.setItem(9, sixtyFourGold);
    inventory.setItem(10, sixteenVoidCoins);
    inventory.setItem(11, sixtyFourGold);
    inventory.setItem(12, sixteenVoidCoins);
    inventory.setItem(13, thankYouBook);
    inventory.setItem(14, sixteenVoidCoins);
    inventory.setItem(15, sixtyFourGold);
    inventory.setItem(16, sixteenVoidCoins);
    inventory.setItem(17, sixtyFourGold);
    inventory.setItem(18, sixteenVoidCoins);
    inventory.setItem(19, sixtyFourGold);
    inventory.setItem(20, sixteenVoidCoins);
    inventory.setItem(21, sixtyFourGold);
    inventory.setItem(22, prideShears);
    inventory.setItem(23, sixtyFourGold);
    inventory.setItem(24, sixteenVoidCoins);
    inventory.setItem(25, sixtyFourGold);
    inventory.setItem(26, sixteenVoidCoins);
    meta.setBlockState(box);
    is.setItemMeta(meta);
    return is;
  }

}
