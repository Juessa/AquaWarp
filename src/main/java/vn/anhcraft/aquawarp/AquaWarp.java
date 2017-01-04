package vn.anhcraft.aquawarp;

import org.bukkit.plugin.java.JavaPlugin;
import vn.anhcraft.aquawarp.API.SoftDepends.MVdWPlaceholderAPI;
import vn.anhcraft.aquawarp.API.SoftDepends.PlaceHolderAPI;
import vn.anhcraft.aquawarp.API.SoftDepends.Vault;
import vn.anhcraft.aquawarp.Converter.ExportSetup;
import vn.anhcraft.aquawarp.Converter.ImportSetup;
import vn.anhcraft.aquawarp.GUI.WarpGUI;
import vn.anhcraft.aquawarp.Listeners.EnergyOnJoin;
import vn.anhcraft.aquawarp.Listeners.TpWarpLocked;
import vn.anhcraft.aquawarp.Listeners.WarpSign;
import vn.anhcraft.aquawarp.Scheduler.CheckUpdate;
import vn.anhcraft.aquawarp.Scheduler.RecoveryEnergy;
import vn.anhcraft.aquawarp.Scheduler.WelcomeMessages;
import vn.anhcraft.aquawarp.Util.*;

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

        Strings.sendGlobal(AquaWarpsMessages.get("System.BoardcastOnEnable"));
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
        Strings.sendGlobal(AquaWarpsMessages.get("System.BoardcastOnDisable"));
    }
}
