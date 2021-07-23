package condor.phantom;

import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class PhantomDropTable {

/*
*   Item	Rarity (%)	Amount
* Phantom membrane	75.00%	1-3
* Quartz	25.00%	1-2
* Amethyst shard	5.00%	1
* Crying obsidian	1.00%	1
*/

  private static Random rng = new Random();
  private static final int ITEM_INDEX = 0;
  private static final int RARITY_INDEX = 1;
  private static final int MIN_AMT_INDEX = 2;
  private static final int MAX_AMT_INDEX = 3;

  // Protocol: ItemStack, Rarity(double), min amount (int), max amount(int)
  private static ArrayList<Object[]> vanillaDrops = new ArrayList<>();
  private static ArrayList<Object[]> extraXPPhantomDrops = new ArrayList<>();
  private static ArrayList<Object[]> flamingPhantomDrops = new ArrayList<>();
  private static ArrayList<Object[]> invisiblePhantomDrops = new ArrayList<>();
  private static ArrayList<Object[]> mountedPhantomDrops = new ArrayList<>();
  private static ArrayList<Object[]> enderPhantomDrops = new ArrayList<>();

  private static TreeMap<PhantomType, ArrayList<Object[]>> dropMap = new TreeMap<>();
  private static TreeMap<PhantomType, Integer> xpMap = new TreeMap<>();

  static {
    // Initialize drop lists here
    vanillaDrops.add(constructDrop(new ItemStack(Material.PHANTOM_MEMBRANE), 0.75, 1, 3));
    vanillaDrops.add(constructDrop(new ItemStack(Material.QUARTZ), 0.25, 1, 2));
    vanillaDrops.add(constructDrop(new ItemStack(Material.AMETHYST_SHARD), 0.05, 1, 1));
    vanillaDrops.add(constructDrop(new ItemStack(Material.CRYING_OBSIDIAN), 0.01, 1, 1));

    flamingPhantomDrops.add(constructDrop(new ItemStack(Material.GLOWSTONE_DUST), 0.75, 1, 3));
    flamingPhantomDrops.add(constructDrop(new ItemStack(Material.BLAZE_POWDER), 0.75, 1, 3));

    mountedPhantomDrops.add(constructDrop(new ItemStack(Material.PHANTOM_MEMBRANE), 0.75, 1, 3));
    mountedPhantomDrops.add(constructDrop(new ItemStack(Material.QUARTZ), 0.25, 1, 2));
    mountedPhantomDrops.add(constructDrop(new ItemStack(Material.AMETHYST_SHARD), 0.05, 1, 1));
    mountedPhantomDrops.add(constructDrop(new ItemStack(Material.CRYING_OBSIDIAN), 0.01, 1, 1));
    mountedPhantomDrops.add(constructDrop(new ItemStack(Material.SKELETON_SPAWN_EGG), 0.01, 1, 1));

    // Append to drop map
    dropMap.put(PhantomType.VANILLA, vanillaDrops);
    dropMap.put(PhantomType.EXTRA_XP_PHANTOM, extraXPPhantomDrops);
    dropMap.put(PhantomType.FLAMING_PHANTOM, flamingPhantomDrops);
    dropMap.put(PhantomType.INVISIBLE_PHANTOM, invisiblePhantomDrops);
    dropMap.put(PhantomType.MOUNTED_PHANTOM, mountedPhantomDrops);
    dropMap.put(PhantomType.ENDER_PHANTOM, enderPhantomDrops);

    // Append to XP map
    xpMap.put(PhantomType.VANILLA, 50);
    xpMap.put(PhantomType.EXTRA_XP_PHANTOM, 75);
    xpMap.put(PhantomType.FLAMING_PHANTOM, 100);
    xpMap.put(PhantomType.INVISIBLE_PHANTOM, 125);
    xpMap.put(PhantomType.MOUNTED_PHANTOM, 150);
    xpMap.put(PhantomType.ENDER_PHANTOM, 200);
  }

  public static ArrayList<ItemStack> getDrops(PhantomType type, double rolledNum) {
    ArrayList<Object[]> dropTable = getDropTable(type);
    ArrayList<ItemStack> drops = new ArrayList<>();
    for (Object[] possibleDrop : dropTable) {
      double rarity = (double) possibleDrop[RARITY_INDEX];
      if (rolledNum <= rarity) {
        int minAmt = (int) possibleDrop[MIN_AMT_INDEX];
        int maxAmt = (int) possibleDrop[MAX_AMT_INDEX];
        int amt = rng.nextInt(maxAmt) + minAmt;
        drops.add(new ItemStack(((ItemStack) possibleDrop[ITEM_INDEX]).getType(), amt));
      }
    }
    return drops;
  }

  public static Object[] constructDrop(ItemStack item, double rarity, int minAmt, int maxAmt) {
    Object[] ret = {item, rarity, minAmt, maxAmt};
    return ret;
  }

  public static ArrayList<Object[]> getDropTable(PhantomType type) {
    return (ArrayList<Object[]>) dropMap.get(type);
  }

  public static int getXP(PhantomType type) {
    return xpMap.get(type);
  }
}
