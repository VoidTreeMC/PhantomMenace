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
    Bukkit.getLogger().log(Level.INFO, uuid + " removing from death immunity list.");
    RecentPlayerDeaths.removeFromList(uuid, time);
	}
}
