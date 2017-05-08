package org.anhcraft.aquawarp.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.anhcraft.aquawarp.API.SoftDepends.MVdWPlaceholderAPI;
import org.anhcraft.aquawarp.API.SoftDepends.PlaceHolderAPI;
import org.anhcraft.aquawarp.AquaWarp;
import org.anhcraft.aquawarp.Commands.*;
import org.anhcraft.aquawarp.Converter.ExportSetup;
import org.anhcraft.aquawarp.Converter.ImportSetup;
import org.anhcraft.aquawarp.GUI.WarpGUI;
import org.anhcraft.aquawarp.Options;
/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class OnCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("warp")) {
            if(1 < args.length){
                TpWarp.main(args[0], args[1], sender, true);
                return true;
            }
            else if(args.length == 1){
                TpWarp.main(args[0], sender.getName(), sender, false);
                return true;
            } else {
                if(!(sender instanceof Player) || !Configuration.config.getBoolean("tpWarp.useGUI")) {
                    new ListWarps().run(sender);
                    return true;
                } else {
                    WarpGUI.show((Player) sender, 0);
                    return true;
                }
            }
        }

        /** MOST COMMAND
         *
         *  /warps <sub> <arg...>
         *
         **/
        if (cmd.getName().equals("warps")) {
            if(args.length < 1){
                for(String s : Configuration.config.getStringList("helpWarp")){
                    Strings.sendSenderNoPrefix(s, sender);
                }
                return true;
            } else{
                if(args[0].equals(Configuration.commands.getString("warps.setwarp"))){
                    if(args.length < 8){
                        if(sender instanceof Player) {
                            if(args.length == 2) {
                                SetWarp.main(sender, args[1], ((Player) sender).getLocation());
                                return true;
                            } else {
                                Strings.sendSender(Configuration.message.getString("setwarp_syntax_error")
                                            .replace("@cmd",Configuration.commands.getString("warps.setwarp")),sender);
                                return false;
                            }
                        } else {
                            Strings.sendSender(Configuration.message.getString("sender_must_a_player"),sender);
                            return false;
                        }
                    } else {
                        Location loc = SetWarp.toLocation(args[2],args[3],args[4],args[5],args[6],args[7]);
                        if(loc != null) {
                            SetWarp.main(sender, args[1], loc);
                            return true;
                        } else {
                            Strings.sendSender(Configuration.message.getString("world_not_found"),sender);
                            return false;
                        }
                    }
                }
                else if(args[0].equals(Configuration.commands.getString("warps.delwarp"))){
                    if(1 < args.length){
                        new DelWarp(sender, args[1]);
                        return true;
                    } else {
                        Strings.sendSender(Configuration.message.getString("delwarp_syntax_error")
                                .replace("@cmd",Configuration.commands.getString("warps.delwarp")),sender);
                        return false;
                    }
                }
                else if(args[0].equals(Configuration.commands.getString("warps.lockwarp"))){
                    if(2 < args.length){
                        new LockWarp(sender, args[1], args[2]);
                        return true;
                    } else {
                        Strings.sendSender(Configuration.message.getString("lockwarp_syntax_error")
                                .replace("@cmd",Configuration.commands.getString("warps.lockwarp")),sender);
                        return false;
                    }
                }
                else if(args[0].equals(Configuration.commands.getString("warps.unlockwarp"))){
                    if(1 < args.length){
                        new UnlockWarp(sender, args[1]);
                        return true;
                    } else {
                        Strings.sendSender(Configuration.message.getString("unlockwarp_syntax_error")
                                .replace("@cmd",Configuration.commands.getString("warps.unlockwarp")),sender);
                        return false;
                    }
                }
                else if(args[0].equals(Configuration.commands.getString("warps.fee"))){
                    if(3 < args.length){
                        new Fee(sender, args[1], args[2], args[3]);
                        return true;
                    } else {
                        Strings.sendSender(Configuration.message.getString("fee_syntax_error")
                                .replace("@cmd",Configuration.commands.getString("warps.fee")),sender);
                        return false;
                    }
                }
                else if(args[0].equals(Configuration.commands.getString("warps.gui"))){
                    if(sender instanceof Player){
                        WarpGUI.show((Player) sender, 0);
                    } else {
                        Strings.sendSender(Configuration.message.getString("sender_must_a_player"),sender);
                        return false;
                    }
                }
                else {
                    Strings.sendSender(Configuration.message.getString("cmd_not_found"),sender);
                    return false;
                }
            }
        }

        /**
         * 1.4.0
         * Aqua Warp
         */
        if (cmd.getName().equals("aquawarp")) {
            Strings.sendSenderNoPrefix(
                    "&b&lAquaWarp&r &5v"+ Options.pluginVersion+".&r &6Created by&r &6&lAnh Craft.", sender);
            return true;
        }

        /**
         * 1.3.0
         * Warp Admin
         */
        if (cmd.getName().equals("warpadmin")) {
            /** 1.3.0 **/
            if(0 < args.length){
                if(args[0].equals(Configuration.commands.getString("warpadmin.reload"))){
                    String[] reload = PermissionsManager.getPermission("warpadmin.reload");
                    if(sender.hasPermission(reload[0]) ||
                       sender.hasPermission(reload[0]) ||
                       sender.hasPermission(reload[0]) ||
                       PermissionsManager.hasAll(sender)
                      ){
                        Configuration.load();
                        ConnectDatabase.setup();
                        MVdWPlaceholderAPI.setup(true); // + register placeholders
                        PlaceHolderAPI.setup(true); // + register placeholders
                        // schedulers
                        Bukkit.getServer().getScheduler().cancelTasks(AquaWarp.plugin);
                        AquaWarp.setup_SchedulersEveryTime();
                        //--------------------------
                        Strings.sendSender(Configuration.message.getString("reloaded_configuration"),sender);
                        return true;
                    } else {
                        Strings.sendSender(Configuration.message.getString("require_permission"),sender);
                        return false;
                    }
                }
                else if(args[0].equals(Configuration.commands.getString("warpadmin.export"))){
                    if(sender instanceof Player){
                        String[] export = PermissionsManager.getPermission("warpadmin.export");
                        if(sender.hasPermission(export[0]) || PermissionsManager.hasAll(sender)){
                            ExportSetup.addPlayer((Player) sender);
                            return true;
                        } else {
                            Strings.sendSender(Configuration.message.getString("require_permission"),sender);
                            return false;
                        }
                    } else {
                        Strings.sendSender(Configuration.message.getString("sender_must_a_player"),sender);
                        return false;
                    }
                } else if(args[0].equals(Configuration.commands.getString("warpadmin.import"))){
                    if(sender instanceof Player){
                        String[] import_ = PermissionsManager.getPermission("warpadmin.import");
                        if(sender.hasPermission(import_[0]) || PermissionsManager.hasAll(sender)){
                            ImportSetup.addPlayer((Player) sender);
                            return true;
                        } else {
                            Strings.sendSender(Configuration.message.getString("require_permission"),sender);
                            return false;
                        }
                    } else {
                        Strings.sendSender(Configuration.message.getString("sender_must_a_player"),sender);
                        return false;
                    }
                } else {
                    Strings.sendSender(Configuration.message.getString("cmd_not_found"),sender);
                    return false;
                }
            } else {
                for(String str : Options.listWarpAdminCommandMessages){
                    Strings.sendSenderNoPrefix(str, sender);
                }
                return true;
            }
        }

        return false;
    }
}
