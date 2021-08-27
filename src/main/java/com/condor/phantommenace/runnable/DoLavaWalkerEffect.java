package com.condor.phantommenace.runnable;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Levelled;

import com.condor.phantommenace.main.PhantomMain;

public class DoLavaWalkerEffect extends BukkitRunnable {

	// The plugin
	JavaPlugin plugin;

  // A list of locations whose blocks have been changed to obsidian
  ArrayList<Location> changedBlocks = new ArrayList<>();

  // The location to begin search from
  Location loc;

  // Radius of effect
  private static final int RADIUS = 4;

  // The duration
  private static final int DURATION = 5;

	public DoLavaWalkerEffect(Location loc) {
		this.plugin = PhantomMain.getPlugin();
		this.loc = loc;
	}

	/**
	 * Search for lava blocks nearby, and replace them with obsidian
	 */
	@Override
	public void run() {
    int X = (int) loc.getX();
    // We're doing the blocks under the player
    int Y = (int) loc.getY() - 1;
    int Z = (int) loc.getZ();
    for (int x = (X - RADIUS); x <= (X + RADIUS); x++) {
      for (int z = (Z - RADIUS); z <= (Z + RADIUS); z++) {
        Location currLoc = new Location(loc.getWorld(), x, Y, z);
        if (currLoc.getBlock().getType() == Material.LAVA) {
          Levelled blockData = (Levelled) currLoc.getBlock().getBlockData();
          boolean isSourceBlock = blockData.getLevel() == 0;
          if (isSourceBlock) {
            currLoc.getBlock().setType(Material.OBSIDIAN);
            changedBlocks.add(currLoc);
          }
        }
      }
    }

    (new UndoLavaWalkerEffect(changedBlocks)).runTaskLater(PhantomMain.getPlugin(), 20 * DURATION);

    this.cancel();
	}
}
