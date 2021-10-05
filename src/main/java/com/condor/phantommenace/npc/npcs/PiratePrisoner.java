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

public class PiratePrisoner extends PHNPC {

  private static final String TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTYzMzQwNjQ4MTIyNywKICAicHJvZmlsZUlkIiA6ICJkODAwZDI4MDlmNTE0ZjkxODk4YTU4MWYzODE0Yzc5OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJ0aGVCTFJ4eCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81Y2E0MTlhZjA4ZDgzOTExYzJkYjcwODgwNzhjMDFiODgxOTU4ZGU5YmY1MTkyNzg1YWM5MjgzYzdjYzNkNGVjIgogICAgfQogIH0KfQ==";
  private static final String TEXTURE_SIGNATURE = "X8ktmkC3GqfhngawAz65RpY71YWJqJq6r56CufOndOmawIdZuApYfd8o9h3rJlBUvjUtr73FJ4VEjRGYxTyt5yYfOSShbj/jdpDMDuOZDNt7qGiDesYW/UyaRRqe8Ca9hhdUeNgAUexRNWU2lwfHlgdwD0vMuckE2MSzJNaTqaUHdcjcabJD4vtYKITQL2U/qT+FhXbSb32FguPd+UvCzw3AJKw1tnSgcHo53rVAoWYYVbtcLgiu6luB01d6wYy2VdkBObqtqReWCKy2khN2n/JBgnQovUsEzJXLGLE+FGBSI+pC1UfSMU62P8O4aFyOCvytGxaQMUnPY9cQAZZXMHWUsTHuAkvVdFeWDBa+v/ulCCU0IHCDXlsiZ16WijqeiCe63/77VRP4ioV2Y25OewDHcMHlmM+b+HrE88gxBdLOqujLk+eR69z1u+8hd4LH7juJ19uWljs2LOpJroUKKtRVb7eq4yW5BSCY6ttYnBfqssFsvEUwMZTxVUqmoEWPW/JT8ApPhk441gsYafQxsOlRpa4pN+NP317mETj0nQDuULoc/OdUtuwb+heLE3Ypz2SFR7z82G7+/ExUTdCTw9dpPAjT346KSkb1n7iM3SMqD+Hp7sK7AhIYKOTQYnwvoJMHD9gfYHnePUfBsdIz6vqN00V2dvAn5qGwJtvUtTI=";
  private static final String NAME = "Vesper";
  private static final UUID UNIQUE_ID = UUID.fromString("bc12bcd3-8feb-45a0-b68c-070729c4cd51");
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("lobby");
  private static final double X = 2732;
  private static final double Y = 60;
  private static final double Z = -6708;
  private static final float PITCH = 92.7f;
  private static final float YAW = 0.1f;
  private static final Location loc = new Location(WORLD, X, Y, Z, PITCH, YAW);

  public PiratePrisoner() {
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
