package com.condor.phantommenace.item;

import java.util.HashMap;

import org.bukkit.event.Event;

import com.condor.phantommenace.item.legendaryitems.*;
import com.condor.phantommenace.item.simpleitems.*;

public class CustomItemManager {
  private static HashMap<CustomItemType, CustomItem> itemMap = new HashMap<>();

  private static final double priceScale = 1;

  // Add custom items here
  static {
    // itemMap.put(CustomItemType.INSOMNIA_POTION, InsomniaPotion);
    itemMap.put(CustomItemType.FANCY_PANTS, new FancyPants());
    itemMap.put(CustomItemType.DEFENDER_TOKEN, new DefenderToken());
    itemMap.put(CustomItemType.ENDER_BLADE, new EnderBlade());
    itemMap.put(CustomItemType.SUBDUED_ENDER_BLADE, new SubduedEnderBlade());
    itemMap.put(CustomItemType.PRIDE_SHEARS, new PrideShears());
    itemMap.put(CustomItemType.CREEPER_BOW, new CreeperBow());
    itemMap.put(CustomItemType.LAVA_WALKERS, new LavaWalkers());
    itemMap.put(CustomItemType.CREEPER_FIREWORK, new CreeperFirework());
    itemMap.put(CustomItemType.SUPER_PICK, new SuperPick());
    itemMap.put(CustomItemType.SLAYER_SWORD, new SlayerSword());
    itemMap.put(CustomItemType.FLIGHT_POTION, new FlightPotion());
    itemMap.put(CustomItemType.ZOMBIE_EGG, new ZombieSpawnEgg());
    itemMap.put(CustomItemType.FOX_EGG, new FoxSpawnEgg());
    itemMap.put(CustomItemType.SPIDER_EGG, new SpiderSpawnEgg());
    itemMap.put(CustomItemType.SLIME_EGG, new SlimeSpawnEgg());
    itemMap.put(CustomItemType.ZOMBIE_PIGLIN_EGG, new ZombiePiglinSpawnEgg());
    itemMap.put(CustomItemType.COW_EGG, new CowSpawnEgg());
    itemMap.put(CustomItemType.BAT_EGG, new BatSpawnEgg());
    itemMap.put(CustomItemType.COPPER_VOUCHER, new CopperVoucher());
    itemMap.put(CustomItemType.IRON_VOUCHER, new IronVoucher());
    itemMap.put(CustomItemType.GOLD_VOUCHER, new GoldVoucher());
    itemMap.put(CustomItemType.DIAMOND_VOUCHER, new DiamondVoucher());
    itemMap.put(CustomItemType.NETHERITE_VOUCHER, new NetheriteVoucher());
    itemMap.put(CustomItemType.COW_VOUCHER, new CowVoucher());
    itemMap.put(CustomItemType.FISH_VOUCHER, new FishVoucher());
    itemMap.put(CustomItemType.BEE_VOUCHER, new BeeVoucher());
    itemMap.put(CustomItemType.ALMOND_CAKE, new AlmondCake());
    itemMap.put(CustomItemType.BEDROCK_BREAKER, new BedrockBreaker());
    itemMap.put(CustomItemType.REPLANTER_HOE, new ReplanterHoe());
    itemMap.put(CustomItemType.FOLIAGE_AXE, new FoliageAxe());
    itemMap.put(CustomItemType.BEE_LORD_WAND, new BeeLordWand());
  }

  public static double getPriceScale() {
    return priceScale;
  }

  public static HashMap<CustomItemType, CustomItem> getMap() {
    return itemMap;
  }

  public static CustomItem getItemByType(CustomItemType type) {
    return itemMap.get(type);
  }
}
