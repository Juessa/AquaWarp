package org.anhcraft.aquawarp.API;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.anhcraft.aquawarp.AquaWarpsMessages;
import org.anhcraft.aquawarp.Exceptions.WarpNotFound;
import org.anhcraft.aquawarp.Exceptions.WrongSaveDataType;
import org.anhcraft.aquawarp.Options;
import org.anhcraft.aquawarp.Util.Configuration;
import org.anhcraft.aquawarp.Util.ConnectDatabase;
import org.anhcraft.aquawarp.Util.Strings;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class WarpStatus {
    public static WarpStatusTypes getStatusWithMySQL(String warp)
            throws SQLException, WrongSaveDataType {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("mysql")){
            Boolean b;
            ResultSet r = ConnectDatabase.exeq(
                    "SELECT * FROM " + Options.mysql_table_protection + " WHERE name='" + warp + "';");
            b = r.next();
            r.close();
            if(b) {
                return WarpStatusTypes.LOCKED;
            } else {
                return WarpStatusTypes.UNLOCKED;
            }
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
    }

    public static WarpStatusTypes getStatusWithFiles(String warp)
            throws WrongSaveDataType, WarpNotFound {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            File a = new File(Options.pluginDir + "warps/" + warp + ".yml");
            if(a.exists()) {
                FileConfiguration f = YamlConfiguration.loadConfiguration(a);
                if(f.getBoolean("protection.enable")) {
                    return WarpStatusTypes.LOCKED;
                } else {
                    return WarpStatusTypes.UNLOCKED;
                }
            } else {
                throw new WarpNotFound(AquaWarpsMessages.get("System.Exception.300"));
            }
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
    }

    public static WarpStatusTypes getStatusAutoSaveDataType(String warp)
            throws WarpNotFound, SQLException, WrongSaveDataType {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            return getStatusWithFiles(warp);
        } else {
            return getStatusWithMySQL(warp);
        }
    }

    public static void setStatusWithMySQL(String warp, WarpStatusTypes status, String password)
            throws SQLException, WrongSaveDataType {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("mysql")){
            if(getStatusWithMySQL(warp).equals(WarpStatusTypes.LOCKED) && status.equals(WarpStatusTypes.UNLOCKED)){
                ConnectDatabase.exeu("DELETE FROM " + Options.mysql_table_protection + "  WHERE `name`='"+warp+"';");
            }
            else if(getStatusWithMySQL(warp).equals(WarpStatusTypes.UNLOCKED) && status.equals(WarpStatusTypes.LOCKED)){
                ConnectDatabase.exeu("INSERT INTO " + Options.mysql_table_protection + " (`name`,`pass`) " +
                        "VALUES ('"+warp+"','"+Strings.hashAutoType(password)+"');");
            }
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
    }

    @Deprecated
    public static void setStatusWithMySQL(String warp, WarpStatusTypes status)
            throws SQLException, WrongSaveDataType {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("mysql")){
            if(getStatusWithMySQL(warp).equals(WarpStatusTypes.LOCKED) && status.equals(WarpStatusTypes.UNLOCKED)){
                ConnectDatabase.exeu("DELETE FROM " + Options.mysql_table_protection + "  WHERE `name`='"+warp+"';");
            }
            else if(getStatusWithMySQL(warp).equals(WarpStatusTypes.UNLOCKED) && status.equals(WarpStatusTypes.LOCKED)){
                ConnectDatabase.exeu("INSERT INTO " + Options.mysql_table_protection + " (`name`,`pass`) " +
                        "VALUES ('"+warp+"','');");
            }
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
    }

    public static void setStatusWithFiles(String warp, WarpStatusTypes status, String password)
            throws WrongSaveDataType, WarpNotFound, IOException {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            File a = new File(Options.pluginDir + "warps/" + warp + ".yml");
            if(a.exists()) {
                if(getStatusWithFiles(warp).equals(WarpStatusTypes.LOCKED) && status.equals(WarpStatusTypes.UNLOCKED)){
                    FileConfiguration f = YamlConfiguration.loadConfiguration(a);
                    f.set("protection.enable", false);
                    f.set("protection.value", "");
                    f.save(a);
                }
                else if(getStatusWithFiles(warp).equals(WarpStatusTypes.UNLOCKED) && status.equals(WarpStatusTypes.LOCKED)){
                    FileConfiguration f = YamlConfiguration.loadConfiguration(a);
                    f.set("protection.enable", true);
                    f.set("protection.value",Strings.hashAutoType(password));
                    f.save(a);
                }
            } else {
                throw new WarpNotFound(AquaWarpsMessages.get("System.Exception.300"));
            }
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
    }

    @Deprecated
    public static void setStatusWithFiles(String warp, WarpStatusTypes status)
            throws WrongSaveDataType, WarpNotFound, IOException {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            File a = new File(Options.pluginDir + "warps/" + warp + ".yml");
            if(a.exists()) {
                if(getStatusWithFiles(warp).equals(WarpStatusTypes.LOCKED) && status.equals(WarpStatusTypes.UNLOCKED)){
                    FileConfiguration f = YamlConfiguration.loadConfiguration(a);
                    f.set("protection.enable", true);
                    f.set("protection.value","");
                    f.save(a);
                }
                else if(getStatusWithFiles(warp).equals(WarpStatusTypes.UNLOCKED) && status.equals(WarpStatusTypes.LOCKED)){
                    FileConfiguration f = YamlConfiguration.loadConfiguration(a);
                    f.set("protection.enable", true);
                    f.set("protection.value","");
                    f.save(a);
                }
            } else {
                throw new WarpNotFound(AquaWarpsMessages.get("System.Exception.300"));
            }
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
    }

    public static void setStatusAutoSaveDataType(String warp, WarpStatusTypes status, String password)
            throws WarpNotFound, SQLException, WrongSaveDataType, IOException {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            setStatusWithFiles(warp, status, password);
        } else {
            setStatusWithMySQL(warp, status, password);
        }
    }

    @Deprecated
    public static void setStatusAutoSaveDataType(String warp, WarpStatusTypes status)
            throws WarpNotFound, SQLException, WrongSaveDataType, IOException {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            setStatusWithFiles(warp, status);
        } else {
            setStatusWithMySQL(warp, status);
        }
    }
}
