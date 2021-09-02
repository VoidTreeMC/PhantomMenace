package com.condor.phantommenace.event.waves;

import java.util.TreeMap;
import java.lang.Integer;

import com.condor.phantommenace.event.Wave;
import com.condor.phantommenace.phantom.PhantomType;

public class InvisibleAndMountedWave extends Wave {

  private static TreeMap<PhantomType, Integer> map = new TreeMap<>();

  static {
    map.put(PhantomType.INVISIBLE_PHANTOM, 3);
    map.put(PhantomType.MOUNTED_PHANTOM, 5);
  }

  public InvisibleAndMountedWave() {
    super(map);
  }
}
