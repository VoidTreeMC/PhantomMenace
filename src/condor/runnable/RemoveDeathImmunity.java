package condor.runnable;

import java.util.List;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.LivingEntity;

import condor.main.PhantomMain;
import condor.event.PhantomEvent;
import condor.phantom.PhantomType;
import condor.phantom.RecentPlayerDeaths;

public class RemoveDeathImmunity extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The player whose immunity to remove
  UUID uuid;

	long time;

	public RemoveDeathImmunity(UUID uuid, long time) {
		this.plugin = PhantomMain.getPlugin();
		this.uuid = uuid;
		this.time = time;
	}

	@Override
	public void run() {
    RecentPlayerDeaths.removeFromList(uuid, time);
	}
}
