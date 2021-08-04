package condor.event.waves;

import java.util.TreeMap;
import java.lang.Integer;

import condor.event.Wave;
import condor.phantom.PhantomType;

public class VanillaWave extends Wave {

  private static TreeMap<PhantomType, Integer> map = new TreeMap<>();

  static {
    // map.put(PhantomType.VANILLA, 30);
    map.put(PhantomType.VANILLA, 3);
  }

  public VanillaWave() {
    super(map);
  }
}
