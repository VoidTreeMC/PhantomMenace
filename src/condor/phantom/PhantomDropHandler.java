package condor.phantom;

import java.util.Random;
import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Phantom;

import condor.item.CustomItemManager;
import condor.item.CustomItemType;

/**
 * Class used to control Phantom drops.
 * This can be used to handle event drops, XP drops, etc.
 */
public class PhantomDropHandler {

  private static Random rng = new Random();

  /**
   * Handles drops for a generic phantom.
   * A generic phantom is defined as a phantom that doesn't have any
   * special death or drop behavior that is defined outside of its
   * drop table.
   * @param event  The phantom's death event
   * @param type   The type of phantom that has died
   */
  public static void handleGenericPhantomDrops(EntityDeathEvent event, PhantomType type) {
    // Random number for determining what to drop
    double randomNum = rng.nextDouble();
    // The items to drop
    ArrayList<ItemStack> newDrops = PhantomDropTable.getDrops(type, randomNum);

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
        handleGenericPhantomDrops(event, PhantomType.VANILLA);
        break;
      case EXTRA_XP_PHANTOM:
        handleGenericPhantomDrops(event, PhantomType.EXTRA_XP_PHANTOM);
        break;
      case FLAMING_PHANTOM:
        handleGenericPhantomDrops(event, PhantomType.FLAMING_PHANTOM);
        break;
      case MOUNTED_PHANTOM:
        handleGenericPhantomDrops(event, PhantomType.MOUNTED_PHANTOM);
        break;
      case INVISIBLE_PHANTOM:
        handleGenericPhantomDrops(event, PhantomType.INVISIBLE_PHANTOM);
        break;
      case ENDER_PHANTOM:
        handleGenericPhantomDrops(event, PhantomType.ENDER_PHANTOM);
        break;
      // Subsequent levels are presently disabled until such time that they exist
    }
  }

  /**
   * Determines the type of phantom and assigns XP based on the phantom type
   * @param event   The entity damaged by entity event
   * @param player  The player that slayed the phantom, to whom XP is awarded
   */
  public static void classifyAndAwardXP(EntityDamageByEntityEvent event, Player player) {
    Phantom phantom = (Phantom) event.getEntity();
    PhantomType type = PhantomType.getTypeFromPhantom(phantom);
    int xpAmt = PhantomDropTable.getXP(type);

    player.giveExp(xpAmt);
  }

  /**
   * Determines the type of phantom and drops tokens based on the phantom type
   * @param event   The entity damaged by entity event
   * @param player  The player that slayed the phantom, to whom the tokens are awarded
   */
  public static void classifyAndAwardTokens(EntityDamageByEntityEvent event, Player player) {
    Phantom phantom = (Phantom) event.getEntity();
    PhantomType type = PhantomType.getTypeFromPhantom(phantom);
    int tokenAmt = PhantomDropTable.getNumTokens(type);

    if (tokenAmt > 0) {
      player.getInventory().addItem(CustomItemManager.getItemByType(CustomItemType.DEFENDER_TOKEN).getInstance());
    }
  }
}
