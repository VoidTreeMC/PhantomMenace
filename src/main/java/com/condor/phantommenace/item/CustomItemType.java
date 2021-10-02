package com.condor.phantommenace.item;

import java.util.List;
import java.util.TreeMap;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum CustomItemType {
  INSOMNIA_POTION,
  FANCY_PANTS,
  DEFENDER_TOKEN,
  ENDER_BLADE,
  PRIDE_SHEARS,
  CREEPER_BOW,
  LAVA_WALKERS,
  CREEPER_FIREWORK,
  SUPER_PICK,
  SLAYER_SWORD,
  FLIGHT_POTION,
  SUBDUED_ENDER_BLADE,
  ZOMBIE_EGG,
  FOX_EGG,
  SPIDER_EGG,
  COPPER_VOUCHER,
  IRON_VOUCHER,
  GOLD_VOUCHER,
  ALMOND_CAKE;

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

  public static boolean isLegendaryItem(ItemStack customItem) {
    CustomItemType type = getTypeFromCustomItem(customItem);
    return isLegendaryItem(type);
  }

  public static boolean isLegendaryItem(CustomItemType type) {
    return type != null && type != DEFENDER_TOKEN && type != INSOMNIA_POTION && type != CREEPER_FIREWORK && type != FLIGHT_POTION;
  }

  public static CustomItemType getTypeFromCustomItem(ItemStack customItem) {
    if (customItem == null || !customItem.hasItemMeta()) {
      return null;
    }
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
      case "VoidCoin":
        return DEFENDER_TOKEN;
      case "Ender Blade":
        return ENDER_BLADE;
      case "Subdued Ender Blade":
        return SUBDUED_ENDER_BLADE;
      case "Pride Shears":
        return PRIDE_SHEARS;
      case "Creeper Bow":
      case "CreeperBane":
      case "Creeper Bane":
        return CREEPER_BOW;
      case "Lava Walkers":
        return LAVA_WALKERS;
      case "Creeper Firework":
        return CREEPER_FIREWORK;
      case "Super Pick":
      case "TAB":
        return SUPER_PICK;
      case "Slayer Sword":
        return SLAYER_SWORD;
      case "Flight Potion":
        return FLIGHT_POTION;
      case "Copper Voucher":
        return COPPER_VOUCHER;
      case "Iron Voucher":
        return IRON_VOUCHER;
      case "Gold Voucher":
        return GOLD_VOUCHER;
      case "Almond Cake":
        return ALMOND_CAKE;
      default:
        return null;
    }
  }
}
