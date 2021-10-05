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

public class SubdeckPirate extends PHNPC {

  private static final String TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTYzMzQwNTk4NzMxNCwKICAicHJvZmlsZUlkIiA6ICI5MWYwNGZlOTBmMzY0M2I1OGYyMGUzMzc1Zjg2ZDM5ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdG9ybVN0b3JteSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iN2FkNjJlYjNiNjFhMjAyNDU5MTdkMTcyOTk2Mzg5Zjk1MGMzZWRkY2RlZTY5OTE3ZjgxNGM4ZmZlMzgzZjVhIgogICAgfQogIH0KfQ==";
  private static final String TEXTURE_SIGNATURE = "S5C/+rtaI9JTSGYkmHLbzNRTtLDAC+1kZ7QkutQS1VVSHEOpFOvJWB9b1y606Zf4oALJ3KIObxU9UfpI/1bjWkIVlFv+bcM2kAZF3DqrDpbiZzqDH3gHxdH7TH7YEApiZryUkyONeNXpSQ1gKL7Z6okQdBJFENfZqI1wO6Ti+KnHV2fzyIjqAPDWjB7hca+9LP903+OP0ieXc2EE6FcVvHjRK5ORNS1WrfpMY9fEWbwegpNF4iHSArghS2ryR58pBOSjMLpff6vWvQY+tFD7H+CwXCW9k6qZLacjpi0Wgdb+dsf/hL335zIRXX36Ebw2EHaU7Ncu5g2ztJixmdycV5vcCXwq2ubGo/4GTLmX6vGHg1mWs5mZEKv44ra1slvWLeaxWyRWZgIpyZYcmBJoDPfOZnAWGJ/SZO2BabgCOgme2uwWJ4rM3O53bhGFtOfJ5aSTvPpE1UtKmAvoTtuU3sZC54HndljtBKCTm1AKBqutyAtKTvpDTYkJw6q6u/LTa8peYl7Y38HCkk8qS2XeFIFDqLMQQAi6tSaZCQ4R/0vZ6+QidbdqZbKtFabk9juAXJtBOH0jguK0Syu5QxaY7vS34fYayCZXwE+isNgs75s8zJ5HPpG4KJrHHja3w4z/up/7e+RqW0wcY/uN7kH1jE32kOU1/Js9a7/n7omACZU=";
  private static final String NAME = "Morgan";
  private static final UUID UNIQUE_ID = UUID.fromString("bc12acc3-7feb-45a0-b68c-070729c4cd51");
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("lobby");
  private static final double X = 2719;
  private static final double Y = 60;
  private static final double Z = -6709;
  private static final float PITCH = -82.6f;
  private static final float YAW = 1.4f;
  private static final Location loc = new Location(WORLD, X, Y, Z, PITCH, YAW);

  public SubdeckPirate() {
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
