package vn.anhcraft.aquawarp.Events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public final class onPlayerTeleportWarp extends Event implements Cancellable {
    public static final HandlerList handlers = new HandlerList();
    public static boolean cancelled;
    public static String warp;
    public static Player player;
    public static CommandSender sender;

    public onPlayerTeleportWarp(String warp, Player player, CommandSender sender){
        this.warp = warp;
        this.player = player;
        this.sender = sender;
        this.cancelled = false;
    }

    public String getWarpName(){
        return this.warp;
    }

    public void setWarpName(String warp){
        this.warp = warp;
    }

    public Player getPlayer(){
        return this.player;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public CommandSender getSender(){
        return this.sender;
    }

    public void setSender(CommandSender sender){
        this.sender = sender;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
