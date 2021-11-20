package com.condor.phantommenace.event.waves;

import java.util.TreeMap;
import java.lang.Integer;

import com.condor.phantommenace.event.Wave;
import com.condor.phantommenace.phantom.PhantomType;

public class FlamingAndExpWave extends Wave {

  private static TreeMap<PhantomType, Integer> map = new TreeMap<>();

  static {
    map.put(PhantomType.FLAMING_PHANTOM, 4);
    map.put(PhantomType.EXTRA_XP_PHANTOM, 5);
    map.put(PhantomType.KAMIKAZE_PHANTOM, 1);
  }

  public FlamingAndExpWave() {
    super(map);
  }
}
