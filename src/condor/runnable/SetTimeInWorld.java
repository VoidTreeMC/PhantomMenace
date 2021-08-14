package condor.runnable;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.World;

import condor.main.PhantomMain;

public class SetTimeInWorld extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The world whose time to set
  World world;

  // The time to set the world to
  long time;

	public SetTimeInWorld(World world, long time) {
		this.plugin = PhantomMain.getPlugin();
		this.time = time;
    this.world = world;
	}

	/**
	 * Make the phantom glow
	 */
	@Override
	public void run() {
    world.setTime(time);
    this.cancel();
	}
}
