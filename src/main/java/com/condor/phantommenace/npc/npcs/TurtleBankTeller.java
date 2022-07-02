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

public class TurtleBankTeller extends PHNPC {

  private static final String TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTY1NjYxODMyOTEzNiwKICAicHJvZmlsZUlkIiA6ICI1NTZjNDNmOWJmZjU0MjI1OTI0NDU5M2EwN2QyYzE1MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJHaW50b2tsIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2VkNWRlMzE2NmRmNWY2ZTkxMzgwOGJkZjNlNjA1NDY4YjkyM2UzYTI3YTUxNzM2ZGEyY2I0NzQ3NzljNTczNzkiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
  private static final String TEXTURE_SIGNATURE = "mWeNjGqAPs4+r47IMHbi+R7A8t2skbwf8aPrfT7yfCnwhgK3DaCw2xex4Z1uF1cyyG0PXZDsi+IdbOkUBmoY/iEM+wU8bNZxj2WpJalrvdqkWpCRtpqOBSwKXiZqec6fmwUhMu/RzV3S4RJqx+48d+9FdPTLq/8rHMTcmPVrLmau9P3aIxDfa4QYfOY1sLJyRBXD9r2alRBhQN2WjwXPWUEfaWvVLX0JBGN9ZRdgppvmG/YfnP/ImS0XLZkZg64scCbMRCssWHH+gxr0TcJ8zl2wnDSzVouOClZ1PMROSJnpqEhoIt92F8vbheYYWMI/szYoKottqGYRtrDKbcg3OOLx15OslM5GliSmWvZ6WdtPjuWzwsial1pY7bPy1Xg/5WnuGFQKKdFkKWerbbGDwi4sBlpvNLcpusshynWgu3ZluxviIJ7pa+ydIH2bdX0AYQalBH+RmyfNJwRE1xh25GD/DWg2Y6Bv0NKBKK+3K64suIgNvOST5+bpIdpFF90EtyabEYYvZ4V0gn6TnQTvJF2unKVFU3GDRVqr6uk75PVMKgnmIeLcGBD78SUtNN2VMgDA4Xa66zH9g+cSkO3+T7pWHSko/61yEqILVu8J6yUSJzNWXCw7qoDKJaiHD45a+ybN/j1G9IW7jtWzY2neYGLUPFGuQVZQjZYWOMdqrao=";
  private static final String NAME = "Whitley";
  private static final UUID UNIQUE_ID = UUID.fromString("bc72acc3-7feb-45a0-b68c-070729c4cd25");
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("survival");
  private static final double X = 418.5;
  private static final double Y = 68;
  private static final double Z = -493.5;
  private static final float PITCH = -180.0f;
  private static final float YAW = 0.0f;
  private static final Location loc = new Location(WORLD, X, Y, Z, PITCH, YAW);

  public TurtleBankTeller() {
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
