package com.condor.phantommenace.npc;

import java.util.UUID;
import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.Location;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.npc.npcs.*;
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

    QueenBear queenBear = new QueenBear();
    phnpcMap.put(queenBear.getUniqueId(), queenBear);

    BartenderRaccoon bartenderRaccoon = new BartenderRaccoon();
    phnpcMap.put(bartenderRaccoon.getUniqueId(), bartenderRaccoon);

    CrowPirate crowPirate = new CrowPirate();
    phnpcMap.put(crowPirate.getUniqueId(), crowPirate);

    PirateCaptain pirateCaptain = new PirateCaptain();
    phnpcMap.put(pirateCaptain.getUniqueId(), pirateCaptain);

    PiratePrisoner piratePrisoner = new PiratePrisoner();
    phnpcMap.put(piratePrisoner.getUniqueId(), piratePrisoner);

    SubdeckPirate subdeckPirate = new SubdeckPirate();
    phnpcMap.put(subdeckPirate.getUniqueId(), subdeckPirate);
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
    PHNPC target = phnpcMap.get(profile.getUniqueId());
    boolean isVendor = (phnpcMap.get(profile.getUniqueId()) instanceof PhantomVendor);
    boolean lookAtPlayer = !((target instanceof SubdeckPirate) || (target instanceof PirateCaptain));
    // building the NPC
    NPC npc = NPC.builder()
      .profile(profile)
      .location(location)
      .lookAtPlayer(lookAtPlayer)
      .imitatePlayer(isVendor)
      // appending it to the NPC pool
      .build(npcPool);
    return npc;
  }
}
