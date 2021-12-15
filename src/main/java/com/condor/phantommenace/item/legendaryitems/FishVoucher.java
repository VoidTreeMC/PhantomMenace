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

public class FishVoucher extends CustomItem {

  private static final String NAME = "Fish Voucher";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static HashMap<UUID, Long> mapOfTimesUsed = new HashMap<>();

  private static Random rng = new Random();

  static {
    loreList.add("Fish Voucher");
    loreList.add("Right-click with this to receive");
    loreList.add("your reward.");
    loreList.add("");

    triggerList.add(PlayerInteractEvent.class);
  }

  public FishVoucher() {
    super(NAME, loreList, triggerList, CustomItemType.FISH_VOUCHER, 0);
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
        // If they're holding a fish voucher
        if (isFishVoucher(player.getItemInHand())) {
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

  public boolean isFishVoucher(ItemStack item) {
    return (item != null) && (item.getType() == Material.PAPER) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.FISH_VOUCHER);
  }

  private ItemStack getShulker(Player player) {
    ItemStack is = new ItemStack(Material.LIGHT_BLUE_SHULKER_BOX);
    BlockStateMeta meta = (BlockStateMeta) is.getItemMeta();
    meta.setDisplayName("Fish Bowl");
    ShulkerBox box = (ShulkerBox) meta.getBlockState();
    Inventory inventory = box.getInventory();
    ItemStack twentyFourVoidCoins = CustomItemManager.getItemByType(CustomItemType.DEFENDER_TOKEN).getInstance();
    twentyFourVoidCoins.setAmount(24);
    ItemStack sixtyFourTropicalFish = new ItemStack(Material.TROPICAL_FISH, 64);
    ItemMeta circusMeta = sixtyFourTropicalFish.getItemMeta();
    circusMeta.setDisplayName("An Entire Circus");
    ItemStack thirtyTwoTropicalFish = sixtyFourTropicalFish.clone();
    thirtyTwoTropicalFish.setAmount(32);
    thirtyTwoTropicalFish.setItemMeta(circusMeta);
    sixtyFourTropicalFish.setItemMeta(circusMeta);
    ItemStack sixtyFourPufferfish = new ItemStack(Material.PUFFERFISH, 64);
    ItemMeta pufferfishMeta = sixtyFourPufferfish.getItemMeta();
    pufferfishMeta.setDisplayName("Spiky Balloon");
    sixtyFourPufferfish.setItemMeta(pufferfishMeta);
    ItemStack thirtyTwoPufferfish = sixtyFourPufferfish.clone();
    thirtyTwoPufferfish.setAmount(32);
    thirtyTwoPufferfish.setItemMeta(pufferfishMeta);
    ItemStack sixtyFourCod = new ItemStack(Material.COD, 64);
    ItemMeta codMeta = sixtyFourCod.getItemMeta();
    codMeta.setDisplayName("Tan Armless Gumbo Precursor");
    sixtyFourCod.setItemMeta(codMeta);
    ItemStack thirtyTwoCod = sixtyFourCod.clone();
    thirtyTwoCod.setAmount(32);
    thirtyTwoCod.setItemMeta(codMeta);
    ItemStack sixtyFourSalmon = new ItemStack(Material.SALMON, 64);
    ItemMeta salmonMeta = sixtyFourSalmon.getItemMeta();
    salmonMeta.setDisplayName("Red Armless Gremlin");
    sixtyFourSalmon.setItemMeta(salmonMeta);
    ItemStack thirtyTwoSalmon = sixtyFourSalmon.clone();
    thirtyTwoSalmon.setAmount(32);
    thirtyTwoSalmon.setItemMeta(salmonMeta);
    ItemStack thirtyTwoSeaGrass = new ItemStack(Material.SEAGRASS, 32);
    ItemMeta seaGrassMeta = thirtyTwoSeaGrass.getItemMeta();
    seaGrassMeta.setDisplayName("Sea \"Grass\"");
    thirtyTwoSeaGrass.setItemMeta(seaGrassMeta);
    ItemStack twoHeartsOfTheSea = new ItemStack(Material.HEART_OF_THE_SEA, 2);
    ItemMeta heartOfTheSeaMeta = twoHeartsOfTheSea.getItemMeta();
    heartOfTheSeaMeta.setDisplayName("x^2 + y^2 + z^2 = r^2");
    twoHeartsOfTheSea.setItemMeta(heartOfTheSeaMeta);
    ItemStack fishEgg = new ItemStack(Material.TROPICAL_FISH_SPAWN_EGG);
    ItemMeta fishEggMeta = fishEgg.getItemMeta();
    fishEggMeta.setDisplayName("Unpopped Fish Corn");
    fishEgg.setItemMeta(fishEggMeta);
    ItemStack thirtyTwoSeaPickles = new ItemStack(Material.SEA_PICKLE, 32);
    ItemMeta seaPickleMeta = thirtyTwoSeaPickles.getItemMeta();
    seaPickleMeta.setDisplayName("Vinegarized Sea Cucumber");
    thirtyTwoSeaPickles.setItemMeta(seaPickleMeta);
    ItemStack trident = new ItemStack(Material.TRIDENT, 1);
    ItemMeta tridentMeta = trident.getItemMeta();
    tridentMeta.setDisplayName("Seaweed Salad Fork");
    trident.setItemMeta(tridentMeta);
    inventory.setItem(0, sixtyFourTropicalFish);
    inventory.setItem(1, twoHeartsOfTheSea);
    inventory.setItem(2, thirtyTwoTropicalFish);
    inventory.setItem(3, thirtyTwoSeaGrass);
    inventory.setItem(4, trident);
    inventory.setItem(5, thirtyTwoSeaGrass);
    inventory.setItem(6, thirtyTwoSalmon);
    inventory.setItem(7, thirtyTwoSeaPickles);
    inventory.setItem(8, sixtyFourSalmon);
    inventory.setItem(9, twentyFourVoidCoins);
    inventory.setItem(10, thirtyTwoSeaGrass);
    inventory.setItem(11, twentyFourVoidCoins);
    inventory.setItem(12, thirtyTwoTropicalFish);
    inventory.setItem(13, fishEgg);
    inventory.setItem(14, thirtyTwoSalmon);
    inventory.setItem(15, twentyFourVoidCoins);
    inventory.setItem(16, thirtyTwoSeaGrass);
    inventory.setItem(17, twentyFourVoidCoins);
    inventory.setItem(18, sixtyFourPufferfish);
    inventory.setItem(19, thirtyTwoSeaPickles);
    inventory.setItem(20, thirtyTwoPufferfish);
    inventory.setItem(21, thirtyTwoCod);
    inventory.setItem(22, twentyFourVoidCoins);
    inventory.setItem(23, thirtyTwoPufferfish);
    inventory.setItem(24, thirtyTwoCod);
    inventory.setItem(25, twoHeartsOfTheSea);
    inventory.setItem(26, sixtyFourCod);
    meta.setBlockState(box);
    is.setItemMeta(meta);
    return is;
  }

}
