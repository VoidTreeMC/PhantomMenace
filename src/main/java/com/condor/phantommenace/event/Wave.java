package com.condor.phantommenace.event;

import java.util.Random;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import com.condor.phantommenace.phantom.PhantomType;
import com.condor.phantommenace.runnable.SpawnPhantomAtLocation;
import com.condor.phantommenace.main.PhantomMain;

public abstract class Wave {

  // Measured in ticks
  private static final long TIME_BETWEEN_PHANTOMS = 5;

  private static final int MAX_PHANTOMS = 200;
  private static final int PHANTOMS_PER_PLAYER = 10;

  final static int ARENA_MIN_Z = -2885;
  final static int ARENA_MAX_Z = -2784;
  final static int ARENA_MIN_X = 1503;
  final static int ARENA_MAX_X = 1555;

  private static final Random rng = new Random();
  private static final int MAX_DISTANCE = 10;

  protected HashMap<PhantomType, Integer> waveMap;

  public Wave(HashMap<PhantomType, Integer> map) {
    this.waveMap = map;
  }

  public int getNumberOfPlayersInArena() {
    int ret = 0;
    Player[] onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[1]);

    for (Player player : onlinePlayers) {
      // Bukkit.getLogger().log(Level.INFO, "Evaluating player: " + player.getName());
      if (player != null) {
        double x = player.getLocation().getX();
        double z = player.getLocation().getZ();
        // Bukkit.getLogger().log(Level.INFO, "Less than max X: " + (x <= ARENA_MAX_X));
        // Bukkit.getLogger().log(Level.INFO, "Less than max Z: " + (z <= ARENA_MAX_Z));
        // Bukkit.getLogger().log(Level.INFO, "Greater than min X: " + (x >= ARENA_MIN_X));
        // Bukkit.getLogger().log(Level.INFO, "Greater than min Z: " + (z >= ARENA_MIN_Z));
        if (isInArena(x, z)) {
          ret++;
        }
      }
    }

    return ret;
  }

  public static boolean isInArena(double x, double z) {
    return (x <= ARENA_MAX_X && x >= ARENA_MIN_X && z <= ARENA_MAX_Z && z >= ARENA_MIN_Z);
  }

  /**
   * Gets the total number of phantoms in this wave
   * @return Returns the total number of phantoms in this wave
   */
  public int getTotalPhantoms() {
    int sum = 0;

    for (Integer num : waveMap.values()) {
      sum += num;
    }


    // Update the number of phantoms to reflect the number of players online
    int numPlayers = getNumberOfPlayersInArena();
    Bukkit.getLogger().log(Level.INFO, "Number of players in arena: " + numPlayers);
    int numToSpawn = numPlayers * sum;
    if (numToSpawn >= MAX_PHANTOMS) {
      numToSpawn = 200;
    }

    if (waveMap.get(PhantomType.MOTHER_OF_ALL_PHANTOMS) != null) {
      numToSpawn -= (waveMap.get(PhantomType.MOTHER_OF_ALL_PHANTOMS) * numPlayers);
      numToSpawn++;
    }

    return numToSpawn;
  }

  /**
   * Spawns the wave at the specified location
   * @param  loc The location
   */
  public void spawnWave(Location loc) {
    int numToSpawn = getTotalPhantoms();
    // Bukkit.getLogger().log(Level.INFO, "I should spawn " + numToSpawn + " phantoms.");
    for (Entry<PhantomType, Integer> entry : waveMap.entrySet()) {
      waveMap.put(entry.getKey(), entry.getValue());
    }
    // Set the number of phantoms in the current event wave to the
    // number of phantoms in this wave
    PhantomMain.getPlugin().getPhantomEvent().setWaveCount(numToSpawn);

    ArrayList<PhantomType> waveList = new ArrayList<>();
    int numSoFar = 0;
    boolean hasMoapSpawned = false;
    while (numSoFar < numToSpawn) {
      for (Entry<PhantomType, Integer> entry : waveMap.entrySet()) {
        for (int i = 0; i < entry.getValue(); i++) {
          if (entry.getKey() == PhantomType.MOTHER_OF_ALL_PHANTOMS) {
            if (!hasMoapSpawned) {
              waveList.add(entry.getKey());
              numSoFar++;
              hasMoapSpawned = true;
            }
          } else {
            waveList.add(entry.getKey());
            numSoFar++;
          }
        }
      }
    }
    // Shuffle the list of phantoms, so we deploy them in a random order
    Collections.shuffle(waveList);
    // Bukkit.getLogger().log(Level.INFO, "I am spawning " + waveList.size() + " phantoms.");
    long delay = 0;

    // Iterate through the list of phantoms, and summon them in a random
    // order with some time between each phantom as defined by
    // TIME_BETWEEN_PHANTOMS
    for (PhantomType phantom : waveList) {
      (new SpawnPhantomAtLocation(phantom, getRandomSpotNearLoc(loc), true)).runTaskLater(PhantomMain.getPlugin(), delay);
      delay += TIME_BETWEEN_PHANTOMS;
    }
  }

  private static Location getRandomSpotNearLoc(Location loc) {
    int xDelta = rng.nextInt(MAX_DISTANCE * 2) - MAX_DISTANCE;
    int zDelta = rng.nextInt(MAX_DISTANCE * 2) - MAX_DISTANCE;
    return new Location(loc.getWorld(), loc.getX() + xDelta, loc.getY(), loc.getZ() + zDelta);
  }
}
