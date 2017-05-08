package org.anhcraft.aquawarp.API;

import org.bukkit.Location;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public final class WarpLocation {
    private float x;
    private float y;
    private float z;
    private float yaw;
    private float pitch;
    private String world;

    public WarpLocation(float x, float y, float z, float yaw, float pitch, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

    public WarpLocation(Location location) {
        this.x = (float) location.getX();
        this.y = (float) location.getY();
        this.z = (float) location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        this.world = location.getWorld().getName();
    }

    public float getX(){
        return this.x;
    }
    public float getY(){
        return this.y;
    }
    public float getZ(){
        return this.z;
    }
    public float getPitch(){
        return this.pitch;
    }
    public float getYaw(){
        return this.yaw;
    }
    public String getWorld(){
        return this.world;
    }
    public void setX(float x){
        this.x = x;
    }
    public void setY(float y){
        this.y = y;
    }
    public void setZ(float z){
        this.z = z;
    }
    public void setYaw(float yaw){
        this.yaw = yaw;
    }
    public void setPitch(float pitch){
        this.pitch = pitch;
    }
}
