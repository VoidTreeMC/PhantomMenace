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

  protected TreeMap<PhantomType, Integer> waveMap;

  public Wave(TreeMap<PhantomType, Integer> map) {
    this.waveMap = map;
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

    int scaleNum = Bukkit.getOnlinePlayers().size();
    // If the phantom number would exceed the max, change the
    // scaling factor
    if (scaleNum * sum > MAX_PHANTOMS) {
      scaleNum = MAX_PHANTOMS / sum;
    }

    return scaleNum * sum;
  }

  /**
   * Spawns the wave at the specified location
   * @param  loc The location
   */
  public void spawnWave(Location loc) {
    // Update the number of phantoms to reflect the number of players online
    int scaleNum = Bukkit.getOnlinePlayers().size();
    int totalPhantoms = this.getTotalPhantoms();
    // If the phantom number would exceed the max, change the
    // scaling factor
    if (totalPhantoms > MAX_PHANTOMS) {
      scaleNum = MAX_PHANTOMS / totalPhantoms;
    }
    for (Entry<PhantomType, Integer> entry : waveMap.entrySet()) {
      waveMap.put(entry.getKey(), entry.getValue() * scaleNum);
    }
    // Set the number of phantoms in the current event wave to the
    // number of phantoms in this wave
    PhantomMain.getPlugin().getPhantomEvent().setWaveCount(this.getTotalPhantoms());

    ArrayList<PhantomType> waveList = new ArrayList<>();
    for (Entry<PhantomType, Integer> entry : waveMap.entrySet()) {
      for (int i = 0; i < entry.getValue(); i++) {
        waveList.add(entry.getKey());
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
