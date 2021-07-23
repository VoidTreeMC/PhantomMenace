package condor.listener.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Phantom;
import org.bukkit.Statistic;

import condor.listener.PHListener;
import condor.main.PhantomMain;
import condor.phantom.PhantomStatus;
import condor.phantom.PhantomDropHandler;
import condor.phantom.PhantomType;

/**
 *
 * Listens for Minecraft Events
 *
 * @author iron-condor
 */
public class EventListener  extends PHListener {

  @EventHandler
  public void onPhantomDeath(EntityDeathEvent event) {
    // If it's a phantom that was killed
    if (event.getEntityType() == EntityType.PHANTOM) {
      PhantomDropHandler.classifyAndDividePDE(event);
    }
  }


  @EventHandler
  public void onPhantomSpawn(EntitySpawnEvent event) {
    Entity spawnedEntity = event.getEntity();
    // If phantoms are disabled
    if (spawnedEntity.getType() == EntityType.PHANTOM && !PhantomStatus.isEnabled()) {
      event.setCancelled(true);
    }
  }

	/**
	 * Called when a {@link Player} right-clicks an {@link Entity} <p>
	 *
	 * Forwards to {@link CityEventProcessor#manageEvent(Object, org.bukkit.event.Event)}
	 *
	 * @param event {@link PlayerInteractEntityEvent}
	 */
	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent event) {

	}

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    Player player = event.getPlayer();
    player.sendMessage("" + player.getStatistic(Statistic.TIME_SINCE_REST));
    player.setStatistic(Statistic.TIME_SINCE_REST, 1000000);
  }

	/**
	 * Called when an {@link Entity} takes damage <p?
	 *
	 * WARNING: UNDER NO CIRCUMSTANCES DO YOU ADD ANY LISTENERS FOR CHILD EVENTS OF
	 * EntityDamageEvent
	 * @param event {@link EntityDamageEvent}
	 */
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
    managePossiblePhantomDeath(event);
    managePossiblePlayerDamagedByPhantom(event);
	}

  public void managePossiblePlayerDamagedByPhantom(EntityDamageEvent event) {
    if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      Entity entity = edbee.getEntity();
      if (entity.getType() == EntityType.PLAYER) {
        Entity damager = edbee.getDamager();
        if (damager.getType() == EntityType.PHANTOM) {
          Phantom phantom = (Phantom) damager;
          // If it's a flaming phantom
          PhantomType phantomType = PhantomType.getTypeFromPhantom(phantom);
          if (phantomType == PhantomType.EVENT_LEVEL_TWO) {
            final int THREE_SECONDS = 20 * 3;
            entity.setFireTicks(THREE_SECONDS);
          }
        }
      }
    }
  }

  public void managePossiblePhantomDeath(EntityDamageEvent event) {
    if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      Entity entity = edbee.getEntity();
      if (entity.getType() == EntityType.PHANTOM) {
        Phantom phantom = (Phantom) entity;
        Entity damager = edbee.getDamager();
        boolean isPlayer = false;
        Player player = null;
        if (damager instanceof Player) {
          isPlayer = true;
          player = (Player) damager;
        } else if (damager instanceof Projectile) {
          Projectile proj = (Projectile) damager;
          if (proj.getShooter() instanceof Player) {
            player = (Player) proj.getShooter();
            isPlayer = true;
          }
        }
        boolean isDead = (phantom.getHealth() - edbee.getFinalDamage()) <= 0;
        // If the phantom is dead and the killer is a player
        if (isDead && isPlayer) {
          PhantomDropHandler.classifyAndAwardXP(edbee, player);
        }
      }
    }
  }
}
