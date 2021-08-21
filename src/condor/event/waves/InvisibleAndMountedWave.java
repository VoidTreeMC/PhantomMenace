package condor.event.waves;

import java.util.TreeMap;
import java.lang.Integer;

import condor.event.Wave;
import condor.phantom.PhantomType;

public class InvisibleAndMountedWave extends Wave {

  private static TreeMap<PhantomType, Double> map = new TreeMap<>();

  static {
    map.put(PhantomType.INVISIBLE_PHANTOM, 1.5);
    map.put(PhantomType.MOUNTED_PHANTOM, 3.0);
  }

  public InvisibleAndMountedWave() {
    super(map);
  }
}
