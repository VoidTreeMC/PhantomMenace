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
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.World;

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.runnable.DoEnderBladeTeleport;
import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.runnable.DisplayCooldown;

public class EnderBlade extends CustomItem {

  /**
   * TODO: Make it extra effective vs endermen and endermites
   *       Make it weaker (or not work) in water/rain
   */

  private static final String NAME = "Ender Blade";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  // 15 second cooldown
  private static final long COOLDOWN_DURATION = 15 * 1000;

  private static final int MAX_KILLS = 10000;

  private static TreeMap<UUID, Long> mapOfTimesUsed = new TreeMap<>();

  private static Random rng = new Random();

  static {
    loreList.add("Ender Blade");
    loreList.add("The Quacken battled endless hordes");
    loreList.add("of the tall demons while retreating to");
    loreList.add("VoidTree. Without this sword, all would");
    loreList.add("have been lost.");
    loreList.add("The wild Enderblade is difficult to control.");
    loreList.add("Tame it by feeding it souls.");
    loreList.add("");
    loreList.add("0 / " + MAX_KILLS + " souls killed.");

    triggerList.add(PlayerInteractEvent.class);
    triggerList.add(EntityDamageByEntityEvent.class);
  }

  public EnderBlade() {
    super(NAME, loreList, triggerList, CustomItemType.ENDER_BLADE, 50);
    loreList.add(this.getPrice() + " VoidCoins");
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.NETHERITE_SWORD, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName(NAME);
    meta.setLore(loreList);
    meta.addEnchant(Enchantment.MENDING, 1, false);
    meta.addEnchant(Enchantment.DURABILITY, 3, false);
    // meta.setUnbreakable(true);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    boolean ret = false;
    if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      // If they shift-right-clicked
      if ((pie.getAction() == Action.RIGHT_CLICK_AIR ||
          pie.getAction() == Action.RIGHT_CLICK_BLOCK) &&
          player.isSneaking()) {
        // If they're holding an ender blade
        if (isEnderBlade(player.getItemInHand())) {
          ret = true;
        }
      }
    } else if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      Entity oDamagee = edbee.getEntity();
      Entity oDamager = edbee.getDamager();
      // If the player kills something with an ender blade
      if ((oDamagee instanceof LivingEntity) && (oDamager instanceof LivingEntity)) {
        LivingEntity damager = (LivingEntity) oDamager;
        LivingEntity damagee = (LivingEntity) oDamagee;
        if (damagee != null && ((damagee.getHealth() - edbee.getFinalDamage()) <= 0)) {
          if (damager != null && damager.getType() == EntityType.PLAYER) {
            Player player = (Player) damager;
            if (isEnderBlade(player.getItemInHand())) {
              ret = true;
            }
          }
        }
      }
    }
    return ret;
  }

  public void execute(Event event) {
    if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      if (player.getLocation().getWorld().getEnvironment() == World.Environment.THE_END) {
        player.sendMessage("The ender blade's teleport has been temporarily disabled in the end. The subdued ender blade will work as normal.");
        return;
      }
      long currTime = System.currentTimeMillis();
      long lastTimeUsed = 0;
      if (mapOfTimesUsed.containsKey(player.getUniqueId())) {
        lastTimeUsed = mapOfTimesUsed.get(player.getUniqueId());
      }
      // If it's off cooldown
      if ((currTime - lastTimeUsed) >= COOLDOWN_DURATION) {
        (new DoEnderBladeTeleport(player)).runTask(PhantomMain.getPlugin());
        (new DisplayCooldown(player, ChatColor.GOLD + "Blink cooldown", COOLDOWN_DURATION, 1000)).runTaskAsynchronously(PhantomMain.getPlugin());
        mapOfTimesUsed.put(player.getUniqueId(), currTime);
      } else {
        player.sendMessage("The blade vibrates weakly. It must recharge.");
      }
    } else if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      Player player = (Player) edbee.getDamager();
      ItemStack eBladeItem = player.getItemInHand();
      int kills = getNumKills(eBladeItem) + 1;
      setNumKills(eBladeItem, kills);
      if (kills >= MAX_KILLS) {
        levelUpBlade(player, eBladeItem);
      }
    }
  }

  public boolean isEnderBlade(ItemStack item) {
    return (item != null) && (item.getType() == Material.NETHERITE_SWORD) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.ENDER_BLADE);
  }

  private void levelUpBlade(Player player, ItemStack blade) {
    ItemMeta meta = blade.getItemMeta();
    meta.setLore(SubduedEnderBlade.getBladeLore());
    meta.setDisplayName("Subdued Ender Blade");
    blade.setItemMeta(meta);
    player.sendMessage(ChatColor.GREEN + "You feel a strange shuddering from the ender blade. Afterwards, it rests more comfortably in your hand.");
  }

  private int getNumKills(ItemStack blade) {
    int numKills = 0;

    ItemMeta meta = blade.getItemMeta();
    List<String> lore = meta.getLore();
    for (String string : lore) {
      if (string.endsWith(" souls killed.")) {
        numKills = Integer.parseInt(string.split(" ")[0]);
      }
    }
    return numKills;
  }

  private void setNumKills(ItemStack blade, int numKills) {
    ItemMeta meta = blade.getItemMeta();
    List<String> lore = meta.getLore();
    for (int i = 0; i < lore.size(); i++) {
      String string = lore.get(i);
      if (string.endsWith(" souls killed.")) {
        lore.set(i, numKills + " / " + MAX_KILLS + " souls killed.");
      }
    }
    meta.setLore(lore);
    blade.setItemMeta(meta);
  }
}
