package vn.anhcraft.aquawarp.Scheduler;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import vn.anhcraft.aquawarp.API.Energy;
import vn.anhcraft.aquawarp.AquaWarp;
import vn.anhcraft.aquawarp.Exceptions.EnergyDataNotFound;
import vn.anhcraft.aquawarp.Util.Configuration;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class RecoveryEnergy {
    public static ArrayList<String> playerUUIDOnlineChecked = new ArrayList<String>();
    public static ArrayList<String> playerUUIDOfflineChecked = new ArrayList<String>();

    public RecoveryEnergy(){

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AquaWarp.plugin, new Runnable() {
            @Override
            public void run() {
                onlinePlayers();
                offlinePlayers();
            }
        }, Configuration.config.getInt("tpWarp.energy.energyRecoveryTime"),
                Configuration.config.getInt("tpWarp.energy.energyRecoveryTime"));
    }

    public void onlinePlayers(){
        playerUUIDOnlineChecked.clear();
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            String uuid = p.getUniqueId().toString();
            try {
                int e = Energy.get(uuid);
                if(e < Energy.getMax(p)){
                    int q =  Energy.getMax(p)-e;
                    if(q < Energy.getRecovery(p)){
                        try {
                            Energy.set(uuid, Energy.getMax(p));
                        } catch (IOException g) {
                            g.printStackTrace();
                        }
                    } else {
                        try {
                            Energy.set(uuid, e+ Energy.getRecovery(p));
                        } catch (IOException g) {
                            g.printStackTrace();
                        }
                    }
                }
                playerUUIDOnlineChecked.add(uuid);
            } catch(EnergyDataNotFound energyDataNotFound) {
                energyDataNotFound.printStackTrace();
            }
        }
    }

    public void offlinePlayers(){
        playerUUIDOfflineChecked.clear();
        if(Configuration.config.getBoolean("tpWarp.energy.energyRecoveryForOffline")) {
            for(OfflinePlayer ps : Bukkit.getServer().getOnlinePlayers()) {
                Player p = ps.getPlayer();
                String uuid = p.getUniqueId().toString();
                try {
                    if(!isPlayerOnline(uuid)) {
                        int e = Energy.get(uuid);
                        if(e < Energy.getMax(p)) {
                            int q = Energy.getMax(p) - e;
                            if(q < Energy.getRecovery(p)) {
                                try {
                                    Energy.set(uuid, Energy.getMax(p));
                                } catch(IOException g) {
                                    g.printStackTrace();
                                }
                            } else {
                                try {
                                    Energy.set(uuid, e + Energy.getRecovery(p));
                                } catch(IOException g) {
                                    g.printStackTrace();
                                }
                            }
                        }

                        playerUUIDOfflineChecked.add(uuid);
                    }
                } catch(EnergyDataNotFound energyDataNotFound) {
                    energyDataNotFound.printStackTrace();
                }
            }
        }
    }

    public Boolean isPlayerOnline(String uuid){
        Boolean e = false;
        for(int v = 0; v < playerUUIDOnlineChecked.size(); v++){
            String px = playerUUIDOnlineChecked.get(v);
            if(px.equals(uuid)){
                e = true;
                break;
            }
        }
        return e;
    }
}
