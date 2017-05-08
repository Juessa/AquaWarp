package org.anhcraft.aquawarp.API;

import org.anhcraft.aquawarp.AquaWarpsMessages;
import org.anhcraft.aquawarp.Exceptions.PlayerIsNotOnline;
import org.anhcraft.aquawarp.Exceptions.WarpNotFound;
import org.anhcraft.aquawarp.Exceptions.WrongSaveDataType;
import org.anhcraft.aquawarp.Options;
import org.anhcraft.aquawarp.Util.Configuration;
import org.anhcraft.aquawarp.Util.ConnectDatabase;
import org.anhcraft.spaciouslib.Converter.DataTypes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public interface TeleportToWarp {
     static Boolean teleportWithMySQL(Player p, String warp)
             throws WarpNotFound, SQLException, WrongSaveDataType, PlayerIsNotOnline {
         Boolean m = false;
         for(Player x : Bukkit.getServer().getOnlinePlayers()) {
             if(x.getName().equals(p.getName())){
                 m = true;
             }
         }
         if(m) {
            if(Configuration.config.getString("saveDataType").toLowerCase().equals("mysql")){
                ResultSet r = ConnectDatabase.exeq("SELECT * FROM " + Options.mysql_table_warp + " WHERE name='" + warp + "';");
                Boolean i = r.next();
                if(i) {
                    float x = r.getFloat("x");
                    float y = r.getFloat("y");
                    float z = r.getFloat("z");
                    float yaw = r.getFloat("yaw");
                    float pitch = r.getFloat("pitch");
                    String w = r.getString("world");
                    World world = Bukkit.getServer().getWorld(w);
                    Location l = new Location(world, x, y, z);
                    l.setYaw(yaw);
                    l.setPitch(pitch);
                    r.close();
                    return p.teleport(l);
                } else {
                    throw new WarpNotFound(AquaWarpsMessages.get("System.Exception.300"));
                }
            } else {
                throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
            }
         } else {
             throw new PlayerIsNotOnline(AquaWarpsMessages.get("System.Exception.303"));
         }
    }

     static Boolean teleportWithFiles(Player p, String warp)
             throws WarpNotFound, WrongSaveDataType, PlayerIsNotOnline {
         Boolean m = false;
         for(Player x : Bukkit.getServer().getOnlinePlayers()) {
             if(x.getName().equals(p.getName())){
                 m = true;
             }
         }
         if(m) {
             if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")) {
                 File a = new File(Options.pluginDir + "warps/" + warp + ".yml");
                 if(a.exists()) {
                     FileConfiguration f = YamlConfiguration.loadConfiguration(a);
                     DataTypes ic = new DataTypes();
                     float x = ic.toFloat(f.getString("location.x"));
                     float y = ic.toFloat(f.getString("location.y"));
                     float z = ic.toFloat(f.getString("location.z"));
                     float yaw = ic.toFloat(f.getString("location.yaw"));
                     float pitch = ic.toFloat(f.getString("location.pitch"));
                     String w = f.getString("location.world");
                     World world = Bukkit.getServer().getWorld(w);
                     Location l = new Location(world, x, y, z);
                     l.setYaw(yaw);
                     l.setPitch(pitch);
                     return p.teleport(l);
                 } else {
                     throw new WarpNotFound(AquaWarpsMessages.get("System.Exception.300"));
                 }
             } else {
                 throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
             }
         } else {
             throw new PlayerIsNotOnline(AquaWarpsMessages.get("System.Exception.303"));
         }
    }

    static Boolean teleportAutoSaveDataType(Player p, String warp)
            throws WarpNotFound, SQLException, WrongSaveDataType, PlayerIsNotOnline {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            return teleportWithFiles(p, warp);
        } else {
            return teleportWithMySQL(p, warp);
        }
    }
}
