package com.condor.phantommenace.item.legendaryitems;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.Block;
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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.block.Action;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.ChatColor;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.Bukkit;


import com.condor.phantommenace.item.CustomItem;
import com.condor.phantommenace.item.CustomItemType;
import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.runnable.DoDisableTargetAI;

public class BedrockBreaker extends CustomItem {

  public static final String NAME = ChatColor.AQUA + "T" + ChatColor.LIGHT_PURPLE + "r" + ChatColor.WHITE + "a" + ChatColor.LIGHT_PURPLE + "n" + ChatColor.AQUA + "s" + ChatColor.WHITE + " (Dimensional) Pick";
  private static ArrayList<String> loreList = new ArrayList<>();
  private static ArrayList<Class> triggerList = new ArrayList<>();

  private static final int MAX_USES = 30;
  private static final long COOLDOWN_DURATION = 125;

  private static HashMap<UUID, Long> mapOfTimesUsed = new HashMap<>();

  static {
    loreList.add("Trans (Dimensional) Pick");
    loreList.add("No walls. Only windows.");
    loreList.add("Right-click a piece of bedrock");
    loreList.add(" to transform it into glass.");
    loreList.add("");
    loreList.add(MAX_USES + " / " + MAX_USES + " uses remaining.");
    triggerList.add(PlayerInteractEvent.class);
  }

  public BedrockBreaker() {
    super(NAME, loreList, triggerList, CustomItemType.BEDROCK_BREAKER, 50);
    loreList.add(this.getPrice() + " VoidCoins");
  }

  public ItemStack getInstance() {
    ItemStack is = new ItemStack(Material.GOLDEN_PICKAXE, 1);
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
      if (pie.getAction() == Action.RIGHT_CLICK_BLOCK && pie.getHand() == EquipmentSlot.HAND) {
        Block clickedBlock = pie.getClickedBlock();
        if (clickedBlock.getType() == Material.BEDROCK) {
          ret = true;
        }
      }
      ret = ret && CustomItemType.getTypeFromCustomItem(player.getItemInHand()) == CustomItemType.BEDROCK_BREAKER;
    }
    return ret;
  }

  public void execute(Event event) {
    PlayerInteractEvent pie = (PlayerInteractEvent) event;
    Player player = pie.getPlayer();
    long currTime = System.currentTimeMillis();
    long lastTimeUsed = 0;
    if (mapOfTimesUsed.containsKey(player.getUniqueId())) {
      lastTimeUsed = mapOfTimesUsed.get(player.getUniqueId());
    }
    // If it's on cooldown
    if ((currTime - lastTimeUsed) < COOLDOWN_DURATION) {
      return;
    }
    mapOfTimesUsed.put(player.getUniqueId(), currTime);
    ItemStack pick = player.getItemInHand();
    Block clickedBlock = pie.getClickedBlock();
    int numUses = getNumUses(pick);
    if (numUses > 0) {
      player.breakBlock(clickedBlock);
      if (clickedBlock.getType() != Material.BEDROCK) {
        Bukkit.getScheduler().runTask(PhantomMain.getPlugin(), new Runnable() {
          @Override
          public void run() {
            clickedBlock.setType(Material.GLASS);
          }
        });
        if (--numUses <= 0) {
          player.getItemInHand().setAmount(0);
          player.sendMessage("You have broken all the boundaries you can for now. Well done!");
        } else {
          setNumUses(pick, numUses);
        }
      }
    }
    pie.setCancelled(true);
  }

  private int getNumUses(ItemStack pick) {
    int numUses = 0;

    ItemMeta meta = pick.getItemMeta();
    List<String> lore = meta.getLore();
    for (String string : lore) {
      if (string.endsWith(" uses remaining.")) {
        numUses = Integer.parseInt(string.split(" ")[0]);
      }
    }
    return numUses;
  }

  private void setNumUses(ItemStack pick, int numUses) {
    ItemMeta meta = pick.getItemMeta();
    List<String> lore = meta.getLore();
    for (int i = 0; i < lore.size(); i++) {
      String string = lore.get(i);
      if (string.endsWith(" uses remaining.")) {
        lore.set(i, numUses + " / " + MAX_USES + " uses remaining.");
      }
    }
    meta.setLore(lore);
    pick.setItemMeta(meta);
  }

  public boolean isBedrockBreaker(ItemStack item) {
    return (item != null) && (item.getType() == Material.GOLDEN_PICKAXE) &&
     (CustomItemType.getTypeFromCustomItem(item) == CustomItemType.BEDROCK_BREAKER);
  }
}
