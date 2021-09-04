package com.condor.phantommenace.runnable;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Phantom;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BarFlag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.EntityType;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.phantom.PhantomType;
import com.condor.phantommenace.phantom.PhantomGenerator;
import com.condor.phantommenace.event.PhantomEvent;

public class SpawnSilverfishAtLoc extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The location at which to spawn the silverfish
  Location loc;

	public SpawnSilverfishAtLoc(Location loc) {
		this.plugin = PhantomMain.getPlugin();
		this.loc = loc;
	}

	/**
	 * Spawns the silverfish at the location and tags them
	 * with metadata to mark it as an event entity
	 */
	@Override
	public void run() {
    for (int i = 0; i < 4; i++) {
      Silverfish silverfish = (Silverfish) loc.getWorld().spawnEntity(loc, EntityType.SILVERFISH);
      silverfish.setMetadata(PhantomEvent.EVENT_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), true));
    }
	}
}
