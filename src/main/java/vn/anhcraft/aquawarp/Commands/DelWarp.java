package vn.anhcraft.aquawarp.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import vn.anhcraft.aquawarp.API.DeleteWarp;
import vn.anhcraft.aquawarp.API.WarpMethods;
import vn.anhcraft.aquawarp.API.WarpStatus;
import vn.anhcraft.aquawarp.API.WarpStatusTypes;
import vn.anhcraft.aquawarp.Events.onDeleteWarp;
import vn.anhcraft.aquawarp.Exceptions.WarpNotFound;
import vn.anhcraft.aquawarp.Exceptions.WrongSaveDataType;
import vn.anhcraft.aquawarp.Util.Configuration;
import vn.anhcraft.aquawarp.Util.PermissionsManager;
import vn.anhcraft.aquawarp.Util.Strings;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class DelWarp extends WarpMethods {
    public DelWarp(CommandSender s, String warp){
        if(PermissionsManager.hasAll(s) ||
                s.hasPermission(PermissionsManager.getPermission("warps.delwarp")[0].replace("<name>",warp)) ||
                s.hasPermission(PermissionsManager.getPermission("warps.delwarp")[1].replace("<name>",warp)) ||
                s.hasPermission(PermissionsManager.getPermission("warps.delwarp")[2].replace("<name>",warp))) {
            try {
                if(isWarpAvailableAutoSaveData(warp)) {
                    onDeleteWarp event = new onDeleteWarp(warp, s);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    s = event.getSender();
                    warp = event.getWarpName();
                    if(!event.isCancelled()) {
                        WarpStatus.setStatusAutoSaveDataType(warp, WarpStatusTypes.UNLOCKED, "");
                        if(DeleteWarp.DeleteWarpAutoSaveDataType(warp)) {
                            Strings.sendSender(Configuration.message.getString("warp_deleted").replace("@warp", warp), s);
                        }
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
