package com.condor.phantommenace.item.legendaryitems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
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

public class NetheriteVoucher extends CustomItem {

  private static final String NAME = "Netherite Voucher";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static HashMap<UUID, Long> mapOfTimesUsed = new HashMap<>();

  private static Random rng = new Random();

  static {
    loreList.add("Netherite Voucher");
    loreList.add("Right-click with this to receive");
    loreList.add("your reward.");
    loreList.add("");

    triggerList.add(PlayerInteractEvent.class);
  }

  public NetheriteVoucher() {
    super(NAME, loreList, triggerList, CustomItemType.NETHERITE_VOUCHER, 0);
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
        // If they're holding a netherite voucher
        if (isNetheriteVoucher(player.getItemInHand())) {
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

  public boolean isNetheriteVoucher(ItemStack item) {
    return (item != null) && (item.getType() == Material.PAPER) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.NETHERITE_VOUCHER);
  }

  private ItemStack getShulker(Player player) {
    ItemStack is = new ItemStack(Material.GRAY_SHULKER_BOX);
    BlockStateMeta meta = (BlockStateMeta) is.getItemMeta();
    meta.setDisplayName("Netherite Contributor");
    ShulkerBox box = (ShulkerBox) meta.getBlockState();
    Inventory inventory = box.getInventory();
    ItemStack thirtyTwoVoidCoins = CustomItemManager.getItemByType(CustomItemType.DEFENDER_TOKEN).getInstance();
    thirtyTwoVoidCoins.setAmount(32);
    ItemStack netheriteIngot = new ItemStack(Material.NETHERITE_INGOT, 1);
    ItemStack sixtyFourDispensers = new ItemStack(Material.DISPENSER, 64);
    ItemStack sixtyFourObservers = new ItemStack(Material.OBSERVER, 64);
    ItemStack twoNetheriteIngots = new ItemStack(Material.NETHERITE_INGOT, 2);
    ItemStack twoAncientDebris = new ItemStack(Material.ANCIENT_DEBRIS, 2);
    ItemStack sixtyFourGoldIngots = new ItemStack(Material.GOLD_INGOT, 64);
    ItemStack sixtyFourIronBlocks = new ItemStack(Material.IRON_BLOCK, 64);
    ItemStack sixtyFourRedstoneBlocks = new ItemStack(Material.REDSTONE_BLOCK, 64);
    ItemStack prideShears = CustomItemManager.getItemByType(CustomItemType.PRIDE_SHEARS).getInstance();
    ItemStack flightPotion = CustomItemManager.getItemByType(CustomItemType.FLIGHT_POTION).getInstance();
    ItemStack thankYouBook = new ItemStack(Material.WRITTEN_BOOK);
    BookMeta bookMeta = (BookMeta) thankYouBook.getItemMeta();
    bookMeta.setTitle("Thank you!");
    bookMeta.setAuthor(ChatColor.RED + "Void" + ChatColor.GRAY + "Tree");
    bookMeta.addPage("Thank you for contributing to " + ChatColor.RED + "Void" + ChatColor.GRAY + "Tree" + ChatColor.RESET + ", " + player.getDisplayName());
    thankYouBook.setItemMeta(bookMeta);
    inventory.setItem(0, netheriteIngot);
    inventory.setItem(1, thirtyTwoVoidCoins);
    inventory.setItem(2, thirtyTwoVoidCoins);
    inventory.setItem(3, sixtyFourDispensers);
    inventory.setItem(4, prideShears);
    inventory.setItem(5, sixtyFourRedstoneBlocks);
    inventory.setItem(6, thirtyTwoVoidCoins);
    inventory.setItem(7, thirtyTwoVoidCoins);
    inventory.setItem(8, netheriteIngot);
    inventory.setItem(9, sixtyFourGoldIngots);
    inventory.setItem(10, thirtyTwoVoidCoins);
    inventory.setItem(11, twoNetheriteIngots);
    inventory.setItem(12, flightPotion);
    inventory.setItem(13, thankYouBook);
    inventory.setItem(14, flightPotion);
    inventory.setItem(15, twoNetheriteIngots);
    inventory.setItem(16, thirtyTwoVoidCoins);
    inventory.setItem(17, sixtyFourGoldIngots);
    inventory.setItem(18, netheriteIngot);
    inventory.setItem(19, thirtyTwoVoidCoins);
    inventory.setItem(20, thirtyTwoVoidCoins);
    inventory.setItem(21, sixtyFourRedstoneBlocks);
    inventory.setItem(22, prideShears);
    inventory.setItem(23, sixtyFourObservers);
    inventory.setItem(24, thirtyTwoVoidCoins);
    inventory.setItem(25, thirtyTwoVoidCoins);
    inventory.setItem(26, netheriteIngot);
    meta.setBlockState(box);
    is.setItemMeta(meta);
    return is;
  }

}
