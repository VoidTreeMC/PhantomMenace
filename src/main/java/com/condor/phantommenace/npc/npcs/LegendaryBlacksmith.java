package com.condor.phantommenace.npc.npcs;

import java.util.UUID;

import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.npc.PHNPC;
import com.condor.phantommenace.gui.BlacksmithGUI;

import com.github.juliarn.npc.profile.Profile;
import com.github.juliarn.npc.profile.Profile.Property;
import com.github.juliarn.npc.modifier.NPCModifier;
import com.github.juliarn.npc.modifier.EquipmentModifier;
import com.github.juliarn.npc.event.PlayerNPCInteractEvent;

public class LegendaryBlacksmith extends PHNPC {

  // https://mineskin.org/

  private static final String TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTYyMzg5OTUyNTM0MCwKICAicHJvZmlsZUlkIiA6ICI0ZTMwZjUwZTdiYWU0M2YzYWZkMmE3NDUyY2ViZTI5YyIsCiAgInByb2ZpbGVOYW1lIiA6ICJfdG9tYXRvel8iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjk1ODI3ZjE5YzdmMmM1OTRmMjlkNGRmNWE0OWZlMDk2MmVlZWEwMTM2MjY5MWVjNGM5Mjc5YjgxYmEyNjQzYSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
  private static final String TEXTURE_SIGNATURE = "M3cf5wInFSxkZxyKJUDOpJYP9t0/F5c6y+S1r9GhQYGJe08u52vEoFhcnkOFW1wfUpSRiLxHhDKg6RYVBJU36c/JmWl7cLZjKO31N4D9UTA6EnEzJHgJdDRPH/2zjlhF5pdd5g+ZBlhbmx7Uz0vEYrFiG1ms4pUcvu70YQsPL5V+jW3ID+SwhmSO0yjpD0t30Ynd+u4m8QLBfPdz4kJ9gH3EdU86v7f57ml7DYqk2rJypWC0v203yXVEx9eQcDW2rc2DsJpz6nliFvrcwLCZ1aLKeWy/4oeKvJeczSknHs1kI+nfJgLXJIQ4xa/QGVdD4jq4y25BmHbKnLzFghUXx79eoqPc3q79drbLvAiPsIn+ZuEvbkAfavVB5qQPUvIJtw6L/dALgH3FlKjXEuYz0nwlMxTxGCtK2lC55WGNpUm3OY1BF+SqGSHUwzdpfHMUZ3ghitqKDp+71EfdIfDvSbit9ifYFzrF8y2ejI6KKEct3muc2cxQhtFE1X179Ej5XSD0TuUja7I57nwv9pQ9IanbpBo8DY7jcn/evvfJ0BbYKG9CFzcUw7kBlTrR3oGtAJOekfj2444RxWcY3X8hSquRUoaXfbUThC6I+huyLDz8G5HYTwl+kye55qSmkjqnh+GKY2pAeFuReD7f7mLl0FkCAVk9o+GpAtyw2ch/A38=";
  private static final String NAME = "Zenobius";
  private static final UUID UNIQUE_ID = UUID.fromString("bc72acc3-7feb-45a0-b78c-060729c4cc03");
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("survival");
  private static final double X = 527.497;
  private static final double Y = 72;
  private static final double Z = -433.548;
  private static final Location loc = new Location(WORLD, X, Y, Z);

  public LegendaryBlacksmith() {
    super(TEXTURE, TEXTURE_SIGNATURE, NAME, UNIQUE_ID, loc);
  }

  public Profile getProfile() {
    Profile profile = new Profile(UNIQUE_ID);

    // we want to keep the textures, but change the name and UUID.
    profile.setName(NAME);
    // // with a UUID like this, the NPC can play LabyMod emotes!
    // profile.setUniqueId(new UUID(random.nextLong(), 0));

    // Naive attempt to set the texture
    profile.setProperty(new Property("textures", TEXTURE, TEXTURE_SIGNATURE));

    // Synchronously completing the profile, as we only created the profile with a UUID.
    // Through this, the name and textures will be filled in.
    profile.complete();
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
        BlacksmithGUI.displayShopGUI(player);
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
