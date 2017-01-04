package vn.anhcraft.aquawarp.Util;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class PermissionsManager {
    public static Boolean hasAll(CommandSender s){
        Boolean r = false;
        if(s.isOp()){
            r = true;
        } else {
            for(String d : Configuration.permissions.getStringList("all_permission")){
                if(s.hasPermission(d)){
                    r = true;
                }
            }
        }
        return r;
    }

    public static String[] getPermission(String group){
        ArrayList<String> b = new ArrayList<>();
        for(String d : Configuration.permissions.getStringList(group)){
            b.add(d);
        }
        return b.toArray(new String[0]);
    }
}
