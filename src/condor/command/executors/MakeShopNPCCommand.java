package condor.command.executors;

import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;

import condor.command.CommandControl;
import condor.command.SubCommand;
import condor.item.CustomItemGenerator;
import condor.main.PhantomMain;
import condor.runnable.MakeShopNPCRunnable;

public class MakeShopNPCCommand extends CommandControl {

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
    (new MakeShopNPCRunnable(player)).runTask(PhantomMain.getPlugin());

    player.sendMessage("NPC created successfully.");

		return FailureCode.SUCCESS;
	}

	@Override
	protected FailureCode isNecessary(CommandSender sender, String label, String[] args) {
		return FailureCode.SUCCESS;
	}

}
