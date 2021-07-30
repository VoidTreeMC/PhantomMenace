package condor.runnable;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import condor.main.PhantomMain;

public class SpawnCreepersInSky extends BukkitRunnable {

  private static Random rng = new Random();

	// The plugin
	JavaPlugin plugin;

  // The location to begin dropping creepers around
  Location loc;

  // Radius of effect
  private static final int RADIUS = 10;

	public SpawnCreepersInSky(Location loc) {
		this.plugin = PhantomMain.getPlugin();
		this.loc = loc;
	}

	/**
	 * Drop a random number of creepers in a radius around the firework explosion
	 */
	@Override
	public void run() {
    World world = loc.getWorld();
    int X = (int) loc.getX();
    int Y = (int) loc.getY();
    int Z = (int) loc.getZ();
    int numCreepers = rng.nextInt(10) + 5;

    for (int i = 0; i < numCreepers; i++) {
      int xDelta = rng.nextInt(RADIUS * 2) - RADIUS;
      int zDelta = rng.nextInt(RADIUS * 2) - RADIUS;
      Location newLoc = new Location(world, X + xDelta, Y, Z + zDelta);
      LivingEntity creeper = (LivingEntity) world.spawnEntity(newLoc, EntityType.CREEPER);
      creeper.setHealth(0.1);
    }
    this.cancel();
	}
}
