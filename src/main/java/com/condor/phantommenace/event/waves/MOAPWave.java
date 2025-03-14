package com.condor.phantommenace.event.waves;

import java.util.HashMap;
import java.lang.Integer;

import com.condor.phantommenace.event.Wave;
import com.condor.phantommenace.phantom.PhantomType;

public class MOAPWave extends Wave {

  private static HashMap<PhantomType, Integer> map = new HashMap<>();

  static {
    map.put(PhantomType.MOTHER_OF_ALL_PHANTOMS, 1);
    map.put(PhantomType.EXTRA_XP_PHANTOM, 18);
    map.put(PhantomType.HEALER_PHANTOM, 2);
    // map.put(PhantomType.KAMIKAZE_PHANTOM, 2);
  }

  public MOAPWave() {
    super(map);
  }
}
