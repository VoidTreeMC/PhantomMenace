package com.condor.phantommenace.command.executors;

import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.condor.phantommenace.command.CommandControl;
import com.condor.phantommenace.command.SubCommand;
import com.condor.phantommenace.phantom.PhantomStatus;
import com.condor.phantommenace.main.PhantomMain;

public class StartPhantomEventCommand extends CommandControl {

  static final String MSG = "Phantom event beginning.";

	public StartPhantomEventCommand(String name) {
		super(name,0);
	}

	@Override
	protected FailureCode execute(CommandSender sender, String label, String[] args) {

    if (!sender.hasPermission("condor.commands.executors.startphantomevent")) {
      sender.sendMessage("You do not have permission to start a phantom event.");
      return FailureCode.PERMISSION_DENIED;
    }

    PhantomStatus.setEnabled(true);
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
