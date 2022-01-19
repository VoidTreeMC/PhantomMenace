// package com.condor.phantommenace.item.legendaryitems;
//
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.Random;
// import java.util.Collection;
//
// import org.bukkit.inventory.meta.ItemMeta;
// import org.bukkit.inventory.ItemStack;
// import org.bukkit.event.Event;
// import org.bukkit.Material;
// import org.bukkit.entity.EntityType;
// import org.bukkit.entity.Player;
// import org.bukkit.event.block.BlockBreakEvent;
// import org.bukkit.event.player.PlayerInteractEvent;
// import org.bukkit.block.BlockFace;
// import org.bukkit.block.Block;
// import org.bukkit.enchantments.Enchantment;
// import org.bukkit.Location;
// import org.bukkit.util.Vector;
// import org.bukkit.inventory.meta.Damageable;
// import org.bukkit.metadata.FixedMetadataValue;
// import org.bukkit.block.data.type.Leaves;
//
// import com.condor.phantommenace.item.CustomItem;
// import com.condor.phantommenace.item.CustomItemType;
// import com.condor.phantommenace.main.PhantomMain;
// import com.condor.phantommenace.runnable.DoDisableTargetAI;
//
// public class FoliageAxe extends CustomItem {
//
//   private static final String NAME = "Foliage Axe";
//   private static ArrayList<String> loreList = new ArrayList<>();
//   private static ArrayList<Class> triggerList = new ArrayList<>();
//
//   private static Random rng = new Random();
//
//   private HashMap<String, BlockFace> blockMap = new HashMap<>();
//
//   private static final String METADATA_KEY = "isFoliageAxeAdjacent";
//
//   static {
//     loreList.add("Foliage Axe");
//     loreList.add("");
//
//     triggerList.add(BlockBreakEvent.class);
//     triggerList.add(PlayerInteractEvent.class);
//   }
//
//   public FoliageAxe() {
//     super(NAME, loreList, triggerList, CustomItemType.FOLIAGE_AXE, 50, false);
//     loreList.add(this.getPrice() + " VoidCoins");
//   }
//
//   public ItemStack getInstance() {
//     ItemStack is = new ItemStack(Material.NETHERITE_AXE, 1);
//     ItemMeta meta = is.getItemMeta();
//     meta.setDisplayName(NAME);
//     meta.setLore(loreList);
//     meta.addEnchant(Enchantment.MENDING, 1, false);
//     meta.addEnchant(Enchantment.DIG_SPEED, 5, false);
//     meta.addEnchant(Enchantment.DURABILITY, 3, false);
//     meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 4, true);
//     // meta.setUnbreakable(true);
//     is.setItemMeta(meta);
//     return is;
//   }
//
//   public boolean isNecessary(Event event) {
//     boolean ret = false;
//     if (event instanceof BlockBreakEvent) {
//       BlockBreakEvent bbe = (BlockBreakEvent) event;
//       Player player = bbe.getPlayer();
//       ret = CustomItemType.getTypeFromCustomItem(player.getItemInHand()) == CustomItemType.FOLIAGE_AXE;
//     } else if (event instanceof PlayerInteractEvent) {
//       PlayerInteractEvent pie = (PlayerInteractEvent) event;
//       Player player = pie.getPlayer();
//       ret = CustomItemType.getTypeFromCustomItem(player.getItemInHand()) == CustomItemType.FOLIAGE_AXE;
//       ret = ret && pie.getClickedBlock() != null && isLog(pie.getClickedBlock());
//     }
//     return ret;
//   }
//
//   public void execute(Event event) {
//     if (event instanceof BlockBreakEvent) {
//       BlockBreakEvent bbe = (BlockBreakEvent) event;
//       Player player = bbe.getPlayer();
//       Block block = bbe.getBlock();
//       // If it's one of the adjacent blocks, set the drops to none
//       // and stop processing early
//       if (block.hasMetadata(METADATA_KEY)) {
//         bbe.setDropItems(false);
//         return;
//       }
//       BlockFace blockFace = blockMap.get(block.getLocation().toString());
//       Vector vector = blockFace.getDirection();
//       Location blockLoc = block.getLocation();
//       ArrayList<Location> blocksToRemove = new ArrayList<>();
//       if (vector.getBlockX() == 0 && vector.getBlockZ() == 0) {
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() + 1, blockLoc.getY(), blockLoc.getZ()));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() - 1, blockLoc.getY(), blockLoc.getZ()));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY(), blockLoc.getZ() + 1));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY(), blockLoc.getZ() - 1));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() + 1, blockLoc.getY(), blockLoc.getZ() + 1));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() - 1, blockLoc.getY(), blockLoc.getZ() - 1));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() - 1, blockLoc.getY(), blockLoc.getZ() + 1));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() + 1, blockLoc.getY(), blockLoc.getZ() - 1));
//       }
//       if (vector.getBlockY() == 0 && vector.getBlockX() == 0) {
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY() + 1, blockLoc.getZ()));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY() - 1, blockLoc.getZ()));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() + 1, blockLoc.getY(), blockLoc.getZ()));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() - 1, blockLoc.getY(), blockLoc.getZ()));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() + 1, blockLoc.getY() + 1, blockLoc.getZ()));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() - 1, blockLoc.getY() - 1, blockLoc.getZ()));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() + 1, blockLoc.getY() - 1, blockLoc.getZ()));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX() - 1, blockLoc.getY() + 1, blockLoc.getZ()));
//       }
//       if (vector.getBlockZ() == 0 && vector.getBlockY() == 0) {
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY(), blockLoc.getZ() + 1));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY(), blockLoc.getZ() - 1));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY() + 1, blockLoc.getZ()));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY() - 1, blockLoc.getZ()));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY() + 1, blockLoc.getZ() + 1));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY() - 1, blockLoc.getZ() - 1));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY() + 1, blockLoc.getZ() - 1));
//         blocksToRemove.add(new Location(blockLoc.getWorld(), blockLoc.getX(), blockLoc.getY() - 1, blockLoc.getZ() + 1));
//       }
//
//       short numBlocksBroken = 1;
//
//       for (Location loc : blocksToRemove) {
//         if (isLeaf(loc.getBlock().getType())) {
//           numBlocksBroken++;
//         } else {
//           continue;
//         }
//         // Uncomment this to re-enable drops
//         Collection<ItemStack> drops = loc.getBlock().getDrops(player.getItemInHand());
//         for (ItemStack is : drops) {
//           loc.getWorld().dropItem(loc, is);
//         }
//         Block adjBlock = loc.getBlock();
//         adjBlock.setMetadata(METADATA_KEY, new FixedMetadataValue(PhantomMain.getPlugin(), true));
//         player.breakBlock(adjBlock);
//       }
//
//       blockMap.remove(block.getLocation().toString());
//     } else {
//       PlayerInteractEvent pie = (PlayerInteractEvent) event;
//       Block block = pie.getClickedBlock();
//       ArrayList<Block> logsInTree = new ArrayList<>();
//       setBlockBreakTime(block, logsInTree);
//     }
//   }
//
//   private static boolean isLeaf(Material material) {
//     return (material.createBlockData() instanceof Leaves);
//   }
//
//   private static boolean isLog(Material material) {
//     switch (material) {
//       case ACACIA_LOG:
//       case BIRCH_LOG:
//       case DARK_OAK_LOG:
//       case JUNGLE_LOG:
//       case OAK_LOG:
//       case SPRUCE_LOG:
//         return true;
//       default:
//         return false;
//     }
//   }
// }
