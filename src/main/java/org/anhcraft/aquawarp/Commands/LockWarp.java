package org.anhcraft.aquawarp.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.anhcraft.aquawarp.API.WarpMethods;
import org.anhcraft.aquawarp.API.WarpStatus;
import org.anhcraft.aquawarp.API.WarpStatusTypes;
import org.anhcraft.aquawarp.Events.onLockWarp;
import org.anhcraft.aquawarp.Exceptions.WarpNotFound;
import org.anhcraft.aquawarp.Exceptions.WrongSaveDataType;
import org.anhcraft.aquawarp.Util.Configuration;
import org.anhcraft.aquawarp.Util.PermissionsManager;
import org.anhcraft.aquawarp.Util.Strings;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class LockWarp extends WarpMethods {
    public LockWarp(CommandSender s, String warp, String password){
            if(PermissionsManager.hasAll(s) ||
                    s.hasPermission(PermissionsManager.getPermission("warps.lockwarp")[0].replace("<name>",warp)) ||
                    s.hasPermission(PermissionsManager.getPermission("warps.lockwarp")[1].replace("<name>",warp)) ||
                    s.hasPermission(PermissionsManager.getPermission("warps.lockwarp")[2].replace("<name>",warp))) {
                try {
                    if(isWarpAvailableAutoSaveData(warp)) {
                        if(WarpStatus.getStatusAutoSaveDataType(warp).equals(WarpStatusTypes.UNLOCKED)) {
                            if(isPasswordSafe(password)) {
                                onLockWarp event = new onLockWarp(warp, s, password);
                                Bukkit.getServer().getPluginManager().callEvent(event);
                                s = event.getSender();
                                warp = event.getWarpName();
                                if(!event.isCancelled()) {
                                    WarpStatus.setStatusAutoSaveDataType(warp, WarpStatusTypes.LOCKED, password);
                                    Strings.sendSender(Configuration.message.getString("warp_locked").replace("@warp", warp), s);
                                }
                            } else {
                                Strings.sendSender(Configuration.message.getString("password_unsafe"), s);
                                return;
                            }
                        } else {
                            Strings.sendSender(Configuration.message.getString("warp_already_locked"), s);
                            return;
                        }
                    } else {
                        Strings.sendSender(Configuration.message.getString("warp_not_found"), s);
                        return;
                    }
                } catch(SQLException | WrongSaveDataType | WarpNotFound | IOException e) {
                    e.printStackTrace();
                }
            } else {
                Strings.sendSender(Configuration.message.getString("require_permission"), s);
            }
        }
}
