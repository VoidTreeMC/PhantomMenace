package com.condor.phantommenace.runnable;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.event.PhantomEvent;

public class DespawnEntity extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The entity to despawn
  Entity entity;

  // The location at which to summon the bee
  Location loc;

	public DespawnEntity(Entity entity) {
		this.plugin = PhantomMain.getPlugin();
		this.entity = entity;
	}

	@Override
	public void run() {
    entity.remove();
	}
}
