package condor.phantom;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.metadata.FixedMetadataValue;

import condor.main.PhantomMain;

public class PhantomGenerator {
  /**
   * Summons a phantom of the specified type at the specified location
   * @param type  The type of phantom to summon
   * @param loc   The location to summon the phantom at
   */
  public static void summonPhantom(PhantomType type, Location loc) {
    switch (type) {
      case VANILLA:
        loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
        break;
      case EVENT_LEVEL_ONE:
        summonLevelOne(loc);
        break;
      case EVENT_LEVEL_TWO:
        summonLevelTwo(loc);
        break;
    }
  }

  /**
   * Summons a level one phantom at the specified location
   * @param loc  The location
   */
  public static void summonLevelOne(Location loc) {
    // TODO: Method stub
  }

  /**
   * Summons a level two phantom at the specified location
   * @param loc  The location
   */
  public static void summonLevelTwo(Location loc) {
    Phantom phantom = (Phantom) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
    PotionEffect fireRes = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1000000, 0, true, false, false);
    PotionEffect strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000000, 0, true, false, false);
    phantom.setMaxHealth(30);
    phantom.addPotionEffect(fireRes);
    phantom.addPotionEffect(strength);
    phantom.setFireTicks(100000000);
    // phantom.setSize(5);

    // Add metadata
    phantom.setMetadata(PhantomType.PHANTOM_TYPE_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), PhantomType.EVENT_LEVEL_TWO.toString()));
  }
}
