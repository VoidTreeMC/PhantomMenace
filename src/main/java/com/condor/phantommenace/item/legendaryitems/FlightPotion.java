package com.condor.phantommenace.item.legendaryitems;

import java.util.ArrayList;
import java.util.Random;

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
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.ChatColor;

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.runnable.RemoveFlightEffect;
import com.condor.phantommenace.main.PhantomMain;

public class FlightPotion extends CustomItem {

  private static final String NAME = "Flight Potion";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  // 20 minutes
  private static final long DURATION = 20 * 20 * 60;

  static {
    loreList.add("Flight Potion");
    loreList.add("For meeting the phantoms");
    loreList.add("in their own domain.");
    loreList.add("Or just building a roof.");
    loreList.add("This potion is provided");
    loreList.add("with no warranty.");
    loreList.add("");

    triggerList.add(PlayerItemConsumeEvent.class);
  }

  public FlightPotion() {
    super(NAME, loreList, triggerList, CustomItemType.FLIGHT_POTION, 5);
    loreList.add(this.getPrice() + " VoidCoins");
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.POTION, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName(NAME);
    meta.setLore(loreList);
    meta.setUnbreakable(true);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    boolean ret = false;
    if (event instanceof PlayerItemConsumeEvent) {
      PlayerItemConsumeEvent pice = (PlayerItemConsumeEvent) event;
      ItemStack item = pice.getItem();
      CustomItemType type = CustomItemType.getTypeFromCustomItem(item);
      if (type == CustomItemType.FLIGHT_POTION) {
        ret = true;
      }
    }
    return ret;
  }

  public void execute(Event event) {
    PlayerItemConsumeEvent pice = (PlayerItemConsumeEvent) event;
    Player player = pice.getPlayer();
    player.setAllowFlight(true);
    player.sendMessage(ChatColor.AQUA + "The ground loses its grip on you.");
    // player.sendMessage(ChatColor.AQUA + "You are able to fly for " + ChatColor.GOLD + "20" + ChatColor.AQUA + " minutes.");
    (new RemoveFlightEffect(player)).runTaskLaterAsynchronously(PhantomMain.getPlugin(), DURATION);
  }
}
