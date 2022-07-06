package com.condor.phantommenace.npc;

import java.util.UUID;
import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.Location;

import com.condor.phantommenace.main.PhantomMain;
import java.util.HashMap;
import java.util.Map.Entry;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.NPCPool;

public class NPCManager {

  private static HashMap<UUID, PHNPC> phnpcMap = new HashMap<>();

  private static NPCPool npcPool;

  public static PHNPC getPHNPCFromUUID(UUID uuid) {
    return phnpcMap.get(uuid);
  }

  static {
    npcPool = NPCPool.builder(PhantomMain.getPlugin())
      .spawnDistance(60)
      .actionDistance(30)
      .tabListRemoveTicks(20)
      .build();
  }

  /**
   * Appends a new NPC to the pool
   * @param newNPC The new NPC
   */
  public static void appendNPC(PHNPC newNPC) {
    // building the NPC
    NPC npc = NPC.builder()
      .profile(newNPC.getProfile())
      .location(newNPC.getLocation())
      .lookAtPlayer(newNPC.getFaceUser())
      // appending it to the NPC pool
      .build(npcPool);
    newNPC.setNPC(npc);
    phnpcMap.put(newNPC.getUniqueId(), newNPC);
  }
}
