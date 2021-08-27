package com.condor.phantommenace.runnable;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Phantom;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.LivingEntity;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.event.PhantomEvent;

public class DeleteIfIsWild extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The phantom to evaluate
  Phantom phantom;

	public DeleteIfIsWild(Phantom entity) {
		this.plugin = PhantomMain.getPlugin();
		this.phantom = entity;
	}

	@Override
	public void run() {
    // If the entity is dead, we are done.
    if (this.phantom.isDead()) {
      this.cancel();
      return;
    }

    // If it's not an event phantom, despawn it
    if (!(this.phantom.hasMetadata(PhantomEvent.EVENT_METADATA_KEY))) {
      phantom.remove();
    }
	}
}
