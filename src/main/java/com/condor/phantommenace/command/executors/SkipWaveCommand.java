package com.condor.phantommenace.command.executors;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.condor.phantommenace.command.CommandControl;
import com.condor.phantommenace.command.SubCommand;
import com.condor.phantommenace.phantom.PhantomStatus;
import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.event.PhantomEvent;

public class SkipWaveCommand extends CommandControl {

  static final String MSG = "Skipping wave.";
  static final String NOT_ACTIVE = "There is no phantom event active.";

	public SkipWaveCommand(String name) {
		super(name,0);
	}

	@Override
	protected FailureCode execute(CommandSender sender, String label, String[] args) {

    if (!sender.hasPermission("condor.commands.executors.skipwave")) {
      sender.sendMessage("You do not have permission to skip phantom waves.");
      return FailureCode.PERMISSION_DENIED;
    }

    if (PhantomEvent.isActive()) {
      PhantomMain.getPlugin().getPhantomEvent().skipWave();
      sender.sendMessage(MSG);
    } else {
      sender.sendMessage(NOT_ACTIVE);
    }


		return FailureCode.SUCCESS;
	}

	@Override
	protected FailureCode isNecessary(CommandSender sender, String label, String[] args) {
		return FailureCode.SUCCESS;
	}

}
