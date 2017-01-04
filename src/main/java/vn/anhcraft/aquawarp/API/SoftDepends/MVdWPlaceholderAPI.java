package vn.anhcraft.aquawarp.API.SoftDepends;

import org.bukkit.Bukkit;
import vn.anhcraft.aquawarp.AquaWarp;
import vn.anhcraft.aquawarp.AquaWarpsMessages;
import vn.anhcraft.aquawarp.Util.Configuration;
import vn.anhcraft.aquawarp.Util.RegisterMVdWPlaceholderAPI;
import vn.anhcraft.aquawarp.Util.Strings;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class MVdWPlaceholderAPI {
    public static void setup(Boolean disableIfNotFound){
        if (Configuration.config.getBoolean("hook.MVdWPlaceholderAPI")){
            if(!Bukkit.getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")
                && disableIfNotFound){
                Strings.sendSender(AquaWarpsMessages.get("System.SoftDepends.MVdWPlaceholderAPINotReady"));
                Bukkit.getServer().getPluginManager().disablePlugin(AquaWarp.plugin);
            } else {
                new RegisterMVdWPlaceholderAPI(AquaWarp.plugin);
            }
        }
    }
}
