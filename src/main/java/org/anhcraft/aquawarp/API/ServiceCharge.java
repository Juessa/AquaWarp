package org.anhcraft.aquawarp.API;

import org.anhcraft.aquawarp.API.SoftDepends.Vault;
import org.anhcraft.aquawarp.AquaWarpsMessages;
import org.anhcraft.aquawarp.Exceptions.LackMoney;
import org.anhcraft.aquawarp.Exceptions.WarpNotFound;
import org.anhcraft.aquawarp.Exceptions.WrongSaveDataType;
import org.anhcraft.aquawarp.Options;
import org.anhcraft.aquawarp.Util.Configuration;
import org.anhcraft.aquawarp.Util.ConnectDatabase;
import org.anhcraft.spaciouslib.Converter.DataTypes;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public interface ServiceCharge {
    static Boolean isEnable(){
        return Configuration.config.getBoolean("tpWarp.serviceCharge");
    }

    static double getMoney(OfflinePlayer player){
        return Vault.economy.getBalance(player);
    }

    @Deprecated
    static double getMoney(Player player){
        return Vault.economy.getBalance(player);
    }

    static void depositMoney(OfflinePlayer player, double money){
        Vault.economy.depositPlayer(player, money);
    }

    @Deprecated
    static void depositMoney(Player player, double money){
        Vault.economy.depositPlayer(player,money);
    }

    static void withdrawMoney(OfflinePlayer player, double money){
        Vault.economy.withdrawPlayer(player, money);
    }

    @Deprecated
    static void withdrawMoney(Player player, double money){
        Vault.economy.withdrawPlayer(player,money);
    }

    static double getCostWithFiles(String warp, WarpStatusTypes status) throws WarpNotFound, WrongSaveDataType {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")) {
            File a = new File(Options.pluginDir + "warps/" + warp + ".yml");
            if(a.exists()) {
                FileConfiguration f = YamlConfiguration.loadConfiguration(a);
                if(status.equals(WarpStatusTypes.LOCKED)) {
                    return new DataTypes().toDouble(f.getString("service_charges.lock"));
                } else if(status.equals(WarpStatusTypes.UNLOCKED)){
                    return new DataTypes().toDouble(f.getString("service_charges.unlock"));
                }
            } else {
                throw new WarpNotFound(AquaWarpsMessages.get("System.Exception.300"));
            }
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
        return 0;
    }

    static void setCostWithFiles(String warp, double costLock, double costUnlock)
            throws WarpNotFound, WrongSaveDataType, IOException {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")) {
            File a = new File(Options.pluginDir + "warps/" + warp + ".yml");
            if(a.exists()) {
                FileConfiguration f = YamlConfiguration.loadConfiguration(a);
                f.set("service_charges.lock",costLock);
                f.set("service_charges.unlock",costUnlock);
                f.save(a);
            } else {
                throw new WarpNotFound(AquaWarpsMessages.get("System.Exception.300"));
            }
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
    }

    static double getCostWithMySQL(String warp, WarpStatusTypes status) throws
            SQLException, WrongSaveDataType {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("mysql")) {
            ResultSet r = ConnectDatabase.exeq(
                    "SELECT * FROM " + Options.mysql_table_service_charges + " WHERE name='" + warp + "';");
            if(r.next()) {
                double o = 0D;
                if(status.equals(WarpStatusTypes.LOCKED)) {
                    o = (double) r.getFloat("lock");
                } else if(status.equals(WarpStatusTypes.UNLOCKED)){
                    o = (double) r.getFloat("unlock");
                }
                r.close();
                return o;
            } else {
                r.close();
                return 0;
            }
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
    }

    static void setCostWithMySQL(String warp, double costLock, double costUnlock) throws
            SQLException, WrongSaveDataType {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("mysql")) {
            ResultSet r = ConnectDatabase.exeq(
                    "SELECT * FROM " + Options.mysql_table_service_charges + " WHERE name='" + warp + "';");
            if(r.next()) {
                ConnectDatabase.exeu("UPDATE " + Options.mysql_table_service_charges + "" +
                        " SET `lock`='"+costLock+"', `unlock`='"+costUnlock+"'" +
                        " WHERE `name`='"+warp+"'");
            } else {
                ConnectDatabase.exeu("INSERT INTO " + Options.mysql_table_service_charges + " (`name`,`lock`,`unlock`)" +
                        " VALUES ('"+warp+"','"+costLock+"','"+costUnlock+"');");
            }
            r.close();
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
    }

    static double getCostAutoSaveDataType(String warp, WarpStatusTypes status)
            throws WarpNotFound, WrongSaveDataType, SQLException {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            return getCostWithFiles(warp, status);
        } else {
            return getCostWithMySQL(warp, status);
        }
    }

    static void setCostAutoSaveDataType(String warp, double costLocked, double costUnlocked)
            throws WarpNotFound, WrongSaveDataType, SQLException, IOException {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            setCostWithFiles(warp, costLocked, costUnlocked);
        } else {
            setCostWithMySQL(warp, costLocked, costUnlocked);
        }
    }

    @Deprecated
    static Boolean fee(String warp, Player player)
            throws WarpNotFound, WrongSaveDataType, SQLException, LackMoney {
        WarpStatusTypes wst = WarpStatus.getStatusAutoSaveDataType(warp);
        double cost = getCostAutoSaveDataType(warp, wst);
        double current = getMoney(player);
        double new_money = current - cost;
        if(new_money < 0){
            throw new LackMoney(AquaWarpsMessages.get("System.Exception.304"));
        } else {
            withdrawMoney(player, cost);
            return true;
        }
    }

    static Boolean fee(String warp, OfflinePlayer player)
            throws WarpNotFound, WrongSaveDataType, SQLException, LackMoney {
        WarpStatusTypes wst = WarpStatus.getStatusAutoSaveDataType(warp);
        double cost = getCostAutoSaveDataType(warp, wst);
        double current = getMoney(player);
        double new_money = current - cost;
        if(new_money < 0){
            throw new LackMoney(AquaWarpsMessages.get("System.Exception.304"));
        } else {
            withdrawMoney(player, cost);
            return true;
        }
    }
}
