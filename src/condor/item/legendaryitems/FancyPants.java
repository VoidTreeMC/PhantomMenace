package condor.item.legendaryitems;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Entity;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Event;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import condor.item.CustomItem;
import condor.item.CustomItemType;

public class FancyPants extends CustomItem {

  private static final String NAME = "Fancy Pants";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static Random rng = new Random();

  static {
    loreList.add("Fancy Pants");
    loreList.add("Gift of the Glam God.");
    loreList.add("Wear these and walk between the raindrops.");
    loreList.add("");

    triggerList.add(EntityDamageEvent.class);
  }

  public FancyPants() {
    super(NAME, loreList, triggerList, CustomItemType.FANCY_PANTS, 25);
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.GOLDEN_LEGGINGS, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName(NAME);
    loreList.add(this.getPrice() + " VoidCoins");
    meta.setLore(loreList);
    meta.setUnbreakable(true);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    boolean ret = false;
    if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      Entity entity = edbee.getEntity();
      Entity damager = edbee.getDamager();
      if (entity.getType() == EntityType.PLAYER && damager instanceof Projectile) {
        Player player = (Player) entity;
        boolean hasLeggings = player.getInventory().getLeggings() != null;
        ret = hasLeggings && CustomItemType.getTypeFromCustomItem(player.getInventory().getLeggings()) == CustomItemType.FANCY_PANTS;
      }
    }
    return ret;
  }

  public void execute(Event event) {
    EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
    if (rng.nextInt(10) <= 3) {
      edbee.setCancelled(true);
      Entity entity = edbee.getEntity();
      Location loc = entity.getLocation();
      loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.MUSIC, 1, 1);
    }
  }
}
