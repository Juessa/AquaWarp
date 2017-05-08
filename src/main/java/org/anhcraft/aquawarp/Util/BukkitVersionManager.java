package org.anhcraft.aquawarp.Util;

import org.bukkit.Bukkit;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class BukkitVersionManager {
    public static boolean isMC111(){
        return Bukkit.getBukkitVersion().contains("1.11");
    }

    public static boolean isMC110(){
        return Bukkit.getBukkitVersion().contains("1.10");
    }

    public static boolean isMC19(){
        return Bukkit.getBukkitVersion().contains("1.9");
    }

    public static boolean isMC18(){
        return Bukkit.getBukkitVersion().contains("1.8");
    }

    public static boolean isMC111OrNewer(){
        if (isMC111()){
            return true;
        } else if (isMC110()||isMC19()||isMC18()){
            return false;
        } else {
            return true;
        }
    }

    public static boolean isMC110OrNewer(){
        if (isMC110()){
            return true;
        } else if (isMC19()||isMC18()){
            return false;
        } else {
            return true;
        }
    }

    public static boolean isMC19OrNewer(){
        if (isMC19()){
            return true;
        } else if (isMC18()){
            return false;
        } else {
            return true;
        }
    }
}
