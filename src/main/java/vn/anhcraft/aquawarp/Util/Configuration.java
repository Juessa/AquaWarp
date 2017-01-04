package vn.anhcraft.aquawarp.Util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vn.anhcraft.aquawarp.Options;
import java.io.File;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class Configuration {
    public static FileConfiguration config;
    public static FileConfiguration message;
    public static FileConfiguration commands;
    public static FileConfiguration permissions;

    public static void loadConfigFile(){
        File d = new File(Options.configFileDir);
        config = YamlConfiguration.loadConfiguration(d);
    }

    public static void loadMessageFile(){
        File d = new File(Options.configMessageDir.replace("<lang>",config.getString("lang")));
        message = YamlConfiguration.loadConfiguration(d);
    }

    public static void loadCommandsFile(){
        File d = new File(Options.configCommandsDir);
        commands = YamlConfiguration.loadConfiguration(d);
    }

    public static void loadPermissionsFile(){
        File d = new File(Options.configPermissionsDir);
        permissions = YamlConfiguration.loadConfiguration(d);
    }

    public static void load(){
        loadConfigFile();
        loadMessageFile();
        loadCommandsFile();
        loadPermissionsFile();
    }
}
