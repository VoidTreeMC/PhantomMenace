package condor.item;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItemGenerator {
  public static ItemStack getInsomniaPotion() {
    ItemStack is = new ItemStack(Material.POTION, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName("Insomnia Potion");
    ArrayList<String> loreList = new ArrayList<>();
    loreList.add("Insomnia Potion");
    loreList.add("Phantom-roast coffee.");
    loreList.add("");
    loreList.add("For fighting the menace.");
    meta.setLore(loreList);
    is.setItemMeta(meta);

    return is;
  }
}
