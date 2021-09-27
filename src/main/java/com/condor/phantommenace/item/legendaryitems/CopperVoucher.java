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

public class CopperVoucher extends CustomItem {

  private static final String NAME = "Copper Voucher";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static TreeMap<UUID, Long> mapOfTimesUsed = new TreeMap<>();

  private static Random rng = new Random();

  static {
    loreList.add("Copper Voucher");
    loreList.add("Right-click with this to receive");
    loreList.add("your reward.");
    loreList.add("");

    triggerList.add(PlayerInteractEvent.class);
  }

  public CopperVoucher() {
    super(NAME, loreList, triggerList, CustomItemType.COPPER_VOUCHER, 0);
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
        // If they're holding a copper voucher
        if (isCopperVoucher(player.getItemInHand())) {
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

  public boolean isCopperVoucher(ItemStack item) {
    return (item != null) && (item.getType() == Material.PAPER) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.COPPER_VOUCHER);
  }

  private ItemStack getShulker(Player player) {
    ItemStack is = new ItemStack(Material.ORANGE_SHULKER_BOX);
    BlockStateMeta meta = (BlockStateMeta) is.getItemMeta();
    meta.setDisplayName("Copper Contributor");
    ShulkerBox box = (ShulkerBox) meta.getBlockState();
    Inventory inventory = box.getInventory();
    ItemStack sixVoidCoins = CustomItemManager.getItemByType(CustomItemType.DEFENDER_TOKEN).getInstance();
    sixVoidCoins.setAmount(6);
    ItemStack sixtyFourCopper = new ItemStack(Material.COPPER_INGOT, 64);
    ItemStack sixteenCreeperFireworks = CustomItemManager.getItemByType(CustomItemType.CREEPER_FIREWORK).getInstance();
    ItemStack thankYouBook = new ItemStack(Material.WRITTEN_BOOK);
    BookMeta bookMeta = (BookMeta) thankYouBook.getItemMeta();
    bookMeta.setTitle("Thank you!");
    bookMeta.setAuthor(ChatColor.RED + "Void" + ChatColor.GRAY + "Tree");
    bookMeta.addPage("Thank you for contributing to " + ChatColor.RED + "Void" + ChatColor.GRAY + "Tree" + ChatColor.RESET + ", " + player.getDisplayName());
    thankYouBook.setItemMeta(bookMeta);
    sixteenCreeperFireworks.setAmount(16);
    inventory.setItem(0, sixVoidCoins);
    inventory.setItem(1, sixtyFourCopper);
    inventory.setItem(2, sixVoidCoins);
    inventory.setItem(3, sixtyFourCopper);
    inventory.setItem(4, sixVoidCoins);
    inventory.setItem(5, sixtyFourCopper);
    inventory.setItem(6, sixVoidCoins);
    inventory.setItem(7, sixtyFourCopper);
    inventory.setItem(8, sixVoidCoins);
    inventory.setItem(9, sixtyFourCopper);
    inventory.setItem(10, sixteenCreeperFireworks);
    inventory.setItem(11, sixtyFourCopper);
    inventory.setItem(12, sixteenCreeperFireworks);
    inventory.setItem(13, thankYouBook);
    inventory.setItem(14, sixteenCreeperFireworks);
    inventory.setItem(15, sixtyFourCopper);
    inventory.setItem(16, sixteenCreeperFireworks);
    inventory.setItem(17, sixtyFourCopper);
    inventory.setItem(18, sixVoidCoins);
    inventory.setItem(19, sixtyFourCopper);
    inventory.setItem(20, sixVoidCoins);
    inventory.setItem(21, sixtyFourCopper);
    inventory.setItem(22, sixVoidCoins);
    inventory.setItem(23, sixtyFourCopper);
    inventory.setItem(24, sixVoidCoins);
    inventory.setItem(25, sixtyFourCopper);
    inventory.setItem(26, sixVoidCoins);
    meta.setBlockState(box);
    is.setItemMeta(meta);
    return is;
  }

}
