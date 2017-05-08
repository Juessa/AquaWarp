package org.anhcraft.aquawarp.Events;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class onChangeCost extends Event implements Cancellable {
    public static final HandlerList handlers = new HandlerList();
    public static boolean cancelled;
    public static String warp;
    public static CommandSender sender;
    public static double costLock;
    public static double costUnlock;

    public onChangeCost(String warp, CommandSender sender, double costLock, double costUnlock){
        this.warp = warp;
        this.sender = sender;
        this.cancelled = false;
        this.costLock = costLock;
        this.costUnlock = costUnlock;
    }

    public String getWarpName(){
        return this.warp;
    }

    public void setWarpName(String warp){
        this.warp = warp;
    }

    public CommandSender getSender(){
        return this.sender;
    }

    public void setSender(CommandSender sender){
        this.sender = sender;
    }

    public double getCostLock(){
        return this.costLock;
    }

    public void setCostLock(double costLock){
        this.costLock = costLock;
    }

    public double getCostUnlock(){
        return this.costUnlock;
    }

    public void setCostUnlock(double costUnlock){
        this.costUnlock = costUnlock;
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