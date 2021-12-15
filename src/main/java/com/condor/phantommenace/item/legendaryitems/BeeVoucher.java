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
import org.bukkit.inventory.meta.FireworkMeta;

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.runnable.DoEnderBladeTeleport;
import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.runnable.DisplayCooldown;
import com.condor.phantommenace.item.CustomItemManager;

public class BeeVoucher extends CustomItem {

  private static final String NAME = "Bee Voucher";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static HashMap<UUID, Long> mapOfTimesUsed = new HashMap<>();

  private static Random rng = new Random();

  static {
    loreList.add("Bee Voucher");
    loreList.add("Right-click with this to receive");
    loreList.add("your reward.");
    loreList.add("");

    triggerList.add(PlayerInteractEvent.class);
  }

  public BeeVoucher() {
    super(NAME, loreList, triggerList, CustomItemType.BEE_VOUCHER, 0);
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
        // If they're holding a bee voucher
        if (isBeeVoucher(player.getItemInHand())) {
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

  public boolean isBeeVoucher(ItemStack item) {
    return (item != null) && (item.getType() == Material.PAPER) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.BEE_VOUCHER);
  }

  private ItemStack getShulker(Player player) {
    ItemStack is = new ItemStack(Material.LIME_SHULKER_BOX);
    BlockStateMeta meta = (BlockStateMeta) is.getItemMeta();
    meta.setDisplayName("Bee Box");
    ShulkerBox box = (ShulkerBox) meta.getBlockState();
    Inventory inventory = box.getInventory();
    ItemStack twentyVoidCoins = CustomItemManager.getItemByType(CustomItemType.DEFENDER_TOKEN).getInstance();
    twentyVoidCoins.setAmount(15);
    ItemStack twoBeeNests = new ItemStack(Material.BEE_NEST, 2);
    ItemMeta beeNestMeta = twoBeeNests.getItemMeta();
    beeNestMeta.setDisplayName("Buzzing Box - do not shake!");
    twoBeeNests.setItemMeta(beeNestMeta);
    ItemStack sixtyFourRedTulips = new ItemStack(Material.RED_TULIP, 64);
    ItemMeta redTulipMeta = sixtyFourRedTulips.getItemMeta();
    redTulipMeta.setDisplayName("Red Bee Chow");
    sixtyFourRedTulips.setItemMeta(redTulipMeta);
    ItemStack sixtyFourWhiteTulips = new ItemStack(Material.WHITE_TULIP, 64);
    ItemMeta whiteTulipMeta = sixtyFourWhiteTulips.getItemMeta();
    whiteTulipMeta.setDisplayName("White Bee Chow");
    sixtyFourWhiteTulips.setItemMeta(whiteTulipMeta);
    ItemStack sixtyFourOrangeTulips = new ItemStack(Material.ORANGE_TULIP, 64);
    ItemMeta orangeTulipMeta = sixtyFourOrangeTulips.getItemMeta();
    orangeTulipMeta.setDisplayName("Orange Bee Chow");
    sixtyFourOrangeTulips.setItemMeta(orangeTulipMeta);
    ItemStack sixtyFourCornflowers = new ItemStack(Material.CORNFLOWER, 64);
    ItemMeta cornflowerMeta = sixtyFourCornflowers.getItemMeta();
    cornflowerMeta.setDisplayName("Blue Bee Chow");
    sixtyFourCornflowers.setItemMeta(cornflowerMeta);
    ItemStack sixteenHoneyBottles = new ItemStack(Material.HONEY_BOTTLE, 16);
    ItemMeta honeyBottleMeta = sixteenHoneyBottles.getItemMeta();
    honeyBottleMeta.setDisplayName("Yellow Insect Vomit");
    sixteenHoneyBottles.setItemMeta(honeyBottleMeta);
    ItemStack beeEgg = new ItemStack(Material.BEE_SPAWN_EGG);
    ItemMeta beeEggMeta = beeEgg.getItemMeta();
    beeEggMeta.setDisplayName("Bee Container");
    beeEgg.setItemMeta(beeEggMeta);
    ItemStack sixtyFourHoneycomb = new ItemStack(Material.HONEYCOMB, 64);
    ItemMeta honeycombMeta = sixtyFourHoneycomb.getItemMeta();
    honeycombMeta.setDisplayName("Structural Bee Construct");
    sixtyFourHoneycomb.setItemMeta(honeycombMeta);
    ItemStack prideShears = CustomItemManager.getItemByType(CustomItemType.PRIDE_SHEARS).getInstance();
    ItemStack flightPotion = CustomItemManager.getItemByType(CustomItemType.FLIGHT_POTION).getInstance();
    inventory.setItem(0, twoBeeNests);
    inventory.setItem(1, sixteenHoneyBottles);
    inventory.setItem(2, twentyVoidCoins);
    inventory.setItem(3, sixtyFourRedTulips);
    inventory.setItem(4, prideShears);
    inventory.setItem(5, sixtyFourWhiteTulips);
    inventory.setItem(6, twentyVoidCoins);
    inventory.setItem(7, sixteenHoneyBottles);
    inventory.setItem(8, twoBeeNests);
    inventory.setItem(9, twentyVoidCoins);
    inventory.setItem(10, twentyVoidCoins);
    inventory.setItem(11, sixtyFourHoneycomb);
    inventory.setItem(12, flightPotion);
    inventory.setItem(13, beeEgg);
    inventory.setItem(14, flightPotion);
    inventory.setItem(15, sixtyFourHoneycomb);
    inventory.setItem(16, twentyVoidCoins);
    inventory.setItem(17, twentyVoidCoins);
    inventory.setItem(18, twoBeeNests);
    inventory.setItem(19, sixteenHoneyBottles);
    inventory.setItem(20, twentyVoidCoins);
    inventory.setItem(21, sixtyFourCornflowers);
    inventory.setItem(22, twentyVoidCoins);
    inventory.setItem(23, sixtyFourOrangeTulips);
    inventory.setItem(24, twentyVoidCoins);
    inventory.setItem(25, sixteenHoneyBottles);
    inventory.setItem(26, twoBeeNests);
    meta.setBlockState(box);
    is.setItemMeta(meta);
    return is;
  }

}
