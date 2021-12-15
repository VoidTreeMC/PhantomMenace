package com.condor.phantommenace.event.waves;

import java.util.HashMap;
import java.lang.Integer;

import com.condor.phantommenace.event.Wave;
import com.condor.phantommenace.phantom.PhantomType;

public class VanillaWave extends Wave {

  private static HashMap<PhantomType, Integer> map = new HashMap<>();

  static {
    map.put(PhantomType.VANILLA, 10);
  }

  public VanillaWave() {
    super(map);
  }
}
