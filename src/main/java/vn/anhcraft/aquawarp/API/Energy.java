package vn.anhcraft.aquawarp.API;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import vn.anhcraft.aquawarp.AquaWarpsMessages;
import vn.anhcraft.aquawarp.Exceptions.EnergyDataNotFound;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.Util.Configuration;
import vn.anhcraft.aquawarp.Util.PermissionsManager;
import vn.anhcraft.iceapi.Converter.IceConverter;
import java.io.File;
import java.io.IOException;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public interface Energy {
     static int consumption(String warp, WarpStatusTypes status){
         if(status.equals(WarpStatusTypes.LOCKED)) {
             int c = Configuration.config.getInt("tpWarp.energy.energyConsumption.default.locked");
             for(String ws : Configuration.config.getStringList("tpWarp.energy.energyConsumption.warps")) {
                 if(ws.split(":")[0].equals(warp)) {
                     c = new IceConverter().toInt(ws.split(":")[2]);
                     break;
                 } else {
                     continue;
                 }
             }
             return c;
         } else if(status.equals(WarpStatusTypes.UNLOCKED)) {
             int c = Configuration.config.getInt("tpWarp.energy.energyConsumption.default.unlocked");
             for(String ws : Configuration.config.getStringList("tpWarp.energy.energyConsumption.warps")) {
                 if(ws.split(":")[0].equals(warp)) {
                     c = new IceConverter().toInt(ws.split(":")[1]);
                     break;
                 } else {
                     continue;
                 }
             }
             return c;
         } else {
             return -1;
         }
     }

     static int get(String uuid) throws EnergyDataNotFound {
        File e = new File(Options.pluginDir + "energydata/"+uuid+".yml");
        if(e.exists()) {
            YamlConfiguration a = YamlConfiguration.loadConfiguration(e);
            return a.getInt("energy");
        } else {
            throw new EnergyDataNotFound(AquaWarpsMessages.get("System.Exception.302"));
        }
     }

    static void setupFile(Player player) throws IOException {
        String uuid = player.getUniqueId().toString();
        File e = new File(Options.pluginDir + "energydata/"+uuid+".yml");
        if(!e.exists()) {
            e.createNewFile();
        }
    }

    static int getMax(Player p) {
        if(p.isOp()){
            return Configuration.config.getInt("tpWarp.energy.maxEnergy.op");
        } else {
            int max = Configuration.config.getInt("tpWarp.energy.maxEnergy.default");
            for(String perm: Configuration.config.getStringList("tpWarp.energy.maxEnergy.groups")){
                if(p.hasPermission(
                        PermissionsManager.getPermission("warps.tpwarp_maxEnergy")[0]
                                .replace("<group>",
                                        perm.split(":")[0]))){
                    max = new IceConverter().toInt(perm.split(":")[1]);
                    break;
                } else {
                    continue;
                }
            }
            return max;
        }
    }

    static int getRecovery(Player p) {
        if(p.isOp()){
            return Configuration.config.getInt("tpWarp.energy.energyRecoveryAmount.op");
        } else {
            int max = Configuration.config.getInt("tpWarp.energy.energyRecoveryAmount.default");
            for(String perm: Configuration.config.getStringList("tpWarp.energy.energyRecoveryAmount.groups")){
                if(p.hasPermission(
                        PermissionsManager.getPermission("warps.tpwarp_energyRecovery")[0]
                                .replace("<group>",
                                        perm.split(":")[0]))){
                    max = new IceConverter().toInt(perm.split(":")[1]);
                    break;
                } else {
                    continue;
                }
            }
            return max;
        }
    }

     static void set(String uuid, int value) throws EnergyDataNotFound, IOException {
        File e = new File(Options.pluginDir + "energydata/"+uuid+".yml");
        if(e.exists()) {
            YamlConfiguration a = YamlConfiguration.loadConfiguration(e);
            a.set("energy", value);
            a.save(e);
        } else {
            throw new EnergyDataNotFound(AquaWarpsMessages.get("System.Exception.302"));
        }
    }
}
