package condor.runnable;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Entity;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import condor.main.PhantomMain;

public class MakePhantomGlow extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The phantom whose target is to be updated
  Phantom phantom;

	public MakePhantomGlow(Phantom phantom) {
		this.plugin = PhantomMain.getPlugin();
		this.phantom = phantom;
	}

	/**
	 * Make the phantom glow
	 */
	@Override
	public void run() {
    // If the entity is dead, we are done.
    if (this.phantom.isDead()) {
      this.cancel();
      return;
    }

    PotionEffect glowEffect = new PotionEffect(PotionEffectType.GLOWING, 1000000, 0, true, false, false);
    this.phantom.addPotionEffect(glowEffect);

    this.cancel();
	}
}
