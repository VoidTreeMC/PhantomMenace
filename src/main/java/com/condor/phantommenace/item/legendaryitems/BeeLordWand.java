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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.block.Action;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.runnable.DoDisableTargetAI;
import com.condor.phantommenace.runnable.SummonAngryBee;
import com.condor.phantommenace.runnable.DisplayCooldown;

public class BeeLordWand extends CustomItem {

  private static final String NAME = "Bee Lord's Wand";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  // Max distance (in blocks) at which an entity
  // can be "attacked" with the wand
  private static final int MAX_DISTANCE = 30;
  // Max lifetime of a bee spawned by this -- 30 seconds.
  private static final int MAX_LIFETIME = 30 * 20;

  private static HashMap<UUID, Long> mapOfTimesUsed = new HashMap<>();

  // 5 second cooldown
  private static final long COOLDOWN_DURATION = 5 * 1000;

  public static final String BEE_METADATA = "isBeeLordBee";

  private static Random rng = new Random();

  static {
    loreList.add("Bee Lord's Wand");
    loreList.add("Wand of the bee lord,");
    loreList.add("lord of the bees.");
    loreList.add("");

    triggerList.add(PlayerInteractEvent.class);
    triggerList.add(EntityDamageByEntityEvent.class);
  }

  public BeeLordWand() {
    super(NAME, loreList, triggerList, CustomItemType.BEE_LORD_WAND, 25);
    loreList.add(this.getPrice() + " VoidCoins");
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.GOLDEN_HOE, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName(NAME);
    meta.setLore(loreList);
    meta.addEnchant(Enchantment.MENDING, 1, false);
    meta.addEnchant(Enchantment.DAMAGE_ALL, 4, false);
    meta.setUnbreakable(true);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    boolean ret = false;
    if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      // If they shift right-clicked
      if ((pie.getAction() == Action.RIGHT_CLICK_AIR ||
          pie.getAction() == Action.RIGHT_CLICK_BLOCK) &&
          player.isSneaking() && pie.getHand() == EquipmentSlot.HAND) {
        // If they're holding a bee lord's wand
        if (isBeeWand(player.getItemInHand())) {
          ret = true;
        }
      }
    } else if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      Entity damager = edbee.getDamager();
      if (damager.hasMetadata(BEE_METADATA)) {
        ret = true;
      }
    }
    return ret;
  }

  public void execute(Event event) {
    if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      Entity target = player.getTargetEntity(MAX_DISTANCE);

      long currTime = System.currentTimeMillis();
      long lastTimeUsed = 0;
      if (mapOfTimesUsed.containsKey(player.getUniqueId())) {
        lastTimeUsed = mapOfTimesUsed.get(player.getUniqueId());
      }
      // If it's off cooldown
      if ((currTime - lastTimeUsed) >= COOLDOWN_DURATION) {
        if (target instanceof LivingEntity && target.getType() != EntityType.PLAYER) {
          // Summon a bee to attack the entity
          (new SummonAngryBee((LivingEntity) target, player, MAX_LIFETIME)).runTask(PhantomMain.getPlugin());
          (new DisplayCooldown(player, ChatColor.GOLD + "Bee cooldown", COOLDOWN_DURATION, 1000)).runTaskAsynchronously(PhantomMain.getPlugin());
          mapOfTimesUsed.put(player.getUniqueId(), currTime);
        }
      } else {
        player.sendMessage("The wand buzzes. It needs to " + ChatColor.ITALIC + "beecharge.");
      }
    } else if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      Entity damager = edbee.getDamager();
      edbee.setCancelled(true);
      UUID playerUUID = UUID.fromString(damager.getMetadata(BEE_METADATA).get(0).asString());
      LivingEntity target = (LivingEntity) edbee.getEntity();
      damager.remove();
      Player player = Bukkit.getPlayer(playerUUID);
      if (player != null) {
        player.attack(target);
      }
    }
  }

  public boolean isBeeWand(ItemStack item) {
    return (item != null) && (item.getType() == Material.GOLDEN_HOE) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.BEE_LORD_WAND);
  }
}
