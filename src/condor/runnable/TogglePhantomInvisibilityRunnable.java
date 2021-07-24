package condor.runnable;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Phantom;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import condor.main.PhantomMain;

public class TogglePhantomInvisibilityRunnable extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // Whether or not the phantom is invisible
  static boolean isInvis = false;

  // The phantom
  Phantom phantom;

  private static PotionEffect invisEffect = new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 0, true, false, false);

	/**
	* Builds the BukkitRunnable
	* @param phantom The phantom
	*/
	public TogglePhantomInvisibilityRunnable(Phantom phantom) {
		this.plugin = PhantomMain.getPlugin();
		this.phantom = phantom;
	}

	/**
	 * Toggles the phantom's invisibility on a timer
	 */
	@Override
	public void run() {
    // If the phantom is dead, we are done.
    if (this.phantom.isDead()) {
      this.cancel();
      return;
    }

    // Otherwise, the phantom is alive, and we should continue
    if (isInvis) {
      // Clear its invis effect
      phantom.removePotionEffect(PotionEffectType.INVISIBILITY);
    } else {
      // Make it invis
      phantom.addPotionEffect(invisEffect);
    }

    isInvis = !isInvis;

    // Wait 5 seconds, and do the same thing again
    (new TogglePhantomInvisibilityRunnable(this.phantom)).runTaskLater(this.plugin, 20 * 5);
    this.cancel();
	}
}
