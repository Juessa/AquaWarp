package vn.anhcraft.aquawarp.Scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import vn.anhcraft.aquawarp.AquaWarp;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.API.Functions;
import vn.anhcraft.aquawarp.API.URLContent;

public class CheckUpdate {
	public CheckUpdate(){
		BukkitScheduler s = Bukkit.getServer().getScheduler();
        s.scheduleSyncDelayedTask(AquaWarp.plugin, new Runnable() {
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
        }, 30L);
	}
}
