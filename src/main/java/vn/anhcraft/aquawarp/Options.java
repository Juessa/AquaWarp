package vn.anhcraft.aquawarp;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class Options {
    public static String pluginName = "AquaWarp";
    public static String pluginVersion = "1.4.1";
    public static String author = "Anh Craft";
    public static String contributor = "";
    public static String pluginDir = "plugins/AquaWarp/";
    public static String serverCheckUpdate = "update.aquawarp.anhcraft.tk";

    public static String[] listGenerateFolders_018374 = {
            "messages",
            "warps",
            "energydata"
    }; // generated 018374
    public static String[] listLoadFile_294008 = {
            "/messages/message_en-uk.yml~messages/en-uk.yml",
            "/messages/message_vi.yml~messages/vi.yml",
            "/messages/message_fil.yml~messages/fil.yml",
            "/commands.yml~commands.yml",
            "/permissions.yml~permissions.yml",
            "/config.yml~config.yml"
    }; // generated 294008

    public static String[] listWelcomeMessages = {
            "&5",
            "&5",
            "&c[+]-[+]-[+]-[+]-[+]-[+]-[+]-[+]-[+]-[+]-[+]",
            "&5",
            "&a=-&r &5&l{plugin_name}&r &a-=&r",
            "&b",
            "&6Author: {plugin_author}",
            "&c",
            "&bv{plugin_version}&r",
            "&5",
            "&c[+]-[+]-[+]-[+]-[+]-[+]-[+]-[+]-[+]-[+]-[+]",
            "&5",
            "&5"
    };

    public static String[] listWarpAdminCommandMessages = {
            "&6/warpadmin: List all commands for admin",
            "&6/warpadmin import: Import warps from another plugin",
            "&6/warpadmin export: Export warps to another plugin",
            "&6/warpadmin reload: Reload all files and reconnect to database",
    };

    public static String configFileDir = Options.pluginDir + "config.yml";
    public static String configFileOldDir = Options.pluginDir + "config_old.yml";
    public static String configCommandsDir = Options.pluginDir + "commands.yml";
    public static String configCommandsOldDir = Options.pluginDir + "commands_old.yml";
    public static String configMessageDir = Options.pluginDir + "messages/<lang>.yml";
    public static String configPermissionsDir = Options.pluginDir + "permissions.yml";
    public static String configPermissionsOldDir = Options.pluginDir + "permissions_old.yml";

    public static String mysql_table_warp = "aquawarp_warps";
    public static String mysql_table_protection = "aquawarp_protection";
    public static String mysql_table_service_charges = "aquawarp_service_charges";
}
