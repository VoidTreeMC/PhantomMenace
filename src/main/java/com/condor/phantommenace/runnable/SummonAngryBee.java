package com.condor.phantommenace.runnable;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Bee;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.event.PhantomEvent;
import com.condor.phantommenace.item.legendaryitems.BeeLordWand;

public class SummonAngryBee extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The phantom to evaluate
  LivingEntity target;

  // The player who summoned the bee
  Player player;

  // The lifespan of the bee
  int lifespan;

	public SummonAngryBee(LivingEntity entity, Player player, int lifespan) {
		this.plugin = PhantomMain.getPlugin();
		this.target = entity;
    this.player = player;
    this.lifespan = lifespan;
	}

	@Override
	public void run() {
    // If the entity is dead, we are done.
    if (this.target.isDead()) {
      this.cancel();
      return;
    }

    Location loc = player.getLocation().add(0, 1, 0);

    Bee bee = (Bee) loc.getWorld().spawnEntity(loc, EntityType.BEE);
    bee.setAdult();
    bee.setAnger(1);
    bee.setTarget(target);
    bee.setMaxHealth(1);
    bee.setMetadata(BeeLordWand.BEE_METADATA, new FixedMetadataValue(PhantomMain.getPlugin(), player.getUniqueId().toString()));
    (new DespawnEntity(bee)).runTaskLater(PhantomMain.getPlugin(), lifespan);
	}
}
