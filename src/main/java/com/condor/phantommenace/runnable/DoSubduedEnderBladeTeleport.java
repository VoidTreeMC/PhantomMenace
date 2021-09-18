package com.condor.phantommenace.runnable;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Phantom;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.SoundCategory;
import org.bukkit.Sound;
import org.bukkit.Particle;
import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.bukkit.Bukkit;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.item.legendaryitems.SubduedEnderBlade;

public class DoSubduedEnderBladeTeleport extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The player
  Player player;

  // RNG
  static Random rng = new Random();

	/**
	* Builds the BukkitRunnable
	* @param player The player
	*/
	public DoSubduedEnderBladeTeleport(Player player) {
		this.plugin = PhantomMain.getPlugin();
		this.player = player;
	}

  public boolean isInvalidLocation(Location loc) {
    Location blockAbove = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
    Location blockBelow = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
    boolean hasRoomForPlayer = loc.getBlock().getType() == Material.AIR && blockAbove.getBlock().getType() == Material.AIR;
    // boolean isOnGround = blockBelow.getBlock().getType() != Material.AIR && blockBelow.getBlock().getType() != Material.LAVA;
    return (!hasRoomForPlayer/* || !isOnGround*/);
  }

  /**
   * Causes the player to blink to a random location nearby
   * @param player  The player
   */
  public void blink(Player player) {
    boolean foundValidLocation = false;

    Location playerLoc = player.getLocation();
    Vector playerDirection = playerLoc.getDirection();

    int blocksAway = SubduedEnderBlade.MAX_TELEPORT_DISTANCE + 1;

    while (!foundValidLocation && --blocksAway > 0) {
      Vector newVec = playerDirection.clone().multiply(blocksAway);
      Location potential = playerLoc.clone().add(newVec.clone());

      foundValidLocation = !isInvalidLocation(potential);

      // If we've found a valid location
      if (foundValidLocation) {
        World world = potential.getWorld();
        // Create end particles at the source location
        world.spawnParticle(Particle.PORTAL, playerLoc, 5);
        // Create end particles at the destination location
        world.spawnParticle(Particle.PORTAL, potential, 5);
        // Play sound effect at new location
        world.playSound(potential, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1, 1);
        // Move the player to the destination
        player.teleport(potential);
      }
    }

    if (blocksAway <= 0) {
      player.sendMessage("The blade vibrates harshly. It was unable to find a valid location to teleport you to.");
    }
  }

	/**
	 * Causes the player to blink
	 */
	@Override
	public void run() {
    blink(player);
    this.cancel();
	}
}
