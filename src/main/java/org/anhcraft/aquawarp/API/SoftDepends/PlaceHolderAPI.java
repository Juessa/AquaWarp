package org.anhcraft.aquawarp.API.SoftDepends;

import org.bukkit.Bukkit;
import org.anhcraft.aquawarp.AquaWarp;
import org.anhcraft.aquawarp.AquaWarpsMessages;
import org.anhcraft.aquawarp.Util.Configuration;
import org.anhcraft.aquawarp.Util.RegisterPlaceholderAPI;
import org.anhcraft.aquawarp.Util.Strings;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class PlaceHolderAPI {
    public static void setup(Boolean disableIfNotFound){
        if (Configuration.config.getBoolean("hook.PlaceholderAPI")){
            if(!Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")
                && disableIfNotFound){
                Strings.sendSender(AquaWarpsMessages.get("System.SoftDepends.PlaceholderAPINotReady"));
                Bukkit.getServer().getPluginManager().disablePlugin(AquaWarp.plugin);
            } else {
                new RegisterPlaceholderAPI(AquaWarp.plugin).hook();
            }
        }
    }
}
