package com.condor.phantommenace.command.executors;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.condor.phantommenace.command.CommandControl;
import com.condor.phantommenace.command.SubCommand;
import com.condor.phantommenace.phantom.PhantomStatus;
import com.condor.phantommenace.event.PhantomEvent;

public class EndEventCommand extends CommandControl {

  static final String EVENT_END_MSG = "Phantom Event terminated.";

	public EndEventCommand(String name) {
		super(name,0);
	}

	@Override
	protected FailureCode execute(CommandSender sender, String label, String[] args) {

    if (!sender.hasPermission("condor.commands.executors.endevent")) {
      sender.sendMessage("You do not have permission to use this command.");
      return FailureCode.PERMISSION_DENIED;
    }

    PhantomEvent.endEvent(true);

		return FailureCode.SUCCESS;
	}

	@Override
	protected FailureCode isNecessary(CommandSender sender, String label, String[] args) {
		return FailureCode.SUCCESS;
	}

}
