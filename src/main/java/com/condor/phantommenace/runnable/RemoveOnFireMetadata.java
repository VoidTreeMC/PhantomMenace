package com.condor.phantommenace.runnable;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.phantom.RecentPlayerDeaths;

public class RemoveOnFireMetadata extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The player who will no longer be on fire
  Player player;

	public RemoveOnFireMetadata(Player player) {
		this.plugin = PhantomMain.getPlugin();
		this.player = player;
	}

	/**
	 * Removes the metadata from the player that indicates
	 * that they are on fire from an event phantom
	 */
	@Override
	public void run() {
    this.player.removeMetadata(RecentPlayerDeaths.ON_FIRE_EVENT_METADATA, PhantomMain.getPlugin());
    this.cancel();
	}
}
