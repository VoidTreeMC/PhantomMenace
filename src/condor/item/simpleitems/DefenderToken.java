package condor.item.simpleitems;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.event.Event;

import condor.item.CustomItem;
import condor.item.CustomItemType;

public class DefenderToken extends CustomItem {

  private static final String NAME = "VoidCoin";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static Random rng = new Random();

  static {
    loreList.add("VoidCoin");
    loreList.add("Awarded to defenders of the realm.");
  }

  public DefenderToken() {
    super(NAME, loreList, triggerList, CustomItemType.DEFENDER_TOKEN, 0);
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.SUNFLOWER, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName(NAME);
    meta.addEnchant(Enchantment.DURABILITY, 1, false);
    meta.setLore(loreList);
    meta.setUnbreakable(true);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    return false;
  }

  public void execute(Event event) {

  }

  public static boolean isDefenderToken(ItemStack item) {
    return (item != null) && (item.getType() == Material.SUNFLOWER) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.DEFENDER_TOKEN);
  }
}
