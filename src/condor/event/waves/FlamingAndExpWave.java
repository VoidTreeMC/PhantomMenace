package condor.event.waves;

import java.util.TreeMap;
import java.lang.Integer;

import condor.event.Wave;
import condor.phantom.PhantomType;

public class FlamingAndExpWave extends Wave {

  private static TreeMap<PhantomType, Double> map = new TreeMap<>();

  static {
    map.put(PhantomType.FLAMING_PHANTOM, 1.5);
    map.put(PhantomType.EXTRA_XP_PHANTOM, 3.0);
  }

  public FlamingAndExpWave() {
    super(map);
  }
}
