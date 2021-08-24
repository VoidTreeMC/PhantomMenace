package condor.event.waves;

import java.util.TreeMap;
import java.lang.Integer;

import condor.event.Wave;
import condor.phantom.PhantomType;

public class InvisibleAndMountedWave extends Wave {

  private static TreeMap<PhantomType, Integer> map = new TreeMap<>();

  static {
    map.put(PhantomType.INVISIBLE_PHANTOM, 2);
    map.put(PhantomType.MOUNTED_PHANTOM, 3);
  }

  public InvisibleAndMountedWave() {
    super(map);
  }
}
