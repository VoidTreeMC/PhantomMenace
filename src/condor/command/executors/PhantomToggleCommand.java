package condor.command.executors;

import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import condor.command.CommandControl;
import condor.command.SubCommand;
import condor.phantom.PhantomStatus;

public class PhantomToggleCommand extends CommandControl {

  static final String ENABLE_MSG = "Phantoms are now enabled.";
  static final String DISABLE_MSG = "Phantoms are now disabled.";
  static final String INVALID_ARG_MSG = "Error: Invalid argument. Please use the words on/off, true/false, or enable/disable.";

	public PhantomToggleCommand(String name) {
		super(name,0);
	}

	@Override
	protected FailureCode execute(CommandSender sender, String label, String[] args) {


		//If there are no arguments
		if (args.length <= 0) {
      boolean currentStatus = PhantomStatus.isEnabled();
      PhantomStatus.setEnabled(!currentStatus);
      if (currentStatus) {
        sender.sendMessage(DISABLE_MSG);
      } else {
        sender.sendMessage(ENABLE_MSG);
      }
			return FailureCode.NOT_AN_ARGUMENT;
		} else {
      String todo = args[0].toLowerCase();
      boolean newStatus = false;
      boolean error = false;
      switch (todo) {
        case "on":
        case "true":
        case "enable":
          newStatus = true;
          break;
        case "off":
        case "false":
        case "disable":
          newStatus = false;
          break;
        default:
          error = true;
          sender.sendMessage(INVALID_ARG_MSG);
          break;
      }

      if (!error) {
        PhantomStatus.setEnabled(newStatus);
        if (newStatus) {
          sender.sendMessage(ENABLE_MSG);
        } else {
          sender.sendMessage(DISABLE_MSG);
        }
      }
    }

		return FailureCode.SUCCESS;
	}

	@Override
	protected FailureCode isNecessary(CommandSender sender, String label, String[] args) {
		return FailureCode.SUCCESS;
	}

}
