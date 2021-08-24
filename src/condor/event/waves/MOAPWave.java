package condor.event.waves;

import java.util.TreeMap;
import java.lang.Integer;

import condor.event.Wave;
import condor.phantom.PhantomType;

public class MOAPWave extends Wave {

  private static TreeMap<PhantomType, Integer> map = new TreeMap<>();

  static {
    map.put(PhantomType.MOTHER_OF_ALL_PHANTOMS, 1);
    map.put(PhantomType.EXTRA_XP_PHANTOM, 6);
  }

  public MOAPWave() {
    super(map);
  }
}
