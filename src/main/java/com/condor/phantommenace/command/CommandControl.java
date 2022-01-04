package com.condor.phantommenace.command;

import java.util.Map.Entry;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import com.condor.phantommenace.command.executors.PhantomToggleCommand;
import com.condor.phantommenace.command.executors.SummonPhantomCommand;
import com.condor.phantommenace.command.executors.GetInsomniaPotionCommand;
import com.condor.phantommenace.command.executors.GetCustomItemCommand;
import com.condor.phantommenace.command.executors.StartPhantomEventCommand;
import com.condor.phantommenace.command.executors.SkipWaveCommand;
import com.condor.phantommenace.command.executors.DessertCommand;
import com.condor.phantommenace.command.executors.EndEventCommand;

/**
 * Extends CommandExecutor to create new commands for RaceWars <br>
 * Commands must be given a name which is automatically toUPPER <br>
 * The same name MUST be in the plugin.yml file <br>
 * Commands must be initiated in "initExecutors" <br>
 * New Commands are added to the map of executors <br>
 * Call "loadExecutors" to load all in the plugin
 * @author Mexon
 *
 */
public abstract class CommandControl implements CommandExecutor {

	public enum FailureCode {
		UNINIT,
		SUCCESS,
		FAILURE,
		LENGTH_MISMATCH,
		TYPE_MISMATCH,
		EVALUATION,
		DNE,
		ENTITY_DNE,
		ENTITY_MISMATCH,
		NOT_A_PLAYER,
		TEST,
		NOT_AN_ARGUMENT,
    PLAYER_OFFLINE,
    PERMISSION_DENIED,
    STILL_PROCESSING;
	}



	private static HashMap<String,CommandControl> executors = new HashMap<>();

	public static void initExecutors() {
		new PhantomToggleCommand("togglephantoms");
    new SummonPhantomCommand("summonphantom");
    new GetInsomniaPotionCommand("getinsomniapotion");
    new GetCustomItemCommand("getcustomitem");
    new StartPhantomEventCommand("startphantomevent");
    new SkipWaveCommand("skipwave");
    new DessertCommand("dessert");
    new EndEventCommand("endevent");
	}

	/**
	 * Loops through all commands to load them with given JavaPlugin
	 * @param j javaplugin
	 */
	public static final void loadExecutors(JavaPlugin j) {

    Bukkit.getLogger().log(Level.INFO, "loadExecutors -> initExecutors");

		initExecutors();
    Bukkit.getLogger().log(Level.INFO, "initExecutors completed.");

		//loadall
		for(Entry<String,CommandControl> c : executors.entrySet()) {
			//IF A COMMAND IS NULL HERE - ITS PROBABLY NOT IN THE PLUGIN.YML DOOFUS

			j.getCommand(c.getKey()).setExecutor(c.getValue());
		}
	}

	//------------------------------------
	//--------------[END OF STATIC]----------------------
	//------------------------------------

	private String name;

	private final int ARGS_LENGTH;

	/**
	 * Constructor for All new commands.  <br>
	 * Any commands created this way must be declared in the initExecutors section
	 * @param name String of the command name that will be used
	 * @param argsLength int of required number of arguments, 0 if none required
	 */
	public CommandControl(String name, int argsLength) {

		//always upper
		this.name=name.toUpperCase();

		//sets our needed args length
		this.ARGS_LENGTH=argsLength;

		//places this isn the map
		executors.put(name, this);
	}

	/**
	 * @return object's declared name
	 */
	public String name() {
		return this.name;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		FailureCode ret = null;

		//success!
		FailureCode necc = null;
		if(this.ARGS_LENGTH==0) {
			necc = FailureCode.SUCCESS;
			// sender.sendMessage("no args required");
		}
		else if(args==null||args.length<this.ARGS_LENGTH) {
			necc = FailureCode.LENGTH_MISMATCH;
		}
		else {
			necc = this.isNecessary(sender, label, args);
		}

		// sender.sendMessage("necc was: " + necc);
		if(necc==FailureCode.SUCCESS) {
			// sender.sendMessage("command execing");
			ret = this.execute(sender, label, args);
		}
		//failure
		else {
			// sender.sendMessage("No Exec: Failure Code: " + necc);
		}

		return ret==FailureCode.SUCCESS;
	}

	/**
	 * Executes command with given arguments
	 * @param sender The command's sender
	 * @param label The command label
	 * @param args The arguments for the command
	 * @return The method's failure code
	 */
	protected abstract FailureCode execute(CommandSender sender, String label, String[] args);


	/**
	 * Used to determine if action is necessary <p>
	 * Called in evaluate before execute
	 * @param sender The command's sender
	 * @param label  The command's label
	 * @param args   The arguments for the command
	 * @return       The method's failure code
	 */
	protected abstract FailureCode isNecessary(CommandSender sender, String label, String[] args);

}
