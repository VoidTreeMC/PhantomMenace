package condor.event;

import java.util.ArrayList;
import java.util.TreeMap;
import java.lang.Integer;
import java.util.UUID;
import java.util.SortedSet;
import java.util.Map;
import java.util.Comparator;
import java.util.TreeSet;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Phantom;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatMessageType;

import condor.event.waves.*;
import condor.main.PhantomMain;
import condor.phantom.PhantomStatus;
import condor.item.CustomItemManager;
import condor.item.CustomItemType;

import net.milkbowl.vault.economy.Economy;

public class PhantomEvent extends BukkitRunnable {
  private static TreeMap<UUID, Integer> totalPhantomKillMap = new TreeMap<>();
  private static ArrayList<TreeMap<UUID, Integer>> waveKillMapList = new ArrayList<>();
  private static ArrayList<Wave> waveList = new ArrayList<>();
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("lobby");
  private static final double X = 3931.5;
  private static final double Y = 130;
  private static final double Z = 108.5;
  private static final Location loc = new Location(WORLD, X, Y, Z);

  public static final String EVENT_METADATA_KEY = "isAnEventPhantom";

  private static final String BOSS_BAR_TEXT = "Defend " + ChatColor.RED + "Void" + ChatColor.GRAY + "Tree" + ChatColor.RESET + " from the Phantoms! Wave ";
  // 10 seconds between waves
  private static final long TIME_BETWEEN_WAVES = 10 * 20;

  private JavaPlugin plugin;
  private int waveIndex = 0;
  private static boolean isActive = false;

  private static int numKilledThisWave = 0;
  private static int totalThisWave = 0;

  private static BossBar bossBar = null;

  static {
    waveList.add(new VanillaWave());
    // waveList.add(new FlamingAndExpWave());
    // waveList.add(new InvisibleAndFlamingWave());
    // waveList.add(new InvisibleAndMountedWave());
    // waveList.add(new EnderAndInvisibleWave());
    for (int i = 0; i < waveList.size(); i++) {
      waveKillMapList.add(new TreeMap<UUID, Integer>());
    }
  }

  public PhantomEvent(int waveIndex) {
    this.plugin = PhantomMain.getPlugin();
    this.waveIndex = waveIndex;
    PhantomMain.getPlugin().phantomEvent = this;
  }

  /**
   * Initializes the event
   */
  public static void init() {
    bossBar = Bukkit.createBossBar(BOSS_BAR_TEXT + (getWaveIndex() + 1), BarColor.RED, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);
    Bukkit.broadcastMessage(ChatColor.YELLOW + "Phantoms are invading " + ChatColor.GRAY + "Void" + ChatColor.RED +
                            "Tree" + ChatColor.RESET + "! Type " + ChatColor.GOLD + "/warp phantomarena" + ChatColor.YELLOW +
                            " to defend the server from the menace!");
    // Add it to all players
    for (Player p : Bukkit.getOnlinePlayers()) {
      bossBar.addPlayer(p);
    }
  }

  public void run() {
    if (waveIndex == 0) {
      init();
    }
    isActive = true;
    if (waveIndex < waveList.size()) {
      waveList.get(waveIndex++).spawnWave(loc);
      Bukkit.broadcastMessage(ChatColor.YELLOW + "Beginning wave " + ChatColor.GOLD + (waveIndex));
      bossBar.setProgress(1);
      bossBar.setTitle(BOSS_BAR_TEXT + (waveIndex));
    } else {
      // Event is now over! Reset the wave index and do event-ended stuff
      waveIndex = 0;
      isActive = false;
      // Remove bossbar from all palyers
      bossBar.removeAll();
      // Give message
      Bukkit.broadcastMessage(ChatColor.YELLOW + "The phantoms have been vanquished! Thank you for defending" + ChatColor.RED + " Void" + ChatColor.GRAY + "Tree");
      PhantomStatus.setEnabled(false);
      printTopFiveForEvent();
      // TODO: Do the top player prize awards here
      awardTopFive();
    }
  }

