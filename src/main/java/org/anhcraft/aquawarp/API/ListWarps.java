package org.anhcraft.aquawarp.API;

import org.anhcraft.aquawarp.Options;
import org.anhcraft.aquawarp.Util.Configuration;
import org.anhcraft.aquawarp.Util.ConnectDatabase;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class ListWarps {
    public static List<String> getListWarpsWithMySQL(){
        List<String> output = new ArrayList<>();
        try {
            ResultSet r = ConnectDatabase.exeq("SELECT * FROM " + Options.mysql_table_warp + ";");
            while(r.next()) {
                output.add(r.getString("name"));
            }
            r.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static List<String> getListWarpsWithFiles(){
        List<String> output = new ArrayList<>();
        File[] files = new File(Options.pluginDir + "warps").listFiles(
                new FilenameFilter() {
                    @Override public boolean accept(File dir, String name) {
                        return name.endsWith(".yml");
                    }
                }
        );
        for (File file : files) {
            FileConfiguration f = YamlConfiguration.loadConfiguration(file);
            output.add(f.getString("name"));
        }
        return output;
    }

    public static List<String> getListWarpsAutoSaveDataType(){
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            return getListWarpsWithFiles();
        } else {
            return getListWarpsWithMySQL();
        }
    }
}
