package com.condor.phantommenace.item.legendaryitems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Collection;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Event;
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

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.runnable.DoDisableTargetAI;

public class SuperPick extends CustomItem {

  private static final String NAME = "TAB";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static Random rng = new Random();

  private HashMap<String, BlockFace> blockMap = new HashMap<>();

  private static final String METADATA_KEY = "isSuperPickAdjacent";

  static {
    loreList.add("TAB");
    loreList.add("Breaks 3x3 areas of blocks when mining,");
    loreList.add("but only drops the original block");
    loreList.add("");

    triggerList.add(BlockBreakEvent.class);
    triggerList.add(PlayerInteractEvent.class);
  }

  public SuperPick() {
    super(NAME, loreList, triggerList, CustomItemType.SUPER_PICK, 100, false);
    loreList.add(this.getPrice() + " VoidCoins");
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.NETHERITE_PICKAXE, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName(NAME);
    meta.setLore(loreList);
    meta.addEnchant(Enchantment.MENDING, 1, false);
    // meta.setUnbreakable(true);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    boolean ret = false;
    if (event instanceof BlockBreakEvent) {
      BlockBreakEvent bbe = (BlockBreakEvent) event;
      Player player = bbe.getPlayer();
      ret = CustomItemType.getTypeFromCustomItem(player.getItemInHand()) == CustomItemType.SUPER_PICK;
    } else if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      ret = CustomItemType.getTypeFromCustomItem(player.getItemInHand()) == CustomItemType.SUPER_PICK;
    }
    return ret;
  }

  public void execute(Event event) {
    if (event instanceof BlockBreakEvent) {
      BlockBreakEvent bbe = (BlockBreakEvent) event;
      Player player = bbe.getPlayer();
      Block block = bbe.getBlock();
      // If it's one of the adjacent blocks, set the drops to none
      // and stop processing early
      if (block.hasMetadata(METADATA_KEY)) {
        bbe.setDropItems(false);
        block.removeMetadata(METADATA_KEY, PhantomMain.getPlugin());
        return;
      }
      BlockFace blockFace = blockMap.get(block.getLocation().toString());
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
        if (loc.getBlock().getType() == Material.BEDROCK) {
          continue;
        }
        if (loc.getBlock().getType() != Material.AIR) {
          numBlocksBroken++;
        }
        // Uncomment this to re-enable drops
        // Collection<ItemStack> drops = loc.getBlock().getDrops(player.getItemInHand());
        // for (ItemStack is : drops) {
        //   loc.getWorld().dropItem(loc, is);
        // }
        Block adjBlock = loc.getBlock();
        adjBlock.setMetadata(METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), true));
        player.breakBlock(adjBlock);
      }

      blockMap.remove(block.getLocation().toString());
    } else {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Block block = pie.getClickedBlock();
      if (block != null) {
        BlockFace blockFace = pie.getBlockFace();
        blockMap.put(block.getLocation().toString(), blockFace);
      }
    }
  }
}
