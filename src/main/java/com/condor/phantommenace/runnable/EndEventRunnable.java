package com.condor.phantommenace.runnable;

import java.util.List;
import java.util.Random;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Phantom;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.SoundCategory;
import org.bukkit.Sound;
import org.bukkit.Particle;
import org.bukkit.Material;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.event.PhantomEvent;

public class EndEventRunnable extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  /**
	* Builds the BukkitRunnable
	*/
	public EndEventRunnable() {
		this.plugin = PhantomMain.getPlugin();
	}

  /**
	 * Ends the phantom event early
	 */
	@Override
	public void run() {
    if (PhantomEvent.isActive()) {
      PhantomEvent.endEvent(true);
    }
    this.cancel();
	}
}
