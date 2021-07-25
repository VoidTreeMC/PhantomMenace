package condor.command.executors;

import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;

import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCSlot;
import dev.sergiferry.playernpc.api.NPCSkin;

import condor.command.CommandControl;
import condor.command.SubCommand;
import condor.item.CustomItemGenerator;
import condor.main.PhantomMain;

public class MakeShopNPCCommand extends CommandControl {

  private static final String TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTYyNzE3NDMxODcwNiwKICAicHJvZmlsZUlkIiA6ICJiNjM2OWQ0MzMwNTU0NGIzOWE5OTBhODYyNWY5MmEwNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJCb2JpbmhvXyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83NGFiYzhlNjZiYTBiNjBlMjZhYjgxYWZlNjAxNjE5Mzk1NDcwM2Q0OGRlMDU0NTViZjI4YjVjODAzZDc4YzY4IgogICAgfQogIH0KfQ==";
  private static final String TEXTURE_SIGNATURE = "rgxwLer5BUA/kR+M6LyAvQHGCM4BAW9HCTvq67J3EqYX7QC71Qhwvj6al9l2hJyST0x1BW3n9ldB6snX35p7vKhMl+eL4EPc80fUYnKFkh+rZVRx48zr8mcrdOBmgEsYi0UQjBr9wueJPNiLrPiscUwXLWX2uBXgN10Kx9G9oP5aGqZw82vyMcZ5qibNU+cSpAdee6WCz9gabHfcwwmQKuG2YqBx5nnt7mSppzmCg6F8bZnjJx0BKP++HzwGC+gTOYQM9m4foDvmSaFwsj5EWMnQTwUPnyduBBg1iw635Zp3ImOx3f7rlju2EVi6D5WvVcTSXOmPNoiMWr8ZCknLXdUDrXGpLHGVmLdbV7Vj30vFQJAZPOxlLiy2WhEUtfb6zfBlcar8R3iMtuPkf5Qcxi29R2pqzQNZAcROwViBTxi1jLNbk9g6+StOi8+VU4siy2goL89E50S0431c7OyolneXybUsdXD8ZOU5xpcCphOwpTj2kWktWAwUULJgLbZYeITwxii1FMzOBlBMN8JhubD8dUp+keOQ5q6KpIJ2r2pvXA9WP6QjdyATEamilLucd/LxDRqJVe8vYMJk8DvFaaWICvlHP7yJjY5uHVqkbMLZV8pPnfGXQT1OuDBiGeESmNKoWHTbEnIyHBpegl1oDH3j2ejLkfB+OwVm36x3BOs=";

  static final String GIVEN_MSG = "Insomnia potion given.";

	public MakeShopNPCCommand(String name) {
		super(name,0);
	}

	@Override
	protected FailureCode execute(CommandSender sender, String label, String[] args) {
    // TODO: Make it so you can give it to another player by specifying their name

    if (!(sender instanceof Player)) {
      sender.sendMessage("You must be a player to run this command.");
      return FailureCode.NOT_A_PLAYER;
    }

    Player player = (Player) sender;
    Location loc = player.getLocation();
    NPC npc = PhantomMain.getNPCLib().createNPC(player, "SalesmanNPC", loc);
    // Set skin here
    npc.setSkin(new NPCSkin(TEXTURE, TEXTURE_SIGNATURE));
    // npc.setText("PhantomSalesman");
    npc.setCollidable(false);
    NPCSlot handSlot = NPCSlot.MAINHAND;

    npc.setItem(handSlot, CustomItemGenerator.getInsomniaPotion());

    npc.create();
    npc.show();

    player.sendMessage("NPC created successfully.");

		return FailureCode.SUCCESS;
	}

	@Override
	protected FailureCode isNecessary(CommandSender sender, String label, String[] args) {
		return FailureCode.SUCCESS;
	}

}
