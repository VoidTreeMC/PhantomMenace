package condor.command.executors;

import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import condor.command.CommandControl;
import condor.command.SubCommand;
import condor.item.CustomItemGenerator;
import condor.item.CustomItemType;
import condor.item.CustomItemManager;

public class GetCustomItemCommand extends CommandControl {

  static final String GIVEN_MSG = "Legendary item given.";

	public GetCustomItemCommand(String name) {
		super(name,0);
	}

	@Override
	protected FailureCode execute(CommandSender sender, String label, String[] args) {
    // TODO: Make it so you can give it to another player by specifying their name

    if (!(sender instanceof Player)) {
      sender.sendMessage("You must be a player to run this command.");
      return FailureCode.NOT_A_PLAYER;
    }

    if (args.length < 1) {
      sender.sendMessage("Please provide an argument.");
      return FailureCode.NOT_AN_ARGUMENT;
    }

    Player player = (Player) sender;
    String typeStr = args[0].toLowerCase();
    ItemStack is = null;
    switch (typeStr) {
      case "fancypants":
        is = CustomItemManager.getItemByType(CustomItemType.FANCY_PANTS).getInstance();
        break;
      case "insomniapotion":
        is = CustomItemGenerator.getInsomniaPotion();
        break;
      case "defendertoken":
        is = CustomItemManager.getItemByType(CustomItemType.DEFENDER_TOKEN).getInstance();
        break;
      case "enderblade":
        is = CustomItemManager.getItemByType(CustomItemType.ENDER_BLADE).getInstance();
        break;
    }

    if (is != null) {
      player.getInventory().addItem(is);
      sender.sendMessage(GIVEN_MSG);
    } else {
      sender.sendMessage("No item of that name was found.");
    }

		return FailureCode.SUCCESS;
	}

	@Override
	protected FailureCode isNecessary(CommandSender sender, String label, String[] args) {
		return FailureCode.SUCCESS;
	}

}
