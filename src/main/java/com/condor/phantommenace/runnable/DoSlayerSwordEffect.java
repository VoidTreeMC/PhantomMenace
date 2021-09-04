package com.condor.phantommenace.runnable;

import java.util.List;
import java.util.Random;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Phantom;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;

import com.condor.phantommenace.main.PhantomMain;

public class DoSlayerSwordEffect extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The player
  Player player;

  // Effect range
  final static int RANGE = 10;

  // The fire duration. 5 seconds
  final static int FIRE_DURATION = 5 * 20;

  /**
	* Builds the BukkitRunnable
	* @param player The player
	*/
	public DoSlayerSwordEffect(Player player) {
		this.plugin = PhantomMain.getPlugin();
		this.player = player;
	}


	/**
	 * Lights all nearby undead on fire
	 */
	@Override
	public void run() {
    List<Entity> nearbyEntities = player.getNearbyEntities(RANGE, RANGE, RANGE);
    for (Entity e : nearbyEntities) {
      if (e instanceof LivingEntity) {
        LivingEntity le = (LivingEntity) e;
        if (le.getCategory() == EntityCategory.UNDEAD) {
          le.setFireTicks(FIRE_DURATION);
        }
      }
    }
    this.cancel();
	}
}
