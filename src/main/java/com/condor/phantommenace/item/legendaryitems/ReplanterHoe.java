package com.condor.phantommenace.item.legendaryitems;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Event;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.main.PhantomMain;

public class ReplanterHoe extends CustomItem {

  private static final String NAME = "Wand of Regeneration";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static final String REPLANTER_HOE_METADATA = "harvestedByReplanterHoe";

  static {
    loreList.add("Wand of Regeneration");
    loreList.add("The gods demand much from VoidTree.");
    loreList.add("But sometimes they grant us");
    loreList.add("the means to please them.");
    loreList.add("");
    loreList.add("Harvest with this hoe and it replants,");
    loreList.add("as long as you have the seeds.");
    loreList.add("Right-click to apply bone meal");
    loreList.add("from your inventory to a crop.");
    loreList.add("");

    triggerList.add(BlockDropItemEvent.class);
    triggerList.add(BlockBreakEvent.class);
    triggerList.add(PlayerInteractEvent.class);
  }

  public ReplanterHoe() {
    super(NAME, loreList, triggerList, CustomItemType.REPLANTER_HOE, 25);
    loreList.add(this.getPrice() + " VoidCoins");
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.STONE_HOE, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName(NAME);
    meta.setLore(loreList);
    meta.addEnchant(Enchantment.DURABILITY, 3, false);
    meta.setUnbreakable(true);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    boolean ret = false;
    if (event instanceof BlockDropItemEvent) {
      BlockDropItemEvent bdie = (BlockDropItemEvent) event;
      Block block = bdie.getBlock();
      Player player = bdie.getPlayer();
      ret = block.hasMetadata(REPLANTER_HOE_METADATA);
      ret = ret && isReplanterHoe(player.getItemInHand());
    } else if (event instanceof BlockBreakEvent) {
      BlockBreakEvent bbe = (BlockBreakEvent) event;
      ret = isCrop(bbe.getBlock().getType());
    } else if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      Block clickedBlock = pie.getClickedBlock();
      ret = pie.getHand() == EquipmentSlot.HAND;
      ret = ret && clickedBlock != null && isCrop(clickedBlock.getType());
      ret = ret && isReplanterHoe(player.getItemInHand());
    }
    return ret;
  }

  public void execute(Event event) {
    if (event instanceof BlockDropItemEvent) {
      BlockDropItemEvent bdie = (BlockDropItemEvent) event;
      Material theType = Material.getMaterial(bdie.getBlock().getMetadata(REPLANTER_HOE_METADATA).get(0).asString());
      theType = getBlockFromMat(theType);
      bdie.getBlock().removeMetadata(REPLANTER_HOE_METADATA, PhantomMain.getPlugin());
      List<Item> drops = bdie.getItems();
      bdie.getBlock().setType(theType);
      for (Item item : drops) {
        ItemStack is = item.getItemStack();
        if (isSeeds(is.getType())) {
          is.setAmount(is.getAmount() - 1);
        }
      }
    } else if (event instanceof BlockBreakEvent) {
      BlockBreakEvent bbe = (BlockBreakEvent) event;
      Block block = bbe.getBlock();
      block.setMetadata(REPLANTER_HOE_METADATA, new FixedMetadataValue(PhantomMain.getPlugin(), block.getType().toString()));
    } else if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      Block clickedBlock = pie.getClickedBlock();
      if (hasBonemeal(player)) {
        boolean wasBonemealed = clickedBlock.applyBoneMeal(pie.getBlockFace());
        if (wasBonemealed) {
          removeBonemeal(player);
        }
      }
    }
  }

  public static boolean hasBonemeal(Player player) {
    for (ItemStack is : player.getInventory().getContents()) {
      if (is != null && is.getType() == Material.BONE_MEAL) {
        return true;
      }
    }
    return false;
  }

  public static void removeBonemeal(Player player) {
    for (ItemStack is : player.getInventory().getContents()) {
      if (is != null && is.getType() == Material.BONE_MEAL) {
        is.setAmount(is.getAmount() - 1);
        return;
      }
    }
  }

  public boolean isReplanterHoe(ItemStack item) {
    return (item != null) && (item.getType() == Material.STONE_HOE) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.REPLANTER_HOE);
  }

  public boolean isCrop(Material mat) {
    switch (mat) {
      case WHEAT:
      case BEETROOTS:
      case CARROTS:
      case POTATOES:
      case MELON_STEM:
      case ATTACHED_MELON_STEM:
      case PUMPKIN_STEM:
      case ATTACHED_PUMPKIN_STEM:
      // Currently disabled on cocoa beans because the orientation isn't preserved upon placing new beans
      // case COCOA:
      case NETHER_WART:
        return true;
      default:
        return false;
    }
  }

  public boolean isSeeds(Material mat) {
    switch (mat) {
      case CARROT:
      case POTATO:
      // Currently disabled on cocoa beans because the orientation isn't preserved upon placing new beans
      // case COCOA_BEANS:
      case NETHER_WART:
      case MELON_SEEDS:
      case WHEAT_SEEDS:
      case PUMPKIN_SEEDS:
      case BEETROOT_SEEDS:
        return true;
      default:
        return false;
    }
  }

  public Material getBlockFromMat(Material origMat) {
    switch (origMat) {
      case ATTACHED_MELON_STEM:
        return Material.MELON_STEM;
      case ATTACHED_PUMPKIN_STEM:
        return Material.PUMPKIN_STEM;
      default:
        return origMat;
    }
  }
}
