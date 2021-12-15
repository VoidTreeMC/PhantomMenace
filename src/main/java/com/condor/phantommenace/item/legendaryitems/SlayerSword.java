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
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.Monster;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.ChatColor;
import org.bukkit.inventory.EquipmentSlot;

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.runnable.DoSlayerSwordEffect;
import com.condor.phantommenace.runnable.DisplayCooldown;

public class SlayerSword extends CustomItem {

  private static final String NAME = "Slayer Sword";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static HashMap<UUID, Long> mapOfTimesUsed = new HashMap<>();

  private static Random rng = new Random();

  // 15 second cooldown
  private static final long COOLDOWN_DURATION = 15 * 1000;

  // The difference between Sharpess 5 damage and Smite 5 damage,
  // to make the sword act like it has Smite 5 when attacking undead
  private static final double DAMAGE_TO_ADD_TO_UNDEAD = 9.5;

  public static final String METADATA_KEY = "hitBySlayerSword";

  static {
    loreList.add("Slayer Sword");
    loreList.add("Her name is Buffy.");
    loreList.add("");

    triggerList.add(EntityDamageEvent.class);
    triggerList.add(PlayerInteractEvent.class);
    triggerList.add(EntityDeathEvent.class);
  }

  public SlayerSword() {
    super(NAME, loreList, triggerList, CustomItemType.SLAYER_SWORD, 50, false);
    loreList.add(this.getPrice() + " VoidCoins");
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.NETHERITE_SWORD, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName(NAME);
    meta.setLore(loreList);
    meta.addEnchant(Enchantment.MENDING, 1, false);
    meta.addEnchant(Enchantment.DURABILITY, 3, false);
    meta.addEnchant(Enchantment.DAMAGE_ALL, 5, false);
    meta.addEnchant(Enchantment.SWEEPING_EDGE, 3, false);
    meta.addEnchant(Enchantment.FIRE_ASPECT, 2, false);
    meta.addEnchant(Enchantment.LOOT_BONUS_MOBS, 3, false);
    // meta.setUnbreakable(true);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    boolean ret = false;
    if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      // If they right-clicked and are crouching
      if ((pie.getAction() == Action.RIGHT_CLICK_AIR ||
          pie.getAction() == Action.RIGHT_CLICK_BLOCK) &&
          player.isSneaking() && pie.getHand() == EquipmentSlot.HAND) {
        // If they're holding a slayer sword
        if (isSlayerSword(player.getItemInHand())) {
          ret = true;
        }
      }
    } else if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      if (edbee.getDamager() instanceof Player) {
        Player player = (Player) edbee.getDamager();
        if (isSlayerSword(player.getItemInHand())) {
          ret = true;
        }
      }
    } else if (event instanceof EntityDeathEvent) {
      EntityDeathEvent ede = (EntityDeathEvent) event;
      LivingEntity entity = (LivingEntity) ede.getEntity();
      boolean hasBeenHitBySlayerSword = entity.getMetadata(METADATA_KEY).size() > 0;
      ret = hasBeenHitBySlayerSword;
    }
    return ret;
  }

  public void execute(Event event) {
    if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      long currTime = System.currentTimeMillis();
      long lastTimeUsed = 0;
      if (mapOfTimesUsed.containsKey(player.getUniqueId())) {
        lastTimeUsed = mapOfTimesUsed.get(player.getUniqueId());
      }
      // If it's off cooldown
      if ((currTime - lastTimeUsed) >= COOLDOWN_DURATION) {
        (new DoSlayerSwordEffect(player)).runTask(PhantomMain.getPlugin());
        (new DisplayCooldown(player, ChatColor.GOLD + "Divine Smite cooldown", COOLDOWN_DURATION, 1000)).runTaskAsynchronously(PhantomMain.getPlugin());
        mapOfTimesUsed.put(player.getUniqueId(), currTime);
      } else {
        player.sendMessage("The hilt sears your hand. It must cool down.");
      }
    } else if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      Player player = (Player) edbee.getDamager();
      if (!(edbee.getEntity() instanceof LivingEntity)) {
        return;
      }
      LivingEntity target = (LivingEntity) edbee.getEntity();
      // If the target is undead, make it deal smite-level damage
      if (target.getCategory() == EntityCategory.UNDEAD) {
        edbee.setDamage(edbee.getDamage() + DAMAGE_TO_ADD_TO_UNDEAD);
        target.setMetadata(METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), true));
      } else {
        if (!shouldDealDamageTo(target)) {
        // If the target is innocent, make it deal no damage
          edbee.setCancelled(true);
        }
      }
    } else if (event instanceof EntityDeathEvent) {
      EntityDeathEvent ede = (EntityDeathEvent) event;
      List<ItemStack> drops = ede.getDrops();
      for (ItemStack itemStack : drops) {
        if (itemStack.getType() != Material.NETHER_STAR && itemStack.getType() != Material.WITHER_SKELETON_SKULL) {
          int maxAmt = itemStack.getMaxStackSize();
          int amt = itemStack.getAmount() + (rng.nextInt(3) + 1);
          amt = (amt > maxAmt) ? maxAmt : amt;
          itemStack.setAmount(amt);
        }
      }
    }
  }

  public static boolean shouldDealDamageTo(LivingEntity entity) {
    switch (entity.getType()) {
      case ARMOR_STAND:
      case BLAZE:
      case BOAT:
      case CAVE_SPIDER:
      case CREEPER:
      case DRAGON_FIREBALL:
      case DROPPED_ITEM:
      case DROWNED:
      case EGG:
      case ELDER_GUARDIAN:
      case ENDER_CRYSTAL:
      case ENDER_DRAGON:
      case ENDER_PEARL:
      case ENDERMAN:
      case ENDERMITE:
      case EVOKER:
      case EVOKER_FANGS:
      case EXPERIENCE_ORB:
      case FALLING_BLOCK:
      case FIREBALL:
      case FIREWORK:
      case FISHING_HOOK:
      case GHAST:
      case GIANT:
      case GLOW_ITEM_FRAME:
      case GUARDIAN:
      case HOGLIN:
      case HUSK:
      case ILLUSIONER:
      case ITEM_FRAME:
      case LEASH_HITCH:
      case LIGHTNING:
      case MAGMA_CUBE:
      case MINECART:
      case MINECART_CHEST:
      case MINECART_COMMAND:
      case MINECART_FURNACE:
      case MINECART_MOB_SPAWNER:
      case MINECART_TNT:
      case PAINTING:
      case PHANTOM:
      case PIGLIN:
      case PIGLIN_BRUTE:
      case PILLAGER:
      case PRIMED_TNT:
      case RAVAGER:
      case SHULKER:
      case SHULKER_BULLET:
      case SILVERFISH:
      case SKELETON:
      case SKELETON_HORSE:
      case SLIME:
      case SMALL_FIREBALL:
      case SNOWBALL:
      case SPIDER:
      case SPLASH_POTION:
      case STRAY:
      case THROWN_EXP_BOTTLE:
      case TRIDENT:
      case UNKNOWN:
      case VEX:
      case VINDICATOR:
      case WITCH:
      case WITHER:
      case WITHER_SKELETON:
      case WITHER_SKULL:
      case ZOGLIN:
      case ZOMBIE:
      case ZOMBIE_HORSE:
      case ZOMBIE_VILLAGER:
      case ZOMBIFIED_PIGLIN:
        return true;
      default:
        return false;
    }
  }

  public boolean isSlayerSword(ItemStack item) {
    return (item != null) && (item.getType() == Material.NETHERITE_SWORD) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.SLAYER_SWORD);
  }
}
