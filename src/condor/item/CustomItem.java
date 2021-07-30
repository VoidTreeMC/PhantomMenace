package condor.item;

import java.util.ArrayList;

import org.bukkit.event.Event;

import org.bukkit.inventory.ItemStack;

public abstract class CustomItem {
  private String name;
  private ArrayList<String> lore;
  private ArrayList<Class> triggers;
  private CustomItemType type;
  private boolean isEnchantable;

  protected CustomItem(String name, ArrayList<String> lore, ArrayList<Class> triggers, CustomItemType type) {
    this.name = name;
    this.lore = lore;
    this.triggers = triggers;
    this.type = type;
    this.isEnchantable = true;
  }

  protected CustomItem(String name, ArrayList<String> lore, ArrayList<Class> triggers, CustomItemType type, boolean enchantable) {
    this.name = name;
    this.lore = lore;
    this.triggers = triggers;
    this.type = type;
    this.isEnchantable = enchantable;
  }

  public String getName() {
    return this.name;
  }

  public boolean isEnchantable() {
    return this.isEnchantable;
  }

  /**
   * Returns the lore for the item
   * @return An ArrayList<String> containing the item's lore
   */
  public ArrayList<String> getLore() {
    return this.lore;
  }

  /**
   * Determines if the item's function should be
   * activated. If it should be, it activates it.
   * @param event  The relevant event
   */
  public void eval(Event event) {
    boolean isTriggerEvent = false;
    for (Class trigger : triggers) {
      if (event.getClass().equals(trigger) || trigger.isAssignableFrom(event.getClass())) {
        isTriggerEvent = true;
        break;
      }
    }
    boolean isNecessary = isNecessary(event);

    if (isTriggerEvent && isNecessary) {
      execute(event);
    }
  }

  /**
   * Gets the type of the custom item
   * @return The type of the custom item
   */
  public CustomItemType getType() {
    return this.type;
  }

  /**
   * Determines if the item's execution is necessary or not
   * based on the event
   * @param  event The triggering event
   * @return       True if is necessary, false if not
   */
  public abstract boolean isNecessary(Event event);

  /**
   * Performs the item's function
   * @param event  The relevant event
   */
  public abstract void execute(Event event);

  /**
   * Returns an instance of the custom item
   * @return An instance of the item
   */
  public abstract ItemStack getInstance();
}
