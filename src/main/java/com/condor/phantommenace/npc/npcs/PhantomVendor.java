package com.condor.phantommenace.npc.npcs;

import java.util.UUID;

import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.npc.PHNPC;
import com.condor.phantommenace.item.CustomItemGenerator;
import com.condor.phantommenace.gui.PhantomShopGUI;

import com.github.juliarn.npc.profile.Profile;
import com.github.juliarn.npc.profile.Profile.Property;
import com.github.juliarn.npc.modifier.NPCModifier;
import com.github.juliarn.npc.modifier.EquipmentModifier;
import com.github.juliarn.npc.event.PlayerNPCInteractEvent;

public class PhantomVendor extends PHNPC {

  private static final String TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTYyNzE3NDMxODcwNiwKICAicHJvZmlsZUlkIiA6ICJiNjM2OWQ0MzMwNTU0NGIzOWE5OTBhODYyNWY5MmEwNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJCb2JpbmhvXyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83NGFiYzhlNjZiYTBiNjBlMjZhYjgxYWZlNjAxNjE5Mzk1NDcwM2Q0OGRlMDU0NTViZjI4YjVjODAzZDc4YzY4IgogICAgfQogIH0KfQ==";
  private static final String TEXTURE_SIGNATURE = "rgxwLer5BUA/kR+M6LyAvQHGCM4BAW9HCTvq67J3EqYX7QC71Qhwvj6al9l2hJyST0x1BW3n9ldB6snX35p7vKhMl+eL4EPc80fUYnKFkh+rZVRx48zr8mcrdOBmgEsYi0UQjBr9wueJPNiLrPiscUwXLWX2uBXgN10Kx9G9oP5aGqZw82vyMcZ5qibNU+cSpAdee6WCz9gabHfcwwmQKuG2YqBx5nnt7mSppzmCg6F8bZnjJx0BKP++HzwGC+gTOYQM9m4foDvmSaFwsj5EWMnQTwUPnyduBBg1iw635Zp3ImOx3f7rlju2EVi6D5WvVcTSXOmPNoiMWr8ZCknLXdUDrXGpLHGVmLdbV7Vj30vFQJAZPOxlLiy2WhEUtfb6zfBlcar8R3iMtuPkf5Qcxi29R2pqzQNZAcROwViBTxi1jLNbk9g6+StOi8+VU4siy2goL89E50S0431c7OyolneXybUsdXD8ZOU5xpcCphOwpTj2kWktWAwUULJgLbZYeITwxii1FMzOBlBMN8JhubD8dUp+keOQ5q6KpIJ2r2pvXA9WP6QjdyATEamilLucd/LxDRqJVe8vYMJk8DvFaaWICvlHP7yJjY5uHVqkbMLZV8pPnfGXQT1OuDBiGeESmNKoWHTbEnIyHBpegl1oDH3j2ejLkfB+OwVm36x3BOs=";
  private static final String NAME = "Winter";
  private static final UUID UNIQUE_ID = UUID.fromString("bc72acc3-7feb-45a0-b68c-060729c4cc02");
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("lobby");
  private static final double X = 3882.5;
  private static final double Y = 116;
  private static final double Z = 129.5;
  private static final Location loc = new Location(WORLD, X, Y, Z);

  public PhantomVendor() {
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
        PhantomShopGUI.displayShopGUI(player);
        break;
    }
  }

  public NPCModifier getEquipmentModifier() {
    return this.npc.equipment().queue(EquipmentModifier.MAINHAND, CustomItemGenerator.getInsomniaPotion());
  }

  public Location getLocation() {
    return loc;
  }
}
