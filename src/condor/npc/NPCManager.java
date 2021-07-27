package condor.npc;

import java.util.UUID;

import org.bukkit.World;
import org.bukkit.Location;

import condor.main.PhantomMain;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.NPCPool;
import com.github.juliarn.npc.profile.Profile;
import com.github.juliarn.npc.profile.Profile.Property;

public class NPCManager {

  // TODO: Generalize this

  private static final String VENDOR_TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTYyNzE3NDMxODcwNiwKICAicHJvZmlsZUlkIiA6ICJiNjM2OWQ0MzMwNTU0NGIzOWE5OTBhODYyNWY5MmEwNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJCb2JpbmhvXyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83NGFiYzhlNjZiYTBiNjBlMjZhYjgxYWZlNjAxNjE5Mzk1NDcwM2Q0OGRlMDU0NTViZjI4YjVjODAzZDc4YzY4IgogICAgfQogIH0KfQ==";
  private static final String VENDOR_TEXTURE_SIGNATURE = "rgxwLer5BUA/kR+M6LyAvQHGCM4BAW9HCTvq67J3EqYX7QC71Qhwvj6al9l2hJyST0x1BW3n9ldB6snX35p7vKhMl+eL4EPc80fUYnKFkh+rZVRx48zr8mcrdOBmgEsYi0UQjBr9wueJPNiLrPiscUwXLWX2uBXgN10Kx9G9oP5aGqZw82vyMcZ5qibNU+cSpAdee6WCz9gabHfcwwmQKuG2YqBx5nnt7mSppzmCg6F8bZnjJx0BKP++HzwGC+gTOYQM9m4foDvmSaFwsj5EWMnQTwUPnyduBBg1iw635Zp3ImOx3f7rlju2EVi6D5WvVcTSXOmPNoiMWr8ZCknLXdUDrXGpLHGVmLdbV7Vj30vFQJAZPOxlLiy2WhEUtfb6zfBlcar8R3iMtuPkf5Qcxi29R2pqzQNZAcROwViBTxi1jLNbk9g6+StOi8+VU4siy2goL89E50S0431c7OyolneXybUsdXD8ZOU5xpcCphOwpTj2kWktWAwUULJgLbZYeITwxii1FMzOBlBMN8JhubD8dUp+keOQ5q6KpIJ2r2pvXA9WP6QjdyATEamilLucd/LxDRqJVe8vYMJk8DvFaaWICvlHP7yJjY5uHVqkbMLZV8pPnfGXQT1OuDBiGeESmNKoWHTbEnIyHBpegl1oDH3j2ejLkfB+OwVm36x3BOs=";
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("lobby");
  private static final double X = 3885.303;
  private static final double Y = 115;
  private static final double Z = 129.429;
  private static final float PITCH = -92.6f;
  private static final float YAW = -3f;

  // private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("world");
  // private static final double X = -225;
  // private static final double Y = 69;
  // private static final double Z = -110;


  private static NPCPool npcPool;

  /**
   * Initializes all NPCs
   */
  public static void init() {
    npcPool = NPCPool.builder(PhantomMain.getPlugin())
      .spawnDistance(60)
      .actionDistance(30)
      .tabListRemoveTicks(20)
      .build();

    appendNPC(new Location(WORLD, X, Y, Z, PITCH, YAW));
  }

  /**
   * Appends a new NPC to the pool.
   *
   * @param location       The location the NPC will be spawned at
   */
  public static void appendNPC(Location location) {
    // building the NPC
    NPC npc = NPC.builder()
      .profile(createPhantomVendorProfile())
      .location(location)
      .lookAtPlayer(true)
      // appending it to the NPC pool
      .build(npcPool);
  }

  /**
   * Creates an example profile for NPCs.
   *
   * @return The new profile
   */
  public static Profile createPhantomVendorProfile() {
    Profile profile = new Profile(UUID.fromString("bc72acc3-7feb-45a0-b68c-060729c4cc02"));
    // Synchronously completing the profile, as we only created the profile with a UUID.
    // Through this, the name and textures will be filled in.
    profile.complete();

    // we want to keep the textures, but change the name and UUID.
    profile.setName("Phantom Vendor");
    // // with a UUID like this, the NPC can play LabyMod emotes!
    // profile.setUniqueId(new UUID(random.nextLong(), 0));

    // Naive attempt to set the texture
    profile.setProperty(new Property("textures", VENDOR_TEXTURE, VENDOR_TEXTURE_SIGNATURE));

    return profile;
  }
}
