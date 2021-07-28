package condor.runnable;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Phantom;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.LivingEntity;

import condor.main.PhantomMain;

public class DoDisableTargetAI extends BukkitRunnable {

	//The plugin
	JavaPlugin plugin;

  // The entity whose AI is to be disabled
  LivingEntity entity;

  // The duration for which it is to be disabled
  int duration;

	public DoDisableTargetAI(LivingEntity entity, int duration) {
		this.plugin = PhantomMain.getPlugin();
		this.entity = entity;
    this.duration = duration;
	}

	/**
	 * Disables the entity's AI for the set duration
	 */
	@Override
	public void run() {
    // If the entity is dead, we are done.
    if (this.entity.isDead()) {
      this.cancel();
      return;
    }

    this.entity.setAI(false);

    // Wait the duration, then re-enable it
    (new DoEnableTargetAI(this.entity)).runTaskLater(this.plugin, 20 * this.duration);
    this.cancel();
	}
}
