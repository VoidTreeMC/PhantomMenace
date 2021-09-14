package com.condor.phantommenace.runnable;

import java.util.List;
import java.util.Random;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Phantom;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.potion.PotionType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.Color;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.event.PhantomEvent;

public class MOAPBombs extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The MOAP
  Phantom phantom;

  private static final Random rng = new Random();

	public MOAPBombs(Phantom entity) {
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

    // Summon a splash potion at this location

    Location loc = phantom.getLocation();
    ThrownPotion potion = (ThrownPotion) loc.getWorld().spawnEntity(loc, EntityType.SPLASH_POTION);
    potion.setShooter(this.phantom);
    ItemStack potionItem = new ItemStack(Material.SPLASH_POTION);
    PotionMeta potionMeta = (PotionMeta) potionItem.getItemMeta();
	  PotionData potionData = null;

    if (rng.nextInt(2) == 0) {
      potionData = new PotionData(PotionType.INSTANT_DAMAGE, false, true);
    } else {
      potionData = new PotionData(PotionType.POISON, false, true);
    }

    potionMeta.setBasePotionData(potionData);
  	potionMeta.setColor(Color.MAROON);
    potionItem.setItemMeta(potionMeta);
    potion.setItem(potionItem);

    potion.setMetadata(PhantomEvent.EVENT_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), PhantomEvent.EVENT_METADATA_KEY.toString()));


    (new MOAPBombs(phantom)).runTaskLater(plugin, 10);
    this.cancel();
	}
}
