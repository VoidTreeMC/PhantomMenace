package com.condor.phantommenace.npc;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.profile.Profile;
import com.github.juliarn.npc.modifier.NPCModifier;
import com.github.juliarn.npc.event.PlayerNPCInteractEvent;
import com.github.juliarn.npc.profile.Profile.Property;
import com.github.juliarn.npc.modifier.EquipmentModifier;

public class PHNPC {
  private String textureString;
  private String textureStringSignature;
  private String name;
  private UUID uuid;
  private Location location;
  private boolean faceUser = true;
  Profile profile;
  protected NPC npc;

  private NPCAction action = NPCAction.NONE;

  public PHNPC(UUID uuid, String texture, String signature, String name, Location loc, boolean faceUser, NPCAction action) {
    this.uuid = uuid;
    this.textureString = texture;
    this.textureStringSignature = signature;
    this.name = name;
    this.location = loc;
    this.faceUser = faceUser;
    this.action = action;

    profile = new Profile(uuid);
    // Synchronously completing the profile, as we only created the profile with a UUID.
    // Through this, the name and textures will be filled in.
    profile.complete();

    // we want to keep the textures, but change the name and UUID.
    profile.setName(name);
    // // with a UUID like this, the NPC can play LabyMod emotes!
    // profile.setUniqueId(new UUID(random.nextLong(), 0));

    // Naive attempt to set the texture
    profile.setProperty(new Property("textures", texture, signature));
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

  public boolean getFaceUser() {
    return faceUser;
  }

  public void setFaceUser(boolean faceUser) {
    this.faceUser = faceUser;
  }

  public NPCModifier getEquipmentModifier() {
    return this.npc.equipment().queue(EquipmentModifier.MAINHAND, new ItemStack(Material.AIR));
  }

  public Profile getProfile() {
    return profile;
  }

  public Location getLocation() {
    return this.location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public void handleInteraction(PlayerNPCInteractEvent event) {
    Player player = event.getPlayer();
    PlayerNPCInteractEvent.EntityUseAction clickType = event.getUseAction();
    switch (clickType) {
      case ATTACK:
        player.sendMessage("Hey! Ow! That hurts!");
        // making the NPC look at the player
        if (faceUser) {
          npc.rotation()
            .queueLookAt(player.getEyeLocation())
            // sending the change only to the player who interacted with the NPC
            .send(player);
        }
        break;
      default:
        NPCAction.act(action, player, event);
        break;
    }
  }
}
