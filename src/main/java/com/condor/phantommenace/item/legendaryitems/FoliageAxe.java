package com.condor.phantommenace.item.legendaryitems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Event;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.event.block.Action;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.runnable.DoDisableTargetAI;
import com.condor.phantommenace.runnable.DisplayCooldown;
import com.condor.phantommenace.runnable.RemoveMetadataAfterDelay;

public class FoliageAxe extends CustomItem {

  private static final String NAME = "Foliage Axe";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static Random rng = new Random();

  private HashMap<String, BlockFace> blockMap = new HashMap<>();

  private static final String METADATA_KEY = "isFoliageAxeAdjacent";
  private static final String RECENT_SAPLING_METADATA_KEY = "grewFromSapling";
  private static final int MAX_WIDTH = 5;
  private static final int MAX_HEIGHT = 30;

  // 15 second cooldown
  private static final long COOLDOWN_DURATION = 15 * 1000;

  private static HashMap<UUID, Long> mapOfTimesUsed = new HashMap<>();

  static {
    loreList.add("Foliage Axe");
    loreList.add("I'm a lumberjack and I'm okay.");
    loreList.add("");

    triggerList.add(BlockBreakEvent.class);
    triggerList.add(PlayerInteractEvent.class);
  }

  public FoliageAxe() {
    super(NAME, loreList, triggerList, CustomItemType.FOLIAGE_AXE, 50, false);
    loreList.add(this.getPrice() + " VoidCoins");
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.NETHERITE_AXE, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName(NAME);
    meta.setLore(loreList);
    meta.addEnchant(Enchantment.MENDING, 1, false);
    meta.addEnchant(Enchantment.DIG_SPEED, 5, false);
    meta.addEnchant(Enchantment.DURABILITY, 3, false);
    meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 4, true);
    // meta.setUnbreakable(true);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    boolean ret = false;
    if (event instanceof BlockBreakEvent) {
      BlockBreakEvent bbe = (BlockBreakEvent) event;
      Player player = bbe.getPlayer();
      ret = CustomItemType.getTypeFromCustomItem(player.getItemInHand()) == CustomItemType.FOLIAGE_AXE;
    } else if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      ret = CustomItemType.getTypeFromCustomItem(player.getItemInHand()) == CustomItemType.FOLIAGE_AXE;
      ret = ret && pie.getClickedBlock() != null;
    }
    return ret;
  }

  public void execute(Event event) {
    if (event instanceof BlockBreakEvent) {
      BlockBreakEvent bbe = (BlockBreakEvent) event;
      handleLeafDestroyer(bbe);
    } else {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      Block block = pie.getClickedBlock();
      Material blockType = block.getType();
      if (isLeaf(blockType)) {
        BlockFace blockFace = pie.getBlockFace();
        blockMap.put(block.getLocation().toString(), blockFace);
      } else if (pie.getAction() == Action.RIGHT_CLICK_BLOCK && player.isSneaking()) {
        if (isLog(blockType)) {
          if (block.hasMetadata(RECENT_SAPLING_METADATA_KEY)) {
            return;
          }
          long currTime = System.currentTimeMillis();
          long lastTimeUsed = 0;
          if (mapOfTimesUsed.containsKey(player.getUniqueId())) {
            lastTimeUsed = mapOfTimesUsed.get(player.getUniqueId());
          }
          // If it's off cooldown
          if ((currTime - lastTimeUsed) >= COOLDOWN_DURATION) {
            handleTreefeller(pie, block);
            (new DisplayCooldown(player, ChatColor.GOLD + "Treefeller cooldown", COOLDOWN_DURATION, 1000)).runTaskAsynchronously(PhantomMain.getPlugin());
            mapOfTimesUsed.put(player.getUniqueId(), currTime);
          }
        } else if (isSapling(blockType)) {
          if (ReplanterHoe.hasBonemeal(player)) {
            boolean wasBonemealed = block.applyBoneMeal(pie.getBlockFace());
            if (wasBonemealed) {
              ReplanterHoe.removeBonemeal(player);
            }
            block.setMetadata(RECENT_SAPLING_METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), true));
            (new RemoveMetadataAfterDelay(block, RECENT_SAPLING_METADATA_KEY)).runTaskLater(PhantomMain.getPlugin(), 10);
          }
        }
      }
    }
  }

  private void handleLeafDestroyer(BlockBreakEvent bbe) {
    Player player = bbe.getPlayer();
    Block block = bbe.getBlock();
    // If it's one of the adjacent blocks, stop processing early
    if (block.hasMetadata(METADATA_KEY)) {
      block.removeMetadata(METADATA_KEY, PhantomMain.getPlugin());
      return;
    }
    BlockFace blockFace = blockMap.get(block.getLocation().toString());
    // It was broken from the treefeller code -- stop processing
    if (blockFace == null) {
      return;
    }
    Vector vector = blockFace.getDirection();
    Location blockLoc = block.getLocation();
    ArrayList<Location> blocksToRemove = new ArrayList<>();
    if (vector.getBlockX() == 0 && vector.getBlockZ() == 0) {
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() + 1, blockLoc.getY(), blockLoc.getZ()));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() - 1, blockLoc.getY(), blockLoc.getZ()));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY(), blockLoc.getZ() + 1));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY(), blockLoc.getZ() - 1));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() + 1, blockLoc.getY(), blockLoc.getZ() + 1));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() - 1, blockLoc.getY(), blockLoc.getZ() - 1));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() - 1, blockLoc.getY(), blockLoc.getZ() + 1));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() + 1, blockLoc.getY(), blockLoc.getZ() - 1));
    }
    if (vector.getBlockY() == 0 && vector.getBlockX() == 0) {
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY() + 1, blockLoc.getZ()));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY() - 1, blockLoc.getZ()));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() + 1, blockLoc.getY(), blockLoc.getZ()));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() - 1, blockLoc.getY(), blockLoc.getZ()));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() + 1, blockLoc.getY() + 1, blockLoc.getZ()));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() - 1, blockLoc.getY() - 1, blockLoc.getZ()));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() + 1, blockLoc.getY() - 1, blockLoc.getZ()));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() - 1, blockLoc.getY() + 1, blockLoc.getZ()));
    }
    if (vector.getBlockZ() == 0 && vector.getBlockY() == 0) {
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY(), blockLoc.getZ() + 1));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY(), blockLoc.getZ() - 1));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY() + 1, blockLoc.getZ()));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY() - 1, blockLoc.getZ()));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY() + 1, blockLoc.getZ() + 1));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY() - 1, blockLoc.getZ() - 1));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY() + 1, blockLoc.getZ() - 1));
      blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY() - 1, blockLoc.getZ() + 1));
    }

    short numBlocksBroken = 1;

    for (Location loc : blocksToRemove) {
      if (isLeaf(loc.getBlock().getType())) {
        numBlocksBroken++;
      } else {
        continue;
      }
      // Uncomment this to re-enable drops
      Collection<ItemStack> drops = loc.getBlock().getDrops(player.getItemInHand());
      for (ItemStack is : drops) {
        loc.getWorld().dropItem(loc, is);
      }
      Block adjBlock = loc.getBlock();
      adjBlock.setMetadata(METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), true));
      player.breakBlock(adjBlock);
    }

    blockMap.remove(block.getLocation().toString());
  }

  private void handleTreefeller(PlayerInteractEvent pie, Block block) {
    Player player = pie.getPlayer();
    ArrayList<Block> logsInTree = new ArrayList<>();
    Location loc = block.getLocation();
    World world = loc.getWorld();
    int currX = (int) loc.getX();
    int currZ = (int) loc.getZ();
    int currY = (int) loc.getY();
    for (int i = -MAX_WIDTH; i < MAX_WIDTH; i++) {
      int x = currX + i;
      for (int j = -MAX_WIDTH; j < MAX_WIDTH; j++) {
        int z = currZ + j;
        for (int k = 0; k < MAX_HEIGHT; k++) {
          int y = currY + k;
          Block newBlock = world.getBlockAt(x, y, z);
          if (isLog(newBlock.getType())) {
            player.breakBlock(newBlock);
          }
        }
      }
    }
    player.breakBlock(block);
  }

  private static boolean isLeaf(Material material) {
    return (material.createBlockData() instanceof Leaves) || material == Material.NETHER_WART_BLOCK || material == Material.WARPED_WART_BLOCK || material == Material.SHROOMLIGHT;
  }

  private static boolean isLog(Material material) {
    switch (material) {
      case ACACIA_LOG:
      case BIRCH_LOG:
      case DARK_OAK_LOG:
      case JUNGLE_LOG:
      case OAK_LOG:
      case SPRUCE_LOG:
      case CRIMSON_STEM:
      case WARPED_STEM:
      case STRIPPED_ACACIA_LOG:
      case STRIPPED_BIRCH_LOG:
      case STRIPPED_DARK_OAK_LOG:
      case STRIPPED_JUNGLE_LOG:
      case STRIPPED_OAK_LOG:
      case STRIPPED_SPRUCE_LOG:
      case STRIPPED_CRIMSON_STEM:
      case STRIPPED_WARPED_STEM:
        return true;
      default:
        return false;
    }
  }

  private static boolean isSapling(Material material) {
    return (material.createBlockData() instanceof Sapling) || material == Material.CRIMSON_FUNGUS || material == Material.WARPED_FUNGUS;
  }
}
