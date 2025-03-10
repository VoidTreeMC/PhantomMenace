package com.condor.phantommenace.item.legendaryitems;

import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;

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
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.block.Action;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Sheep;
import org.bukkit.block.data.type.Beehive;
import io.papermc.paper.event.block.PlayerShearBlockEvent;
import org.bukkit.event.block.BlockShearEntityEvent;
import org.bukkit.block.Block;

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.runnable.DoEnderBladeTeleport;
import com.condor.phantommenace.main.PhantomMain;

public class PrideShears extends CustomItem {

  private static final String NAME = "Pride Shears";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static ArrayList<Material> WOOL_TYPES = new ArrayList<>();
  private static ArrayList<Material> CANDLE_TYPES = new ArrayList<>();

  private static Random rng = new Random();

  static {
    loreList.add("Pride Shears");
    loreList.add("Because the world needs more rainbows.");
    loreList.add("");

    triggerList.add(PlayerShearEntityEvent.class);
    triggerList.add(PlayerShearBlockEvent.class);
    triggerList.add(BlockShearEntityEvent.class);

    WOOL_TYPES.add(Material.WHITE_WOOL);
    WOOL_TYPES.add(Material.ORANGE_WOOL);
    WOOL_TYPES.add(Material.MAGENTA_WOOL);
    WOOL_TYPES.add(Material.LIGHT_BLUE_WOOL);
    WOOL_TYPES.add(Material.YELLOW_WOOL);
    WOOL_TYPES.add(Material.LIME_WOOL);
    WOOL_TYPES.add(Material.PINK_WOOL);
    WOOL_TYPES.add(Material.GRAY_WOOL);
    WOOL_TYPES.add(Material.LIGHT_GRAY_WOOL);
    WOOL_TYPES.add(Material.CYAN_WOOL);
    WOOL_TYPES.add(Material.PURPLE_WOOL);
    WOOL_TYPES.add(Material.BLUE_WOOL);
    WOOL_TYPES.add(Material.BROWN_WOOL);
    WOOL_TYPES.add(Material.GREEN_WOOL);
    WOOL_TYPES.add(Material.RED_WOOL);
    WOOL_TYPES.add(Material.BLACK_WOOL);

    CANDLE_TYPES.add(Material.CANDLE);
    CANDLE_TYPES.add(Material.WHITE_CANDLE);
    CANDLE_TYPES.add(Material.ORANGE_CANDLE);
    CANDLE_TYPES.add(Material.MAGENTA_CANDLE);
    CANDLE_TYPES.add(Material.LIGHT_BLUE_CANDLE);
    CANDLE_TYPES.add(Material.YELLOW_CANDLE);
    CANDLE_TYPES.add(Material.LIME_CANDLE);
    CANDLE_TYPES.add(Material.PINK_CANDLE);
    CANDLE_TYPES.add(Material.GRAY_CANDLE);
    CANDLE_TYPES.add(Material.LIGHT_GRAY_CANDLE);
    CANDLE_TYPES.add(Material.CYAN_CANDLE);
    CANDLE_TYPES.add(Material.PURPLE_CANDLE);
    CANDLE_TYPES.add(Material.BLUE_CANDLE);
    CANDLE_TYPES.add(Material.BROWN_CANDLE);
    CANDLE_TYPES.add(Material.GREEN_CANDLE);
    CANDLE_TYPES.add(Material.RED_CANDLE);
    CANDLE_TYPES.add(Material.BLACK_CANDLE);
  }

