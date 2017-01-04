package vn.anhcraft.aquawarp.Converter;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.api.IWarps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import vn.anhcraft.aquawarp.API.CreateWarp;
import vn.anhcraft.aquawarp.API.DeleteWarp;
import vn.anhcraft.aquawarp.API.WarpLocation;
import vn.anhcraft.aquawarp.AquaWarpsMessages;
import vn.anhcraft.aquawarp.Exceptions.WarpNotFound;
import vn.anhcraft.aquawarp.GUI.WarpGUI;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.Util.ConnectDatabase;
import vn.anhcraft.iceapi.Converter.IceConverter;
import java.io.File;
import java.sql.ResultSet;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class EssentialsWarp {
    public static Essentials ess = null;

    public static Boolean hook(){
        Plugin plugin_es = Bukkit.getPluginManager().getPlugin("Essentials");
        if(plugin_es instanceof Essentials) {
            ess = (Essentials) plugin_es;
            return true;
        } else {
            return false;
        }
    }

    private static Boolean isEssentialsHasWarp(String warp){
        IWarps iw = ess.getWarps();
        for(String w : iw.getList()){
            if(w.equals(warp)){
                return true;
            }
        }
        return false;
    }

    private static Boolean isAquaWarpHasWarpWithFiles(String warp){
        for(String w : WarpGUI.getListWarpsWithFiles()){
            if(w.equals(warp)){
                return true;
            }
        }
        return false;
    }

    private static Boolean isAquaWarpHasWarpWithMySQL(String warp){
        for(String w : WarpGUI.getListWarpsWithMySQL()){
            if(w.equals(warp)){
                return true;
            }
        }
        return false;
    }

    public static Boolean exportWithFiles(Boolean keepOldWarp) throws Exception {
        IWarps iw = ess.getWarps();
        if(keepOldWarp.equals(Boolean.FALSE)) {
            for(String w : iw.getList()) {
                iw.removeWarp(w);
            }
        }
        for(String warp : WarpGUI.getListWarpsWithFiles()){
            if(!isEssentialsHasWarp(warp)){
                File a = new File(Options.pluginDir + "warps/" + warp + ".yml");
                if(a.exists()) {
                    FileConfiguration f = YamlConfiguration.loadConfiguration(a);
                    IceConverter ic = new IceConverter();
                    float x = ic.toFloat(f.getString("location.x"));
                    float y = ic.toFloat(f.getString("location.y"));
                    float z = ic.toFloat(f.getString("location.z"));
                    float yaw = ic.toFloat(f.getString("location.yaw"));
                    float pitch = ic.toFloat(f.getString("location.pitch"));
                    String w = f.getString("location.world");
                    World world = Bukkit.getServer().getWorld(w);
                    Location l = new Location(world, x, y, z);
                    l.setYaw(yaw);
                    l.setPitch(pitch);
                    iw.setWarp(warp, l);
                }
            }
        }
        return true;
    }

    public static Boolean importWithFiles(Boolean keepOldWarp) throws Exception {
        if(keepOldWarp.equals(Boolean.FALSE)){
            for(String warp : WarpGUI.getListWarpsWithFiles()){
                DeleteWarp.DeleteWarpWithFile(warp);
            }
        }
        for(String warp : ess.getWarps().getList()){
            Location loc = ess.getWarps().getWarp(warp);
            if(!isAquaWarpHasWarpWithFiles(warp)){
                CreateWarp.CreateWarpWithFile(warp, new WarpLocation(loc));
            }
        }
        return true;
    }

    public static Boolean exportWithMySQL(Boolean keepOldWarp) throws Exception {
        IWarps iw = ess.getWarps();
            if(keepOldWarp.equals(Boolean.FALSE)){
                for(String w : iw.getList()){
                    iw.removeWarp(w);
                }
            }
            for(String warp : WarpGUI.getListWarpsWithMySQL()){
                if(!isEssentialsHasWarp(warp)){
                    ResultSet r = ConnectDatabase.exeq("SELECT * FROM " + Options.mysql_table_warp + "" +
                            " WHERE name='" + warp + "';");
                    Boolean i = r.next();
                    if(i) {
                        float x = r.getFloat("x");
                        float y = r.getFloat("y");
                        float z = r.getFloat("z");
                        float yaw = r.getFloat("yaw");
                        float pitch = r.getFloat("pitch");
                        String w = r.getString("world");
                        World world = Bukkit.getServer().getWorld(w);
                        Location l = new Location(world, x, y, z);
                        l.setYaw(yaw);
                        l.setPitch(pitch);
                        r.close();
                        iw.setWarp(warp, l);
                    } else {
                        throw new WarpNotFound(AquaWarpsMessages.get("System.Exception.300"));
                    }
            }
        }
        return true;
    }

    public static Boolean importWithMySQL(Boolean keepOldWarp) throws Exception {
        if(keepOldWarp.equals(Boolean.FALSE)){
            for(String warp : WarpGUI.getListWarpsWithMySQL()){
                DeleteWarp.DeleteWarpWithMySQL(warp);
            }
        }
        for(String warp : ess.getWarps().getList()){
            Location loc = ess.getWarps().getWarp(warp);
            if(!isAquaWarpHasWarpWithMySQL(warp)){
                CreateWarp.CreateWarpWithMySQL(warp, new WarpLocation(loc));
            }
        }
        return true;
    }
}
