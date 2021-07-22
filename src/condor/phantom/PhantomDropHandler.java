package condor.phantom;

import java.util.Random;
import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;

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
   * Classifies the phantom death event based on the
   * type of phantom that was killed, and calls the
   * appropriate method.
   * @param event The EntityDeathEvent that pertains to the phantom's death
   */
  public static void classifyAndDividePDE(EntityDeathEvent event) {
    // If the phantom was a vanila phantom
    // TODO: Add phantom type detection via metadata here.
    // Won't become relevant until events are coded.
    PhantomType type = PhantomType.VANILLA;

    switch (type) {
      case VANILLA:
        handleVanillaPhantom(event);
        break;
      // Subsequent levels are presently disabled until such time that they exist
      // case EVENT_LEVEL_ONE:
      //   handleEventLevelOne(event);
      //   break;
      // case EVENT_LEVEL_TWO:
      //   handleEventLevelTwo(event);
      //   break;
      // case EVENT_LEVEL_THREE:
      //   handleEventLevelThree(event);
      //   break;
      // case EVENT_LEVEL_FOUR:
      //   handleEventLevelFour(event);
      //   break;
      // case EVENT_LEVEL_FIVE:
      //   handleEventLevelFive(event);
      //   break;
    }
  }

  public static void classifyAndAwardXP(EntityDamageByEntityEvent event, Player player) {
    // If the phantom was a vanila phantom
    // TODO: Add phantom type detection via metadata here.
    // Won't become relevant until events are coded.
    PhantomType type = PhantomType.VANILLA;
    int xpAmt = PhantomDropTable.getXP(type);

    switch (type) {
      case VANILLA:
        player.giveExp(xpAmt);
        break;
      // Subsequent levels are presently disabled until such time that they exist
      // case EVENT_LEVEL_ONE:
      //   handleEventLevelOne(event);
      //   break;
      // case EVENT_LEVEL_TWO:
      //   handleEventLevelTwo(event);
      //   break;
      // case EVENT_LEVEL_THREE:
      //   handleEventLevelThree(event);
      //   break;
      // case EVENT_LEVEL_FOUR:
      //   handleEventLevelFour(event);
      //   break;
      // case EVENT_LEVEL_FIVE:
      //   handleEventLevelFive(event);
      //   break;
    }
  }
}
