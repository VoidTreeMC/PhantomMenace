package condor.main;

import java.sql.Connection;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.RegisteredServiceProvider;

import condor.command.CommandControl;
import condor.listener.PHListener;
import condor.npc.NPCManager;
import condor.event.PhantomEvent;

public class PhantomMain extends JavaPlugin {

	public static final String HEIRO = "<F#SDF";

	public static final String VERSION = "v0.0.2";

	public static final String TIMEID = HEIRO + " " + VERSION;

	static {
		System.out.println("PhantomMain: [" + TIMEID + "]");
	}

  public PhantomEvent phantomEvent;

	private static PhantomMain plugin;
	public static PhantomMain getPlugin() {
		return plugin;
	}
	/**
	 * Debug printing for this class
	 */
	public static boolean
		bc = false,
		log = false;

	/**
	 * Our instance
	 */
	//public static RaceWarsMain instance = new RaceWarsMain();

	public PhantomMain() {
		System.out.println("Initializing");
	}

	static final String RWLOAD =
			"\n"
			+ "-----------------\n"
			+ "-----------------\n"
			+ "Plugin loading...\n"
			+ "-----------------\n"
			+ "-----------------\n";

	@Override
	public void onEnable() {
		plugin = this;
		getLogger().info(RWLOAD + TIMEID);

		System.out.println("Command Control...");

		System.out.println("<><><><><><><><><>");
		CommandControl.loadExecutors(this);
		System.out.println("<><><><><><><><><>");

    System.out.println("<><><><><><><><><>");
    System.out.println("Loading NPCs...");
    NPCManager.init();
    System.out.println("<><><><><><><><><>");

    System.out.println("<><><><><><><><><>");
    System.out.println("Loading (but not starting) Phantom Event...");
    this.phantomEvent = new PhantomEvent(0);
    System.out.println("<><><><><><><><><>");

		//This registers the listener
		System.out.println("Loading listeners...");
		try {
			PHListener.loadListeners(this);
		}
		catch(Exception e) {

		}

		System.out.println("Calling onStart...");
		this.onStart();

    System.out.println("Loading has been completed!");

	}

  public PhantomEvent getPhantomEvent() {
    return this.phantomEvent;
  }

	/**
	 * Called on server shutdown <p>
	 *
	 * 1. {@link OnPlayerJoinLeaveStartOrShutdown#shutdown()}
	 */
	@Override
	public void onDisable() {

	}

	//-------------------------------------------------------------------------------------

	/**
	 * Called on server start <p>
	 *
	 * 1. Calls {@link OnPlayerJoinLeaveStartOrShutdown#startup()}
	 */
	public void onStart() {

	}
}
