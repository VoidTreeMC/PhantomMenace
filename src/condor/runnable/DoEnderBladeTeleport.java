package condor.runnable;

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
import org.bukkit.SoundCategory;
import org.bukkit.Sound;
import org.bukkit.Particle;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import condor.main.PhantomMain;

public class DoEnderBladeTeleport extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The player
  Player player;

  // Maximum range
  final static int MAX_RANGE = 10;

  // RNG
  static Random rng = new Random();

	/**
	* Builds the BukkitRunnable
	* @param player The player
	*/
	public DoEnderBladeTeleport(Player player) {
		this.plugin = PhantomMain.getPlugin();
		this.player = player;
	}

  public boolean isInvalidLocation(Location loc) {
    Location blockAbove = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
    Location blockBelow = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
    boolean hasRoomForPlayer = loc.getBlock().getType() == Material.AIR && blockAbove.getBlock().getType() == Material.AIR;
    boolean isOnGround = blockBelow.getBlock().getType() != Material.AIR;
    return (!hasRoomForPlayer || !isOnGround);
  }

  /**
   * Causes the player to blink to a random location nearby
   * @param player  The player
   */
  public void blink(Player player) {
    int xDelta = rng.nextInt(MAX_RANGE * 2) - MAX_RANGE;
    int zDelta = rng.nextInt(MAX_RANGE * 2) - MAX_RANGE;
    int yDelta = rng.nextInt(MAX_RANGE);
    Location curr = player.getLocation();
    Location potential = new Location(curr.getWorld(), curr.getX() + xDelta, curr.getY() + yDelta, curr.getZ() + zDelta);

    // If the block isn't empty, try the block below it.
    // Repeat until an empty space is found
    while (isInvalidLocation(potential)) {
      potential.setY(potential.getY() - 1);
      if (potential.getY() < 2) {
        break;
      }
    }

    // TODO: Make sure you dont drop them off a fucking mountain

    boolean foundValidLocation = !isInvalidLocation(potential);

    // If we've found a valid location
    if (foundValidLocation) {
      World world = potential.getWorld();
      // Create end particles at the source location
      world.spawnParticle(Particle.PORTAL, curr, 5);
      // Create end particles at the destination location
      world.spawnParticle(Particle.PORTAL, potential, 5);
      // Play sound effect at new location
      world.playSound(potential, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1, 1);
      // Move the player to the destination
      player.teleport(potential);
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
