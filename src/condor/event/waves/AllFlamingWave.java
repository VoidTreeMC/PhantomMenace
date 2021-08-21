package condor.event.waves;

import java.util.TreeMap;
import java.lang.Integer;

import condor.event.Wave;
import condor.phantom.PhantomType;

public class AllFlamingWave extends Wave {

  private static TreeMap<PhantomType, Double> map = new TreeMap<>();

  static {
    map.put(PhantomType.FLAMING_PHANTOM, 6.0);
  }

  public AllFlamingWave() {
    super(map);
  }
}
