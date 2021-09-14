package com.condor.phantommenace.phantom;

import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class RecentPlayerDeaths {

  private static final String IMMUNE_MESSAGE = ChatColor.YELLOW + "You have died. You are immune to event damage for " + ChatColor.GOLD + "1 minute" + ChatColor.YELLOW + "." + ChatColor.GOLD + " If you attack during this time, " + ChatColor.UNDERLINE + "your immunity will expire.";
  private static final String EXPIRED = ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Your immunity has expired. You can take damage from the event once again.";

  private static TreeMap<UUID, Long> idMap = new TreeMap<>();

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
