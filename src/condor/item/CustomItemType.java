package condor.item;

import java.util.List;
import java.util.TreeMap;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum CustomItemType {
  INSOMNIA_POTION,
  FANCY_PANTS,
  DEFENDER_TOKEN,
  ENDER_BLADE;

  private static TreeMap<String, CustomItemType> CUSTOM_ITEM_TYPES;

  /**
	 * Gets the tree map of customItem types.
	 * If it is not yet constructed, it constructs it
	 * @return A TreeMap that maps strings to evidence types
	 */
  public static final TreeMap<String, CustomItemType> customItemTypes() {
		if (CUSTOM_ITEM_TYPES != null) {
			return CUSTOM_ITEM_TYPES;
		}

		CUSTOM_ITEM_TYPES = new TreeMap<>();

		for (CustomItemType c : CustomItemType.values()) {
			CUSTOM_ITEM_TYPES.put(c.name(), c);
		}

		return CUSTOM_ITEM_TYPES;
	}

  /**
	 * Gets the CustomItemType enum by its name
	 * @param name The name of the CustomItemType
	 * @return The CustomItemType enum by that name
	 */
	public static CustomItemType fromString(String name) {
		return customItemTypes().get(name.toUpperCase());
	}

  public static final String CUSTOM_ITEM_TYPE_METADATA_KEY = "customItemType";

  public static CustomItemType getTypeFromCustomItem(ItemStack customItem) {
    ItemMeta meta = customItem.getItemMeta();
    if (!meta.hasLore()) {
      return null;
    }
    List<String> lore = meta.getLore();
    String firstLineOfLore = lore.get(0);
    switch (firstLineOfLore) {
      case "Insomnia Potion":
        return INSOMNIA_POTION;
      case "Fancy Pants":
        return FANCY_PANTS;
      case "Defender Token":
        return DEFENDER_TOKEN;
      case "Ender Blade":
        return ENDER_BLADE;
      default:
        return null;
    }
  }
}
