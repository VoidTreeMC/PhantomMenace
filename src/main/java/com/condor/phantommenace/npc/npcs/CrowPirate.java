package com.condor.phantommenace.npc.npcs;

import java.util.UUID;

import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.npc.PHNPC;
import com.condor.phantommenace.item.CustomItemGenerator;
import com.condor.phantommenace.gui.PhantomShopGUI;

import com.github.juliarn.npc.profile.Profile;
import com.github.juliarn.npc.profile.Profile.Property;
import com.github.juliarn.npc.modifier.NPCModifier;
import com.github.juliarn.npc.modifier.EquipmentModifier;
import com.github.juliarn.npc.event.PlayerNPCInteractEvent;

public class CrowPirate extends PHNPC {

  private static final String TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTYzMzQwNTYyNTg0OCwKICAicHJvZmlsZUlkIiA6ICJmMjU5MTFiOTZkZDU0MjJhYTcwNzNiOTBmOGI4MTUyMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJmYXJsb3VjaDEwMCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iNWIyMWFiOGZkYTRlZjgxNTRmMGMwZmUxZjYxM2ZlZGQ0YzBiZjI1NDMxMTNjZDljMzY2NzVlMDM4YTQ5MTNiIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
  private static final String TEXTURE_SIGNATURE = "hbtvww9vWfzoL8pVi2medQnvC7JhGUC0beFbEwNl48WKLpw02DyCDzaV/pdVnrLRvyHkBRK2Bu0ohlksEm2JTVJQGe9EWiGsq/XZg30r9+tEhSllUdHgWdbhaIa4wfk/THyBMhWkRwi521M3ViPhSQIQiW5S9zhUrV3ZJC2aAmM8HVTIToYxMY2/DCkWSNACIOOX9t1YOZR+nikgVcxIk8wmIYcsiduQWX18E9dTaUH7CVRpMvWJ8kQcRyiQnq4q8cWJB7Y8si6APYPIYd2pULwi8TdsjG5tMebBXpL1F3YJQ0TdAvr1QxkKc6AQxIhnceRvGM82doSoZnWrnyfO16vqJWcqYzja3GGMGWOaC9wmQdjFLXBIqY/YLfKfHP7OpuFHc7SlWhSDrv5oCxVmZJ8BZWhBwx9ycDdM7e/BixgsH4HyN7CUMnYjdSr3UaZOA5sp8rjQFDafPDXmEyeZtXwfzgvJzGXKBduimTZEuh8IhvoGO7pgBXha7gy4weD8M30l59d9uCBdC/5esX3u8rP9kqzyvTAPFT/JFYdD/FvHUG5T56XtGKL6VWCvGaR+uFa5UEBGdI8RGfm7JvZErt4/1HBqwdou+ydjI8TrbqqgiLiJi5OEbAsoj2y/y3xsQ8G22fsImEJ59SPmoJKkC/TE6y1bKmx+XSnEHViXQbw=";
  private static final String NAME = "Poppy";
  private static final UUID UNIQUE_ID = UUID.fromString("bc72acc3-7feb-45a0-b68c-070729c4cd51");
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("survival");
  private static final double X = 625.656;
  private static final double Y = 79;
  private static final double Z = -423.616;
  private static final float PITCH = 18.1f;
  private static final float YAW = 1.3f;
  private static final Location loc = new Location(WORLD, X, Y, Z, PITCH, YAW);

  public CrowPirate() {
    super(TEXTURE, TEXTURE_SIGNATURE, NAME, UNIQUE_ID, loc);
  }

  public Profile getProfile() {
    Profile profile = new Profile(UNIQUE_ID);
    // Synchronously completing the profile, as we only created the profile with a UUID.
    // Through this, the name and textures will be filled in.
    profile.complete();

    // we want to keep the textures, but change the name and UUID.
    profile.setName(NAME);
    // // with a UUID like this, the NPC can play LabyMod emotes!
    // profile.setUniqueId(new UUID(random.nextLong(), 0));

    // Naive attempt to set the texture
    profile.setProperty(new Property("textures", TEXTURE, TEXTURE_SIGNATURE));

    return profile;
  }

  public void handleInteraction(PlayerNPCInteractEvent event) {
    Player player = event.getPlayer();
    PlayerNPCInteractEvent.EntityUseAction clickType = event.getUseAction();
    switch (clickType) {
      case ATTACK:
        player.sendMessage("Hey! Ow! That hurts!");
        // making the NPC look at the player
        npc.rotation()
          .queueLookAt(player.getEyeLocation())
          // sending the change only to the player who interacted with the NPC
          .send(player);
        break;
      default:
        // Do the dialogue here
        break;
    }
  }

  public NPCModifier getEquipmentModifier() {
    return this.npc.equipment().queue(EquipmentModifier.MAINHAND, new ItemStack(Material.AIR));
  }

  public Location getLocation() {
    return loc;
  }
}
