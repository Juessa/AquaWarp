package org.anhcraft.aquawarp.API.SoftDepends;

import org.bukkit.Bukkit;
import org.anhcraft.aquawarp.AquaWarp;
import org.anhcraft.aquawarp.AquaWarpsMessages;
import org.anhcraft.aquawarp.Util.Configuration;
import org.anhcraft.aquawarp.Util.RegisterMVdWPlaceholderAPI;
import org.anhcraft.aquawarp.Util.Strings;

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
