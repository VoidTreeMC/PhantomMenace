package com.condor.phantommenace.runnable;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.condor.phantommenace.main.PhantomMain;

import java.util.List;

public class DoFireworkShow extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The location for the center of the show
  Location loc;

	public DoFireworkShow(Location loc) {
		this.plugin = PhantomMain.getPlugin();
		this.loc = loc;
	}

	/**
	 * Make the phantom glow
	 */
	@Override
	public void run() {
    FireworkEffect redFireworkEffect = FireworkEffect.builder()
                                    .trail(true)
                                    .flicker(true)
                                    .with(FireworkEffect.Type.STAR)
                                    .withColor(Color.RED)
                                    .withFade(Color.GRAY)
                                    .build();
    FireworkEffect grayFireworkEffect = FireworkEffect.builder()
                                    .trail(true)
                                    .flicker(true)
                                    .with(FireworkEffect.Type.BALL_LARGE)
                                    .withColor(Color.GRAY)
                                    .withFade(Color.GRAY)
                                    .build();

    final int FIREWORK_INTERVAL = 3;
    int delay = 0;

    for (int i = 0; i < 30; i++) {
      (new SpawnFireworkAtLocation(redFireworkEffect, loc)).runTaskLater(PhantomMain.getPlugin(), delay);
      delay += FIREWORK_INTERVAL;
      (new SpawnFireworkAtLocation(grayFireworkEffect, loc)).runTaskLater(PhantomMain.getPlugin(), delay);
      delay += FIREWORK_INTERVAL;
    }

    this.cancel();
	}
}
