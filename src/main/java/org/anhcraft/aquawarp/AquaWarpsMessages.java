package org.anhcraft.aquawarp;

import java.util.HashMap;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class AquaWarpsMessages {
    public static HashMap<String, String> messages = new HashMap<String, String>();

    public static String get(String key){
        return messages.get(key);
    }

    public static void set(String k, String v){
        messages.put(k, v);
    }
    
    public void setup(){
        set("System.BoardcastOnEnable", "&aPlugin has been enabled!");
        set("System.BoardcastOnDisable", "&cPlugin has been disabled!");
        set("System.Error.1000", "&cError: 1000 &5[<fname>]");
        set("System.Error.1001", "&cError: 1001 &5[<fname>]");
        set("System.Error.1002", "&cError: 1002 &5[<fname>]");
        set("System.Error.1003", "&cError: 1003 &5[<v>]");
        set("System.SoftDepends.VaultNotReady", "&cThis plugin requires installing " +
                "&r&bVault&r&c (and &r&bVault&r&c has been enabled). " +
                "Because can't find &r&bVault&r&c, therefore, this plugin will be disabled.");
        set("System.SoftDepends.MVdWPlaceholderAPINotReady", "&cA feature of this plugin requires installing " +
                "&r&bMVdWPlaceholderAPI&r&c (and &r&bMVdWPlaceholderAPI&r&c has been enabled). " +
                "Because can't find &r&bMVdWPlaceholderAPI&r&c, therefore, this plugin will be disabled.");
        set("System.SoftDepends.PlaceholderAPINotReady", "&cA feature of this plugin requires installing " +
                "&r&bPlaceholderAPI&r&c (and &r&bPlaceholderAPI&r&c has been enabled). " +
                "Because can't find &r&bPlaceholderAPI&r&c, therefore, this plugin will be disabled.");
        set("System.CheckUpdate.available.1", "&4There is a newer version available.");
        set("System.CheckUpdate.available.2", "&6Newest version: &5&l");
        set("System.CheckUpdate.newest", "&aThis is the newest version!");
        set("System.Exception.300", "Exception-300");
        set("System.Exception.301", "Exception-301");
        set("System.Exception.302", "Exception-302");
        set("System.Exception.303", "Exception-303");
        set("System.Exception.304", "Exception-304");
        set("System.Exception.305", "Exception-305");
        set("System.UpdateFile.Config", "Your config file is oldated. " +
                "We will update it for you. You can check the old file with name config_old.yml");
        set("System.UpdateFile.Commands", "Your commands file is old. " +
                "We will update it for you. You can check the old file with name commands_old.yml");
        set("System.UpdateFile.Permissions", "Your permissions file is old. " +
                "We will update it for you. You can check the old file with name permissions_old.yml");
        set("Utils.Converter.FirstStage", "&6Welcome to Converter Center! " +
                "First, enter a plugin you want to import or export. Or type 'cancel' to exit.");
        set("Utils.Converter.Exited", "&cExited.");
        set("Utils.Converter.SecondStage", "&6You selected <type>. Second, do you want to keep old warp?:" +
                "&a 'true' - 'false'. Or type 'cancel' to exit.");
        set("Utils.Converter.ThirdStage", "&6You selected <type>. Third, select the save data type to export/import:" +
                "&a 'mysql' - 'file'. Or type 'cancel' to exit.");
        set("Utils.Converter.Starting", "&6Starting export/import with <type>...");
        set("Utils.Converter.Complete", "&aCompleted!");
    }
}
