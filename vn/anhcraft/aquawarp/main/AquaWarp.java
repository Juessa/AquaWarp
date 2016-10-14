package vn.anhcraft.aquawarp.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import net.milkbowl.vault.economy.Economy;
import vn.anhcraft.aquawarp.api.Functions;
import vn.anhcraft.aquawarp.api.URLContent;
import vn.anhcraft.aquawarp.event.SignWarp;
import vn.anhcraft.aquawarp.event.TpWarpPasswordInput;

/**
 * AquaWarp
 * @author Anh Craft
 * @version 1.2.2
 * 
 * Vault: Copyright (C) 2011 Morgan Humes morgan@lanaddict.com
 */

public class AquaWarp extends JavaPlugin {
	public static Economy economy = null;
	public static boolean EcoReady = false;
	
	 @Override
	 public void onEnable(){
		 Cache.delAll();
		 setupFiles();
	     setupScheduler();
	     setupEvents();
	     setupVault();
	     checkUpdate();
	     Bukkit.broadcastMessage(Options.message.enable_bc);
	     this.getCommand(Options.cmd.Warp).setExecutor(new Cmd(this));
	     this.getCommand(Options.cmd.Warps).setExecutor(new Cmd(this));
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

	private void checkUpdate() {
		BukkitScheduler s = getServer().getScheduler();
        s.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
            	Boolean a = false;
            	BufferedReader br = URLContent.get(
            		"http://"+Options.plugin.serverCheckUpdate+
            		"?version="+Options.plugin.version+
            		"&server="+Bukkit.getServer().getName()+
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
                    Bukkit.getConsoleSender().sendMessage(Options.message.newUpdateAvailable);
    			} else {
                	Bukkit.getConsoleSender().sendMessage(Options.message.versionNewest);
                }
            }
        }, Options.plugin.timeToMessageOnEnable+10L);
	}

	@Override
	 public void onDisable(){
		 Cache.delAll();
		 Bukkit.broadcastMessage(Options.message.disable_bc);
		 if(Options.plugin.disable_stop){
			Bukkit.getServer().shutdown();
		 }
	}
	
	private void setupFiles() {
		if(!Config.isExists(Options.files.file_config)){
			newConfigFile();
		}
		if(!Config.isExists(Options.files.file_perm)){
			newPermissionFile();
		}
		if(!Config.isExists(Options.files.file_message)){
			newMessageFile();
		}
	}

	private void newConfigFile() {
		String fn = Options.files.file_config;
		File pf = new File(Options.files.plugin_dir + fn);
        FileConfiguration f = YamlConfiguration.loadConfiguration(pf);
        if (!Config.isExists(fn)) {
            try {
                f.options().header("(c) Created by Anh Craft. Apache License.");
        		f.set("plugin.disable_serverStop", Default.plugin.disable_stop);
        		f.set("plugin.checkUpdate", Default.plugin.checkUpdate);
        		
        		f.set("mysql.prefix", Default.mysql._GOLBAL);
        		f.set("mysql.table.collate", Default.mysql._INFO.collate);
    			f.set("mysql.table.charset", Default.mysql._INFO.charset);
    			f.set("mysql.table.engine", Default.mysql._INFO.engine);
    			f.set("mysql.host", Default.mysql._CONNECT.host);
    			f.set("mysql.port", Default.mysql._CONNECT.port);
    			f.set("mysql.user", Default.mysql._CONNECT.user);
    			f.set("mysql.pass", Default.mysql._CONNECT.pass);
    			f.set("mysql.database", Default.mysql._CONNECT.dtbs);
    			
        		f.set("checkWarp.maxArea", Default.cmd.CheckWarpGroud);
        		f.set("checkWarp.maxAreaSafe", Default.cmd.CheckWarpMaxSafe);
        		f.set("lockWarp.unsafePassword", Default.cmd.UnSafePassword);
        		f.set("delWarp.unlockIfDeleteWarp", Default.cmd.UnlockIfDeleteWarp);
        		f.set("tpWarp.exeCmdBeforeWarping", Default.cmd.cmdListBeforeWarping);
        		f.set("tpWarp.exeCmdAfterWarping", Default.cmd.cmdListAfterWarping);
        		f.set("tpWarp.serviceCharge", Default.cmd.serviceCharge);
        		
    			f.set("other.effectEnable", Default.effect._ENABLE);
    			f.set("other.soundEnable", Default.sound._ENABLE);
    			
                f.save(pf);
                
            } catch (IOException exception) {
                exception.printStackTrace();
            }	
        }
	}
	
	private void newPermissionFile() {
		String fn = Options.files.file_perm;
		File pf = new File(Options.files.plugin_dir + fn);
        FileConfiguration f = YamlConfiguration.loadConfiguration(pf);
        if (!Config.isExists(fn)) {
            try {
                f.options().header("(c) Created by Anh Craft. Apache License.");
                
    			f.set("ALL", Default.perm._GLOBAL);
    			f.set("tpWarp", Default.perm.TpWarp);
    			f.set("helpWarp", Default.perm.HelpWarp);
    			f.set("setWarp", Default.perm.SetWarp);
    			f.set("delWarp", Default.perm.DelWarp);
    			f.set("checkWarp", Default.perm.CheckWarp);
    			f.set("editWarp", Default.perm.EditWarp);
    			f.set("listWarp", Default.perm.ListWarp);
    			f.set("lockWarp", Default.perm.LockWarp);
    			f.set("unLockWarp", Default.perm.UnLockWarp);
    			f.set("feeTp", Default.perm.FeeTp);
    			f.set("signWarpCreate", Default.perm.SignWarpCreate);
    			f.set("signWarpRemove", Default.perm.SignWarpRemove);
    			f.set("signWarpUse", Default.perm.SignWarpUse);
    			
                f.save(pf);
                
            } catch (IOException exception) {
                exception.printStackTrace();
            }	
        }
	}
	
	private void newMessageFile() {
		String fn = Options.files.file_message;
		File pf = new File(Options.files.plugin_dir + fn);
        FileConfiguration f = YamlConfiguration.loadConfiguration(pf);
        if (!Config.isExists(fn)) {
            try {
                f.options().header("(c) Created by Anh Craft. Apache License.");
                
        		f.set("senderNotPlayer", Default.message.invalidSender);
        		f.set("unknownCommand", Default.message.invalidCmd);
        		f.set("senderNotHavePerm", Default.message.doesNotHavePerm);
        		f.set("unknownWarp", Default.message.doNotHaveWarp);
        		f.set("unknownPlayer", Default.message.playerNull);
        		f.set("requireWarpName", Default.message.requireName);
        		f.set("unknownWorld", Default.message.worldNull);
        		f.set("warpNotCreated", Default.message.warpDidNotCreate);
        		f.set("warpCreated", Default.message.warpCreated);
        		f.set("tpSuccess", Default.message.tpWarpSuccess);
        		f.set("tpSuccessReName", Default.message.tpWarpSuccessReTpOther);
        		f.set("createdSuccess", Default.message.warpCreateSuccess);
        		f.set("deletedSuccess", Default.message.warpDeletedSuccess);
        		f.set("checkedSuccess", Default.message.checkWarpSuccess);
        		f.set("warpDanger", Default.message.warpIsDanger);
        		f.set("warpDangerBlock", Default.message.warpBlockDangerLength);
        		f.set("warpSafe", Default.message.warpIsSafe);
        		f.set("warpListMessage", Default.message.warpListMessage);
        		f.set("warpListEach", Default.message.warpListEach);
        		f.set("requirePass", Default.message.requirePass);
        		f.set("warpLocked", Default.message.warpLocked);
        		f.set("passwordUnSafe", Default.message.passwordUnSafe);
        		f.set("warpLockSuccess", Default.message.warpLockSuccess);
        		f.set("warpStatusLocked", Default.message.warpStatusLocked);
        		f.set("warpUnLocked", Default.message.warpUnLocked);
        		f.set("warpUnLockSuccess", Default.message.warpUnLockSuccess);
        		f.set("tpLockedWarpMessage", Default.message.tpLockedWarpMessage);
        		f.set("tpLockedWarpWrongPass", Default.message.tpLockedWarpWrongPass);
        		f.set("requireWarpLockedAmount", Default.message.requireWarpLockedAmount);
        		f.set("requireWarpUnLockedAmount", Default.message.requireWarpUnLockedAmount);
        		f.set("updateMoneySuccess", Default.message.updateMoneySuccess);
        		f.set("lackMoney", Default.message.lackMoney);
        		f.set("signWarpCreateSuccess", Default.message.signWarpCreateSuccess);
        		f.set("signWarpRemoveSuccess", Default.message.signWarpRemoveSuccess);
        		
                f.save(pf);
            } catch (IOException exception) {
                exception.printStackTrace();
            }	
        }
	}
}
