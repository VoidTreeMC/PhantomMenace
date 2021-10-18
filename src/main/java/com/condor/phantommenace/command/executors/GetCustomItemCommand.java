package com.condor.phantommenace.command.executors;

import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.condor.phantommenace.command.CommandControl;
import com.condor.phantommenace.command.SubCommand;
import com.condor.phantommenace.item.CustomItemGenerator;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.item.CustomItemManager;

public class GetCustomItemCommand extends CommandControl {

  static final String GIVEN_MSG = "Legendary item given.";

	public GetCustomItemCommand(String name) {
		super(name,0);
	}

	@Override
	protected FailureCode execute(CommandSender sender, String label, String[] args) {
    // TODO: Make it so you can give it to another player by specifying their name

    if (!sender.hasPermission("condor.commands.executors.getcustomitem")) {
      sender.sendMessage("You do not have permission to use this command.");
      return FailureCode.PERMISSION_DENIED;
    }

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
      case "voidcoin":
      case "voidcoins":
        is = CustomItemManager.getItemByType(CustomItemType.DEFENDER_TOKEN).getInstance();
        break;
      case "enderblade":
        is = CustomItemManager.getItemByType(CustomItemType.ENDER_BLADE).getInstance();
        break;
      case "subduedenderblade":
        is = CustomItemManager.getItemByType(CustomItemType.SUBDUED_ENDER_BLADE).getInstance();
        break;
      case "prideshears":
        is = CustomItemManager.getItemByType(CustomItemType.PRIDE_SHEARS).getInstance();
        break;
      case "creeperbow":
        is = CustomItemManager.getItemByType(CustomItemType.CREEPER_BOW).getInstance();
        break;
      case "lavawalkers":
        is = CustomItemManager.getItemByType(CustomItemType.LAVA_WALKERS).getInstance();
        break;
      case "creeperfirework":
        is = CustomItemManager.getItemByType(CustomItemType.CREEPER_FIREWORK).getInstance();
        break;
      case "superpick":
      case "tab":
      case "thickassboy":
      case "thickassboye":
        is = CustomItemManager.getItemByType(CustomItemType.SUPER_PICK).getInstance();
        break;
      case "slayersword":
        is = CustomItemManager.getItemByType(CustomItemType.SLAYER_SWORD).getInstance();
        break;
      case "flightpotion":
        is = CustomItemManager.getItemByType(CustomItemType.FLIGHT_POTION).getInstance();
        break;
      case "coppervoucher":
        is = CustomItemManager.getItemByType(CustomItemType.COPPER_VOUCHER).getInstance();
        break;
      case "ironvoucher":
        is = CustomItemManager.getItemByType(CustomItemType.IRON_VOUCHER).getInstance();
        break;
      case "goldvoucher":
        is = CustomItemManager.getItemByType(CustomItemType.GOLD_VOUCHER).getInstance();
        break;
      case "cowvoucher":
        is = CustomItemManager.getItemByType(CustomItemType.COW_VOUCHER).getInstance();
        break;
      case "fishvoucher":
        is = CustomItemManager.getItemByType(CustomItemType.FISH_VOUCHER).getInstance();
        break;
      case "beevoucher":
        is = CustomItemManager.getItemByType(CustomItemType.BEE_VOUCHER).getInstance();
        break;
      case "almondcake":
      case "almond_cake":
        is = CustomItemManager.getItemByType(CustomItemType.ALMOND_CAKE).getInstance();
        break;
      case "bedrockbreaker":
        is = CustomItemManager.getItemByType(CustomItemType.BEDROCK_BREAKER).getInstance();
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
