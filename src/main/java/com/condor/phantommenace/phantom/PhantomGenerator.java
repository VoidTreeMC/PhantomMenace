package com.condor.phantommenace.phantom;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Guardian;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.ChatColor;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.runnable.TogglePhantomInvisibilityRunnable;
import com.condor.phantommenace.event.PhantomEvent;
import com.condor.phantommenace.runnable.MOAPBombs;

public class PhantomGenerator {
  /**
   * Summons a phantom of the specified type at the specified location
   * @param type  The type of phantom to summon
   * @param loc   The location to summon the phantom at
   */
  public static Phantom summonPhantom(PhantomType type, Location loc) {
    switch (type) {
      case VANILLA:
        return (Phantom) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
      case EXTRA_XP_PHANTOM:
        return summonExtraXPPhantom(loc);
      case FLAMING_PHANTOM:
        return summonFlamingPhantom(loc);
      case MOUNTED_PHANTOM:
        return summonMountedPhantom(loc);
      case INVISIBLE_PHANTOM:
        return summonInvisiblePhantom(loc);
      case ENDER_PHANTOM:
        return summonEnderPhantom(loc);
      case MOTHER_OF_ALL_PHANTOMS:
        return summonMOAP(loc);
      case KAMIKAZE_PHANTOM:
        return summonKamikaze(loc);
      case HEALER_PHANTOM:
        return summonHealerPhantom(loc);
      default:
        return null;
    }
  }

  /**
   * Summons a phantom of the specified type at the specified location
   * @param type  The type of phantom to summon
   * @param loc   The location to summon the phantom at
   */
  public static Phantom summonPhantom(PhantomType type, Location loc, boolean event) {
    switch (type) {
      case VANILLA:
        return (Phantom) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
      case EXTRA_XP_PHANTOM:
        return summonExtraXPPhantom(loc);
      case FLAMING_PHANTOM:
        return summonFlamingPhantom(loc);
      case MOUNTED_PHANTOM:
        return summonMountedPhantom(loc, event);
      case INVISIBLE_PHANTOM:
        return summonInvisiblePhantom(loc);
      case ENDER_PHANTOM:
        return summonEnderPhantom(loc);
      case MOTHER_OF_ALL_PHANTOMS:
        return summonMOAP(loc);
      case KAMIKAZE_PHANTOM:
        return summonKamikaze(loc);
      case HEALER_PHANTOM:
        return summonHealerPhantom(loc);
      default:
        return null;
    }
  }

  public static Phantom summonHealerPhantom(Location loc) {
    Phantom phantom = (Phantom) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
    phantom.setMetadata(PhantomType.PHANTOM_TYPE_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), PhantomType.HEALER_PHANTOM.toString()));
    Guardian guardian = (Guardian) loc.getWorld().spawnEntity(loc.add(0, 1, 0), EntityType.GUARDIAN);
    guardian.setInvulnerable(true);
    guardian.setMetadata(PhantomEvent.EVENT_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), PhantomType.HEALER_PHANTOM.toString()));
    PotionEffect invisEffect = new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 0, true, false, false);
    guardian.addPotionEffect(invisEffect);
    phantom.addPassenger(guardian);
    return phantom;
  }

  public static Phantom summonKamikaze(Location loc) {
    Phantom phantom = (Phantom) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
    phantom.setMetadata(PhantomType.PHANTOM_TYPE_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), PhantomType.KAMIKAZE_PHANTOM.toString()));
    return phantom;
  }

  /**
   * Summons a Mother-of-all-Phantoms at the specified location
   * @param  loc The location
   */
  public static Phantom summonMOAP(Location loc) {
    Phantom phantom = (Phantom) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
    phantom.setSize(30);
    phantom.setMaxHealth(600);
    phantom.setHealth(600);
    phantom.setCustomName(ChatColor.RED + "Mother of All Phantoms" + ChatColor.RESET);
    PotionEffect speedFour = new PotionEffect(PotionEffectType.SPEED, 1000000, 3, true, false, false);
    phantom.addPotionEffect(speedFour);
    phantom.setMetadata(PhantomType.PHANTOM_TYPE_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), PhantomType.MOTHER_OF_ALL_PHANTOMS.toString()));
    (new MOAPBombs(phantom)).runTask(PhantomMain.getPlugin());
    return phantom;
  }

  /**
   * Summons an extra XP phantom at the specified location
   * @param loc  The location
   */
  public static Phantom summonExtraXPPhantom(Location loc) {
    Phantom phantom = (Phantom) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
    phantom.setMetadata(PhantomType.PHANTOM_TYPE_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), PhantomType.EXTRA_XP_PHANTOM.toString()));
    return phantom;
  }

  /**
   * Summons a flaming phantom at the specified location
   * @param loc  The location
   */
  public static Phantom summonFlamingPhantom(Location loc) {
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
    return phantom;
  }


  /**
   * Summons a mounted phantom at the specified location
   * @param loc  The location
   */
  public static Phantom summonMountedPhantom(Location loc, boolean event) {
    Phantom phantom = (Phantom) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
    PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 1000000, 0, true, false, false);
    phantom.addPotionEffect(speed);

    Skeleton skeleton = (Skeleton) loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
    phantom.setPassenger(skeleton);

    // Add metadata
    phantom.setMetadata(PhantomType.PHANTOM_TYPE_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), PhantomType.MOUNTED_PHANTOM.toString()));
    skeleton.setMetadata(PhantomType.PHANTOM_TYPE_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), PhantomType.MOUNTED_PHANTOM.toString()));

    if (event) {
      skeleton.setMetadata(PhantomEvent.EVENT_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), true));
    }
    return phantom;
  }

  /**
   * Summons a mounted phantom at the specified location
   * @param loc  The location
   */
  public static Phantom summonMountedPhantom(Location loc) {
    Phantom phantom = (Phantom) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
    PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 1000000, 0, true, false, false);
    phantom.addPotionEffect(speed);

    Skeleton skeleton = (Skeleton) loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
    phantom.setPassenger(skeleton);

    // Add metadata
    phantom.setMetadata(PhantomType.PHANTOM_TYPE_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), PhantomType.MOUNTED_PHANTOM.toString()));
    skeleton.setMetadata(PhantomType.PHANTOM_TYPE_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), PhantomType.MOUNTED_PHANTOM.toString()));

    return phantom;
  }

  public static Phantom summonInvisiblePhantom(Location loc) {
    Phantom phantom = (Phantom) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
    phantom.setMetadata(PhantomType.PHANTOM_TYPE_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), PhantomType.INVISIBLE_PHANTOM.toString()));
    TogglePhantomInvisibilityRunnable invisRunnable = new TogglePhantomInvisibilityRunnable(phantom);
		invisRunnable.runTask(PhantomMain.getPlugin());
    return phantom;
  }

  public static Phantom summonEnderPhantom(Location loc) {
    Phantom phantom = (Phantom) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
    phantom.setMetadata(PhantomType.PHANTOM_TYPE_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), PhantomType.ENDER_PHANTOM.toString()));
    return phantom;
  }
}
