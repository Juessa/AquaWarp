package vn.anhcraft.aquawarp.Listeners;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import vn.anhcraft.aquawarp.API.Energy;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.Util.Configuration;

import java.io.File;
import java.io.IOException;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class EnergyOnJoin implements Listener {
    @EventHandler
    private void onPlayerLogin(PlayerLoginEvent e){
        String a = e.getPlayer().getUniqueId().toString();
        File c = new File(Options.pluginDir + "energydata/"+a+".yml");
        if(!c.exists()){
            try {
                Energy.setupFile(e.getPlayer());
                YamlConfiguration y = YamlConfiguration.loadConfiguration(c);
                y.set("name",e.getPlayer().getName());
                y.set("energy",Configuration.config.getInt("tpWarp.energy.maxEnergy.default"));
                y.save(c);
            } catch(IOException es) {
                es.printStackTrace();
            }
        }
        if(Configuration.config.getBoolean("tpWarp.energy.reset")){
            try {
                YamlConfiguration y = YamlConfiguration.loadConfiguration(c);
                y.set("name",e.getPlayer().getName());
                y.set("energy",Energy.getMax(e.getPlayer()));
                y.save(c);
            } catch (IOException es) {
                es.printStackTrace();
            }
        }
    }
}