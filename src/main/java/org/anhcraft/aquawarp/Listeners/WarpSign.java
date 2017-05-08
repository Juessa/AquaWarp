package org.anhcraft.aquawarp.Listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.anhcraft.aquawarp.Commands.TpWarp;
import org.anhcraft.aquawarp.Util.Configuration;
import org.anhcraft.aquawarp.Util.PermissionsManager;
import org.anhcraft.aquawarp.Util.Strings;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class WarpSign implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChangeSign(SignChangeEvent ev){
        Player p = ev.getPlayer();
        if(PermissionsManager.hasAll(p) ||
                p.hasPermission(PermissionsManager.getPermission("warps.warpsign_create")[0]) ||
                p.hasPermission(PermissionsManager.getPermission("warps.warpsign_create")[1])) {
            if(ev.getLines()[0].equals(Configuration.config.getString("warpsign.code"))) {
                ev.setLine(0, Strings.reword(Configuration.config.getString("warpsign.title")));
                ev.setLine(1, Strings.reword(ev.getLines()[1]));
                ev.setLine(2, Strings.reword(ev.getLines()[2]));
                ev.setLine(3, Strings.reword(ev.getLines()[3]));
            }
        } else {
            Strings.sendPlayer(Configuration.message.getString("require_permission"), p);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void UseWarpSign(BlockDamageEvent ev) {
        Block block = ev.getBlock();
        Player p = ev.getPlayer();
        if(block.getType().equals(Material.WALL_SIGN) || block.getType().equals(Material.SIGN_POST)) {
            Sign sign = (Sign) block.getState();
            if(sign.getLines()[0].equals(Strings.reword(Configuration.config.getString("warpsign.title")))) {
                if((PermissionsManager.hasAll(p) ||
                        p.hasPermission(PermissionsManager.getPermission("warps.warpsign_use")[0]
                                .replace("<name>", sign.getLines()[1])) ||
                        p.hasPermission(PermissionsManager.getPermission("warps.warpsign_use")[1]
                                .replace("<name>", sign.getLines()[1])) ||
                        p.hasPermission(PermissionsManager.getPermission("warps.warpsign_use")[2]
                                .replace("<name>", sign.getLines()[1])))) {
                    TpWarp.run(sign.getLines()[1],p.getName(),p);
                    ev.setCancelled(true);
                } else {
                    Strings.sendPlayer(Configuration.message.getString("require_permission"), p);
                    ev.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void WarpSign(BlockBreakEvent ev) {
        Block block = ev.getBlock();
        Player p = ev.getPlayer();
        if(block.getType().equals(Material.WALL_SIGN) || block.getType().equals(Material.SIGN_POST)) {
            Sign sign = (Sign) block.getState();
            if(sign.getLines()[0].equals(Strings.reword(Configuration.config.getString("warpsign.title")))) {
                if(p.getGameMode().equals(GameMode.CREATIVE) && p.isSneaking()) {
                    if((PermissionsManager.hasAll(p) ||
                            p.hasPermission(PermissionsManager.getPermission("warps.warpsign_remove")[0]
                                    .replace("<name>", sign.getLines()[1])) ||
                            p.hasPermission(PermissionsManager.getPermission("warps.warpsign_remove")[1]
                                    .replace("<name>", sign.getLines()[1])) ||
                            p.hasPermission(PermissionsManager.getPermission("warps.warpsign_remove")[2]
                                    .replace("<name>", sign.getLines()[1])))) {
                        return;
                    } else {
                        Strings.sendPlayer(Configuration.message.getString("require_permission"), p);
                        ev.setCancelled(true);
                    }
                } else {
                    if((PermissionsManager.hasAll(p) ||
                            p.hasPermission(PermissionsManager.getPermission("warps.warpsign_use")[0]
                                    .replace("<name>", sign.getLines()[1])) ||
                            p.hasPermission(PermissionsManager.getPermission("warps.warpsign_use")[1]
                                    .replace("<name>", sign.getLines()[1])) ||
                            p.hasPermission(PermissionsManager.getPermission("warps.warpsign_use")[2]
                                    .replace("<name>", sign.getLines()[1])))) {
                            TpWarp.run(sign.getLines()[1], p.getName(), p);
                            ev.setCancelled(true);
                    } else {
                        Strings.sendPlayer(Configuration.message.getString("require_permission"), p);
                        ev.setCancelled(true);
                    }
                }
            }
        }
    }
}
