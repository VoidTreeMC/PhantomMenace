package com.condor.phantommenace.item;

import java.util.TreeMap;

import org.bukkit.event.Event;

import com.condor.phantommenace.item.legendaryitems.*;
import com.condor.phantommenace.item.simpleitems.*;

public class CustomItemManager {
  private static TreeMap<CustomItemType, CustomItem> itemMap = new TreeMap<>();

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
  }

  public static double getPriceScale() {
    return priceScale;
  }

  public static TreeMap<CustomItemType, CustomItem> getMap() {
    return itemMap;
  }

  public static CustomItem getItemByType(CustomItemType type) {
    return itemMap.get(type);
  }
}
