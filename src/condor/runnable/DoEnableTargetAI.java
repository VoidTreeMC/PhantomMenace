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

public class DoEnableTargetAI extends BukkitRunnable {

  // TODO: Maybe make it check to see if there isnt another disable job set for this entity

	//The plugin
	JavaPlugin plugin;

  // The entity whose AI is to be eabled
  LivingEntity entity;

	public DoEnableTargetAI(LivingEntity entity) {
		this.plugin = PhantomMain.getPlugin();
		this.entity = entity;
	}

	/**
	 * Enables the entity's AI
	 */
	@Override
	public void run() {
    // If the entity is dead, we are done.
    if (this.entity.isDead()) {
      this.cancel();
      return;
    }

    this.entity.setAI(true);
    this.cancel();
	}
}