  public static void manageKill(Phantom phantom, Player player) {
    if (phantom.hasMetadata(EVENT_METADATA_KEY) && player != null) {
      UUID playerUUID = player.getUniqueId();
      // Add the kill to the player's kills this wave
      int numPlayerKilledThisWave = (waveKillMapList.get(getWaveIndex() - 1).get(playerUUID) == null) ? 0 : waveKillMapList.get(getWaveIndex() - 1).get(playerUUID);
      waveKillMapList.get(getWaveIndex() - 1).put(playerUUID, numPlayerKilledThisWave + 1);
      // Add the kill to the player's total kills this event
      int numPlayerKilledThisEvent = (totalPhantomKillMap.get(playerUUID) == null) ? 0 : totalPhantomKillMap.get(playerUUID);
      totalPhantomKillMap.put(playerUUID, numPlayerKilledThisEvent + 1);
      // player.sendMessage("You have killed " + waveKillMapList.get(getWaveIndex() - 1).get(player.getUniqueId()) + " phantoms this wave.");
    // We only want to increment the number on EntityDeathEvent, not on EntityDamageByEntityEvent (avoid double register)
    } else if (phantom.hasMetadata(EVENT_METADATA_KEY)) {
      numKilledThisWave++;
    }

    bossBar.setProgress(1 - ((0.0 + numKilledThisWave) / totalThisWave));

    for (Player p : Bukkit.getOnlinePlayers()) {
      TextComponent theText = new TextComponent("" + (totalThisWave - numKilledThisWave) + " phantoms remaining");
      p.spigot().sendMessage(ChatMessageType.ACTION_BAR, theText);
    }

    // If all of this wave's phantoms have been killed,
    // advance to the next wave
    if (numKilledThisWave >= totalThisWave) {
      int num = getWaveIndex();
      (new PhantomEvent(num)).runTaskLater(PhantomMain.getPlugin(), TIME_BETWEEN_WAVES);
      Bukkit.broadcastMessage(ChatColor.YELLOW + "Wave " + ChatColor.GOLD + getWaveIndex() + ChatColor.YELLOW + " completed!");
      printTopFiveForWave();
    }
  }

