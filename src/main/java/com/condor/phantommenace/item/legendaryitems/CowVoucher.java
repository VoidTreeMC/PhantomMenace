package com.condor.phantommenace.item.legendaryitems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Entity;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Event;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.World;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkMeta;

import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.runnable.DoEnderBladeTeleport;
import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.runnable.DisplayCooldown;
import com.condor.phantommenace.item.CustomItemManager;

public class CowVoucher extends CustomItem {

  private static final String NAME = "Cow Voucher";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static HashMap<UUID, Long> mapOfTimesUsed = new HashMap<>();

  private static Random rng = new Random();

  static {
    loreList.add("Cow Voucher");
    loreList.add("Right-click with this to receive");
    loreList.add("your reward.");
    loreList.add("");

    triggerList.add(PlayerInteractEvent.class);
  }

  public CowVoucher() {
    super(NAME, loreList, triggerList, CustomItemType.COW_VOUCHER, 0);
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.PAPER, 1);
    ItemMeta meta = is.getItemMeta();
    meta.setDisplayName(NAME);
    meta.setLore(loreList);
    // meta.setUnbreakable(true);
    is.setItemMeta(meta);
    return is;
  }

  public boolean isNecessary(Event event) {
    boolean ret = false;
    if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      // If they right-clicked
      if ((pie.getAction() == Action.RIGHT_CLICK_AIR ||
          pie.getAction() == Action.RIGHT_CLICK_BLOCK)) {
        // If they're holding a cow voucher
        if (isCowVoucher(player.getItemInHand())) {
          ret = true;
        }
      }
    }
    return ret;
  }

  public void execute(Event event) {
    if (event instanceof PlayerInteractEvent) {
      PlayerInteractEvent pie = (PlayerInteractEvent) event;
      Player player = pie.getPlayer();
      player.getItemInHand().setAmount(0);
      player.getInventory().addItem(getShulker(player));
    }
  }

  public boolean isCowVoucher(ItemStack item) {
    return (item != null) && (item.getType() == Material.PAPER) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.COW_VOUCHER);
  }

  private ItemStack getShulker(Player player) {
    ItemStack is = new ItemStack(Material.MAGENTA_SHULKER_BOX);
    BlockStateMeta meta = (BlockStateMeta) is.getItemMeta();
    meta.setDisplayName("Cow Kit");
    ShulkerBox box = (ShulkerBox) meta.getBlockState();
    Inventory inventory = box.getInventory();
    ItemStack fourVoidCoins = CustomItemManager.getItemByType(CustomItemType.DEFENDER_TOKEN).getInstance();
    fourVoidCoins.setAmount(4);
    ItemStack sixtyFourWheat = new ItemStack(Material.WHEAT, 64);
    ItemMeta wheatMeta = sixtyFourWheat.getItemMeta();
    wheatMeta.setDisplayName("Cow Chow");
    sixtyFourWheat.setItemMeta(wheatMeta);
    ItemStack thirtyTwoHayBales = new ItemStack(Material.HAY_BLOCK, 32);
    ItemMeta hayMeta = thirtyTwoHayBales.getItemMeta();
    hayMeta.setDisplayName("Cow Cubes");
    thirtyTwoHayBales.setItemMeta(hayMeta);
    ItemStack cowEgg = new ItemStack(Material.COW_SPAWN_EGG);
    ItemMeta cowEggMeta = cowEgg.getItemMeta();
    cowEggMeta.setDisplayName("Cow Container");
    cowEgg.setItemMeta(cowEggMeta);
    ItemStack sixteenCreeperFireworks = CustomItemManager.getItemByType(CustomItemType.CREEPER_FIREWORK).getInstance();
    sixteenCreeperFireworks.setAmount(16);
    ItemStack thirtyTwoFlightRockets = new ItemStack(Material.FIREWORK_ROCKET, 32);
    FireworkMeta fireworkMeta = (FireworkMeta) thirtyTwoFlightRockets.getItemMeta();
    fireworkMeta.setPower(3);
    fireworkMeta.setDisplayName("Cow Space Program");
    thirtyTwoFlightRockets.setItemMeta(fireworkMeta);
    inventory.setItem(0, sixtyFourWheat);
    inventory.setItem(1, fourVoidCoins);
    inventory.setItem(2, fourVoidCoins);
    inventory.setItem(3, fourVoidCoins);
    inventory.setItem(4, thirtyTwoFlightRockets);
    inventory.setItem(5, fourVoidCoins);
    inventory.setItem(6, fourVoidCoins);
    inventory.setItem(7, fourVoidCoins);
    inventory.setItem(8, sixtyFourWheat);
    inventory.setItem(9, fourVoidCoins);
    inventory.setItem(10, sixteenCreeperFireworks);
    inventory.setItem(11, thirtyTwoHayBales);
    inventory.setItem(12, thirtyTwoFlightRockets);
    inventory.setItem(13, cowEgg);
    inventory.setItem(14, thirtyTwoFlightRockets);
    inventory.setItem(15, thirtyTwoHayBales);
    inventory.setItem(16, sixteenCreeperFireworks);
    inventory.setItem(17, fourVoidCoins);
    inventory.setItem(18, sixtyFourWheat);
    inventory.setItem(19, fourVoidCoins);
    inventory.setItem(20, fourVoidCoins);
    inventory.setItem(21, fourVoidCoins);
    inventory.setItem(22, thirtyTwoFlightRockets);
    inventory.setItem(23, fourVoidCoins);
    inventory.setItem(24, fourVoidCoins);
    inventory.setItem(25, fourVoidCoins);
    inventory.setItem(26, sixtyFourWheat);
    meta.setBlockState(box);
    is.setItemMeta(meta);
    return is;
  }

}
