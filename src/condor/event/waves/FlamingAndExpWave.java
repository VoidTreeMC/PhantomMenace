package condor.event.waves;

import java.util.TreeMap;
import java.lang.Integer;

import condor.event.Wave;
import condor.phantom.PhantomType;

public class FlamingAndExpWave extends Wave {

  private static TreeMap<PhantomType, Integer> map = new TreeMap<>();

  static {
    map.put(PhantomType.FLAMING_PHANTOM, 2);
    map.put(PhantomType.EXTRA_XP_PHANTOM, 3);
  }

  public FlamingAndExpWave() {
    super(map);
  }
}
