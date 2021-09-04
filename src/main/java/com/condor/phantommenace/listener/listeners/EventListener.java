package com.condor.phantommenace.listener.listeners;

import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockShearEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.PlayerDeathEvent;
import io.papermc.paper.event.block.PlayerShearBlockEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import com.condor.phantommenace.listener.PHListener;
import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.phantom.PhantomStatus;
import com.condor.phantommenace.phantom.PhantomDropHandler;
import com.condor.phantommenace.phantom.PhantomType;
import com.condor.phantommenace.runnable.DoPhantomBlinkRunnable;
import com.condor.phantommenace.runnable.DeleteIfIsWild;
import com.condor.phantommenace.runnable.DeleteIfIsWildSkeleton;
import com.condor.phantommenace.runnable.ManageDeathImmunity;
import com.condor.phantommenace.runnable.SpawnSilverfishAtLoc;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.gui.PhantomShopGUI;
import com.condor.phantommenace.item.CustomItemGenerator;
import com.condor.phantommenace.item.CustomItemEventManager;
import com.condor.phantommenace.item.CustomItemManager;
import com.condor.phantommenace.npc.PHNPC;
import com.condor.phantommenace.npc.NPCManager;
import com.condor.phantommenace.event.PhantomEvent;
import com.condor.phantommenace.phantom.RecentPlayerDeaths;

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
  public void onFireworkExplodeEvent(FireworkExplodeEvent event) {
    CustomItemEventManager.parseEvent(event);
  }

  @EventHandler
  public void onInventoryClickEvent(InventoryClickEvent event) {
    boolean doCancel = false;
    Player player = null;
    if (event.getWhoClicked() instanceof Player) {
      player = (Player) event.getWhoClicked();
    }
    Inventory relevantInventory = event.getInventory();
    if (event.isShiftClick()) {
      relevantInventory = event.getView().getTopInventory();
    }
    if (relevantInventory.getType() == InventoryType.ANVIL ||
        relevantInventory.getType() == InventoryType.ENCHANTING ||
        relevantInventory.getType() == InventoryType.GRINDSTONE) {
      ItemStack item = null;
      if (event.isShiftClick()) {
        item = event.getCurrentItem();
      } else {
        item = event.getCursor();
      }
      if (item != null) {
        CustomItemType type = CustomItemType.getTypeFromCustomItem(item);
        if (type != null) {
          // If it's not enchantable, cancel the event
          if (!CustomItemManager.getItemByType(type).isEnchantable()) {
            event.setCancelled(true);
          }
        }
      }
    }

    if (!(event.isShiftClick()) && event.getSlotType() != SlotType.CRAFTING) {
      event.setCancelled(false);
    }
    if (event.isCancelled()) {
      if (player != null) {
        player.sendMessage("This item cannot be used in an anvil, grindstone, or enchanting table.");
      }
    }
  }

  @EventHandler
  public void onInventoryDragEvent(InventoryDragEvent event) {
    Player player = null;
    if (event.getWhoClicked() instanceof Player) {
      player = (Player) event.getWhoClicked();
    }
    Inventory relevantInventory = event.getInventory();
    if (relevantInventory.getType() == InventoryType.ANVIL ||
        relevantInventory.getType() == InventoryType.ENCHANTING ||
        relevantInventory.getType() == InventoryType.GRINDSTONE) {
      ItemStack item = event.getOldCursor();
      if (item != null) {
        CustomItemType type = CustomItemType.getTypeFromCustomItem(item);
        if (type != null) {
          // If it's not enchantable, cancel the event
          if (!CustomItemManager.getItemByType(type).isEnchantable()) {
            event.setCancelled(true);
            if (player != null) {
              player.sendMessage("This item cannot be used in an anvil, grindstone, or enchanting table.");
            }
          }
        }
      }
    }
  }

  @EventHandler
  public void onPlayerDeathEvent(PlayerDeathEvent event) {
    if (PhantomEvent.isActive()) {
      Player player = event.getEntity();
      if (event.getDeathMessage().endsWith("was killed by Potion using magic")) {
        event.setDeathMessage(player.getDisplayName() + " was killed by the Mother of All Phantoms");
      }
    }
  }

  // @EventHandler
  // public void onPrepareAnvilEvent(PrepareAnvilEvent event) {
  //   PrepareAnvilEvent pae = (PrepareAnvilEvent) event;
  //   ItemStack[] contents = pae.getInventory().getContents();
  //   if (pae.getViewers().size() > 0) {
  //     Player player = (Player) pae.getViewers().get(0);
  //     for (ItemStack item : contents) {
  //       if (item != null) {
  //         CustomItemType type = CustomItemType.getTypeFromCustomItem(item);
  //         if (type != null) {
  //           // If it's not enchantable, cancel the event
  //           if (!CustomItemManager.getItemByType(type).isEnchantable()) {
  //             player.getInventory().addItem(item);
  //             pae.getInventory().removeItem(item);
  //             player.closeInventory();
  //             player.sendMessage("You cannot add enchantments or rename this item.");
  //           }
  //         }
  //       }
  //     }
  //   }
  // }
  //
  // @EventHandler
  // public void onPrepareItemEnchantEvent(PrepareItemEnchantEvent event) {
  //   PrepareItemEnchantEvent piee = (PrepareItemEnchantEvent) event;
  //   ItemStack item = piee.getItem();
  //   if (item != null) {
  //     CustomItemType type = CustomItemType.getTypeFromCustomItem(item);
  //     if (type != null) {
  //       // If it's not enchantable, cancel the event
  //       if (!CustomItemManager.getItemByType(type).isEnchantable()) {
  //         piee.setCancelled(true);
  //       }
  //     }
  //   }
  // }

  @EventHandler
  public void onPlayerMoveEvent(PlayerMoveEvent event) {
    CustomItemEventManager.parseEvent(event);
  }

  @EventHandler
  public void onPlayerShearBlockEvent(PlayerShearBlockEvent event) {
    CustomItemEventManager.parseEvent(event);
  }

  @EventHandler
  public void onBlockShearEntityEvent(BlockShearEntityEvent event) {
    CustomItemEventManager.parseEvent(event);
  }

  @EventHandler
  public void onPlayerShearEntityEvent(PlayerShearEntityEvent event) {
    CustomItemEventManager.parseEvent(event);
  }

  @EventHandler
  public void onPlayerInteractEvent(PlayerInteractEvent event) {
    CustomItemEventManager.parseEvent(event);
  }

  @EventHandler
  public void onEntityDeath(EntityDeathEvent event) {
    if (event.getEntity().getType() == EntityType.PHANTOM) {
      LivingEntity entity = event.getEntity();
      if (entity.hasMetadata(PhantomEvent.EVENT_METADATA_KEY)) {
        PhantomEvent.manageKill(((Phantom) entity), null);
      }
      PhantomDropHandler.classifyAndDividePDE(event);
    }
    CustomItemEventManager.parseEvent(event);
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
  public void onEntitySpawn(EntitySpawnEvent event) {
    Entity spawnedEntity = event.getEntity();
    // If phantoms are disabled
    if (spawnedEntity.getType() == EntityType.PHANTOM && !PhantomStatus.isEnabled()) {
      event.setCancelled(true);
    // If phantoms are enabled and an event is active
    } else if (spawnedEntity.getType() == EntityType.PHANTOM && PhantomEvent.isActive()) {
      (new DeleteIfIsWild((Phantom) spawnedEntity)).runTaskLater(PhantomMain.getPlugin(), 10);
    } else if (spawnedEntity.getType() == EntityType.SKELETON && PhantomEvent.isActive()) {
      (new DeleteIfIsWildSkeleton((LivingEntity) spawnedEntity)).runTaskLater(PhantomMain.getPlugin(), 5);
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

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    CustomItemEventManager.parseEvent(event);
  }

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
    if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      if (edbee.getDamager() instanceof Projectile && edbee.getEntity() instanceof Player) {
        Entity damager = edbee.getDamager();
        Entity entity = edbee.getEntity();
        Projectile proj = (Projectile) damager;
        if (proj.getShooter() instanceof LivingEntity) {
          damager = (LivingEntity) proj.getShooter();
          if (damager.hasMetadata(PhantomEvent.EVENT_METADATA_KEY)) {
            UUID playerUUID = ((Player) edbee.getEntity()).getUniqueId();
            boolean doCancel = RecentPlayerDeaths.isPlayerOnList(playerUUID);
            event.setCancelled(doCancel);
            if (doCancel) {
              entity.setFireTicks(0);
            }

            Player player = (Player) event.getEntity();
            if (!doCancel && (player.getHealth() - event.getFinalDamage()) <= 0) {
              (new ManageDeathImmunity(player.getUniqueId())).runTask(PhantomMain.getPlugin());
            }
          }
        }
      }
    }
  }

  public void managePossiblePlayerDamagedByPhantom(EntityDamageEvent event) {
    if (event instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
      Entity entity = edbee.getEntity();
      if (entity.getType() == EntityType.PLAYER) {
        Entity damager = edbee.getDamager();
        UUID playerUUID = ((Player) edbee.getEntity()).getUniqueId();
        boolean doCancel = RecentPlayerDeaths.isPlayerOnList(playerUUID);
        // If it's an event creature targetting a player
        if (damager.hasMetadata(PhantomEvent.EVENT_METADATA_KEY)) {
          event.setCancelled(doCancel);
          if (doCancel) {
            entity.setFireTicks(0);
          }

          Player player = (Player) event.getEntity();
          if (!doCancel && (player.getHealth() - event.getFinalDamage()) <= 0) {
            (new ManageDeathImmunity(player.getUniqueId())).runTask(PhantomMain.getPlugin());
          }

          if (doCancel) {
            return;
          }
        }
        if (damager.getType() == EntityType.PHANTOM) {
          Phantom phantom = (Phantom) damager;
          // If it's a flaming phantom
          PhantomType phantomType = PhantomType.getTypeFromPhantom(phantom);
          // Add 2 hearts of extra damage to phantom attacks
          edbee.setDamage(edbee.getDamage() + 4);
          if (phantomType == PhantomType.FLAMING_PHANTOM) {
            final int THREE_SECONDS = 20 * 3;
            entity.setFireTicks(THREE_SECONDS);
          } else if (phantomType == PhantomType.MOTHER_OF_ALL_PHANTOMS) {
            edbee.setDamage(20);
          }

          if (edbee.getEntity() != null && damager != null) {

            if (damager instanceof Projectile) {
              Projectile proj = (Projectile) damager;
              if (proj.getShooter() instanceof LivingEntity) {
                damager = (LivingEntity) proj.getShooter();
              }
            }

            if (!doCancel) {
              if (phantomType == PhantomType.KAMIKAZE_PHANTOM && edbee.getCause() != DamageCause.ENTITY_EXPLOSION) {
                Location phantomLoc = phantom.getLocation();
                double x = phantomLoc.getX();
                double y = phantomLoc.getY();
                double z = phantomLoc.getZ();
                phantom.getLocation().getWorld().createExplosion(x, y, z, 2, false, false, phantom);
                phantom.setHealth(0);
                (new SpawnSilverfishAtLoc(phantomLoc)).runTaskLater(PhantomMain.getPlugin(), 3);
              }
            }
          }
        } else if (damager.getType() == EntityType.SPLASH_POTION) {
          if (PhantomEvent.isActive()) {
            if (damager.hasMetadata(PhantomEvent.EVENT_METADATA_KEY)) {
              event.setCancelled(doCancel);
              if (doCancel) {
                entity.setFireTicks(0);
              }

              Player player = (Player) event.getEntity();
              if (!doCancel && (player.getHealth() - event.getFinalDamage()) <= 0) {
                (new ManageDeathImmunity(player.getUniqueId())).runTask(PhantomMain.getPlugin());
              }
            }
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

        if (PhantomType.getTypeFromPhantom(phantom) == PhantomType.MOTHER_OF_ALL_PHANTOMS) {
          if (PhantomEvent.moapBar != null) {
            PhantomEvent.moapBar.setProgress(phantom.getHealth() / phantom.getMaxHealth());
          }
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

          // If there's an event going on
          if (PhantomEvent.isActive()) {
            PhantomEvent.manageKill(phantom, player);
          }
        }

        if (player != null) {
          if (RecentPlayerDeaths.isPlayerOnList(player.getUniqueId())) {
            RecentPlayerDeaths.removeFromList(player.getUniqueId());
          }
        }
      }
    }
  }
}
