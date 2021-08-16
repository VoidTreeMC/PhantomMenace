package condor.item.legendaryitems;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.enchantments.Enchantment;

import condor.item.CustomItem;
import condor.item.CustomItemType;
import condor.runnable.DoEnderBladeTeleport;
import condor.main.PhantomMain;

public class EnderBlade extends CustomItem {

  /**
   * TODO: Make it extra effective vs endermen and endermites
   *       Make it weaker (or not work) in water/rain
   */


  private static final String NAME = "Ender Blade";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  // 15 second cooldown
  private static final long COOLDOWN_DURATION = 15 * 1000;

  private static TreeMap<UUID, Long> mapOfTimesUsed = new TreeMap<>();

  private static Random rng = new Random();

  static {
    loreList.add("Ender Blade");
    loreList.add("The Quacken battled endless hordes");
    loreList.add("of the tall demons while retreating to");
    loreList.add("VoidTree. Without this sword, all would");
    loreList.add("have been lost.");
    loreList.add("");

    triggerList.add(PlayerInteractEvent.class);
  }

  public EnderBlade() {
    super(NAME, loreList, triggerList, CustomItemType.ENDER_BLADE, 50);
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.NETHERITE_SWORD, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName(NAME);
    loreList.add(this.getPrice() + " VoidCoins");
    meta.setLore(loreList);
    meta.addEnchant(Enchantment.MENDING, 1, false);
    meta.addEnchant(Enchantment.DURABILITY, 3, false);
    // meta.setUnbreakable(true);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    boolean ret = false;
    if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      // If they right-clicked
      if (pie.getAction() == Action.RIGHT_CLICK_AIR ||
          pie.getAction() == Action.RIGHT_CLICK_BLOCK) {
        // If they're holding an ender blade
        if (isEnderBlade(player.getItemInHand())) {
          ret = true;
        }
      }
    }
    return ret;
  }

  public void execute(Event event) {
    PlayerInteractEvent pie = (PlayerInteractEvent) event;
    Player player = pie.getPlayer();
    long currTime = System.currentTimeMillis();
    long lastTimeUsed = 0;
    if (mapOfTimesUsed.containsKey(player.getUniqueId())) {
      lastTimeUsed = mapOfTimesUsed.get(player.getUniqueId());
    }
    // If it's off cooldown
    if ((currTime - lastTimeUsed) >= COOLDOWN_DURATION) {
      (new DoEnderBladeTeleport(player)).runTask(PhantomMain.getPlugin());
      mapOfTimesUsed.put(player.getUniqueId(), currTime);
    } else {
      player.sendMessage("The blade vibrates weakly. It must recharge.");
    }
  }

  public boolean isEnderBlade(ItemStack item) {
    return (item != null) && (item.getType() == Material.NETHERITE_SWORD) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.ENDER_BLADE);
  }
}