  public PrideShears() {
    super(NAME, loreList, triggerList, CustomItemType.PRIDE_SHEARS, 10);
    loreList.add(this.getPrice() + " VoidCoins");
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.SHEARS, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName(NAME);
    meta.setLore(loreList);
    meta.addEnchant(Enchantment.MENDING, 1, false);
    meta.addEnchant(Enchantment.DURABILITY, 3, false);
    // meta.setUnbreakable(true);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    boolean ret = false;
    if (event instanceof PlayerShearEntityEvent) {
      PlayerShearEntityEvent psee = (PlayerShearEntityEvent) event;
      Player player = psee.getPlayer();
      // If they right-clicked a sheep
      if (psee.getEntity().getType() == EntityType.SHEEP) {
        if (isPrideShears(psee.getItem())) {
          Sheep sheep = (Sheep) psee.getEntity();
          ret = !sheep.isSheared();
        }
      }
    } else if (event instanceof PlayerShearBlockEvent) {
      PlayerShearBlockEvent psbe = (PlayerShearBlockEvent) event;
      Player player = psbe.getPlayer();
      // If they right-clicked a bee hive
      if (psbe.getBlock().getType() == Material.BEE_NEST || psbe.getBlock().getType() == Material.BEEHIVE) {
        if (isPrideShears(psbe.getItem())) {
          ret = true;
        }
      }
    } else if (event instanceof BlockShearEntityEvent) {
      BlockShearEntityEvent bsee = (BlockShearEntityEvent) event;
      Block block = bsee.getBlock();
      if (block.getType() == Material.DISPENSER) {
        ItemStack tool = bsee.getTool();
        if (bsee.getEntity().getType() == EntityType.SHEEP) {
          if (isPrideShears(tool)) {
            ret = true;
          }
        }
      }
    }
    return ret;
  }

  public void execute(Event event) {
    if (event instanceof PlayerShearEntityEvent) {
      PlayerShearEntityEvent psee = (PlayerShearEntityEvent) event;
      Player player = psee.getPlayer();
      psee.setCancelled(true);
      ((Sheep) (psee.getEntity())).setSheared(true);
      Material woolType = WOOL_TYPES.get(rng.nextInt(WOOL_TYPES.size()));
      int woolAmt = rng.nextInt(5) + 3;
      ItemStack item = new ItemStack(woolType, woolAmt);
      HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
      Location sheepLoc = psee.getEntity().getLocation();
      for (ItemStack leftoverItem : leftovers.values()) {
        sheepLoc.getWorld().dropItem(sheepLoc, leftoverItem);
      }
    } else if (event instanceof PlayerShearBlockEvent) {
      PlayerShearBlockEvent psbe = (PlayerShearBlockEvent) event;
      Player player = psbe.getPlayer();
      Beehive hive = (Beehive) psbe.getBlock().getBlockData();
      // If the hive is at max honey level
      if (hive.getHoneyLevel() == hive.getMaximumHoneyLevel()) {
        hive.setHoneyLevel(0);
        psbe.setCancelled(true);
        Material candleType = CANDLE_TYPES.get(rng.nextInt(CANDLE_TYPES.size()));
        int candleAmt = rng.nextInt(5) + 3;
        ItemStack item = new ItemStack(candleType, candleAmt);
        HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
        Location blockLoc = psbe.getBlock().getLocation();
        for (ItemStack leftoverItem : leftovers.values()) {
          blockLoc.getWorld().dropItem(blockLoc, leftoverItem);
        }
      }
      psbe.getBlock().setBlockData(hive);
    } else if (event instanceof BlockShearEntityEvent) {
      BlockShearEntityEvent bsee = (BlockShearEntityEvent) event;
      bsee.setCancelled(true);
      ((Sheep) (bsee.getEntity())).setSheared(true);
      Material woolType = WOOL_TYPES.get(rng.nextInt(WOOL_TYPES.size()));
      int woolAmt = rng.nextInt(5) + 3;
      ItemStack item = new ItemStack(woolType, woolAmt);
      Location sheepLoc = bsee.getEntity().getLocation();
      sheepLoc.getWorld().dropItem(sheepLoc, item);
    }
  }

  public boolean isPrideShears(ItemStack item) {
    return (item != null) && (item.getType() == Material.SHEARS) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.PRIDE_SHEARS);
  }
}
