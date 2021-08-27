package com.condor.phantommenace.runnable;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Phantom;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BarFlag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.phantom.PhantomType;
import com.condor.phantommenace.phantom.PhantomGenerator;
import com.condor.phantommenace.event.PhantomEvent;

public class SpawnPhantomAtLocation extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The entity to spawn
  PhantomType phantomType;

  // The location at which to spawn the entities
  Location loc;

	public SpawnPhantomAtLocation(PhantomType phantomType, Location loc) {
		this.plugin = PhantomMain.getPlugin();
    this.phantomType = phantomType;
		this.loc = loc;
	}

	/**
	 * Spawns the phantom at the location and tags it
	 * with metadata to mark it as an event phatom
	 */
	@Override
	public void run() {
    Phantom phantom = PhantomGenerator.summonPhantom(phantomType, loc, true);
    phantom.setMetadata(PhantomEvent.EVENT_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), true));
    phantom.setRemoveWhenFarAway(false);

    if (phantomType == PhantomType.MOTHER_OF_ALL_PHANTOMS) {
      PhantomEvent.moapBar = Bukkit.createBossBar(ChatColor.RED + "" + ChatColor.UNDERLINE + "MOTHER OF ALL PHANTOMS", BarColor.RED, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);
      for (Player p : Bukkit.getOnlinePlayers()) {
        PhantomEvent.moapBar.addPlayer(p);
      }
    }

    // After 10 seconds, start making the phantom aggressively target players
    (new PhantomRetarget(phantom)).runTaskLater(PhantomMain.getPlugin(), 10 * 20);
    // After two minutes, make the phantom glow
    (new MakePhantomGlow(phantom)).runTaskLater(PhantomMain.getPlugin(), 2 * 60 * 20);
	}
}
