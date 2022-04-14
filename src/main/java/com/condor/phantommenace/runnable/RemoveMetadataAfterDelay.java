package com.condor.phantommenace.runnable;

import java.util.List;
import java.lang.Thread;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.Bukkit;

import com.condor.phantommenace.main.PhantomMain;

public class RemoveMetadataAfterDelay extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  Block block;
  String metadataKey;

	public RemoveMetadataAfterDelay(Block block, String metadataKey) {
		this.plugin = PhantomMain.getPlugin();
		this.block = block;
    this.metadataKey = metadataKey;
	}

	@Override
	public void run() {
    block.removeMetadata(metadataKey, PhantomMain.getPlugin());
	}
}
