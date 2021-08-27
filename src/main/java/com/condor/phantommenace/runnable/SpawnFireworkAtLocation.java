package com.condor.phantommenace.runnable;

import java.util.List;
import java.util.Random;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Phantom;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.entity.EntityType;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.phantom.PhantomGenerator;
import com.condor.phantommenace.event.PhantomEvent;

public class SpawnFireworkAtLocation extends BukkitRunnable {

  private static Random rng = new Random();

	//The plugin
	JavaPlugin plugin;

  // The firework effect to spawn the firework with
  FireworkEffect effect;

  // The location at which to spawn the entities
  Location loc;

  // The maximum power that the firework can have
  private static final int MAX_POWER = 3;

  // The maximum distance from the center that fireworks will spawn at
  private static final int MAX_DISTANCE = 10;

	public SpawnFireworkAtLocation(FireworkEffect effect, Location loc) {
		this.plugin = PhantomMain.getPlugin();
    this.effect = effect;
		this.loc = loc;
	}

  private Location getRandomLoc() {
    int xDelta = rng.nextInt(MAX_DISTANCE * 2) - MAX_DISTANCE;
    int zDelta = rng.nextInt(MAX_DISTANCE * 2) - MAX_DISTANCE;
    return new Location(loc.getWorld(), loc.getX() + xDelta, loc.getY(), loc.getZ() + zDelta);
  }

	/**
	 * Spawns the LivingEntity at the location
	 */
	@Override
	public void run() {
    Location newLoc = getRandomLoc();
    Firework firework = (Firework) loc.getWorld().spawnEntity(newLoc, EntityType.FIREWORK);
    FireworkMeta meta = firework.getFireworkMeta();
    meta.addEffect(this.effect);
    meta.setPower(rng.nextInt(MAX_POWER) + 1);
    firework.setFireworkMeta(meta);
	}
}