  // From https://stackoverflow.com/questions/2864840/treemap-sort-by-value
  private static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
            new Comparator<Map.Entry<K,V>>() {
                @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                    int res = -1 * (e1.getValue().compareTo(e2.getValue()));
                    return res != 0 ? res : 1; // Special fix to preserve items with equal values
                }
            }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

  public static SortedSet<Map.Entry<UUID, Integer>> getTopFivePlayersForWave() {
    return entriesSortedByValues(waveKillMapList.get(getWaveIndex() - 1));
  }

  public static SortedSet<Map.Entry<UUID, Integer>> getTopFivePlayersForEvent() {
    return entriesSortedByValues(totalPhantomKillMap);
  }

  public static void printTopFiveForWave() {
    int num = getWaveIndex() - 1;
    Bukkit.broadcastMessage(ChatColor.YELLOW + "Top " + ChatColor.GOLD + "5 " + ChatColor.YELLOW + "players for Wave " + ChatColor.GOLD + (num + 1));
    Bukkit.broadcastMessage(ChatColor.GREEN + "=======================");
    SortedSet<Map.Entry<UUID, Integer>> sorted = getTopFivePlayersForWave();
    int i = 0;
    for (Map.Entry<UUID, Integer> entry : sorted) {
      // Only the top 5
      if (++i >= 5) {
        break;
      }
      int numKills = entry.getValue();
      UUID playerUUID = entry.getKey();
      String playerName = PhantomMain.getPlugin().getServer().getOfflinePlayer(playerUUID).getName();
      Bukkit.broadcastMessage(ChatColor.YELLOW + playerName + ": " + ChatColor.GOLD + numKills + ChatColor.YELLOW + " phantoms killed.");
    }
  }

  public static void printTopFiveForEvent() {
    int num = getWaveIndex() - 1;
    Bukkit.broadcastMessage(ChatColor.YELLOW + "Top " + ChatColor.GOLD + "5 " + ChatColor.YELLOW + "players");
    Bukkit.broadcastMessage(ChatColor.GREEN + "=======================");
    SortedSet<Map.Entry<UUID, Integer>> sorted = getTopFivePlayersForEvent();
    int i = 0;
    for (Map.Entry<UUID, Integer> entry : sorted) {
      // Only the top 5
      if (++i >= 5) {
        break;
      }
      int numKills = entry.getValue();
      UUID playerUUID = entry.getKey();
      String playerName = PhantomMain.getPlugin().getServer().getOfflinePlayer(playerUUID).getName();
      Bukkit.broadcastMessage(ChatColor.YELLOW + playerName + ": " + ChatColor.GOLD + numKills + ChatColor.YELLOW + " phantoms killed.");
    }
  }

  public static void awardTopFive() {
    Object[] sorted = getTopFivePlayersForEvent().toArray();
    int numParticipants = sorted.length;
    Economy economy = PhantomMain.getPlugin().getEconomy();
    ItemStack voidCoins = CustomItemManager.getItemByType(CustomItemType.DEFENDER_TOKEN).getInstance();
    // First place
    if (numParticipants >= 1) {
      Player firstPlace = Bukkit.getPlayer(((Map.Entry<UUID, Integer>) sorted[0]).getKey());
      economy.depositPlayer(firstPlace, 10000);
      voidCoins.setAmount(20);
      firstPlace.getInventory().addItem(voidCoins);
      firstPlace.sendMessage(ChatColor.YELLOW + "Congratulations! You came in first place. You have been awarded " + ChatColor.GOLD + "$10,000, " + ChatColor.YELLOW + "and"  + ChatColor.GOLD + " 20 VoidCoins");
    }
    if (numParticipants >= 2) {
      Player secondPlace = Bukkit.getPlayer(((Map.Entry<UUID, Integer>) sorted[1]).getKey());
      economy.depositPlayer(secondPlace, 7500);
      voidCoins.setAmount(15);
      secondPlace.getInventory().addItem(voidCoins);
      secondPlace.sendMessage(ChatColor.YELLOW + "Congratulations! You came in second place. You have been awarded " + ChatColor.GOLD + "$7,500, " + ChatColor.YELLOW + "and"  + ChatColor.GOLD + " 15 VoidCoins");
    }
    if (numParticipants >= 3) {
      Player thirdPlace = Bukkit.getPlayer(((Map.Entry<UUID, Integer>) sorted[2]).getKey());
      economy.depositPlayer(thirdPlace, 5000);
      voidCoins.setAmount(10);
      thirdPlace.getInventory().addItem(voidCoins);
      thirdPlace.sendMessage(ChatColor.YELLOW + "Congratulations! You came in third place. You have been awarded " + ChatColor.GOLD + "$5,000, " + ChatColor.YELLOW + "and"  + ChatColor.GOLD + " 10 VoidCoins");
    }
    if (numParticipants >= 4) {
      Player fourthPlace = Bukkit.getPlayer(((Map.Entry<UUID, Integer>) sorted[3]).getKey());
      economy.depositPlayer(fourthPlace, 5000);
      fourthPlace.sendMessage(ChatColor.YELLOW + "Congratulations! You came in fourth place. You have been awarded " + ChatColor.GOLD + "$5,000");
    }
    if (numParticipants >= 5) {
      Player fifthPlace = Bukkit.getPlayer(((Map.Entry<UUID, Integer>) sorted[4]).getKey());
      economy.depositPlayer(fifthPlace, 5000);
      fifthPlace.sendMessage(ChatColor.YELLOW + "Congratulations! You came in fifth place. You have been awarded " + ChatColor.GOLD + "$5,000");
    }
  }

  public static BossBar getBossBar() {
    return bossBar;
  }

  /**
   * Sets the total number of phantoms
   * in this wave to the amount passed,
   * and resets the number of phantoms
   * killed this wave to 0
   * @param waveCount  The total number of phantoms in this wave
   */
  public void setWaveCount(int waveCount) {
    this.totalThisWave = waveCount;
    this.numKilledThisWave = 0;
  }

  public static boolean isActive() {
    return isActive;
  }

  public static int getWaveIndex() {
    return PhantomMain.getPlugin().getPhantomEvent().waveIndex;
  }

  public static void setWaveIndex(int num) {
    PhantomMain.getPlugin().getPhantomEvent().waveIndex = num;
  }

  public static Location getLocation() {
    return loc;
  }
}
