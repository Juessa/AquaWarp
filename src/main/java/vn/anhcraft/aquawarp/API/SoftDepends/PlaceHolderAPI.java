package vn.anhcraft.aquawarp.API.SoftDepends;

import org.bukkit.Bukkit;
import vn.anhcraft.aquawarp.AquaWarp;
import vn.anhcraft.aquawarp.AquaWarpsMessages;
import vn.anhcraft.aquawarp.Util.Configuration;
import vn.anhcraft.aquawarp.Util.RegisterPlaceholderAPI;
import vn.anhcraft.aquawarp.Util.Strings;

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
