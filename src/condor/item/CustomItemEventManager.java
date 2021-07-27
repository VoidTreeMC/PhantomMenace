package condor.item;

import java.util.Map.Entry;

import org.bukkit.event.Event;

public class CustomItemEventManager {
  /**
   * Checks with each custom item to see if
   * the event is relevant, and lets the
   * item parse the event if it is
   * @param event  The event to be parsed
   */
  public static void parseEvent(Event event) {
    for (Entry<CustomItemType, CustomItem> itemMap : CustomItemManager.getMap().entrySet()) {
      itemMap.getValue().eval(event);
    }
  }
}
