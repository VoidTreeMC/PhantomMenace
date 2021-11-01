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

  // The time that the player drank the potion
  long finishTime;

  // Whether or not we should check if this order is outdated
  boolean checkTimes;

  private static final int SLOW_FALL_DURATION = 20 * 30;

  private static final PotionEffect slowFall = new PotionEffect(PotionEffectType.SLOW_FALLING, SLOW_FALL_DURATION, 0, true, false, false);
  private static final PotionEffect shortSlowFall = new PotionEffect(PotionEffectType.SLOW_FALLING, (int) FlightPotion.COOLDOWN_TIME, 0, true, false, false);

  /**
   * Removes the flight ability from the player
   * IMPORTANT: MUST BE RUN ASYNCHRONOUSLY
   * @param player  The player whose flight ability is to be removed
   */
	public RemoveFlightEffect(Player player, long finishTime, boolean checkTimes) {
		this.plugin = PhantomMain.getPlugin();
    this.finishTime = finishTime;
		this.player = player;
    this.checkTimes = checkTimes;
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

    if (checkTimes && FlightPotion.isOutdated(this.player.getUniqueId(), this.finishTime)) {
      return;
    }

    // Bukkit.getLogger().info("Sending message at: " + System.currentTimeMillis());
    if (checkTimes) {
      this.player.sendMessage(ChatColor.RED + "The ground is pulling you down. Now would be a good time to land.");

      try {
        Thread.sleep(1000 * 10);
      } catch (InterruptedException e) {
        // Do nothing
      }

      if (FlightPotion.isOutdated(this.player.getUniqueId(), this.finishTime)) {
        return;
      }
      this.player.sendMessage(ChatColor.AQUA + "You feel heavier.");
      this.player.removeMetadata(FlightPotion.METADATA_KEY, this.plugin);
      FlightPotion.removeTimestamp(player.getUniqueId(), finishTime);
    }

    this.player.setAllowFlight(false);

    PotionEffect effect = (checkTimes) ? slowFall : shortSlowFall;

    Bukkit.getScheduler().runTask(PhantomMain.getPlugin(), new Runnable() {
			@Override
			public void run() {
        player.addPotionEffect(effect);
	    }
    });

    this.cancel();
	}
}
