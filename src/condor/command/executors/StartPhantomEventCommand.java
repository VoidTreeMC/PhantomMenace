package condor.command.executors;

import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import condor.command.CommandControl;
import condor.command.SubCommand;
import condor.phantom.PhantomStatus;
import condor.main.PhantomMain;

public class StartPhantomEventCommand extends CommandControl {

  static final String MSG = "Phantom event beginning.";

	public StartPhantomEventCommand(String name) {
		super(name,0);
	}

	@Override
	protected FailureCode execute(CommandSender sender, String label, String[] args) {
    // This *may* fail if executed a second time
    PhantomMain.getPlugin().getPhantomEvent().runTask(PhantomMain.getPlugin());
    sender.sendMessage(MSG);

		return FailureCode.SUCCESS;
	}

	@Override
	protected FailureCode isNecessary(CommandSender sender, String label, String[] args) {
		return FailureCode.SUCCESS;
	}

}
