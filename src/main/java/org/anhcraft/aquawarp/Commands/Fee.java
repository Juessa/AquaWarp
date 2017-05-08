package org.anhcraft.aquawarp.Commands;

import org.anhcraft.aquawarp.API.ServiceCharge;
import org.anhcraft.aquawarp.API.WarpMethods;
import org.anhcraft.aquawarp.Events.onChangeCost;
import org.anhcraft.aquawarp.Exceptions.WarpNotFound;
import org.anhcraft.aquawarp.Exceptions.WrongSaveDataType;
import org.anhcraft.aquawarp.Util.Configuration;
import org.anhcraft.aquawarp.Util.PermissionsManager;
import org.anhcraft.aquawarp.Util.Strings;
import org.anhcraft.spaciouslib.Converter.DataTypes;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class Fee extends WarpMethods {
    public Fee(CommandSender s, String warp, String costLock, String costUnlock){
            if(PermissionsManager.hasAll(s) ||
                    s.hasPermission(PermissionsManager.getPermission("warps.fee")[0].replace("<name>",warp)) ||
                    s.hasPermission(PermissionsManager.getPermission("warps.fee")[1].replace("<name>",warp)) ||
                    s.hasPermission(PermissionsManager.getPermission("warps.fee")[2].replace("<name>",warp))) {
                try {
                    if(isWarpAvailableAutoSaveData(warp)) {
                        onChangeCost event = new onChangeCost(warp, s,
                                new DataTypes().toDouble(costLock), new DataTypes().toDouble(costUnlock));
                        Bukkit.getServer().getPluginManager().callEvent(event);
                        s = event.getSender();
                        warp = event.getWarpName();
                        if(!event.isCancelled()) {
                            ServiceCharge.setCostAutoSaveDataType(warp, event.getCostLock(), event.getCostUnlock());
                            Strings.sendSender(Configuration.message.getString("cost_updated").replace("@warp", warp), s);
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
