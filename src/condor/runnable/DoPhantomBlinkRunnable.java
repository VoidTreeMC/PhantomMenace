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

import condor.main.PhantomMain;

public class DoPhantomBlinkRunnable extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The phantom
  Phantom phantom;

  // Maximum range
  final static int MAX_RANGE = 10;

  final static int MIN_Z = 86;
  final static int MAX_Z = 130;
  final static int MIN_X = 3877;
  final static int MAX_X = 3976;

  // RNG
  static Random rng = new Random();

	/**
	* Builds the BukkitRunnable
	* @param phantom The phantom
	*/
	public DoPhantomBlinkRunnable(Phantom phantom) {
		this.plugin = PhantomMain.getPlugin();
		this.phantom = phantom;
	}

  /**
   * Causes the phantom to blink to a random location nearby
   * @param phantom  [description]
   */
  public void blink(Phantom phantom) {
    int xDelta = rng.nextInt(MAX_RANGE * 2) - MAX_RANGE;
    int zDelta = rng.nextInt(MAX_RANGE * 2) - MAX_RANGE;
    Location curr = phantom.getLocation();
    Location potential = new Location(curr.getWorld(), curr.getX() + xDelta, curr.getY(), curr.getZ() + zDelta);

    boolean isInsideBox = true;
    double newX = curr.getX() + xDelta;
    double newZ = curr.getZ() + zDelta;
    if (newX > MAX_X || newX < MIN_X || newZ > MAX_Z || newZ < MIN_Z) {
      isInsideBox = false;
    }

    // If it's not inside the box, abort the teleportation.
    if (!isInsideBox) {
      return;
    }

    // If the block isn't empty, try the block above it.
    // Repeat until an empty space is found
    while (potential.getBlock().getType() != Material.AIR && potential.getY() <= 255) {
      potential.setY(potential.getY() + 1);
    }

    boolean foundValidLocation = potential.getBlock().getType() == Material.AIR;

    // If we've found a valid location
    if (foundValidLocation) {
      World world = potential.getWorld();
      // Create end particles at the source location
      world.spawnParticle(Particle.PORTAL, curr, 5);
      // Create end particles at the destination location
      world.spawnParticle(Particle.PORTAL, potential, 5);
      // Play sound effect at new location
      world.playSound(potential, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1, 1);
      // Move the phantom to the destination
      phantom.teleport(potential);
    }
  }

	/**
	 * Causes the phantom to blink, if it's still alive
	 */
	@Override
	public void run() {
    // If the phantom is dead, we are done.
    if (this.phantom.isDead()) {
      this.cancel();
      return;
    }

    blink(phantom);

    this.cancel();
	}
}
