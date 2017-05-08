package org.anhcraft.aquawarp.API.SoftDepends;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.anhcraft.aquawarp.AquaWarp;
import org.anhcraft.aquawarp.AquaWarpsMessages;
import org.anhcraft.aquawarp.Util.Strings;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class Vault {
    public static Economy economy = null;

    public static void setup(Boolean disableIfNotFound){
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")) {
            RegisteredServiceProvider<Economy> economyProvider =
                    Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            economy = economyProvider.getProvider();
        } else {
            if(disableIfNotFound){
                Strings.sendSender(AquaWarpsMessages.get("System.SoftDepends.VaultNotReady"));
                Bukkit.getServer().getPluginManager().disablePlugin(AquaWarp.plugin);
            }
        }
    }
}
