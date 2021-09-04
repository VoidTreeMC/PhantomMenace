package com.condor.phantommenace.runnable;

import java.util.List;
import java.lang.Thread;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.Bukkit;

import com.condor.phantommenace.main.PhantomMain;

public class DisplayCooldown extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The player to whom to show the cooldown
  Player player;

  String title;
  long interval;
  long duration;
  long progress;
  // The bossbar
  BossBar bar;
  // Whether this is the first time it's being run or not
  boolean firstTime = true;

  /**
   * Displays a cooldown for an item/ability
   * IMPORTANT: MUST BE RUN ASYNCHRONOUSLY
   * @param player    The player to whom to show the cooldown
   * @param title     The title of the cooldown
   * @param duration  The duration of the cooldown, measured in milliseconds
   * @param interval  The interval at which to decrease the cooldown, measured in milliseconds
   */
	public DisplayCooldown(Player player, String title, long duration, long interval) {
		this.plugin = PhantomMain.getPlugin();
		this.player = player;
    this.title = title;
    this.duration = duration;
    this.interval = interval;
    this.progress = duration;
	}

	@Override
	public void run() {
    // If the player is null, we are done.
    if (this.player == null) {
      this.cancel();
      return;
    }

    if (firstTime) {
      bar = Bukkit.createBossBar(title, BarColor.GREEN, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);
      bar.addPlayer(player);
      bar.setVisible(true);
      firstTime = false;
      run();
      return;
    }

    // If we're done with the cooldown
    if (progress <= 0) {
      bar.removeAll();
      return;
    }

    progress -= interval;
    double newProg = 0;
    if (progress != 0) {
      newProg = (progress + 0.0) / duration;
    }

    if (newProg >= 0) {
      bar.setProgress(newProg);
    } else {
      bar.setProgress(0);
    }

    try {
      Thread.sleep(interval);
      run();
    } catch (InterruptedException e) {
      bar.removeAll();
      return;
    }
	}
}
