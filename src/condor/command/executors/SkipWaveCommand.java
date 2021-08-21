package condor.command.executors;

import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import condor.command.CommandControl;
import condor.command.SubCommand;
import condor.phantom.PhantomStatus;
import condor.main.PhantomMain;

public class SkipWaveCommand extends CommandControl {

  static final String MSG = "Skipping wave.";

	public SkipWaveCommand(String name) {
		super(name,0);
	}

	@Override
	protected FailureCode execute(CommandSender sender, String label, String[] args) {

    if (!sender.hasPermission("condor.commands.executors.skipwave")) {
      sender.sendMessage("You do not have permission to skip phantom waves.");
      return FailureCode.PERMISSION_DENIED;
    }

    PhantomMain.getPlugin().getPhantomEvent().skipWave();
    sender.sendMessage(MSG);


		return FailureCode.SUCCESS;
	}

	@Override
	protected FailureCode isNecessary(CommandSender sender, String label, String[] args) {
		return FailureCode.SUCCESS;
	}

}
