package vn.anhcraft.aquawarp.API;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import vn.anhcraft.aquawarp.AquaWarpsMessages;
import vn.anhcraft.aquawarp.Exceptions.EnergyDataNotFound;
import vn.anhcraft.aquawarp.Exceptions.LackMoney;
import vn.anhcraft.aquawarp.Exceptions.WarpNotFound;
import vn.anhcraft.aquawarp.Exceptions.WrongSaveDataType;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.Util.Configuration;
import vn.anhcraft.aquawarp.Util.ConnectDatabase;
import vn.anhcraft.aquawarp.Util.Strings;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class WarpMethods {
    public static Boolean isPasswordSafe(String password){
        Boolean r = true;
        for(String p : Configuration.config.getStringList("lockWarp.unsafePasswords")){
            if(p.equals(password)){
                r = false;
                break;
            }
        }
        return r;
    }

    public static Boolean isWarpAvailableWithFiles(String warp) throws WrongSaveDataType {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            File a = new File(Options.pluginDir + "warps/" + warp + ".yml");
            return a.exists();
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
    }

    public static Boolean isWarpAvailableWithMySQL(String warp) throws SQLException, WrongSaveDataType {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("mysql")){
            Boolean t;
            ResultSet r = ConnectDatabase.exeq(
                    "SELECT * FROM " + Options.mysql_table_warp + " WHERE name='" + warp + "';");
            t = r.next();
            r.close();
            return t;
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
    }

    public static Boolean isWarpAvailableAutoSaveData(String warp) throws SQLException, WrongSaveDataType {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            return isWarpAvailableWithFiles(warp);
        } else {
            return isWarpAvailableWithMySQL(warp);
        }
    }

    public static String getPasswordWithFiles(String warp)
            throws SQLException, WrongSaveDataType, WarpNotFound {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            File a = new File(Options.pluginDir + "warps/" + warp + ".yml");
            if(a.exists()) {
                FileConfiguration f = YamlConfiguration.loadConfiguration(a);
                return f.getString("protection.value");
            } else {
                throw new WarpNotFound(AquaWarpsMessages.get("System.Exception.300"));
            }
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
    }

    public static String getPasswordWithMySQL(String warp)
            throws SQLException, WrongSaveDataType {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("mysql")){
            String t = null;
            ResultSet r = ConnectDatabase.exeq(
                    "SELECT * FROM " + Options.mysql_table_protection + " WHERE name='" + warp + "';");
            if(r.next()) {
                t = r.getString("pass");
            }
            r.close();
            return t;
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
    }

    public static String getPasswordAutoSaveDataType(String warp)
            throws WarpNotFound, SQLException, WrongSaveDataType {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            return getPasswordWithFiles(warp);
        } else {
            return getPasswordWithMySQL(warp);
        }
    }

    public static void exeCmds(String cmd, Player player, String warp, CommandSender sender){
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                cmd.replace("<player>",player.getName())
                        .replace("<warp>",warp)
                        .replace("<sender>",sender.getName())
                        .replace("<world>",player.getWorld().getName()));
    }

    public static Boolean withdrawEnergy(CommandSender s, String warp) {
        if(s instanceof Player){
            try {
                Player sp = (Player) s;
                int ery_withdraw = Energy.consumption(warp, WarpStatus.getStatusAutoSaveDataType(warp));
                int ery_now = Energy.get(sp.getUniqueId().toString());
                if(ery_withdraw <= ery_now) {
                    Energy.set(sp.getUniqueId().toString(), ery_now - ery_withdraw);
                    return true;
                } else {
                    Strings.sendPlayer(Configuration.message.getString("do_not_enough_energy"), sp);
                    return false;
                }
            } catch(IOException | EnergyDataNotFound | WarpNotFound | WrongSaveDataType | SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }

    public static Boolean withdrawMoney(CommandSender s, String warp) {
        if(s instanceof Player){
            Player sp = (Player) s;
            try {
                WarpStatusTypes wst = WarpStatus.getStatusAutoSaveDataType(warp);
                double cost = ServiceCharge.getCostAutoSaveDataType(warp, wst);
                double current = ServiceCharge.getMoney(sp);
                double new_money = current - cost;
                if(new_money < 0) {
                    Strings.sendPlayer(Configuration.message.getString("do_not_enough_money"), sp);
                    return false;
                } else {
                    try {
                        return ServiceCharge.fee(warp, sp);
                    } catch(WarpNotFound | WrongSaveDataType | SQLException | LackMoney e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            } catch(WarpNotFound | WrongSaveDataType | SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }
}
