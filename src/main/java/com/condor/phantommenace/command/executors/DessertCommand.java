package com.condor.phantommenace.command.executors;

import java.util.HashMap;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.condor.phantommenace.command.CommandControl;
import com.condor.phantommenace.command.SubCommand;
import com.condor.phantommenace.item.CustomItemGenerator;

public class DessertCommand extends CommandControl {

	public DessertCommand(String name) {
		super(name,0);
	}

	@Override
	protected FailureCode execute(CommandSender sender, String label, String[] args) {

    if (!(sender instanceof Player)) {
      sender.sendMessage("You must be a player to run this command.");
      return FailureCode.NOT_A_PLAYER;
    }

    Player player = (Player) sender;

    ArrayList<String> possibleKits = new ArrayList<>();
    possibleKits.add("copper");
    possibleKits.add("iron");
    possibleKits.add("gold");
    possibleKits.add("diamond");
    possibleKits.add("netherite");
    possibleKits.add("cow");
    possibleKits.add("fish");
    possibleKits.add("bee");

    for (String kit : possibleKits) {
      if (sender.hasPermission("essentials.kits." + kit)) {
        player.performCommand("kit " + kit);
      }
    }

		return FailureCode.SUCCESS;
	}

	@Override
	protected FailureCode isNecessary(CommandSender sender, String label, String[] args) {
		return FailureCode.SUCCESS;
	}

}
