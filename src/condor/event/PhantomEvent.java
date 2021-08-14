package condor.event;

import java.util.ArrayList;
import java.util.TreeMap;
import java.lang.Integer;
import java.util.UUID;
import java.util.SortedSet;
import java.util.Map;
import java.util.Comparator;
import java.util.TreeSet;
import java.lang.Math;

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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Color;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatMessageType;

import condor.event.waves.*;
import condor.main.PhantomMain;
import condor.phantom.PhantomStatus;
import condor.item.CustomItemManager;
import condor.item.CustomItemType;
import condor.runnable.DoFireworkShow;
import condor.runnable.SetTimeInWorld;

import net.milkbowl.vault.economy.Economy;

public class PhantomEvent extends BukkitRunnable {
  private static TreeMap<UUID, Integer> totalPhantomKillMap = new TreeMap<>();
  private static ArrayList<TreeMap<UUID, Integer>> waveKillMapList = new ArrayList<>();
  private static ArrayList<Wave> waveList = new ArrayList<>();
  private static final World WORLD = PhantomMain.getPlugin().getServer().getWorld("lobby");
  private static ArrayList<ArrayList<ItemStack>> itemAwards = new ArrayList<>();
  private static ArrayList<Double> moneyAwards = new ArrayList<>();
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

  private static long timeBeforeSetting = 0;

  static {
    waveList.add(new VanillaWave());
    waveList.add(new FlamingAndExpWave());
    waveList.add(new AllFlamingWave());
    waveList.add(new InvisibleAndFlamingWave());
    waveList.add(new InvisibleAndMountedWave());
    waveList.add(new EnderAndInvisibleWave());
    waveList.add(new AllPhantomsWave());
    waveList.add(new MOAPWave());
    for (int i = 0; i < waveList.size(); i++) {
      waveKillMapList.add(new TreeMap<UUID, Integer>());
    }

    ArrayList<ItemStack> firstPlaceItems = new ArrayList<>();
    firstPlaceItems.add(CustomItemManager.getItemByType(CustomItemType.DEFENDER_TOKEN).getInstance());
    firstPlaceItems.get(0).setAmount(20);
    ArrayList<ItemStack> secondPlaceItems = new ArrayList<>();
    secondPlaceItems.add(CustomItemManager.getItemByType(CustomItemType.DEFENDER_TOKEN).getInstance());
    secondPlaceItems.get(0).setAmount(15);
    ArrayList<ItemStack> thirdPlaceItems = new ArrayList<>();
    thirdPlaceItems.add(CustomItemManager.getItemByType(CustomItemType.DEFENDER_TOKEN).getInstance());
    thirdPlaceItems.get(0).setAmount(10);
    ArrayList<ItemStack> fourthPlaceItems = new ArrayList<>();
    ArrayList<ItemStack> fifthPlaceItems = new ArrayList<>();

    itemAwards.add(firstPlaceItems);
    itemAwards.add(secondPlaceItems);
    itemAwards.add(thirdPlaceItems);
    itemAwards.add(fourthPlaceItems);
    itemAwards.add(fifthPlaceItems);

    moneyAwards.add(10000d);
    moneyAwards.add(7500d);
    moneyAwards.add(5000d);
    moneyAwards.add(5000d);
    moneyAwards.add(5000d);
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
    timeBeforeSetting = loc.getWorld().getTime();
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "region flag phantomarena -w lobby mob-spawning allow");
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doDaylightCycle false");
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set night");
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doWeatherCycle false");
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "weather lobby sun");
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "announcer togglebroadcasts");
  }

  /**
   * Resets the event and prepares it to be run again.
   */
  public static void reset() {
    totalPhantomKillMap.clear();
    waveKillMapList.clear();
    for (int i = 0; i < waveList.size(); i++) {
      waveKillMapList.add(new TreeMap<UUID, Integer>());
    }
    numKilledThisWave = 0;
    totalThisWave = 0;
    PhantomMain.getPlugin().phantomEvent = new PhantomEvent(0);
    (new SetTimeInWorld(loc.getWorld(), timeBeforeSetting)).runTaskLater(PhantomMain.getPlugin(), 10 * 20);
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "region flag phantomarena -w lobby mob-spawning deny");
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doDaylightCycle true");
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doWeatherCycle true");
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "announcer togglebroadcasts");
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
      printTopFive(true);
      awardTopFive();
      (new DoFireworkShow(loc)).runTask(PhantomMain.getPlugin());
      reset();
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
      printTopFive(false);
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

  public static void printTopFive(boolean forEvent) {
    SortedSet<Map.Entry<UUID, Integer>> sorted = null;
    if (forEvent) {
      Bukkit.broadcastMessage(ChatColor.YELLOW + "Top " + ChatColor.GOLD + "5 " + ChatColor.YELLOW + "players");
      Bukkit.broadcastMessage(ChatColor.GREEN + "=======================");
      sorted = getTopFivePlayersForEvent();
    } else {
      int num = getWaveIndex() - 1;
      Bukkit.broadcastMessage(ChatColor.YELLOW + "Top " + ChatColor.GOLD + "5 " + ChatColor.YELLOW + "players for Wave " + ChatColor.GOLD + (num + 1));
      Bukkit.broadcastMessage(ChatColor.GREEN + "=======================");
      sorted = getTopFivePlayersForWave();
    }
    int i = 0;
    for (Map.Entry<UUID, Integer> entry : sorted) {
      // Only the top 5
      if (++i > 5) {
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

    for (int i = 0; i < Math.min(5, numParticipants); i++) {
      Player awardRecipient = Bukkit.getPlayer(((Map.Entry<UUID, Integer>) sorted[i]).getKey());
      double moneyAmt = moneyAwards.get(i);
      ArrayList<ItemStack> itemsToAward = itemAwards.get(i);
      String position = "";
      switch (i) {
        case 0:
          position = "first";
          break;
        case 1:
          position = "second";
          break;
        case 2:
          position = "third";
          break;
        case 3:
          position = "fourth";
          break;
        case 4:
          position = "fifth";
          break;
      }
      awardRecipient.sendMessage(ChatColor.GREEN + "=======================");
      awardRecipient.sendMessage(ChatColor.YELLOW + "Congratulations! You came in " + position + " place. You have won");
      awardRecipient.sendMessage(ChatColor.GREEN + "- " + ChatColor.GOLD + "$" + String.format("%.02f", moneyAmt));
      economy.depositPlayer(awardRecipient, moneyAmt);
      for (ItemStack is : itemsToAward) {
        awardRecipient.getInventory().addItem(is);
        ItemMeta meta = is.getItemMeta();
        if (CustomItemType.getTypeFromCustomItem(is) == CustomItemType.DEFENDER_TOKEN) {
          awardRecipient.sendMessage(ChatColor.GREEN + "- " + ChatColor.GOLD + is.getAmount() + " " + meta.getDisplayName() + "s");
        } else {
          awardRecipient.sendMessage(ChatColor.GREEN + "- " + ChatColor.GOLD + is.getAmount() + " " + meta.getDisplayName());
        }
      }
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
