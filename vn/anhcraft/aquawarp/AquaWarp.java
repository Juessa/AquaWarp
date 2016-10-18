package vn.anhcraft.aquawarp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import net.milkbowl.vault.economy.Economy;
import vn.anhcraft.aquawarp.API.Files;
import vn.anhcraft.aquawarp.API.Functions;
import vn.anhcraft.aquawarp.API.URLContent;
import vn.anhcraft.aquawarp.Listeners.ClickWarpGUI;
import vn.anhcraft.aquawarp.Listeners.SignWarp;
import vn.anhcraft.aquawarp.Listeners.TpWarpPasswordInput;

public class AquaWarp extends JavaPlugin {
	static AquaWarp plugin;
	public static Economy economy = null;
	public static boolean EcoReady = false;
	
	public static AquaWarp getPlugin(){
		return plugin;
    }
	
	 @Override
	 public void onEnable(){
		 plugin = this;
	     Bukkit.broadcastMessage(Options.message.enable_bc);
	     this.getCommand("warp").setExecutor(new Cmd(this));
	     this.getCommand("warps").setExecutor(new Cmd(this));
	     this.getCommand("warpadmin").setExecutor(new Cmd(this));
	     this.getCommand("aquawarp").setExecutor(new Cmd(this));
	     
	     setupFiles();
	     setupScheduler();
	     setupEvents();
	     setupVault();
	     setupUpdate();
	}
	 
	@Override
	public void onDisable(){
		Files.saveAll();
		Bukkit.broadcastMessage(Options.message.disable_bc);
	}

	private void setupVault() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        if(economy != null){
        	EcoReady = true;
        }
	}

	private void setupEvents() {
		getServer().getPluginManager().registerEvents(new TpWarpPasswordInput(), this);
		getServer().getPluginManager().registerEvents(new SignWarp(), this);
		getServer().getPluginManager().registerEvents(new ClickWarpGUI(), this);
	}

	private void setupScheduler() {
		BukkitScheduler s = getServer().getScheduler();
        s.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                for(String m : Options.plugin.messageOnEnable){
                	Bukkit.getConsoleSender().sendMessage(Functions.reword(m));
                }
            }
       }, Options.plugin.timeToMessageOnEnable);
	}

	private void setupUpdate() {
		if(Functions.Config.gb("checkUpdate", Options.plugin.dir + Options.files.config)){
			BukkitScheduler s = getServer().getScheduler();
	        s.scheduleSyncDelayedTask(this, new Runnable() {
	            @Override
	            public void run() {
	            	Boolean a = false;
	            	BufferedReader br = URLContent.get(
	            		"http://"+Options.plugin.serverCheckUpdate+
	            		"?version="+Options.plugin.version+
	            		"&server="+Bukkit.getServer().getServerName()+
	            		"&bukkit="+Bukkit.getServer().getBukkitVersion()
	            	);
	            	String inputLine;
	    			try {
						while ((inputLine = br.readLine()) != null) {
							if(inputLine == "update("+Options.plugin.version+")"){
								a = true;
								break;
							}
						}
						
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	    			if(a){
	    				Bukkit.getConsoleSender().sendMessage(
	                			Functions.reword("&5[%plugin_name%]&r &cThere is a newer version available!"));
	    			} else {
	                	Bukkit.getConsoleSender().sendMessage(
	                    		Functions.reword("&5[%plugin_name%]&r &aThis is the newest version!"));
	                }
	            }
	        }, Options.plugin.timeToMessageOnEnable+10L);
		}
	}
	
	private void setupFiles() {
		// tạo or load file
        File a = new File(Options.plugin.dir + "messages/");
        if(!a.exists()){
        	a.mkdirs();
        }
		Files.new_config(this);
		Files.new_messages(this);
		Files.new_aquawarphtml(this);
	}
}
