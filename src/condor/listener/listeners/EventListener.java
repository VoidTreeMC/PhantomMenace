package condor.listener.listeners;

import java.util.Random;

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
import org.bukkit.entity.SpectralArrow;
import org.bukkit.inventory.ItemStack;
import org.bukkit.SoundCategory;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerShearEntityEvent;
import java.util.UUID;

import condor.listener.PHListener;
import condor.main.PhantomMain;
import condor.phantom.PhantomStatus;
import condor.phantom.PhantomDropHandler;
import condor.phantom.PhantomType;
import condor.runnable.DoPhantomBlinkRunnable;
import condor.item.CustomItemType;
import condor.gui.PhantomShopGUI;
import condor.item.CustomItemGenerator;
import condor.item.CustomItemEventManager;
import condor.npc.PHNPC;
import condor.npc.NPCManager;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.event.PlayerNPCEvent;
import com.github.juliarn.npc.event.PlayerNPCInteractEvent;
import com.github.juliarn.npc.event.PlayerNPCShowEvent;
import com.github.juliarn.npc.modifier.EquipmentModifier;
import com.github.juliarn.npc.modifier.MetadataModifier;
import com.github.juliarn.npc.modifier.NPCModifier;

import com.comphenix.protocol.wrappers.EnumWrappers;

/**
 *
 * Listens for Minecraft Events
 *
 * @author iron-condor
 */
public class EventListener  extends PHListener {

  private static final Random rng = new Random();

  @EventHandler
  public void onPlayerShearEntityEvent(PlayerShearEntityEvent event) {
    CustomItemEventManager.parseEvent(event);
  }

  @EventHandler
  public void onPlayerInteractEvent(PlayerInteractEvent event) {
    CustomItemEventManager.parseEvent(event);
  }

  @EventHandler
  public void onPhantomDeath(EntityDeathEvent event) {
    // If it's a phantom that was killed
    if (event.getEntityType() == EntityType.PHANTOM) {
      PhantomDropHandler.classifyAndDividePDE(event);
    }
  }

  @EventHandler
  public void onItemConsume(PlayerItemConsumeEvent event) {
    ItemStack item = event.getItem();
    CustomItemType type = CustomItemType.getTypeFromCustomItem(item);
    // If the player has drank an insomnia potion
    if (type == CustomItemType.INSOMNIA_POTION) {
      // Give them insomnia
      Player player = event.getPlayer();
      player.sendMessage("Your eyelids feel heavy. It feels as though you haven't slept in a very long time.");
      player.setStatistic(Statistic.TIME_SINCE_REST, 1000000);
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
   * Doing something when a NPC is shown for a certain player.
   * Alternatively, {@link NPC.Builder#spawnCustomizer(SpawnCustomizer)} can be used.
   *
   * @param event The event instance
   */
  @EventHandler
  public void handleNPCShow(PlayerNPCShowEvent event) {
    NPC npc = event.getNPC();
    UUID npcUUID = npc.getProfile().getUniqueId();
    PHNPC phnpc = NPCManager.getPHNPCFromUUID(npcUUID);
    NPCModifier equipmentModifier = phnpc.getEquipmentModifier();

    // sending the data only to the player from the event
    event.send(
      // equipping the NPC with its equipment
      equipmentModifier,
      // enabling the skin layers
      npc.metadata()
        .queue(MetadataModifier.EntityMetadata.SKIN_LAYERS, true)
    );
  }

  /**
   * Doing something when a player interacts with a NPC.
   *
   * @param event The event instance
   */
  @EventHandler
  public void handleNPCInteract(PlayerNPCInteractEvent event) {
    Player player = event.getPlayer();
    NPC npc = event.getNPC();
    UUID npcUUID = npc.getProfile().getUniqueId();
    PHNPC phnpc = NPCManager.getPHNPCFromUUID(npcUUID);
    phnpc.handleInteraction(event);
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

  // @EventHandler
  // public void onBlockBreak(BlockBreakEvent event) {
  //   Player player = event.getPlayer();
  //   player.sendMessage("" + player.getStatistic(Statistic.TIME_SINCE_REST));
  //   player.setStatistic(Statistic.TIME_SINCE_REST, 1000000);
  // }

	/**
	 * Called when an {@link Entity} takes damage <p?
	 *
	 * WARNING: DO NOT ADD ANY LISTENERS FOR CHILD EVENTS OF
	 * EntityDamageEvent
	 * @param event {@link EntityDamageEvent}
	 */
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
    CustomItemEventManager.parseEvent(event);
    managePhantomDamaged(event);
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
          if (phantomType == PhantomType.FLAMING_PHANTOM) {
            final int THREE_SECONDS = 20 * 3;
            entity.setFireTicks(THREE_SECONDS);
          }
        }
      }
    }
  }

  public void managePhantomDamaged(EntityDamageEvent event) {
    if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      Entity entity = edbee.getEntity();
      if (entity.getType() == EntityType.PHANTOM) {
        Phantom phantom = (Phantom) entity;
        Entity damager = edbee.getDamager();
        boolean isPlayer = false;
        Player player = null;

        boolean isSpectralArrow = false;

        if (damager instanceof Player) {
          isPlayer = true;
          player = (Player) damager;
        } else if (damager instanceof Projectile) {
          isSpectralArrow = (damager instanceof SpectralArrow);
          Projectile proj = (Projectile) damager;
          if (proj.getShooter() instanceof Player) {
            player = (Player) proj.getShooter();
            isPlayer = true;
          }
        }

        // If it's an invisible phantom being shot by a spectral arrow, increase the damage.
        if (PhantomType.getTypeFromPhantom(phantom) == PhantomType.INVISIBLE_PHANTOM && isSpectralArrow) {
          edbee.setDamage(edbee.getDamage() * 1.5);
        }

        // If it's an ender phantom that is being damaged, make it blink
        if (PhantomType.getTypeFromPhantom(phantom) == PhantomType.ENDER_PHANTOM) {
          // Make the phantom blink
          (new DoPhantomBlinkRunnable(phantom)).runTask(PhantomMain.getPlugin());
        }

        boolean isDead = (phantom.getHealth() - edbee.getFinalDamage()) <= 0;
        // If the phantom is dead and the killer is a player
        if (isDead && isPlayer) {
          PhantomDropHandler.classifyAndAwardXP(edbee, player);
          PhantomDropHandler.classifyAndAwardTokens(edbee, player);
        }
      }
    }
  }
}
