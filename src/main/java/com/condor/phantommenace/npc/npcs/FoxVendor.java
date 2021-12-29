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

public class FoxVendor extends PHNPC {

  private static final String TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTYzOTk0NjM0NTc5OSwKICAicHJvZmlsZUlkIiA6ICJiN2ZkYmU2N2NkMDA0NjgzYjlmYTllM2UxNzczODI1NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJDVUNGTDE0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2E3OWZjZTA0NGYzODdiNDcwMDFjZjYyYzZhYTNkMmE5MWE0NmZkOWUxYmM1YTk1M2M5YWE2ZjA1MTRkNDQ4OTciCiAgICB9CiAgfQp9";
  private static final String TEXTURE_SIGNATURE = "cVbJ4VKdbKXQAqgGxHW5KFHBd7h5RKR4dh1Z/cMdy1GO0M5veqLRg2zZ6XGaZiU2+fLh0job37aEo7+Om3BJgGUDK6gWUmutpzAcUsccMLgldT87JybrJBrSapPvhusbQWrrBAgIyoqN9PTggsT0QMxY7Hx6hABzgdvor2gCDRzbfuy1LRBa4lWuQJxK+UTiUxTzg4L4dXAx/dzvW8tjlIQK+0hVzHI5xkWehJK88MR3kMO464Wo80HnkkoMAllphhZoS72Rn2v1dzq0R9xfcrDf+MSpJYkEIirhB8vHOj2rjsRnQBsybuC19wiv8bPSqrfy9JP36OIGeG2C5Ey4K8uUBL+/GpQBVdQHdmDzvoQSO7+m9ZF02CG/FQAYdzWkqBGq5RVrZ6kDkYk1k74Z/vuC7jCzMWnRdmjOLhaWzd2kFLbYuq4jBX+5h1MwQ7h0lRyfNmeKm4hsXBPA+YXvdFBl0WAdMQVC0DBkb5R9FhIt02JkUCJNm10S5XOKup+StkNXb3GBLmr4anRJY7Kb366SbyNXUOXETXgQkGwRH4pxJZvgcbI9a4xAEdP7IWkgVkrIyjIimPefgHqQgdES/+0Xmg0h0eTwB/48Bc6ZryoYgCivllO/b0pTsJQZhVJtza19R3aDvehf/dUOkgtjX0mnw9af9cKWZ+m4X8oQmTc=";
  private static final String NAME = "Taylor";
  private static final UUID UNIQUE_ID = UUID.fromString("bc72acc3-7feb-45a0-a37c-070729c4cd51");
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("survival");
  private static final double X = 1484.5;
  private static final double Y = 76;
  private static final double Z = -2831.5;
  private static final Location loc = new Location(WORLD, X, Y, Z);

  public FoxVendor() {
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
