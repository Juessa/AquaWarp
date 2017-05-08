package org.anhcraft.aquawarp;

import org.bukkit.plugin.java.JavaPlugin;
import org.anhcraft.aquawarp.API.SoftDepends.MVdWPlaceholderAPI;
import org.anhcraft.aquawarp.API.SoftDepends.PlaceHolderAPI;
import org.anhcraft.aquawarp.API.SoftDepends.Vault;
import org.anhcraft.aquawarp.Converter.ExportSetup;
import org.anhcraft.aquawarp.Converter.ImportSetup;
import org.anhcraft.aquawarp.GUI.WarpGUI;
import org.anhcraft.aquawarp.Listeners.EnergyOnJoin;
import org.anhcraft.aquawarp.Listeners.TpWarpLocked;
import org.anhcraft.aquawarp.Listeners.WarpSign;
import org.anhcraft.aquawarp.Scheduler.CheckUpdate;
import org.anhcraft.aquawarp.Scheduler.RecoveryEnergy;
import org.anhcraft.aquawarp.Scheduler.WelcomeMessages;
import org.anhcraft.aquawarp.Util.*;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public final class AquaWarp extends JavaPlugin {
    public static AquaWarp plugin;
    private AquaWarpsMessages AquaWarpsMessages = new AquaWarpsMessages();

    @Override
    public void onEnable(){
        plugin = this;
        AquaWarpsMessages.setup();
        FileManager.setup();
        saveDefaultConfig();
        // check file update
        ConfigVersionManager.check();
        Configuration.load();
        setup_SchedulersOneTime();
        setup_SchedulersEveryTime();
        // listeners
        setup_Listeners();
        // plugin depends
        setup_PluginDepends();
        // commands
        setup_Commands();
        // setup MYSQL
        setup_MySQL();

        Strings.sendGlobal(org.anhcraft.aquawarp.AquaWarpsMessages.get("System.BoardcastOnEnable"));
    }

    private void setup_MySQL() {
        ConnectDatabase.setup();
    }

    private void setup_Commands() {
        this.getCommand("warp").setExecutor(new OnCommands());
        this.getCommand("warps").setExecutor(new OnCommands());
        this.getCommand("warpadmin").setExecutor(new OnCommands());
        this.getCommand("aquawarp").setExecutor(new OnCommands());
    }

    private void setup_PluginDepends() {
        Vault.setup(true);
        MVdWPlaceholderAPI.setup(true); // + register placeholders
        PlaceHolderAPI.setup(true); // + register placeholders
    }

    private void setup_Listeners() {
        getServer().getPluginManager().registerEvents(new EnergyOnJoin(), this);
        getServer().getPluginManager().registerEvents(new TpWarpLocked(), this);
        getServer().getPluginManager().registerEvents(new WarpSign(), this);
        getServer().getPluginManager().registerEvents(new WarpGUI(), this);
        getServer().getPluginManager().registerEvents(new ImportSetup(), this);
        getServer().getPluginManager().registerEvents(new ExportSetup(), this);
    }

    private void setup_SchedulersOneTime() {
        new WelcomeMessages().start();
        new CheckUpdate();
    }

    public static void setup_SchedulersEveryTime() {
        new RecoveryEnergy();
    }

    @Override
    public void onDisable(){
        Strings.sendGlobal(org.anhcraft.aquawarp.AquaWarpsMessages.get("System.BoardcastOnDisable"));
    }
}
