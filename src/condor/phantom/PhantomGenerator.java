package condor.phantom;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Skeleton;
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
      case EXTRA_XP_PHANTOM:
        summonExtraXPPhantom(loc);
        break;
      case FLAMING_PHANTOM:
        summonFlamingPhantom(loc);
        break;
      case MOUNTED_PHANTOM:
        summonMountedPhantom(loc);
        break;
    }
  }

  /**
   * Summons an extra XP phantom at the specified location
   * @param loc  The location
   */
  public static void summonExtraXPPhantom(Location loc) {
    // TODO: Method stub
  }

  /**
   * Summons a flaming phantom at the specified location
   * @param loc  The location
   */
  public static void summonFlamingPhantom(Location loc) {
    Phantom phantom = (Phantom) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
    PotionEffect fireRes = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1000000, 0, true, false, false);
    PotionEffect strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000000, 0, true, false, false);
    phantom.setMaxHealth(30);
    phantom.addPotionEffect(fireRes);
    phantom.addPotionEffect(strength);
    phantom.setFireTicks(100000000);
    // phantom.setSize(5);

    // Add metadata
    phantom.setMetadata(PhantomType.PHANTOM_TYPE_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), PhantomType.FLAMING_PHANTOM.toString()));
  }


  /**
   * Summons a mounted phantom at the specified location
   * @param loc  The location
   */
  public static void summonMountedPhantom(Location loc) {
    Phantom phantom = (Phantom) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
    PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 1000000, 0, true, false, false);
    phantom.addPotionEffect(speed);

    Skeleton skeleton = (Skeleton) loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
    phantom.setPassenger(skeleton);

    // Add metadata
    phantom.setMetadata(PhantomType.PHANTOM_TYPE_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), PhantomType.MOUNTED_PHANTOM.toString()));
    skeleton.setMetadata(PhantomType.PHANTOM_TYPE_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), PhantomType.MOUNTED_PHANTOM.toString()));
  }
}
