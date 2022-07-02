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

public class PotionVendor extends PHNPC {

  private static final String TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTY1NTA1Mzg1OTMxNiwKICAicHJvZmlsZUlkIiA6ICJlN2IxNmI2MzM2OGM0ODIzYmUxZDcxOTg5ODE5YWI3NSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTb3VyY2VXcml0ZXJzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ZmNDFkNDM2YTg0YzMxNDA1M2Y3ZmUyMjc4Y2UyZDExNzJiODk5Y2UyNWQyMDJkYzcxMjc1ZTM0MjE1NDBmMiIKICAgIH0KICB9Cn0=";
  private static final String TEXTURE_SIGNATURE = "IO+YOM0bIdduiZwkszckSAcD5Fb2IKj/5Q9HxwxpTb1xEAKvpEtfRsSxvOpMM2qhriifiaU53mlEH6sNEp9rjUKvZWTqqy2Ws+7GZsXExVZkhWMEKGpOe+HX+DNEnPRsW8ywtbzNmaZa/xwEWi23AgJGbQH7jH5a7kvMavavczlGPBP1QDJTvSsqxuaNneQuemDvX/OID789NNi5W697K1Q9jbwNYiu0fwTa2S3Ipqb86VJd2hRNrVADTGiKIzgRaP6Wugja0oOXDPSliZN6fRbndjL8ALUb6dC52FLmoWZGJWhGgO2rpGYp2Huo00HbwBWdBFYGn1GFvFpkxDpmLpSlJ6M/H8Q6altdQJl8SKu1P0LBDjlPUn9i+GmmCtDWBy4qQpLBwMQYrgqqrtXQHvz/gJlsTPjp1y8x1ASsM4hMj5Zia6ZAHynnUp311/8Ma0lspz6Mci5fE0KV45ATBcHr09LG/P9sqPprb2t8uyGK+/iOhCleRKj2UM5MWIk1rFUAw+6H3SJrkpG6jaQD9oJGjYs52J/eFTmq2ZoLBmNFxyQHEkKQtLKp6nmHtxYaqrr5Ek1cunLLgn9kMKbM3GKYZGE4Pg3YXb2HGiWgLSomnjOi8UKUyOuREKlVEf7yzuMzPksvJzNFggrbuKrQCA6IC+mrrMhcQ7h1J0tAZFw=";
  private static final String NAME = "Miffle";
  private static final UUID UNIQUE_ID = UUID.fromString("bc72acc3-7feb-45a0-b68c-070729c4cd27");
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("survival");
  private static final double X = 536.47;
  private static final double Y = 85;
  private static final double Z = -393.48;
  private static final Location loc = new Location(WORLD, X, Y, Z);

  public PotionVendor() {
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
        player.sendMessage("I don't do anything yet, but I've always dreamed of selling potions.");
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
