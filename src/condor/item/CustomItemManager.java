package condor.item;

import java.util.TreeMap;

import org.bukkit.event.Event;

import condor.item.legendaryitems.*;
import condor.item.simpleitems.*;

public class CustomItemManager {
  private static TreeMap<CustomItemType, CustomItem> itemMap = new TreeMap<>();

  // Add custom items here
  static {
    // itemMap.put(CustomItemType.INSOMNIA_POTION, InsomniaPotion);
    itemMap.put(CustomItemType.FANCY_PANTS, new FancyPants());
    itemMap.put(CustomItemType.DEFENDER_TOKEN, new DefenderToken());
  }

  public static TreeMap<CustomItemType, CustomItem> getMap() {
    return itemMap;
  }

  public static CustomItem getItemByType(CustomItemType type) {
    return itemMap.get(type);
  }
}
