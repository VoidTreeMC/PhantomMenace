package com.condor.phantommenace.item.legendaryitems;

import java.util.ArrayList;
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
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.ChatColor;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.Bukkit;

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.runnable.RemoveFlightEffect;
import com.condor.phantommenace.main.PhantomMain;

public class FlightPotion extends CustomItem {

  private static final String NAME = "Flight Potion";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  // 10 minutes
  private static final long DURATION_TICKS = 20 * 10 * 60;
  private static final long DURATION_MS = DURATION_TICKS / 20 * 1000;
  public static final long COOLDOWN_TIME_IN_SECONDS = 10;
  public static final long COOLDOWN_TIME = 20 * COOLDOWN_TIME_IN_SECONDS;
  // The maximum amount of damage a player can take before their flight potion goes on cooldown
  private static final int MAX_DMG = 10;

  public static final String METADATA_KEY = "affectedByFlightPotion";

  // Keeps track of when the flight potion should wear off for each user
  private static HashMap<UUID, Long> userMap = new HashMap<>();
  // Keeps track of how much damage the user has taken since their last reset
  private static HashMap<UUID, Double> damageMap = new HashMap<>();
  // Keeps track of the last time the player was damaged
  private static HashMap<UUID, Long> damageTimeMap = new HashMap<>();

  static {
    loreList.add("Flight Potion");
    loreList.add("For meeting the phantoms");
    loreList.add("in their own domain.");
    loreList.add("Or just building a roof.");
    loreList.add("This potion is provided");
    loreList.add("with no warranty.");
    loreList.add("");

    triggerList.add(PlayerItemConsumeEvent.class);
    triggerList.add(EntityDamageByEntityEvent.class);
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
    } else if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      if (edbee.getEntity() instanceof Player) {
        ret = isAffectedByFlightPotion((Player) edbee.getEntity());
      }
    }
    return ret;
  }

  public void execute(Event event) {
    if (event instanceof PlayerItemConsumeEvent) {
      PlayerItemConsumeEvent pice = (PlayerItemConsumeEvent) event;
      Player player = pice.getPlayer();
      player.setAllowFlight(true);
      player.setMetadata(METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), true));
      long currTime = System.currentTimeMillis();
      // Bukkit.getLogger().info("Drank at " + currTime);
      long wearOffTime = currTime + DURATION_MS;
      // Bukkit.getLogger().info("Defaulting wear-off time to " + wearOffTime);
      if (userMap.containsKey(player.getUniqueId())) {
        wearOffTime = userMap.get(player.getUniqueId()) + DURATION_MS;
        player.sendMessage(ChatColor.AQUA + "Your flight period has been extended.");
        // Bukkit.getLogger().info("Bumping it up to " + wearOffTime + " ( " + userMap.get(player.getUniqueId()) + " + " + DURATION_MS + " )");
      } else {
        player.sendMessage(ChatColor.AQUA + "The ground loses its grip on you.");
      }
      // Bukkit.getLogger().info("Wear off time: " + wearOffTime);
      (new RemoveFlightEffect(player, wearOffTime, true)).runTaskLaterAsynchronously(PhantomMain.getPlugin(), (wearOffTime - currTime) / 1000 * 20);
      userMap.put(player.getUniqueId(), wearOffTime);
    } else if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      Player player = (Player) edbee.getEntity();
      double finalDam = 0.0;
      boolean over = false;
      if (damageMap.containsKey(player.getUniqueId())) {
        finalDam = damageMap.get(player.getUniqueId());
      }
      double before = finalDam;
      finalDam += edbee.getDamage();
      over = finalDam >= MAX_DMG;
      damageMap.put(player.getUniqueId(), finalDam);
      damageTimeMap.put(player.getUniqueId(), System.currentTimeMillis());
      if (over && before < MAX_DMG) {
        (new RemoveFlightEffect(player, System.currentTimeMillis(), false)).runTaskAsynchronously(PhantomMain.getPlugin());
      }
      if (before >= MAX_DMG || over) {
        Bukkit.getScheduler().runTaskLater(PhantomMain.getPlugin(), new Runnable() {
    			@Override
    			public void run() {
            if (isAffectedByFlightPotion(player) && cooldownOver(player.getUniqueId())) {
              player.setAllowFlight(true);
              damageMap.put(player.getUniqueId(), 0d);
            }
    	    }
        }, COOLDOWN_TIME + 5);
      }
    }
  }

  private static boolean cooldownOver(UUID uuid) {
    Bukkit.getLogger().info("Time passed since last hit: " + ((System.currentTimeMillis() - damageTimeMap.get(uuid)) / 1000));
    return (System.currentTimeMillis() - damageTimeMap.get(uuid)) >= (COOLDOWN_TIME_IN_SECONDS * 1000);
  }

  /**
   * Returns true if the player is affected by a flight potion
   * @param  player The player whose flight condition is to be assessed
   * @return        True if the player can fly due to a flight potion, false otherwise
   */
  public static boolean isAffectedByFlightPotion(Player player) {
    UUID uuid = player.getUniqueId();
    if (!userMap.containsKey(uuid)) {
      return false;
    }
    long now = System.currentTimeMillis();
    if (now <= userMap.get(uuid)) {
      return true;
    }
    return false;
  }

  public static boolean isOutdated(UUID uuid, long time /* no see */) {
    // Bukkit.getLogger().info("Am I outdated? My time: " + time);
    if (!userMap.containsKey(uuid)) {
      return false;
    } else {
      long lastTime = userMap.get(uuid);
      // Bukkit.getLogger().info("Up to date wear-off time: " + lastTime);
      // Bukkit.getLogger().info("Returning: " + (lastTime > time));
      return lastTime > time;
    }
  }

  public static void removeTimestamp(UUID uuid, long time /* no see */) {
    if (!isOutdated(uuid, time)) {
      userMap.remove(uuid);
    }
  }
}
