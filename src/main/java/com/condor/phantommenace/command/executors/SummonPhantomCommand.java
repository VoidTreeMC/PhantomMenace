package com.condor.phantommenace.command.executors;

import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import com.condor.phantommenace.command.CommandControl;
import com.condor.phantommenace.command.SubCommand;
import com.condor.phantommenace.phantom.PhantomStatus;
import com.condor.phantommenace.phantom.PhantomType;
import com.condor.phantommenace.phantom.PhantomGenerator;

public class SummonPhantomCommand extends CommandControl {

  static final String SUMMON_MSG = "Phantom summoned.";
  static final String UNKNOWN_TYPE_MSG = "Unknown phantom type.";
  static final String NOT_A_PLAYER_MSG = "You must be a player to execute this command.";

	public SummonPhantomCommand(String name) {
		super(name,0);
	}

	@Override
	protected FailureCode execute(CommandSender sender, String label, String[] args) {
    // TODO: Let player specify the location, and let a console/command block summon them if a location is specified.

    if (!sender.hasPermission("condor.commands.executors.summonphantom")) {
      sender.sendMessage("You do not have permission to use this command.");
      return FailureCode.PERMISSION_DENIED;
    }

    PhantomType type = PhantomType.VANILLA;
    if (!(sender instanceof Player)) {
      sender.sendMessage(NOT_A_PLAYER_MSG);
    }
    Player player = (Player) sender;
		if (args.length >= 1) {
      String typeStr = args[0].toLowerCase();
      switch (typeStr) {
        case "vanilla":
          type = PhantomType.VANILLA;
          sender.sendMessage(SUMMON_MSG);
          break;
        case "extraxp":
        case "xp":
          type = PhantomType.EXTRA_XP_PHANTOM;
          sender.sendMessage(SUMMON_MSG);
          break;
        case "flaming":
          type = PhantomType.FLAMING_PHANTOM;
          sender.sendMessage(SUMMON_MSG);
          break;
        case "skeletonphantom":
        case "mounted":
        case "mountedphantom":
          type = PhantomType.MOUNTED_PHANTOM;
          sender.sendMessage(SUMMON_MSG);
          break;
        case "invis":
        case "invisible":
          type = PhantomType.INVISIBLE_PHANTOM;
          sender.sendMessage(SUMMON_MSG);
          break;
        case "ender":
        case "enderphantom":
        case "ender_phantom":
        case "ender-phantom":
          type = PhantomType.ENDER_PHANTOM;
          sender.sendMessage(SUMMON_MSG);
          break;
        case "moap":
        case "motherofallphantoms":
          type = PhantomType.MOTHER_OF_ALL_PHANTOMS;
          sender.sendMessage(SUMMON_MSG);
          break;
        case "kamikaze":
        case "kamikazephantom":
        case "kamikaze_phantom":
          type = PhantomType.KAMIKAZE_PHANTOM;
          sender.sendMessage(SUMMON_MSG);
          break;
        default:
          sender.sendMessage(UNKNOWN_TYPE_MSG);
          break;
      }

    }

    Location playerPos = player.getLocation();
    PhantomGenerator.summonPhantom(type, playerPos);
		return FailureCode.SUCCESS;
	}

	@Override
	protected FailureCode isNecessary(CommandSender sender, String label, String[] args) {
		return FailureCode.SUCCESS;
	}

}
