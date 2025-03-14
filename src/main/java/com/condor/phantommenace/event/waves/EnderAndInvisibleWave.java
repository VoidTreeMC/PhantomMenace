package com.condor.phantommenace.event.waves;

import java.util.HashMap;
import java.lang.Integer;

import com.condor.phantommenace.event.Wave;
import com.condor.phantommenace.phantom.PhantomType;

public class EnderAndInvisibleWave extends Wave {

  private static HashMap<PhantomType, Integer> map = new HashMap<>();

  static {
    map.put(PhantomType.INVISIBLE_PHANTOM, 4);
    map.put(PhantomType.ENDER_PHANTOM, 5);
    map.put(PhantomType.KAMIKAZE_PHANTOM, 1);
  }

  public EnderAndInvisibleWave() {
    super(map);
  }
}
