package com.condor.phantommenace.item.legendaryitems;

import java.util.ArrayList;
import java.util.Random;

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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.enchantments.Enchantment;

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.runnable.DoLavaWalkerEffect;
import com.condor.phantommenace.main.PhantomMain;

public class LavaWalkers extends CustomItem {

  private static final String NAME = "Lava Walkers";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private long lastTimeUsed = 0;
  // Only do it every 5 ticks.
  // We don't want to create too much lag.
  private static final long COOLDOWN_DURATION = 5;

  static {
    loreList.add("Lava Walkers");
    loreList.add("Favored footwear of Solly,");
    loreList.add("who conquered the nether.");
    loreList.add("If you stop walking, you die.");
    loreList.add("");

    triggerList.add(PlayerMoveEvent.class);
  }

  public LavaWalkers() {
    super(NAME, loreList, triggerList, CustomItemType.LAVA_WALKERS, 50, false);
    loreList.add(this.getPrice() + " VoidCoins");
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.NETHERITE_BOOTS, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName(NAME);
    meta.setLore(loreList);
    meta.setUnbreakable(true);
    meta.addEnchant(Enchantment.MENDING, 1, false);
    meta.addEnchant(Enchantment.DURABILITY, 3, false);
    meta.addEnchant(Enchantment.PROTECTION_FALL, 4, false);
    meta.addEnchant(Enchantment.PROTECTION_FIRE, 4, false);
    meta.addEnchant(Enchantment.SOUL_SPEED, 3, false);
    meta.addEnchant(Enchantment.THORNS, 3, false);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    boolean ret = false;
    if (event instanceof PlayerMoveEvent) {
      PlayerMoveEvent pme = (PlayerMoveEvent) event;
      Player player = pme.getPlayer();
      Location playerLoc = player.getLocation();
      Location blockUnder = new Location(playerLoc.getWorld(), playerLoc.getX(), playerLoc.getY() - 1, playerLoc.getZ());
      // If they're wearing boots
      ret = player.getInventory().getBoots() != null;
      // If they're wearing lava walkers
      ret = ret && CustomItemType.getTypeFromCustomItem(player.getInventory().getBoots()) == CustomItemType.LAVA_WALKERS;
      // If they're on the ground
      ret = ret && blockUnder.getBlock().getType() != Material.AIR;
    }
    return ret;
  }

  public void execute(Event event) {
    long currTime = System.currentTimeMillis();
    // If it's on cooldown
    if ((currTime - lastTimeUsed) < COOLDOWN_DURATION) {
      return;
    }
    // If it's off cooldown
    PlayerMoveEvent pme = (PlayerMoveEvent) event;
    Player player = pme.getPlayer();
    Location playerLoc = player.getLocation();
    (new DoLavaWalkerEffect(playerLoc)).runTask(PhantomMain.getPlugin());
    lastTimeUsed = currTime;
  }
}
