package vn.anhcraft.aquawarp.API;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vn.anhcraft.aquawarp.AquaWarpsMessages;
import vn.anhcraft.aquawarp.Exceptions.WarpUnavailable;
import vn.anhcraft.aquawarp.Exceptions.WrongSaveDataType;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.Util.Configuration;
import vn.anhcraft.aquawarp.Util.ConnectDatabase;
import vn.anhcraft.aquawarp.Util.FileManager;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public interface CreateWarp {
    static Boolean CreateWarpWithMySQL(String warp, WarpLocation wl)
            throws SQLException, WarpUnavailable, WrongSaveDataType {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("mysql")){
            ResultSet r = ConnectDatabase.exeq("SELECT * FROM " + Options.mysql_table_warp + " WHERE name='" + warp + "';");
            Boolean i = r.next();
            if(i) {
                r.close();
                throw new WarpUnavailable(AquaWarpsMessages.get("System.Exception.305"));
            } else {
                ConnectDatabase.exeu("INSERT INTO " + Options.mysql_table_warp + " (`name`,`x`,`y`,`z`,`world`,`pitch`,`yaw`)" +
                        "VALUES ('"+warp+"',"+wl.getX()+","+wl.getY()+","+wl.getZ()+"," +
                        "'"+wl.getWorld()+"',"+wl.getPitch()+","+wl.getYaw()+"); ");
                r.close();
            }
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
        return true;
    }
    static Boolean CreateWarpWithFile(String warp, WarpLocation wl)
            throws SQLException, WarpUnavailable, WrongSaveDataType, IOException {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")) {
            File a = new File(Options.pluginDir + "warps/" + warp + ".yml");
            if(a.exists()) {
                throw new WarpUnavailable(AquaWarpsMessages.get("System.Exception.305"));
            } else {
                FileManager.setFileFromJar("/lib/warp_template.yml", Options.pluginDir + "warps/" + warp + ".yml");
                FileConfiguration c = YamlConfiguration.loadConfiguration(a);
                c.set("name", warp);
                c.set("location.x", wl.getX());
                c.set("location.y", wl.getY());
                c.set("location.z", wl.getZ());
                c.set("location.world", wl.getWorld());
                c.set("location.yaw", wl.getYaw());
                c.set("location.pitch", wl.getPitch());
                c.save(a);
            }
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
        return true;
    }

    static Boolean CreateWarpAutoSaveDataType(String warp, WarpLocation wl)
            throws SQLException, WrongSaveDataType, IOException, WarpUnavailable {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            return CreateWarpWithFile(warp, wl);
        } else {
            return CreateWarpWithMySQL(warp, wl);
        }
    }
}
