package condor.phantom;

import java.util.Random;
import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Phantom;

/**
 * Class used to control Phantom drops.
 * This can be used to handle event drops, XP drops, etc.
 */
public class PhantomDropHandler {

  private static Random rng = new Random();

  /**
   * Handles the drops for a vanilla phantom death.
   * A vanilla phantom is a normal phantom that does not have
   * any modifications, and was spawned outside of an event.
   * @param event The EntityDeathEvent that pertains to the phantom's death
   */
  public static void handleVanillaPhantom(EntityDeathEvent event) {
    // Random number for determining what to drop
    double randomNum = rng.nextDouble();
    // The items to drop
    ArrayList<ItemStack> newDrops = PhantomDropTable.getDrops(PhantomType.VANILLA, randomNum);

    // Set the drop list to our desired drops
    event.getDrops().clear();
    event.getDrops().addAll(newDrops);

    // Set XP to 0
    event.setDroppedExp(0);
  }

  /**
   * Handles the drops for a flaming phantom death.
   * A flaming phantom is a flaming phantom that has
   * strength, does not take fire damage, and drops glowstone
   * and additional XP
   * @param event The EntityDeathEvent that pertains to the phantom's death
   */
  public static void handleFlamingPhantom(EntityDeathEvent event) {
    // Random number for determining what to drop
    double randomNum = rng.nextDouble();
    // The items to drop
    ArrayList<ItemStack> newDrops = PhantomDropTable.getDrops(PhantomType.FLAMING_PHANTOM, randomNum);

    // Set the drop list to our desired drops
    event.getDrops().clear();
    event.getDrops().addAll(newDrops);

    // Set XP to 0
    event.setDroppedExp(0);
  }

  /**
   * Handles the drops for a mounted phantom death.
   * A mounted phantom is a phantom that is being ridden by
   * a skeleton, and has speed.
   * @param event The EntityDeathEvent that pertains to the phantom's death
   */
  public static void handleMountedPhantom(EntityDeathEvent event) {
    // Random number for determining what to drop
    double randomNum = rng.nextDouble();
    // The items to drop
    ArrayList<ItemStack> newDrops = PhantomDropTable.getDrops(PhantomType.MOUNTED_PHANTOM, randomNum);

    // Set the drop list to our desired drops
    event.getDrops().clear();
    event.getDrops().addAll(newDrops);

    // Set XP to 0
    event.setDroppedExp(0);
  }

  /**
   * Classifies the phantom death event based on the
   * type of phantom that was killed, and calls the
   * appropriate method.
   * @param event The EntityDeathEvent that pertains to the phantom's death
   */
  public static void classifyAndDividePDE(EntityDeathEvent event) {
    Phantom phantom = (Phantom) event.getEntity();
    PhantomType type = PhantomType.getTypeFromPhantom(phantom);
    int xpAmt = PhantomDropTable.getXP(type);

    switch (type) {
      case VANILLA:
        handleVanillaPhantom(event);
        break;
      case FLAMING_PHANTOM:
        handleFlamingPhantom(event);
        break;
      case MOUNTED_PHANTOM:
        handleMountedPhantom(event);
        break;
      // Subsequent levels are presently disabled until such time that they exist
      // case EXTRA_XP_PHANTOM:
      //   handleEventextraXPPhantom(event);
      //   break;
      // case INVISIBLE_PHANTOM:
      //   handleEventinvisiblePhantom(event);
      //   break;
      // case ENDER_PHANTOM:
      //   handleEventenderPhantom(event);
      //   break;
    }
  }

  public static void classifyAndAwardXP(EntityDamageByEntityEvent event, Player player) {
    Phantom phantom = (Phantom) event.getEntity();
    PhantomType type = PhantomType.getTypeFromPhantom(phantom);
    int xpAmt = PhantomDropTable.getXP(type);

    player.giveExp(xpAmt);
  }
}
