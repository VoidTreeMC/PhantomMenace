package com.condor.phantommenace.main;

import java.sql.Connection;
import java.util.Collection;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.condor.phantommenace.command.CommandControl;
import com.condor.phantommenace.listener.PHListener;
import com.condor.phantommenace.npc.NPCManager;
import com.condor.phantommenace.event.PhantomEvent;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class PhantomMain extends JavaPlugin {

	public static final String HEIRO = "<F#SDF";

	public static final String VERSION = "v0.0.2";

	public static final String TIMEID = HEIRO + " " + VERSION;

	static {
    Bukkit.getLogger().log(Level.INFO, "PhantomMain: [" + TIMEID + "]");
	}

  public PhantomEvent phantomEvent;

  public Economy econ = null;

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
    Bukkit.getLogger().log(Level.INFO, "Initializing");
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


		Bukkit.getLogger().log(Level.INFO, "Command Control...");

		Bukkit.getLogger().log(Level.INFO, "<><><><><><><><><>");
		CommandControl.loadExecutors(this);
		Bukkit.getLogger().log(Level.INFO, "<><><><><><><><><>");

    Bukkit.getLogger().log(Level.INFO, "<><><><><><><><><>");
    Bukkit.getLogger().log(Level.INFO, "Loading NPCs...");
    NPCManager.init();
    Bukkit.getLogger().log(Level.INFO, "<><><><><><><><><>");

    Bukkit.getLogger().log(Level.INFO, "<><><><><><><><><>");
    Bukkit.getLogger().log(Level.INFO, "Loading (but not starting) Phantom Event...");
    this.phantomEvent = new PhantomEvent(0);
    Bukkit.getLogger().log(Level.INFO, "<><><><><><><><><>");

    Bukkit.getLogger().log(Level.INFO, "Loading vault-economy hook...");
    if (!setupEconomy()) {
      Bukkit.getLogger().log(Level.INFO, "Economy has failed to load.");
    }

		//This registers the listener
		Bukkit.getLogger().log(Level.INFO, "Loading listeners...");
		try {
			PHListener.loadListeners(this);
		}
		catch(Exception e) {

		}

		Bukkit.getLogger().log(Level.INFO, "Calling onStart...");
		this.onStart();

    Bukkit.getLogger().log(Level.INFO, "Loading has been completed!");

	}

  private boolean setupEconomy() {
    if (getServer().getPluginManager().getPlugin("Vault") == null) {
      return false;
    }
    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null) {
        return false;
    }
    econ = rsp.getProvider();
    return econ != null;
  }

  public PhantomEvent getPhantomEvent() {
    return this.phantomEvent;
  }

  public Economy getEconomy() {
    return econ;
  }

	/**
	 * Called on server shutdown <p>
	 *
	 * 1. {@link OnPlayerJoinLeaveStartOrShutdown#shutdown()}
	 */
	@Override
	public void onDisable() {
		if (PhantomEvent.getBossBar() != null) {
			PhantomEvent.getBossBar().removeAll();
		}
    if (PhantomEvent.moapBar != null) {
      PhantomEvent.moapBar.removeAll();
    }
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
