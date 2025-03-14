package com.condor.phantommenace.runnable;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.LivingEntity;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.event.PhantomEvent;
import com.condor.phantommenace.phantom.PhantomType;

public class DeleteIfIsWildSkeleton extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The skeleton to evaluate
  LivingEntity entity;

  final static int MIN_Z = -2900;
  final static int MAX_Z = -2784;
  final static int MIN_X = 1503;
  final static int MAX_X = 1555;

	public DeleteIfIsWildSkeleton(LivingEntity entity) {
		this.plugin = PhantomMain.getPlugin();
		this.entity = entity;
	}

	@Override
	public void run() {
    // If the entity is dead, we are done.
    if (this.entity.isDead()) {
      this.cancel();
      return;
    }

    // If it's not within the arena, we don't care.
    Location entityLoc = entity.getLocation();
    if (entityLoc.getX() > MAX_X || entityLoc.getX() < MIN_X || entityLoc.getZ() > MAX_Z || entityLoc.getZ() < MIN_Z) {
      this.cancel();
      return;
    }

    // If it's not an event skeleton, despawn it
    if (!(this.entity.hasMetadata(PhantomType.PHANTOM_TYPE_METADATA_KEY))) {
      entity.remove();
    }
	}
}
