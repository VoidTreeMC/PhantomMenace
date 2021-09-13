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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.block.Action;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.ChatColor;


import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.runnable.DoDisableTargetAI;

public class CreeperBow extends CustomItem {

  private static final String NAME = "CreeperBane";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static String METADATA_KEY = "shotByCreeperBow";

  private static Random rng = new Random();

  static {
    loreList.add("CreeperBane");
    loreList.add("Duck hates creepers.");
    loreList.add(ChatColor.UNDERLINE + "Really, really" + ChatColor.RESET + " hates creepers.");
    loreList.add("");

    triggerList.add(EntityDamageEvent.class);
    triggerList.add(EntityDeathEvent.class);
  }

  public CreeperBow() {
    super(NAME, loreList, triggerList, CustomItemType.CREEPER_BOW, 50);
    loreList.add(this.getPrice() + " VoidCoins");
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.BOW, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName(NAME);
    meta.setLore(loreList);
    meta.addEnchant(Enchantment.MENDING, 1, false);
    meta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
    // meta.setUnbreakable(true);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    boolean ret = false;
    if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      boolean isCreeper = edbee.getEntityType() == EntityType.CREEPER;
      boolean isArrow = edbee.getDamager() instanceof Projectile;
      if (isArrow) {
        Projectile arrow = (Projectile) edbee.getDamager();
        boolean isFiredByPlayer = arrow.getShooter() instanceof Player;
        if (isCreeper && isFiredByPlayer) {
          Player player = (Player) arrow.getShooter();
          ret = CustomItemType.getTypeFromCustomItem(player.getItemInHand()) == CustomItemType.CREEPER_BOW;
        }
      }
    } else if (event instanceof EntityDeathEvent) {
      EntityDeathEvent ede = (EntityDeathEvent) event;
      boolean isCreeper = ede.getEntityType() == EntityType.CREEPER;
      LivingEntity entity = (LivingEntity) ede.getEntity();
      boolean hasBeenShotByCreeperBow = entity.getMetadata(METADATA_KEY).size() > 0;
      ret = hasBeenShotByCreeperBow;
    }
    return ret;
  }

  public void execute(Event event) {
    if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      LivingEntity entity = (LivingEntity) edbee.getEntity();
      // Give it metadata that says it's been shot by this bow
      entity.setMetadata(METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), true));
      // Disable entity's AI for 5 seconds
      (new DoDisableTargetAI(entity, 5)).runTaskLater(PhantomMain.getPlugin(), 13);
    } else {
      EntityDeathEvent ede = (EntityDeathEvent) event;
      int amt = rng.nextInt(3) + 1;
      ede.getDrops().add(new ItemStack(Material.GUNPOWDER, amt));
    }
  }

  public boolean isCreeperBow(ItemStack item) {
    return (item != null) && (item.getType() == Material.BOW) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.CREEPER_BOW);
  }
}
