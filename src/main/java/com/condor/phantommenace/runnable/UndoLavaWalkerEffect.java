package com.condor.phantommenace.runnable;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;
import org.bukkit.Material;

import com.condor.phantommenace.main.PhantomMain;

public class UndoLavaWalkerEffect extends BukkitRunnable {

  // TODO: Make it check to see if there are any other DoLavaWalkerEffects happening to those blocks
  // before replacing it

	// The plugin
	JavaPlugin plugin;

  // A list of locations whose blocks have been changed to obsidian
  ArrayList<Location> changedBlocks = new ArrayList<>();

	public UndoLavaWalkerEffect(ArrayList<Location> changedBlocks) {
		this.plugin = PhantomMain.getPlugin();
		this.changedBlocks.addAll(changedBlocks);
	}

	/**
	 * Replace the listed obsidian blocks with lava
	 */
	@Override
	public void run() {
    for (Location loc : changedBlocks) {
      loc.getBlock().setType(Material.LAVA);
    }
    this.cancel();
	}
}
