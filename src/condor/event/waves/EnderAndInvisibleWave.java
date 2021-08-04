package condor.event.waves;

import java.util.TreeMap;
import java.lang.Integer;

import condor.event.Wave;
import condor.phantom.PhantomType;

public class EnderAndInvisibleWave extends Wave {

  private static TreeMap<PhantomType, Integer> map = new TreeMap<>();

  static {
    map.put(PhantomType.INVISIBLE_PHANTOM, 15);
    map.put(PhantomType.ENDER_PHANTOM, 30);
  }

  public EnderAndInvisibleWave() {
    super(map);
  }
}
