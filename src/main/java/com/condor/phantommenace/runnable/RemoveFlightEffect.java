package com.condor.phantommenace.runnable;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Phantom;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.item.legendaryitems.FlightPotion;

public class RemoveFlightEffect extends BukkitRunnable {

  // TODO: Check how this interacts with /gm 1

	//The plugin
	JavaPlugin plugin;

  // The entity whose flight is to be eabled
  Player player;

  private static final int SLOW_FALL_DURATION = 20 * 30;

  /**
   * Removes the flight ability from the player
   * IMPORTANT: MUST BE RUN ASYNCHRONOUSLY
   * @param player  The player whose flight ability is to be removed
   */
	public RemoveFlightEffect(Player player) {
		this.plugin = PhantomMain.getPlugin();
		this.player = player;
	}

	/**
	 * Disables the player's flight
	 */
	@Override
	public void run() {
    // If the player disconnected, we are done
    if (this.player == null) {
      this.cancel();
      return;
    }

    // If they no longer have the metadata key, cancel early.
    if (!this.player.hasMetadata(FlightPotion.METADATA_KEY)) {
      return;
    }

    this.player.sendMessage(ChatColor.RED + "The ground is pulling you down. Now would be a good time to land.");

    try {
      Thread.sleep(1000 * 10);
    } catch (InterruptedException e) {
      // Do nothing
    }

    this.player.sendMessage(ChatColor.AQUA + "You feel heavier.");
    this.player.setAllowFlight(false);
    this.player.removeMetadata(FlightPotion.METADATA_KEY, this.plugin);

    PotionEffect slowFall = new PotionEffect(PotionEffectType.SLOW_FALLING, SLOW_FALL_DURATION, 0, true, false, false);

    Bukkit.getScheduler().runTask(PhantomMain.getPlugin(), new Runnable() {
			@Override
			public void run() {
        player.addPotionEffect(slowFall);
	    }
    });


    this.cancel();
	}
}
