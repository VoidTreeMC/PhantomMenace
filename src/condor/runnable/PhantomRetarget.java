package condor.runnable;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Entity;
import org.bukkit.Location;
import org.bukkit.GameMode;

import condor.main.PhantomMain;

public class PhantomRetarget extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The phantom whose target is to be updated
  Phantom phantom;

  // The interval at which to re-target players if not already targeting.
  // Every 3 seconds
  private static final int INTERVAL = 3 * 20;

  // The radius of blocks in which players are sought out
  private static final int RADIUS = 100;

	public PhantomRetarget(Phantom phantom) {
		this.plugin = PhantomMain.getPlugin();
		this.phantom = phantom;
	}

  private Player getNearestPlayer() {
    List<Entity> nearbyEntities = this.phantom.getNearbyEntities(RADIUS, RADIUS, RADIUS);
    double shortestDistance = 100000000;
    Player nearestPlayer = null;

    for (Entity entity : nearbyEntities) {
      if (entity.getType() == EntityType.PLAYER) {
        if (((Player) entity).getGameMode() != GameMode.CREATIVE) {
          Location phantomPos = this.phantom.getLocation();
          Location playerPos = entity.getLocation();
          double distance = phantomPos.distance(playerPos);
          if (distance < shortestDistance) {
            shortestDistance = distance;
            nearestPlayer = (Player) entity;
          }
        }
      }
    }

    return nearestPlayer;
  }

	/**
	 * Make the phantom target a player if not already doing so
	 */
	@Override
	public void run() {
    // If the entity is dead, we are done.
    if (this.phantom.isDead()) {
      this.cancel();
      return;
    }

    // If the phantom isn't targeting anybody
    if (this.phantom.getTarget() == null) {
      Player nearestPlayer = getNearestPlayer();
      this.phantom.setTarget(nearestPlayer);
    }

    // Wait the duration, then check and re-fire this
    (new PhantomRetarget(this.phantom)).runTaskLater(this.plugin, this.INTERVAL);
    this.cancel();
	}
}
