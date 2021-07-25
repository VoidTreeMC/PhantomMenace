package condor.runnable;

import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCSlot;
import dev.sergiferry.playernpc.api.NPCSkin;

import condor.main.PhantomMain;
import condor.item.CustomItemGenerator;

public class MakeShopNPCRunnable extends BukkitRunnable {

  private static final String TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTYyNzE3NDMxODcwNiwKICAicHJvZmlsZUlkIiA6ICJiNjM2OWQ0MzMwNTU0NGIzOWE5OTBhODYyNWY5MmEwNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJCb2JpbmhvXyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83NGFiYzhlNjZiYTBiNjBlMjZhYjgxYWZlNjAxNjE5Mzk1NDcwM2Q0OGRlMDU0NTViZjI4YjVjODAzZDc4YzY4IgogICAgfQogIH0KfQ==";
  private static final String TEXTURE_SIGNATURE = "rgxwLer5BUA/kR+M6LyAvQHGCM4BAW9HCTvq67J3EqYX7QC71Qhwvj6al9l2hJyST0x1BW3n9ldB6snX35p7vKhMl+eL4EPc80fUYnKFkh+rZVRx48zr8mcrdOBmgEsYi0UQjBr9wueJPNiLrPiscUwXLWX2uBXgN10Kx9G9oP5aGqZw82vyMcZ5qibNU+cSpAdee6WCz9gabHfcwwmQKuG2YqBx5nnt7mSppzmCg6F8bZnjJx0BKP++HzwGC+gTOYQM9m4foDvmSaFwsj5EWMnQTwUPnyduBBg1iw635Zp3ImOx3f7rlju2EVi6D5WvVcTSXOmPNoiMWr8ZCknLXdUDrXGpLHGVmLdbV7Vj30vFQJAZPOxlLiy2WhEUtfb6zfBlcar8R3iMtuPkf5Qcxi29R2pqzQNZAcROwViBTxi1jLNbk9g6+StOi8+VU4siy2goL89E50S0431c7OyolneXybUsdXD8ZOU5xpcCphOwpTj2kWktWAwUULJgLbZYeITwxii1FMzOBlBMN8JhubD8dUp+keOQ5q6KpIJ2r2pvXA9WP6QjdyATEamilLucd/LxDRqJVe8vYMJk8DvFaaWICvlHP7yJjY5uHVqkbMLZV8pPnfGXQT1OuDBiGeESmNKoWHTbEnIyHBpegl1oDH3j2ejLkfB+OwVm36x3BOs=";
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("lobby");
  private static final double X = 3885.303;
  private static final double Y = 115;
  private static final double Z = 129.429;
  private static final float PITCH = -92.6f;
  private static final float YAW = -3f;

  private Player player;
  private JavaPlugin plugin;

  /**
	* Builds the BukkitRunnable
	* @param player The player
	*/
	public MakeShopNPCRunnable(Player player) {
		this.plugin = PhantomMain.getPlugin();
		this.player = player;
	}

  /**
	 * Creates the NPC on the player's screen
	 */
	@Override
	public void run() {
    Location loc = new Location(WORLD, X, Y, Z, PITCH, YAW);
    NPC npc = PhantomMain.getNPCLib().createNPC(this.player, "SalesmanNPC", loc);
    // Set skin here
    npc.setSkin(new NPCSkin(TEXTURE, TEXTURE_SIGNATURE));
    // npc.setText("PhantomSalesman");
    npc.setCollidable(false);
    NPCSlot handSlot = NPCSlot.MAINHAND;

    npc.setItem(handSlot, CustomItemGenerator.getInsomniaPotion());

    npc.create();
    npc.show();

    this.cancel();
	}
}
