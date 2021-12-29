package com.condor.phantommenace.phantom;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.Location;

import com.condor.phantommenace.main.PhantomMain;

public class RecentPlayerDeaths {

  private static final String IMMUNE_MESSAGE = ChatColor.YELLOW + "You have died. You are immune to event damage for " + ChatColor.GOLD + "1 minute" + ChatColor.YELLOW + "." + ChatColor.GOLD + " If you attack during this time, " + ChatColor.UNDERLINE + "your immunity will expire.";
  private static final String EXPIRED = ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Your immunity has expired. You can take damage from the event once again.";

  public static final String DIED_DURING_EVENT_METADATA = "diedDuringEvent";
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("survival");
  private static final double X = 1498.5;
  private static final double Y = 77;
  private static final double Z = -2867.5;
  private static final float PITCH = -88.3f;
  private static final float YAW = -13.5f;
  public static final Location PHANTOM_EVENT_RESPAWN_LOCATION = new Location(WORLD, X, Y, Z, PITCH, YAW);

  private static HashMap<UUID, Long> idMap = new HashMap<>();

  private static void messagePlayer(UUID uuid, String message) {
    Bukkit.getLogger().log(Level.INFO, "Sending death immunity-related message to " + uuid + ", : " + message);
    Bukkit.getPlayer(uuid).sendMessage(message);
  }

  public static boolean isPlayerOnList(UUID uuid) {
    return idMap.containsKey(uuid);
  }

  public static void removeFromList(UUID uuid, long time) {
    if (idMap.get(uuid) != null && idMap.get(uuid) == time) {
      messagePlayer(uuid, EXPIRED);
      idMap.remove(uuid);
    }
  }

  public static void removeFromList(UUID uuid) {
    if (idMap.get(uuid) != null) {
      messagePlayer(uuid, EXPIRED);
      idMap.remove(uuid);
    }
  }

  public static void addToList(UUID uuid, long time) {
    messagePlayer(uuid, IMMUNE_MESSAGE);
    idMap.put(uuid, time);
  }
}
