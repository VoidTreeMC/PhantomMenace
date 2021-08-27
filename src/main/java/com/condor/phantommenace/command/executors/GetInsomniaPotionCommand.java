package com.condor.phantommenace.command.executors;

import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.condor.phantommenace.command.CommandControl;
import com.condor.phantommenace.command.SubCommand;
import com.condor.phantommenace.item.CustomItemGenerator;

public class GetInsomniaPotionCommand extends CommandControl {

  static final String GIVEN_MSG = "Insomnia potion given.";

	public GetInsomniaPotionCommand(String name) {
		super(name,0);
	}

	@Override
	protected FailureCode execute(CommandSender sender, String label, String[] args) {
    // TODO: Make it so you can give it to another player by specifying their name

    if (!sender.hasPermission("condor.commands.executors.getinsomniapotion")) {
      sender.sendMessage("You do not have permission to use this command.");
      return FailureCode.PERMISSION_DENIED;
    }

    if (!(sender instanceof Player)) {
      sender.sendMessage("You must be a player to run this command.");
      return FailureCode.NOT_A_PLAYER;
    }

    Player player = (Player) sender;
    ItemStack potion = CustomItemGenerator.getInsomniaPotion();
    player.getInventory().addItem(potion);
    sender.sendMessage(GIVEN_MSG);

		return FailureCode.SUCCESS;
	}

	@Override
	protected FailureCode isNecessary(CommandSender sender, String label, String[] args) {
		return FailureCode.SUCCESS;
	}

}
