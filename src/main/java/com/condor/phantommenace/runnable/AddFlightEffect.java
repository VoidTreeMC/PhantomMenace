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
import org.bukkit.entity.Player;

import com.condor.phantommenace.main.PhantomMain;

public class AddFlightEffect extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The player whose flight is to be added
  Player player;

	public AddFlightEffect(Player player) {
		this.plugin = PhantomMain.getPlugin();
		this.player = player;
	}

	@Override
	public void run() {
    if (player != null) {
      player.setAllowFlight(true);
    }
	}
}
