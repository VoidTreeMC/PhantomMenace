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

public class BartenderRaccoon extends PHNPC {

  private static final String TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTYyODg3NTI1MTE3NywKICAicHJvZmlsZUlkIiA6ICJmMjM1Njk3MzZkOTI0OTM1YWZlYjRkMDVlMjI1Njk3NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJGb3h5VHViYmkxIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2RlMWYyYjVhMDdhODZmYWMyNmMyNzI3Mzg4Y2E5OWRhYzI4YmFjYmU4Nzk2ZGU0YmQ2M2E5YjI2ZmViYWY4MzMiCiAgICB9CiAgfQp9";
  private static final String TEXTURE_SIGNATURE = "vZ37JkzJlrsfEThPaTVX/2XHTmcaewwM5zW2e8TAsFIXSPCjZ/0FXkYUZlB8jJ1dyEF2dKtSpWtoYduborrcDaKKrVM1Lrd6x6KI6Eove1ixMsw4eFzlYlGyBt6Gnwu+9LXHOxzoLrPfvXeM8N9QlbyzwzUrDWy+Bq7ikFhpd4VF3B9ePBql/UjXzY4LSLp7745PjfIM1YaX5/2kuxTvMrBlMj4+Rm2Jo0Ue3NgEj8MYrEBrPWHTnHcO2gRsYIWcxBK+CT5d5W+QKOiDgp4wxv7gyOOfu0r9ZSH64KqNcf3xLDDIdERNQoe0lTT4AaJD7r2d9h1PdPuCn2anyQJu1X3u+3+LjAK6yjoU48XLC0xIz96PSvz7dSA7qakBVqWopm9piszwk1zNIB1+F1WdhZzQOrD7PTIcCv4Xh/Vn9tKjbQ1XplletFFCta79SdCpf+IeVHIVLnd+5uIy37ET4qCJRQ907L0ZvKh/FdBzKhhPDvS7iRpMyRsyOMzbxB8ReUPDAXR1owplsR77ooQESJtAn3aZNwWk9D+Ng70fNMUq20K/J7+IXksA3C2NHcXjDY0ACX2CPMTccIaxQKWD/dE0ZAfOH6QxI7Qt4qSgT9PRcYv7YlxYFtRNLJc2vxEO0D6Y8CiBMGS2Ds38paCm/A8UC5wEnONtk1vtRNXOTzg=";
  private static final String NAME = "Sykstus";
  private static final UUID UNIQUE_ID = UUID.fromString("bc72acc3-7feb-45a0-b68c-060729c4cd51");
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("survival");
  private static final double X = 565.287;
  private static final double Y = 66;
  private static final double Z = -613.550;
  private static final float PITCH = -159.1f;
  private static final float YAW = 2.5f;
  private static final Location loc = new Location(WORLD, X, Y, Z, PITCH, YAW);

  public BartenderRaccoon() {
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
