package com.condor.phantommenace.runnable;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.LivingEntity;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.event.PhantomEvent;
import com.condor.phantommenace.phantom.PhantomType;
import com.condor.phantommenace.phantom.RecentPlayerDeaths;

public class ManageDeathImmunity extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  private static final int IMMUNITY_PERIOD = 20 * 60;

  // The player to make immune
  UUID uuid;

	long time;

	public ManageDeathImmunity(UUID uuid) {
		this.plugin = PhantomMain.getPlugin();
		this.uuid = uuid;
	}

	@Override
	public void run() {
    Bukkit.getLogger().log(Level.INFO, uuid + " adding to death immunity list.");
		this.time = System.currentTimeMillis();
    RecentPlayerDeaths.addToList(uuid, time);
    (new RemoveDeathImmunity(uuid, time)).runTaskLater(plugin, IMMUNITY_PERIOD);
	}
}
