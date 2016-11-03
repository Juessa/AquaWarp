package vn.anhcraft.aquawarp;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;
import vn.anhcraft.aquawarp.API.Files;
import vn.anhcraft.aquawarp.API.Functions;
import vn.anhcraft.aquawarp.API.MVdWPlaceholderAPI;
import vn.anhcraft.aquawarp.API.PlaceholderAPI;
import vn.anhcraft.aquawarp.Listeners.ClickWarpGUI;
import vn.anhcraft.aquawarp.Listeners.SetupEnergyOnLogin;
import vn.anhcraft.aquawarp.Listeners.SignWarp;
import vn.anhcraft.aquawarp.Listeners.TpWarpPasswordInput;
import vn.anhcraft.aquawarp.Scheduler.CheckUpdate;
import vn.anhcraft.aquawarp.Scheduler.FeatureRequireOffPlugin;
import vn.anhcraft.aquawarp.Scheduler.UpdateEnergy;
import vn.anhcraft.aquawarp.Scheduler.WelcomeMessages;

public class AquaWarp extends JavaPlugin {
	public static AquaWarp plugin;
	public static Economy economy = null;
	public static boolean EcoReady = false;
	public static boolean PlaceholderAPIReady = false;
	public static boolean PlaceholderMVdWReady = false;

	public static AquaWarp getPlugin(){
		return plugin;
    }
	
	 @Override
	 public void onEnable(){
		 plugin = this;
	     Bukkit.broadcastMessage(Options.message.enable_bc);
	     
	     saveDefaultConfig();
	     setupFiles();
	     setupScheduler();
	     setupEvents();
	     setupVault();
	     setupPlaceholder();
	     setupUpdate();
	     setupCommands();
	     setupFeatureRequire();
	}

	@Override
	public void onDisable(){
		Bukkit.broadcastMessage(Options.message.disable_bc);
	}
	
	private void setupVault() {
        if (getServer().getPluginManager().isPluginEnabled("Vault")) {
        	RegisteredServiceProvider<Economy> economyProvider = 
        		getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        	economy = economyProvider.getProvider();
        	EcoReady = true;
        }
	}
	
	private void setupPlaceholder() {
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") && Functions.Config.gb("supportPlaceHolderAPI",
				Options.plugin.dir + Options.files.config)) {
        	PlaceholderAPIReady = true;
        	new PlaceholderAPI(this).hook();
        }
        if (getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI") && Functions.Config.gb("supportMVdWPlaceholderAPI",
				Options.plugin.dir + Options.files.config)) {
        	PlaceholderMVdWReady = true;
        	new MVdWPlaceholderAPI(this);
        }
	}

	private void setupEvents() {
		getServer().getPluginManager().registerEvents(new TpWarpPasswordInput(), this);
		getServer().getPluginManager().registerEvents(new SignWarp(), this);
		getServer().getPluginManager().registerEvents(new ClickWarpGUI(), this);
	    getServer().getPluginManager().registerEvents(new SetupEnergyOnLogin(), this);
	}
	
	private void setupFeatureRequire() {
	     if(Functions.Config.gb("supportPlaceHolderAPI",
					Options.plugin.dir + Options.files.config) && !PlaceholderAPIReady){
	    	 featureRequireOffPlugin("PlaceholderAPI");
	     }
	     if(Functions.Config.gb("supportMVdWPlaceholderAPI",
					Options.plugin.dir + Options.files.config) && !PlaceholderMVdWReady){
	    	 featureRequireOffPlugin("MVdWPlaceholderAPI");
	     }
	     if(Functions.Config.gb("tpWarp.serviceCharge",
					Options.plugin.dir + Options.files.config) && !EcoReady){
	    	 featureRequireOffPlugin("VaultAPI");
	     }
	}
	
	private void setupCommands() {
	     this.getCommand("warp").setExecutor(new Cmd(this));
	     this.getCommand("warps").setExecutor(new Cmd(this));
	     this.getCommand("warpadmin").setExecutor(new Cmd(this));
	     this.getCommand("aquawarp").setExecutor(new Cmd(this));
	}
	
	private void setupScheduler() {
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
            	new WelcomeMessages();
            }
        }, 20L);
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	UpdateEnergy.forOnlinePlayers();
            	if(Functions.Config.gb("tpWarp.energy.energyRecoveryForOffline", Options.plugin.dir + Options.files.config)){
            		UpdateEnergy.forOfflinePlayers();
            	}
            }
        }, 0L, Functions.Config.gi("tpWarp.energy.energyRecoveryTime", Options.plugin.dir + Options.files.config));
	}

	private void setupUpdate() {
		if(Functions.Config.gb("checkUpdate", Options.plugin.dir + Options.files.config)){
			new CheckUpdate();
		}
	}
	
	private void featureRequireOffPlugin(String r){
        getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
            	new FeatureRequireOffPlugin(r);
            }
       }, 20L);
	}
	
	private void setupFiles() {
		// tạo or load file
        File a = new File(Options.plugin.dir + "messages/");
        if(!a.exists()){
        	a.mkdirs();
        }
        File b = new File(Options.plugin.dir + "energydata/");
        if(!b.exists()){
        	b.mkdirs();
        }
		Files.new_config(this);
		Files.new_messages(this);
		Files.new_aquawarphtml(this);
	}
}
