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

public class DiamondVoucher extends CustomItem {

  private static final String NAME = "Diamond Voucher";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static HashMap<UUID, Long> mapOfTimesUsed = new HashMap<>();

  private static Random rng = new Random();

  static {
    loreList.add("Diamond Voucher");
    loreList.add("Right-click with this to receive");
    loreList.add("your reward.");
    loreList.add("");

    triggerList.add(PlayerInteractEvent.class);
  }

  public DiamondVoucher() {
    super(NAME, loreList, triggerList, CustomItemType.DIAMOND_VOUCHER, 0);
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
        // If they're holding a diamond voucher
        if (isDiamondVoucher(player.getItemInHand())) {
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

  public boolean isDiamondVoucher(ItemStack item) {
    return (item != null) && (item.getType() == Material.PAPER) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.DIAMOND_VOUCHER);
  }

  private ItemStack getShulker(Player player) {
    ItemStack is = new ItemStack(Material.CYAN_SHULKER_BOX);
    BlockStateMeta meta = (BlockStateMeta) is.getItemMeta();
    meta.setDisplayName("Diamond Contributor");
    ShulkerBox box = (ShulkerBox) meta.getBlockState();
    Inventory inventory = box.getInventory();
    ItemStack sixtyFourVoidCoins = CustomItemManager.getItemByType(CustomItemType.DEFENDER_TOKEN).getInstance();
    sixtyFourVoidCoins.setAmount(64);
    ItemStack sixteenDiamond = new ItemStack(Material.DIAMOND, 16);
    ItemStack sixtyFourPistons = new ItemStack(Material.PISTON, 64);
    ItemStack sixtyFourSlimeBlocks = new ItemStack(Material.SLIME_BLOCK, 64);
    ItemStack sixtyFourIronBlocks = new ItemStack(Material.IRON_BLOCK, 64);
    ItemStack wandOfRegeneration = CustomItemManager.getItemByType(CustomItemType.REPLANTER_HOE).getInstance();
    ItemStack creeperbane = CustomItemManager.getItemByType(CustomItemType.CREEPER_BOW).getInstance();
    ItemStack thankYouBook = new ItemStack(Material.WRITTEN_BOOK);
    BookMeta bookMeta = (BookMeta) thankYouBook.getItemMeta();
    bookMeta.setTitle("Thank you!");
    bookMeta.setAuthor(ChatColor.RED + "Void" + ChatColor.GRAY + "Tree");
    bookMeta.addPage("Thank you for contributing to " + ChatColor.RED + "Void" + ChatColor.GRAY + "Tree" + ChatColor.RESET + ", " + player.getDisplayName());
    thankYouBook.setItemMeta(bookMeta);
    inventory.setItem(0, sixteenDiamond);
    inventory.setItem(1, sixtyFourPistons);
    inventory.setItem(2, sixteenDiamond);
    inventory.setItem(3, sixtyFourSlimeBlocks);
    inventory.setItem(4, sixteenDiamond);
    inventory.setItem(5, sixtyFourSlimeBlocks);
    inventory.setItem(6, sixteenDiamond);
    inventory.setItem(7, sixtyFourIronBlocks);
    inventory.setItem(8, sixteenDiamond);
    inventory.setItem(9, sixtyFourVoidCoins);
    inventory.setItem(10, sixteenDiamond);
    inventory.setItem(11, sixtyFourVoidCoins);
    inventory.setItem(12, sixteenDiamond);
    inventory.setItem(13, thankYouBook);
    inventory.setItem(14, sixteenDiamond);
    inventory.setItem(15, sixtyFourVoidCoins);
    inventory.setItem(16, sixteenDiamond);
    inventory.setItem(17, sixtyFourVoidCoins);
    inventory.setItem(18, sixteenDiamond);
    inventory.setItem(19, sixtyFourIronBlocks);
    inventory.setItem(20, sixteenDiamond);
    inventory.setItem(21, sixtyFourSlimeBlocks);
    inventory.setItem(22, sixteenDiamond);
    inventory.setItem(23, sixtyFourSlimeBlocks);
    inventory.setItem(24, sixteenDiamond);
    inventory.setItem(25, sixtyFourPistons);
    inventory.setItem(26, sixteenDiamond);
    meta.setBlockState(box);
    is.setItemMeta(meta);
    return is;
  }

}
