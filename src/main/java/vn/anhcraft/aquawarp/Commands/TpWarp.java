package vn.anhcraft.aquawarp.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.anhcraft.aquawarp.API.ServiceCharge;
import vn.anhcraft.aquawarp.API.TeleportToWarp;
import vn.anhcraft.aquawarp.API.WarpStatus;
import vn.anhcraft.aquawarp.API.WarpStatusTypes;
import vn.anhcraft.aquawarp.Events.onPlayerTeleportWarp;
import vn.anhcraft.aquawarp.Exceptions.PlayerIsNotOnline;
import vn.anhcraft.aquawarp.Exceptions.WarpNotFound;
import vn.anhcraft.aquawarp.Exceptions.WrongSaveDataType;
import vn.anhcraft.aquawarp.Listeners.TpWarpLocked;
import vn.anhcraft.aquawarp.Particles.PlayParticles;
import vn.anhcraft.aquawarp.Util.BukkitVersionManager;
import vn.anhcraft.aquawarp.Util.Configuration;
import vn.anhcraft.aquawarp.Util.PermissionsManager;
import vn.anhcraft.aquawarp.Util.Strings;
import java.sql.SQLException;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class TpWarp extends TpWarpLocked {
    public static void teleport(String warp, String player, CommandSender sender) {
        Player p = Bukkit.getServer().getPlayerExact(player);
        Boolean m = false;
        for(Player x : Bukkit.getServer().getOnlinePlayers()) {
            if(x.getName().equals(player)){
                m = true;
            }
        }
        if(m) {
            for(String c: Configuration.config.getStringList("tpWarp.commandBeforeWarping")){
                exeCmds(c, p, warp, sender);
            }

            Boolean wm = true;
            if(ServiceCharge.isEnable()) {
                wm = withdrawMoney(sender, warp);
            }

            onPlayerTeleportWarp event = new onPlayerTeleportWarp(warp, p, sender);
            Bukkit.getServer().getPluginManager().callEvent(event);
            warp = event.getWarpName();
            p = event.getPlayer();
            sender = event.getSender();

            if(withdrawEnergy(sender, warp) && wm && !event.isCancelled()){
                try {
                    Boolean teleport = TeleportToWarp.teleportAutoSaveDataType(p, warp);
                    if(teleport) {
                        for(String c : Configuration.config.getStringList("tpWarp.commandAfterWarping")) {
                            exeCmds(c, p, warp, sender);
                        }
                        World w = p.getLocation().getWorld();
                        if(Configuration.config.getBoolean("tpWarp.effect.enable")) {
                            new PlayParticles(
                                    Configuration.config.getString("tpWarp.effect.type"),
                                    p.getLocation().getBlockX(),
                                    p.getLocation().getBlockY(),
                                    p.getLocation().getBlockZ(),
                                    Configuration.config.getInt("tpWarp.effect.time")
                                    , 0, Configuration.config.getInt("tpWarp.effect.time"), w);
                        }
                        if(Configuration.config.getBoolean("tpWarp.sound")) {
                            Location l = new Location(w, p.getLocation().getBlockX(),
                                    p.getLocation().getBlockY(),
                                    p.getLocation().getBlockZ());
                            for(int c = 0; c < 2; c++){
                                if(BukkitVersionManager.isMC19OrNewer()) {
                                    // for 1.9 and newer
                                    p.getLocation().getWorld().playSound(l, Sound.valueOf("ENTITY_ENDERMEN_TELEPORT"), 3.0F, 0.5F);
                                } else {
                                    p.getLocation().getWorld().playSound(l, Sound.ENDERMAN_TELEPORT, 3.0F, 0.5F);
                                }
                            }
                        }
                    }
                } catch(WarpNotFound | SQLException | WrongSaveDataType | PlayerIsNotOnline e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void tpWarpUnlocked(String warp, String player, CommandSender sender){
        Boolean m = false;
        for(Player x : Bukkit.getServer().getOnlinePlayers()) {
            if(x.getName().equals(player)){
                m = true;
            }
        }
        if(m) {
            teleport(warp, player, sender);
        } else {
            Strings.sendSender(Configuration.message.getString("player_require_online"), sender);
        }
    }

    public static void run(String warp, String player, CommandSender sender){
        try {
            if(isWarpAvailableAutoSaveData(warp)) {
                if(WarpStatus.getStatusAutoSaveDataType(warp).equals(WarpStatusTypes.LOCKED) && sender instanceof Player) {
                    tpWarpLocked(warp, player, sender);
                } else {
                    tpWarpUnlocked(warp, player, sender);
                }
            } else {
                Strings.sendSender(Configuration.message.getString("warp_not_found"), sender);
            }
        } catch(WarpNotFound | SQLException | WrongSaveDataType e) {
            e.printStackTrace();
        }
    }

    public static void main(String warp, String player, CommandSender sender, Boolean isTeleportPlayerMode){
        // chế độ /warp <name> <player>
        if(isTeleportPlayerMode){
            if(sender.hasPermission(PermissionsManager.getPermission("warps.tpwarp_player")[0].replace("<name>",warp)) ||
               sender.hasPermission(PermissionsManager.getPermission("warps.tpwarp_player")[1].replace("<name>",warp)) ||
               sender.hasPermission(PermissionsManager.getPermission("warps.tpwarp_player")[2].replace("<name>",warp)) ||
                    PermissionsManager.hasAll(sender)){
                run(warp, player, sender);
            } else {
                Strings.sendSender(Configuration.message.getString("require_permission"), sender);
            }
        }

        if(!isTeleportPlayerMode) {
            if(sender instanceof Player){
                if(sender.hasPermission(PermissionsManager.getPermission("warps.tpwarp")[0]) ||
                        sender.hasPermission(PermissionsManager.getPermission("warps.tpwarp")[1].replace("<name>",warp)) ||
                        sender.hasPermission(PermissionsManager.getPermission("warps.tpwarp")[2].replace("<name>",warp)) ||
                        PermissionsManager.hasAll(sender)) {
                    run(warp, player, sender);
                } else {
                    Strings.sendSender(Configuration.message.getString("require_permission"), sender);
                }
            } else {
                Strings.sendSender(Configuration.message.getString("sender_must_a_player"),sender);
            }
        }
    }
}
