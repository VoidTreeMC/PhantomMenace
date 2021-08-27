package com.condor.phantommenace.npc;

import java.util.UUID;

import org.bukkit.Location;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.profile.Profile;
import com.github.juliarn.npc.modifier.NPCModifier;
import com.github.juliarn.npc.event.PlayerNPCInteractEvent;

public abstract class PHNPC {
  private String textureString;
  private String textureStringSignature;
  private String name;
  private UUID uuid;
  private Location location;
  protected NPC npc;

  public PHNPC(String texture, String signature, String name, UUID uuid, Location loc) {
    this.textureString = texture;
    this.textureStringSignature = signature;
    this.name = name;
    this.uuid = uuid;
    this.location = loc;
  }

  public UUID getUniqueId() {
    return this.uuid;
  }

  public void setNPC(NPC npc) {
    this.npc = npc;
  }

  public NPC getNPC() {
    return this.npc;
  }

  public abstract NPCModifier getEquipmentModifier();

  public abstract Profile getProfile();

  public abstract Location getLocation();

  public abstract void handleInteraction(PlayerNPCInteractEvent event);
}
