package com.condor.phantommenace.item.simpleitems;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.event.Event;

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;

public class ZombiePiglinSpawnEgg extends CustomItem {

  private static final String NAME = "Zombie Piglin Spawn Egg";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static Random rng = new Random();

  static {
    loreList.add("Zombie Piglin Spawn Egg");
    loreList.add("");
  }

  public ZombiePiglinSpawnEgg() {
    super(NAME, loreList, triggerList, CustomItemType.ZOMBIE_PIGLIN_EGG, 50);
    loreList.add(this.getPrice() + " VoidCoins");
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.ZOMBIFIED_PIGLIN_SPAWN_EGG, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setLore(loreList);
    meta.setUnbreakable(true);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    return false;
  }

  public void execute(Event event) {

  }
}
