package condor.npc;

import java.util.UUID;
import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.Location;

import condor.main.PhantomMain;
import condor.npc.npcs.*;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.NPCPool;
import com.github.juliarn.npc.profile.Profile;
import com.github.juliarn.npc.profile.Profile.Property;

public class NPCManager {

  private static TreeMap<UUID, PHNPC> phnpcMap = new TreeMap<>();

  // Add new NPCs here
  static {
    PhantomVendor phantomVendor = new PhantomVendor();
    phnpcMap.put(phantomVendor.getUniqueId(), phantomVendor);

    LegendaryBlacksmith blacksmith = new LegendaryBlacksmith();
    phnpcMap.put(blacksmith.getUniqueId(), blacksmith);
  }

  private static NPCPool npcPool;

  public static PHNPC getPHNPCFromUUID(UUID uuid) {
    return phnpcMap.get(uuid);
  }

  /**
   * Initializes all NPCs
   */
  public static void init() {
    npcPool = NPCPool.builder(PhantomMain.getPlugin())
      .spawnDistance(60)
      .actionDistance(30)
      .tabListRemoveTicks(20)
      .build();

    for (Entry<UUID, PHNPC> subMap : phnpcMap.entrySet()) {
      PHNPC phnpc = subMap.getValue();
      NPC npc = appendNPC(phnpc.getLocation(), phnpc.getProfile());
      phnpc.setNPC(npc);
    }
  }

  /**
   * Appends a new NPC to the pool.
   *
   * @param location       The location the NPC will be spawned at
   */
  public static NPC appendNPC(Location location, Profile profile) {
    boolean isVendor = (phnpcMap.get(profile.getUniqueId()) instanceof PhantomVendor);
    // building the NPC
    NPC npc = NPC.builder()
      .profile(profile)
      .location(location)
      .lookAtPlayer(true)
      .imitatePlayer(isVendor)
      // appending it to the NPC pool
      .build(npcPool);
    return npc;
  }
}
