package vn.anhcraft.aquawarp.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vn.anhcraft.aquawarp.API.WarpStatus;
import vn.anhcraft.aquawarp.API.WarpStatusTypes;
import vn.anhcraft.aquawarp.Exceptions.WarpNotFound;
import vn.anhcraft.aquawarp.Exceptions.WrongSaveDataType;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.Util.Configuration;
import vn.anhcraft.aquawarp.Util.ConnectDatabase;
import vn.anhcraft.aquawarp.Util.PermissionsManager;
import vn.anhcraft.aquawarp.Util.Strings;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class ListWarps {
    public ListWarps ListWarps(){
        return this;
    }

    public void ListWarpsWithMySQL(CommandSender sender){
        try {
            ResultSet r = ConnectDatabase.exeq("SELECT * FROM "+ Options.mysql_table_warp+";");
            String part = "";
            while (r.next()) {
                String n = r.getString("name");
                if(WarpStatus.getStatusAutoSaveDataType(n).equals(WarpStatusTypes.LOCKED)) {
                    part += "&5,&r &6" + n + "&r";
                } else {
                    part += "&5,&r &a" + n + "&r";
                }
            }
            Strings.sendSenderNoPrefix(
                    Configuration.message.getString("list_warps")
                    .replace("@size", Integer.toString(ConnectDatabase.rowsize(Options.mysql_table_warp)))
                    .replace("@list", part.replaceFirst("&5,&r ","")), sender);
            r.close();
        } catch (SQLException | WrongSaveDataType | WarpNotFound e) {
            e.printStackTrace();
        }
    }

    public void ListWarpsWithFiles(CommandSender sender){
        try {
            String part = "";
            File[] files = new File(Options.pluginDir + "warps").listFiles(
                new FilenameFilter() {
                        @Override public boolean accept(File dir, String name) {
                            return name.endsWith(".yml");
                    }
                }
            );
            int g = 0;
            for (File file : files) {
                g += 1;
                FileConfiguration f = YamlConfiguration.loadConfiguration(file);
                String n = f.getString("name");
                if(WarpStatus.getStatusAutoSaveDataType(n).equals(WarpStatusTypes.LOCKED)) {
                    part += "&5,&r &6" + n + "&r";
                } else {
                    part += "&5,&r &a" + n + "&r";
                }
            }
            Strings.sendSenderNoPrefix(
                    Configuration.message.getString("list_warps")
                            .replace("@size", Integer.toString(g))
                            .replace("@list", part.replaceFirst("&5,&r ","")), sender);
        } catch (SQLException | WrongSaveDataType | WarpNotFound e) {
            e.printStackTrace();
        }
    }

    public void run(CommandSender sender) {
        if(sender.hasPermission(PermissionsManager.getPermission("warps.listwarp")[0]) ||
                PermissionsManager.hasAll(sender)) {
            if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
                ListWarpsWithFiles(sender);
            } else {
                ListWarpsWithMySQL(sender);
            }
        } else {
            Strings.sendSender(Configuration.message.getString("require_permission"), sender);
        }
    }
}
