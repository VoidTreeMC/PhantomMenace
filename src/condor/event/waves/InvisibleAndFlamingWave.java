package condor.event.waves;

import java.util.TreeMap;
import java.lang.Integer;

import condor.event.Wave;
import condor.phantom.PhantomType;

public class InvisibleAndFlamingWave extends Wave {

  private static TreeMap<PhantomType, Integer> map = new TreeMap<>();

  static {
    // map.put(PhantomType.FLAMING_PHANTOM, 15);
    // map.put(PhantomType.INVISIBLE_PHANTOM, 30);
    map.put(PhantomType.FLAMING_PHANTOM, 1);
    map.put(PhantomType.INVISIBLE_PHANTOM, 2);
  }

  public InvisibleAndFlamingWave() {
    super(map);
  }
}
