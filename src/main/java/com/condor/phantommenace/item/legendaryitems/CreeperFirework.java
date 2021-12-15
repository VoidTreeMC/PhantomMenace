package com.condor.phantommenace.item.legendaryitems;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;
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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.FireworkEffect;
import org.bukkit.Color;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.entity.Firework;


import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.runnable.DoDisableTargetAI;
import com.condor.phantommenace.runnable.SpawnCreepersInSky;

public class CreeperFirework extends CustomItem {

  private static final String NAME = "Creeper Firework";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static Random rng = new Random();

  private static HashMap<UUID, ArrayList<Long>> usageMap = new HashMap<>();

  private static final int MAX_ROCKETS = 10;
  private static final long PERIOD = 1000 * 10;

  static {
    loreList.add("Creeper Firework");
    loreList.add("Not recommended for use over water");
    loreList.add("Or indoors");
    loreList.add("");

    triggerList.add(PlayerInteractEvent.class);
    triggerList.add(FireworkExplodeEvent.class);
  }

  public CreeperFirework() {
    super(NAME, loreList, triggerList, CustomItemType.CREEPER_FIREWORK, 1);
    loreList.add(this.getPrice() + " VoidCoins");
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.FIREWORK_ROCKET, 1);
    FireworkMeta meta = (FireworkMeta) is.getItemMeta();
    meta.setDisplayName(NAME);
    meta.setLore(loreList);
    FireworkEffect effect = FireworkEffect.builder()
                            .trail(true)
                            .flicker(true)
                            .with(FireworkEffect.Type.CREEPER)
                            .withColor(Color.GREEN, Color.LIME)
                            .withFade(Color.GRAY)
                            .build();
    meta.addEffect(effect);
    meta.setPower(2);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    boolean ret = false;
    if (event instanceof FireworkExplodeEvent) {
      FireworkExplodeEvent fee = (FireworkExplodeEvent) event;
      Firework firework = fee.getEntity();
      FireworkMeta meta = firework.getFireworkMeta();
      if (meta.hasLore()) {
        List<String> lore = meta.getLore();
        if (lore.size() >= 1) {
          ret = lore.get(0).equals("Creeper Firework");
        }
      }
    } else if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      if (pie.hasItem() && isCreeperFirework(pie.getItem())) {
        if (!canShootFirework(player)) {
          player.sendMessage("You have used too many creeper fireworks lately. Take a break!");
          pie.setUseItemInHand(Event.Result.DENY);
        }
      }
    }
    return ret;
  }

  public void execute(Event event) {
    FireworkExplodeEvent fee = (FireworkExplodeEvent) event;
    Location loc = fee.getEntity().getLocation();
    (new SpawnCreepersInSky(loc)).runTask(PhantomMain.getPlugin());
  }

  public boolean isCreeperFirework(ItemStack item) {
    return (item != null) && (item.getType() == Material.FIREWORK_ROCKET) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.CREEPER_FIREWORK);
  }

  public boolean canShootFirework(Player player) {
    boolean ret = false;

    UUID playerUUID = player.getUniqueId();
    ArrayList<Long> playerUsage = usageMap.get(playerUUID);
    if (playerUsage == null) {
      ret = true;
      ArrayList<Long> arrList = new ArrayList<>();
      arrList.add(System.currentTimeMillis());
      usageMap.put(playerUUID, arrList);
    } else if (playerUsage.size() == MAX_ROCKETS) {
      long currTime = System.currentTimeMillis();
      long lastTimeUsed = playerUsage.get(MAX_ROCKETS - 1);
      if ((currTime - lastTimeUsed) >= PERIOD) {
        playerUsage.remove(0);
        playerUsage.add(currTime);
        ret = true;
      } else {
        ret = false;
      }
    } else {
      playerUsage.add(System.currentTimeMillis());
      usageMap.put(playerUUID, playerUsage);
      ret = true;
    }

    return ret;
  }
}
