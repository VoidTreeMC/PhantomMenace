package com.condor.phantommenace.item.legendaryitems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
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

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.runnable.DoSlayerSwordEffect;

public class SlayerSword extends CustomItem {

  private static final String NAME = "Slayer Sword";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static TreeMap<UUID, Long> mapOfTimesUsed = new TreeMap<>();

  private static Random rng = new Random();

  // 15 second cooldown
  private static final long COOLDOWN_DURATION = 15 * 1000;

  // The difference between Sharpess 5 damage and Smite 5 damage,
  // to make the sword act like it has Smite 5 when attacking undead
  private static final double DAMAGE_TO_ADD_TO_UNDEAD = 9.5;

  public static final String METADATA_KEY = "hitBySlayerSword";

  static {
    loreList.add("Slayer Sword");
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
    meta.addEnchant(Enchantment.FIRE_ASPECT, 3, false);
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
      // If they right-clicked
      if (pie.getAction() == Action.RIGHT_CLICK_AIR ||
          pie.getAction() == Action.RIGHT_CLICK_BLOCK) {
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
        if (isInnocent(target)) {
        // If the target is innocent, make it deal no damage
          edbee.setCancelled(true);
        }
      }
    } else if (event instanceof EntityDeathEvent) {
      EntityDeathEvent ede = (EntityDeathEvent) event;
      List<ItemStack> drops = ede.getDrops();
      for (ItemStack itemStack : drops) {
        int maxAmt = itemStack.getMaxStackSize();
        int amt = itemStack.getAmount() + (rng.nextInt(3) + 1);
        amt = (amt > maxAmt) ? maxAmt : amt;
        itemStack.setAmount(amt);
      }
    }
  }

  public static boolean isInnocent(LivingEntity entity) {
    return !(entity instanceof Monster);
  }

  public boolean isSlayerSword(ItemStack item) {
    return (item != null) && (item.getType() == Material.NETHERITE_SWORD) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.SLAYER_SWORD);
  }
}
