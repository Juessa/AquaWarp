package org.anhcraft.aquawarp.Commands;

import org.anhcraft.aquawarp.API.CreateWarp;
import org.anhcraft.aquawarp.API.WarpLocation;
import org.anhcraft.aquawarp.Events.onCreateNewWarp;
import org.anhcraft.aquawarp.Exceptions.WarpUnavailable;
import org.anhcraft.aquawarp.Exceptions.WrongSaveDataType;
import org.anhcraft.aquawarp.Listeners.TpWarpLocked;
import org.anhcraft.aquawarp.Util.Configuration;
import org.anhcraft.aquawarp.Util.PermissionsManager;
import org.anhcraft.aquawarp.Util.Strings;
import org.anhcraft.spaciouslib.Converter.DataTypes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class SetWarp extends TpWarpLocked {
    public static void main(CommandSender s, String warp, Location loc){
        if(PermissionsManager.hasAll(s) ||
                s.hasPermission(PermissionsManager.getPermission("warps.setwarp")[0].replace("<name>",warp)) ||
                s.hasPermission(PermissionsManager.getPermission("warps.setwarp")[1].replace("<name>",warp)) ||
                s.hasPermission(PermissionsManager.getPermission("warps.setwarp")[2].replace("<name>",warp))) {
            try {
                if(isWarpAvailableAutoSaveData(warp)) {
                    Strings.sendSender(Configuration.message.getString("warp_available"), s);
                    return;
                } else {
                    WarpLocation wl = new WarpLocation(loc);
                    onCreateNewWarp event = new onCreateNewWarp(warp, wl, s);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    warp = event.getWarpName();
                    wl = event.getWarpLocation();
                    if(!event.isCancelled()) {
                        if(CreateWarp.CreateWarpAutoSaveDataType(warp, wl)) {
                            Strings.sendSender(Configuration.message.getString("warp_created").replace("@warp", warp), s);
                        }
                    }
                }
            } catch(SQLException | WrongSaveDataType | WarpUnavailable | IOException e) {
                e.printStackTrace();
            }
        } else {
            Strings.sendSender(Configuration.message.getString("require_permission"), s);
        }
    }

    public static Location toLocation(String x, String y, String z, String yaw, String pitch, String world){
        if(Bukkit.getServer().getWorld(world) != null) {
            return new Location(
                    Bukkit.getServer().getWorld(world),
                    new DataTypes().toDouble(x),
                    new DataTypes().toDouble(y),
                    new DataTypes().toDouble(z),
                    new DataTypes().toFloat(yaw),
                    new DataTypes().toFloat(pitch)
            );
        } else {
            return null;
        }
    }
}
