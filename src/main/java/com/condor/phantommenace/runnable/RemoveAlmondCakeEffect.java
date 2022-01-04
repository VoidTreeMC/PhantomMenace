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
import com.condor.phantommenace.item.legendaryitems.AlmondCake;

public class RemoveAlmondCakeEffect extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The entity whose almond acke effect is to be removed
  Player player;

  /**
   * Removes the almond cake effect from the player
   * IMPORTANT: MUST BE RUN ASYNCHRONOUSLY
   * @param player  The player whose almond cake effect is to be removed
   */
	public RemoveAlmondCakeEffect(Player player) {
		this.plugin = PhantomMain.getPlugin();
		this.player = player;
	}

	/**
	 * Disables the player's almond cake effect
	 */
	@Override
	public void run() {
    // If the player disconnected, we are done
    if (this.player == null) {
      this.cancel();
      return;
    }

    // If they no longer have the metadata key, cancel early.
    if (!this.player.hasMetadata(AlmondCake.ALMOND_METADATA)) {
      return;
    }

    this.player.sendMessage(ChatColor.RED + "Your sugar rush will wear off soon. Get to safety!");

    try {
      Thread.sleep(1000 * 20);
    } catch (InterruptedException e) {
      // Do nothing
    }

    this.player.sendMessage(ChatColor.AQUA + "Your sugar rush is wearing off now. Too late!");
    this.player.removeMetadata(AlmondCake.ALMOND_METADATA, this.plugin);

    this.cancel();
	}
}
