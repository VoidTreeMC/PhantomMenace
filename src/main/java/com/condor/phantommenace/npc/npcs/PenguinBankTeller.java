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

public class PenguinBankTeller extends PHNPC {

  private static final String TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTY1Njc5MzM4NzgxOSwKICAicHJvZmlsZUlkIiA6ICJmMjc0YzRkNjI1MDQ0ZTQxOGVmYmYwNmM3NWIyMDIxMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJIeXBpZ3NlbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82YzlhNjc3ZjJjYmQ5Zjk1NmU2MDNhOTBmMDNhMGY2MDE0ZjgxZTU0N2U0NjcxNTIxOWJkODkxNWFjMmNiM2EiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
  private static final String TEXTURE_SIGNATURE = "F2tjA8MSYnR5sKmwTNTNMQBbmTKM7Z9juaeB4CVJevA+WzesLEDKMP1uchOcZPJ9Ixf5xrSMF2MEyNAH3lhQulhQCoWmgLxdTosSf+MUTUOWSBgmNSfI00ixWOFIZE5nETiADG+nxUqLpehbQREkcoaR6xn86TUOSVWHnfNP9vGMdao5qbirAQgiiUN6bI8bvbbRcwOpXESvTL8y++UBvd7v8KyTFHCt9KAF7lgiMU51+May20kyg6ZXfkDz1F5pNKBV7eyL0CzGttTOvDqv05oW3jnkDw0gQyKUJbhXZcCXrVmuY1D5GUwPA8EfEL0lTukawVf/SDvm7hRXAyr+FqoccgW/LgSSm0zFehf6smuvd6Ln/COEtUW6NQNxIF4el2fB2IgB0sZ05u96RbgbMJgVys9SEEpdFGgB+e5sXya4OBH9XR6jGwUw1ZmWpve1kBCJK/v+gFqvZJAossb5PULZY30d/iVuItAMsi5jrxIwX0G9srt7Y3zq2k1eWcJ5SPBptn6J6TJE1zRj/97dmxWiNhh8xPOhXNh9PxDPbBjOo/YgnZhRH8H9erO1IqiWpmJIfWLWD6tiZoYKlTyVo6T4BfeYU7bvxYwykOBxmQiaytDTnxq224tPdHVv/Rboa0/jbO3jagEv+MdenAxuFqZCw2JtDs6ZxOUEE7wzPXo=";
  private static final String NAME = "Kane";
  private static final UUID UNIQUE_ID = UUID.fromString("bc72acc3-7feb-45a0-b68c-070729c4cd23");
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("survival");
  private static final double X = 420.5;
  private static final double Y = 68;
  private static final double Z = -493.5;
  private static final float PITCH = -180.0f;
  private static final float YAW = 0.0f;
  private static final Location loc = new Location(WORLD, X, Y, Z, PITCH, YAW);

  public PenguinBankTeller() {
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
        player.openInventory(player.getEnderChest());
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
