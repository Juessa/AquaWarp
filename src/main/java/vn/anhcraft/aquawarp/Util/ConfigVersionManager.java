package vn.anhcraft.aquawarp.Util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vn.anhcraft.aquawarp.AquaWarp;
import vn.anhcraft.aquawarp.AquaWarpsMessages;
import vn.anhcraft.aquawarp.Options;

import java.io.*;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class ConfigVersionManager {
    public static void copy(String a, String b){
        InputStream inStream = null;
        OutputStream outStream = null;

        try {
            File afile = new File(a);
            File bfile = new File(b);
            if(bfile.exists()){
                bfile.delete();
            }
            inStream = new FileInputStream(afile);
            outStream = new FileOutputStream(bfile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            inStream.close();
            outStream.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void send(String str){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AquaWarp.plugin, new Runnable() {
            @Override
            public void run() {
                Strings.sendGlobal(AquaWarpsMessages.get(str));
            }
        }, 25);
    }

    public static void checkConfig() throws IOException {
        File f = new File(Options.configFileDir);
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        int v = c.getInt("fileVersion");
        // old version
        // 1 is current version
        if(v < 2){
            copy(Options.configFileDir, Options.configFileOldDir);
            send("System.UpdateFile.Config");
            if(v < 2){
                c.set("tpWarp.useGUI",false);
                c.set("fileVersion",2);
                c.save(f);
            }
        }
    }

    public static void checkCommands() throws IOException {
        File f = new File(Options.configCommandsDir);
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        int v = c.getInt("fileVersion");
        // old version
        // 2 is current version
        if(v < 2){
            copy(Options.configCommandsDir, Options.configCommandsOldDir);
            send("System.UpdateFile.Commands");
            // start convert, first v2
            if(v < 2){
                c.set("warpadmin.export","export");
                c.set("warpadmin.import","import");
                c.set("fileVersion",2);
                c.save(f);
            }
        }
    }

    public static void checkPermission() throws IOException {
        File f = new File(Options.configPermissionsDir);
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        int v = c.getInt("fileVersion");
        // old version
        // 2 is current version
        if(v < 2){
            copy(Options.configPermissionsDir, Options.configPermissionsOldDir);
            send("System.UpdateFile.Permissions");
            // start convert, first v2
            if(v < 2){
                String[] a2 = {"aquawarp.warpadmin.export"};
                String[] b2 = {"aquawarp.warpadmin.import"};
                String[] c2 = {"aquawarp.fee.*","aquawarp.fee.<name>","aquawarp.fee"};
                c.set("fileVersion",2);
                c.set("warpadmin.export",a2);
                c.set("warpadmin.import",b2);
                c.set("warps.fee",c2);
                c.save(f);
            }
        }
    }

    public static void check(){
        try {
            checkConfig();
            checkCommands();
            checkPermission();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
