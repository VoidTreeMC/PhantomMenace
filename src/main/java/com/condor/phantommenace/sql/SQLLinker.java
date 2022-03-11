package com.condor.phantommenace.sql;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.UUID;
import java.net.SocketException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.condor.phantommenace.main.PhantomMain;
import com.condor.phantommenace.sql.SQLConfig;
import com.condor.phantommenace.item.CustomItemType;

/**
 * Utility class that performs most of the SQL interactions
 */
public class SQLLinker {

  // The SQL connection
  private static Connection conn;


  /**
   * Initializes a connection with the host
   */
  public static void initHost() {
		try {
	    String url = SQLConfig.getVal("jdbc-url");
	    String username = SQLConfig.getVal("jdbc-user");
	    String password = SQLConfig.getVal("jdbc-password");

      conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

  /**
   * Probes the connection to see if it is still active,
   * and reinitializes it if it is inactive
   */
  public static void probeConnection() {
    try {
      PreparedStatement stmt = conn.prepareStatement("Select * FROM PhantomVendorPurchaseTable");
      stmt.executeQuery();
    } catch (Exception e) {
      Bukkit.getLogger().log(Level.INFO, "Connection probe failed. Re-establishing connection and re-probing.");
      initHost();
      probeConnection();
    }
  }

  /**
   * Gets the connection object to the SQL server
   * @return A Connection object to the SQL server
   */
  public static Connection getConn() {
    probeConnection();
    return conn;
  }


  public static void pushToDB(Player purchaser, CustomItemType item, int price, long time) {
    probeConnection();
    try {
      PreparedStatement stmt = conn.prepareStatement("INSERT INTO PhantomVendorPurchaseTable(player_uuid, player_name, item, price, time) VALUES (?, ?, ?, ?, ?);");
      stmt.setString(1, purchaser.getUniqueId().toString());
      stmt.setString(2, purchaser.getDisplayName());
      stmt.setString(3, item.toString());
      stmt.setInt(4, price);
      stmt.setLong(5, time);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void pushToDB(Player purchaser, CustomItemType itemOne, CustomItemType itemTwo, CustomItemType itemResult, long time) {
    probeConnection();
    try {
      PreparedStatement stmt = conn.prepareStatement("INSERT INTO BlacksmithReforgeTable(player_uuid, player_name, item_one, item_two, item_result, time) VALUES (?, ?, ?, ?, ?, ?);");
      stmt.setString(1, purchaser.getUniqueId().toString());
      stmt.setString(2, purchaser.getDisplayName());
      stmt.setString(3, itemOne.toString());
      stmt.setString(4, itemTwo.toString());
      stmt.setString(5, itemResult.toString());
      stmt.setLong(6, time);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Initializes the SQL connection
   */
  public static void init() {
    initHost();
  }
}
