package com.condor.phantommenace.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.npc.NPCAction;
import com.condor.phantommenace.npc.NPCManager;
import com.condor.phantommenace.npc.PHNPC;


/**
 * Loads the npcs.yml config file and
 * builds each NPC
 */
public class NPCConfig {

  private static final String CONFIG_LOC = "plugins/PhantomMenace/npcs.yml";
  private static final String PROPERTY_PREFIX = "  ";
  private static HashMap<String, HashMap<String, String>> npcPropMap = new HashMap<>();

  /**
   * Loads the config file and updates each altar structure
   * TODO: Instantiate default file
   */
  public static void init() {
    File file = new File(CONFIG_LOC);
    try (Scanner scanner = new Scanner(file)) {
      file.createNewFile();
      HashMap<String, String> context = new HashMap<>();
      String currTypeName = "";
      String line = "";
      while (scanner.hasNextLine()) {
        line = scanner.nextLine();
        // If it's a header line
        if (!line.startsWith(PROPERTY_PREFIX) && line.endsWith(":")) {
          if (!currTypeName.isEmpty()) {
            npcPropMap.put(currTypeName, ((HashMap<String, String>) context.clone()));
            context.clear();
          }
          currTypeName = line.substring(0, line.length() - 1);
        // If it's a property line
        } else if (line.startsWith(PROPERTY_PREFIX) && !currTypeName.isEmpty()) {
          line = line.substring(PROPERTY_PREFIX.length(), line.length());
          String[] lineSplit = line.split(": ");
          context.put(lineSplit[0], lineSplit[1]);
        } 
      }
      // Make sure the NPC we last read is in the map
      if (!npcPropMap.containsKey(currTypeName)) {
        npcPropMap.put(currTypeName, ((HashMap<String, String>) context.clone()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    updateValues();
  }

  /**
   * Loads the NPCs from the parsed
   * text, and represents it in the codebase
   */
  public static void updateValues() {
    for (Map.Entry<String, HashMap<String, String>> entry : npcPropMap.entrySet()) {
      HashMap<String, String> map = entry.getValue();
      PHNPC npc = null;
      String texture = "";
      String texture_signature = "";
      String name = "";
      String worldName = "";
      double x = 0.0;
      double y = 0.0;
      double z = 0.0;
      float pitch = 0.0f;
      float yaw = 0.0f;
      boolean faceUser = true;
      NPCAction action = NPCAction.NONE;
      for (String key : map.keySet()) {
        // Bukkit.getLogger().info("- " + key + ": " + map.get(key));
        switch (key) {
          case "texture":
            texture = map.get(key);
            break;
          case "texture_signature":
            texture_signature = map.get(key);
            break;
          case "name":
            name = map.get(key);
            break;
          case "world_name":
            worldName = map.get(key);
            break;
          case "x":
            x = Double.parseDouble(map.get(key));
            break;
          case "y":
            y = Double.parseDouble(map.get(key));
            break;
          case "z":
            z = Double.parseDouble(map.get(key));
            break;
          case "pitch":
            pitch = Float.parseFloat(map.get(key));
            break;
          case "yaw":
            yaw = Float.parseFloat(map.get(key));
            break;
          case "face_user":
            faceUser = Boolean.parseBoolean(map.get(key));
            break;
          case "action":
            action = NPCAction.fromString(map.get(key));
            break;
          default:
            Bukkit.getLogger().warning("Encountered unknown key in npcs.yml: \"" + key + "\"");
            break;
        }
      }
      Location loc = new Location(PhantomMain.getPlugin().getServer().getWorld(worldName), x, y, z, pitch, yaw);
      npc = new PHNPC(UUID.randomUUID(), texture, texture_signature, name, loc, faceUser, action);
      NPCManager.appendNPC(npc);
      Bukkit.getLogger().info("- Loaded " + name);
    }
  }
}
