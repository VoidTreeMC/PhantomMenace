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
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.block.Action;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Sheep;


import condor.item.CustomItem;
import condor.item.CustomItemType;
import condor.runnable.DoEnderBladeTeleport;
import condor.main.PhantomMain;

public class PrideShears extends CustomItem {

  private static final String NAME = "Pride Shears";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static ArrayList<Material> WOOL_TYPES = new ArrayList<>();

  private static Random rng = new Random();

  static {
    loreList.add("Pride Shears");
    loreList.add("Because the world needs more rainbows.");
    loreList.add("");
    loreList.add("10 void coins");

    triggerList.add(PlayerShearEntityEvent.class);

    WOOL_TYPES.add(Material.WHITE_WOOL);
    WOOL_TYPES.add(Material.ORANGE_WOOL);
    WOOL_TYPES.add(Material.MAGENTA_WOOL);
    WOOL_TYPES.add(Material.LIGHT_BLUE_WOOL);
    WOOL_TYPES.add(Material.YELLOW_WOOL);
    WOOL_TYPES.add(Material.LIME_WOOL);
    WOOL_TYPES.add(Material.PINK_WOOL);
    WOOL_TYPES.add(Material.GRAY_WOOL);
    WOOL_TYPES.add(Material.LIGHT_GRAY_WOOL);
    WOOL_TYPES.add(Material.CYAN_WOOL);
    WOOL_TYPES.add(Material.PURPLE_WOOL);
    WOOL_TYPES.add(Material.BLUE_WOOL);
    WOOL_TYPES.add(Material.BROWN_WOOL);
    WOOL_TYPES.add(Material.GREEN_WOOL);
    WOOL_TYPES.add(Material.RED_WOOL);
    WOOL_TYPES.add(Material.BLACK_WOOL);
  }

  public PrideShears() {
    super(NAME, loreList, triggerList, CustomItemType.PRIDE_SHEARS);
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.SHEARS, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName(NAME);
    meta.setLore(loreList);
    meta.addEnchant(Enchantment.MENDING, 1, false);
    meta.addEnchant(Enchantment.DURABILITY, 3, false);
    // meta.setUnbreakable(true);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    boolean ret = false;
    if (event instanceof PlayerShearEntityEvent) {
      PlayerShearEntityEvent psee = (PlayerShearEntityEvent) event;
      Player player = psee.getPlayer();
      // If they right-clicked a sheep
      if (psee.getEntity().getType() == EntityType.SHEEP) {
        if (isPrideShears(psee.getItem())) {
          Sheep sheep = (Sheep) psee.getEntity();
          ret = !sheep.isSheared();
        }
      }
    }
    return ret;
  }

  public void execute(Event event) {
    PlayerShearEntityEvent psee = (PlayerShearEntityEvent) event;
    Player player = psee.getPlayer();
    psee.setCancelled(true);
    ((Sheep) (psee.getEntity())).setSheared(true);
    Material woolType = WOOL_TYPES.get(rng.nextInt(WOOL_TYPES.size()));
    int woolAmt = rng.nextInt(5) + 3;
    ItemStack item = new ItemStack(woolType, woolAmt);
    player.getInventory().addItem(item);
  }

  public boolean isPrideShears(ItemStack item) {
    return (item != null) && (item.getType() == Material.SHEARS) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.PRIDE_SHEARS);
  }
}
