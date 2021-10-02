package com.condor.phantommenace.item.legendaryitems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.runnable.DisplayCooldown;
import com.condor.phantommenace.runnable.RemoveAlmondCakeEffect;
import com.condor.phantommenace.item.CustomItemManager;

public class AlmondCake extends CustomItem {

  // TODO: Use cooldown bossbar to show how long it'll be active for

  private static final String NAME = "Almond Cake";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static TreeMap<UUID, Long> mapOfTimesUsed = new TreeMap<>();

  private static Random rng = new Random();

  public static final String ALMOND_METADATA = "hasEatenAlmondCake";

  // 20 minutes
  private static final long DURATION = 20 * 20 * 60;

  static {
    loreList.add("Almond Cake");
    loreList.add("In honor of our favorite nut,");
    loreList.add("makes everyone your friend.");
    loreList.add("");

    triggerList.add(EntityTargetLivingEntityEvent.class);
    triggerList.add(EntityDamageByEntityEvent.class);
    triggerList.add(PlayerInteractEvent.class);
    triggerList.add(PlayerDeathEvent.class);
  }

  public AlmondCake() {
    super(NAME, loreList, triggerList, CustomItemType.ALMOND_CAKE, 5);
    loreList.add(this.getPrice() + " VoidCoins");
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.CAKE, 1);
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
        // If they're holding an almond cake
        if (isAlmondCake(player.getItemInHand())) {
          ret = true;
        }
      }
    } else if (event instanceof EntityTargetLivingEntityEvent) {
      EntityTargetLivingEntityEvent etlee = (EntityTargetLivingEntityEvent) event;
      if (etlee.getTarget() != null) {
        if (etlee.getTarget().getType() == EntityType.PLAYER) {
          Player player = (Player) etlee.getTarget();
          ret = player.hasMetadata(ALMOND_METADATA);
        }
      }
    } else if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      if (edbee.getDamager().getType() == EntityType.PLAYER) {
        Player player = (Player) edbee.getDamager();
        ret = player.hasMetadata(ALMOND_METADATA);
      }
    } else if (event instanceof PlayerDeathEvent) {
      PlayerDeathEvent pde = (PlayerDeathEvent) event;
      Player player = pde.getEntity();
      ret = player.hasMetadata(ALMOND_METADATA);
    }
    return ret;
  }

  public void execute(Event event) {
    if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      player.getItemInHand().setAmount(0);
      player.setMetadata(ALMOND_METADATA, new FixedMetadataValue(PhantomMain.getPlugin(), true));
      (new RemoveAlmondCakeEffect(player)).runTaskLaterAsynchronously(PhantomMain.getPlugin(), DURATION);
      // (new DisplayCooldown(player, ChatColor.GOLD + "Almond Cake Effect", DURATION / 20 * 1000, 60 * 1000)).runTaskAsynchronously(PhantomMain.getPlugin());
      player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
      player.setFoodLevel(Math.min(20, player.getFoodLevel() + 14));
      player.sendMessage(ChatColor.GREEN + "You feel a great sense of peace and contentment.");
    } else if (event instanceof EntityTargetLivingEntityEvent) {
      EntityTargetLivingEntityEvent etlee = (EntityTargetLivingEntityEvent) event;
      etlee.setCancelled(true);
    } else if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      edbee.setCancelled(true);
    } else if (event instanceof PlayerDeathEvent) {
      PlayerDeathEvent pde = (PlayerDeathEvent) event;
      pde.getEntity().removeMetadata(ALMOND_METADATA, PhantomMain.getPlugin());
    }
  }

  public boolean isAlmondCake(ItemStack item) {
    return (item != null) && (item.getType() == Material.CAKE) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.ALMOND_CAKE);
  }
}
