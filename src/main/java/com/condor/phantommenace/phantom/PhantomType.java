package com.condor.phantommenace.phantom;

import java.util.List;
import java.util.HashMap;

import org.bukkit.entity.Phantom;
import org.bukkit.metadata.MetadataValue;

public enum PhantomType {
  VANILLA,
  EXTRA_XP_PHANTOM,
  FLAMING_PHANTOM,
  INVISIBLE_PHANTOM,
  MOUNTED_PHANTOM,
  ENDER_PHANTOM,
  MOTHER_OF_ALL_PHANTOMS,
  KAMIKAZE_PHANTOM,
  HEALER_PHANTOM;

  private static HashMap<String, PhantomType> PHANTOM_TYPES;

  /**
	 * Gets the tree map of phantom types.
	 * If it is not yet constructed, it constructs it
	 * @return A HashMap that maps strings to evidence types
	 */
  public static final HashMap<String, PhantomType> phantomTypes() {
		if (PHANTOM_TYPES != null) {
			return PHANTOM_TYPES;
		}

		PHANTOM_TYPES = new HashMap<>();

		for (PhantomType c : PhantomType.values()) {
			PHANTOM_TYPES.put(c.name(), c);
		}

		return PHANTOM_TYPES;
	}

  /**
	 * Gets the PhantomType enum by its name
	 * @param name The name of the PhantomType
	 * @return The PhantomType enum by that name
	 */
	public static PhantomType fromString(String name) {
		return phantomTypes().get(name.toUpperCase());
	}

  public static final String PHANTOM_TYPE_METADATA_KEY = "phantomType";

  public static PhantomType getTypeFromPhantom(Phantom phantom) {
    if (!phantom.hasMetadata(PHANTOM_TYPE_METADATA_KEY)) {
      return VANILLA;
    }

    List<MetadataValue> metadata = phantom.getMetadata(PHANTOM_TYPE_METADATA_KEY);
    String typeStr = metadata.get(0).asString();
    PhantomType type = fromString(typeStr);
    return type;
  }
}
