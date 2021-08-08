package condor.phantom;

import java.util.List;
import java.util.TreeMap;

import org.bukkit.entity.Phantom;
import org.bukkit.metadata.MetadataValue;

public enum PhantomType {
  VANILLA,
  EXTRA_XP_PHANTOM,
  FLAMING_PHANTOM,
  INVISIBLE_PHANTOM,
  MOUNTED_PHANTOM,
  ENDER_PHANTOM,
  MOTHER_OF_ALL_PHANTOMS;

  private static TreeMap<String, PhantomType> PHANTOM_TYPES;

  /**
	 * Gets the tree map of phantom types.
	 * If it is not yet constructed, it constructs it
	 * @return A TreeMap that maps strings to evidence types
	 */
  public static final TreeMap<String, PhantomType> phantomTypes() {
		if (PHANTOM_TYPES != null) {
			return PHANTOM_TYPES;
		}

		PHANTOM_TYPES = new TreeMap<>();

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
