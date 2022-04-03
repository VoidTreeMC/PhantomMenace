package com.condor.phantommenace.command.executors;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.OfflinePlayer;
import org.bukkit.Bukkit;

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

    if (!sender.hasPermission("condor.commands.executors.getcustomitem")) {
      sender.sendMessage("You do not have permission to use this command.");
      return FailureCode.PERMISSION_DENIED;
    }

    Player player = null;
    int quantity = 1;

    if (args.length < 1) {
      sender.sendMessage("Please provide an argument.");
      return FailureCode.NOT_AN_ARGUMENT;
    } else if (args.length == 1) {
      if (sender instanceof Player) {
        player = (Player) sender;
      } else {
        sender.sendMessage("Please provide the name of the player to give the item to.");
        return FailureCode.NOT_AN_ARGUMENT;
      }
    } else if (args.length >= 2) {
      String playerName = args[1];
      OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(playerName);
      if (offlinePlayer == null) {
        sender.sendMessage("Couldn't find a player by that name");
        return FailureCode.NOT_A_PLAYER;
      }
      player = offlinePlayer.getPlayer();
      if (player == null) {
        sender.sendMessage("Player is currently offline.");
        return FailureCode.PLAYER_OFFLINE;
      }
      if (args.length >= 3) {
        try {
          quantity = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
          sender.sendMessage("Invalid quantity entered.");
          return FailureCode.INVALID_NUMBER;
        }
      }
    }

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
      case "diamondvoucher":
        is = CustomItemManager.getItemByType(CustomItemType.DIAMOND_VOUCHER).getInstance();
        break;
      case "netheritevoucher":
        is = CustomItemManager.getItemByType(CustomItemType.NETHERITE_VOUCHER).getInstance();
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
      case "replanterhoe":
      case "wandofregeneration":
        is = CustomItemManager.getItemByType(CustomItemType.REPLANTER_HOE).getInstance();
        break;
      case "foliageaxe":
        is = CustomItemManager.getItemByType(CustomItemType.FOLIAGE_AXE).getInstance();
        break;
    }

    if (is != null) {
      if (is.getMaxStackSize() > quantity) {
        is.setAmount(quantity);
        HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(is);
        // Drop the leftovers on the ground
        for (ItemStack item : leftovers.values()) {
          player.getLocation().getWorld().dropItem(player.getLocation(), item);
        }
      } else {
        for (int i = 0; i < quantity; i++) {
          HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(is);
          // Drop the leftovers on the ground
          for (ItemStack item : leftovers.values()) {
            player.getLocation().getWorld().dropItem(player.getLocation(), item);
          }
        }
      }
      sender.sendMessage(GIVEN_MSG);
    } else {
      sender.sendMessage("An error was encountered. Perhaps there is no item by that name.");
    }

		return FailureCode.SUCCESS;
	}

	@Override
	protected FailureCode isNecessary(CommandSender sender, String label, String[] args) {
		return FailureCode.SUCCESS;
	}

}
