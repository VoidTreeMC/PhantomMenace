package condor.event;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Collections;
import java.util.TreeMap;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import condor.phantom.PhantomType;
import condor.runnable.SpawnPhantomAtLocation;
import condor.main.PhantomMain;

public abstract class Wave {

  // Measured in ticks
  private static final long TIME_BETWEEN_PHANTOMS = 5;

  private static final int MAX_PHANTOMS = 200;
  private static final int PHANTOMS_PER_PLAYER = 10;

  protected TreeMap<PhantomType, Double> waveMap;

  public Wave(TreeMap<PhantomType, Double> map) {
    this.waveMap = map;
  }

  /**
   * Gets the total number of phantoms in this wave
   * @return Returns the total number of phantoms in this wave
   */
  public int getTotalPhantoms() {
    int sum = 0;

    for (Double num : waveMap.values()) {
      sum += Math.ceil(num);
    }

    // Update the number of phantoms to reflect the number of players online
    int numPlayers = Bukkit.getOnlinePlayers().size();
    int numToSpawn = numPlayers * sum;
    if (numToSpawn >= MAX_PHANTOMS) {
      numToSpawn = 200;
    }

    return numToSpawn;
  }

  /**
   * Spawns the wave at the specified location
   * @param  loc The location
   */
  public void spawnWave(Location loc) {
    int numToSpawn = getTotalPhantoms();
    for (Entry<PhantomType, Double> entry : waveMap.entrySet()) {
      waveMap.put(entry.getKey(), entry.getValue().doubleValue());
    }
    // Set the number of phantoms in the current event wave to the
    // number of phantoms in this wave
    PhantomMain.getPlugin().getPhantomEvent().setWaveCount(numToSpawn);

    ArrayList<PhantomType> waveList = new ArrayList<>();
    int numSoFar = 0;
    boolean hasMoapSpawned = false;
    while (numSoFar < numToSpawn) {
      for (Entry<PhantomType, Double> entry : waveMap.entrySet()) {
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
    long delay = 0;

    // Iterate through the list of phantoms, and summon them in a random
    // order with some time between each phantom as defined by
    // TIME_BETWEEN_PHANTOMS
    for (PhantomType phantom : waveList) {
      (new SpawnPhantomAtLocation(phantom, loc)).runTaskLater(PhantomMain.getPlugin(), delay);
      delay += TIME_BETWEEN_PHANTOMS;
    }
  }
}
