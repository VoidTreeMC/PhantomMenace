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

public class QueenBear extends PHNPC {

  private static final String TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTYxMTE4MDI5MjU4MSwKICAicHJvZmlsZUlkIiA6ICI5ZDEzZjcyMTcxM2E0N2U0OTAwZTMyZGVkNjBjNDY3MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJUYWxvZGFvIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzFlNGE5OWEyZjAyYzYzM2ExYTA3NGFjZDBlZjIzOTY2NTdkMmU5YzU2OTdhNGQ3ZDg3MjQxMDhlYmI5MDQ5YWUiCiAgICB9CiAgfQp9";
  private static final String TEXTURE_SIGNATURE = "kWhugkZjypTu7VlftMVYvvuGus4DtAd6tpFkAZjTiGf09acaNt8Eq7fDrIzJMPCty3mW5eVea3b9FCfNjU9AZ2B42C1PuD56UMX4YDaicX/2k6OlKJ2IDVlqI71A/j4mPhpk+0onZ5R+/MXlshHAEXRRvpxNa8opc3bTMxTVH1icNoYhc21IvmCEhlN2BZk1ohVZSW67AysYNbb3NyFCUtk4hf8zGMmz6k0Br7g2JGXRX4J14F6leIKqCpGGhKMN6a+biidGWXca2fZRtHRCnBlECVBWX078KXDkvCBF5Q8dEBZOf40NXBzgenZwB8fUOEhCou7AhsTB3/ERKQYyAHfjWq6EA23Ha3USN+6FgVdWI5WFQomIvUHrqVcczD9sleHHdYP9sWkIn/mL0HAKOobbBwa2nx4qNFwSEbZ0non8PRfac+JdjhBdYgQr8Q49rliwa/tdgkyqZoxAS0Ql0G+1n42F5Y/vqQrjQY+kcS5CxNl/rOSvsqpA39VXTdDs8s5VyIuyLhZ3tU0AWL7mlFkF3lTDCOu5q6W/ynWPjG8o/Vsk+2N7cnudLyjmO+sTkyQ50vwSswWGFouaHz3fc7bNiWPGNd/B8t+bYpTQwBwF7yjHrzSzOH8X/7I3El2E81pSMIVE6bN1J2d0+KpP2KxvbVNFlcBaXKSuaQxx2/I=";
  private static final String NAME = "Queen Ursula";
  private static final UUID UNIQUE_ID = UUID.fromString("bc72acc3-7feb-45a0-b68c-060729c4cc91");
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("lobby");
  private static final double X = 2810.504;
  private static final double Y = 96.5;
  private static final double Z = -6957.093;
  private static final float PITCH = -0.3f;
  private static final float YAW = 3.6f;
  private static final Location loc = new Location(WORLD, X, Y, Z, PITCH, YAW);

  public QueenBear() {
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
