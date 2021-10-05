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

public class PirateCaptain extends PHNPC {

  private static final String TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTYzMzQwNTgxOTk3MSwKICAicHJvZmlsZUlkIiA6ICIzNDkxZjJiOTdjMDE0MWE2OTM2YjFjMjJhMmEwMGZiNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJKZXNzc3N1aGgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWNiNGViODlkMzYzZTFkM2ZkYzhiODk0OTEwMDFkMzVlZTAzMGE0NWIwMTgzYjMwMTU5ODQyOWY3MWQ4ODZmNiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
  private static final String TEXTURE_SIGNATURE = "W1WNWJ+vSlkJ3WUElMRq/zmqaq2EBgiZpPvMIjKlryN1zoZ20Vn6HZjAcff4Lbp/5hx+JURehjADmnIHnperCOEqUMHsglSeSf1dXKmNvrmG3cn5ROwvzOvX2JwA9WOIjxtMWsn9LerzzYP9xynXu4QftDop9TUYdLh/u8L/FpafMD3NodiYYePTCsx+gfrbf0EacjkGQP2nC1vAVGYmVHXOIR2Ge0psXhoBYqoP96qClVUMja0nyj+J2L68cNqiw4O5Y6UpcBhYKEnVo3iOa1gB6DLFKqlq51Z/SBREVvtkVmQ0mWp46OJZKrVwt833Bye/lAts3KdPP1xFyBPKe3468bJNVP60CF1M5MY4DyeaLYwRhDKEJ5CIqAuW8xzsu9t0fz1gJlXra8S3FZje+R6QEHBPTauO4JyHHp5gSu5Czt6GMJ+nqBfL2/t2cKZukIGpVdWDb3IVh+kdBTK3bYNVZtaG4p56SBu4bZs5cSuBDaA61ELY0tQSAPHPzmNyYXGAe8xt5T9kWG36wuVSJId9aYSfZUdeZ2dodPgb2kljnuQn6qoBoOd4fxCHwNI5IK+C+fwPmkw6lEFCCwlbFdtH592i0YATmQ1nWcsoZ68DFJDipEO2vuAVzk6pB1JRSYTdxxpkDfNVNnhbT/0oqEv1nVcZMN39Z//eLppu/Hc=";
  private static final String NAME = "Captain Ember";
  private static final UUID UNIQUE_ID = UUID.fromString("bc72acc3-7beb-45a0-b68c-070729c4cd51");
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("lobby");
  private static final double X = 2740;
  private static final double Y = 64;
  private static final double Z = -6711;
  private static final float PITCH = -140f;
  private static final float YAW = -1.8f;
  private static final Location loc = new Location(WORLD, X, Y, Z, PITCH, YAW);

  public PirateCaptain() {
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
