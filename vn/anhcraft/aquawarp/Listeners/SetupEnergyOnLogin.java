package vn.anhcraft.aquawarp.Listeners;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.API.Functions;

public class SetupEnergyOnLogin implements Listener {
	@EventHandler
	private void onPlayerLogin(PlayerLoginEvent e){
		String a = e.getPlayer().getUniqueId().toString();
		File c = new File(Options.plugin.dir + "energydata/"+a+".yml");
        if(!c.exists()){
        	try {
				c.createNewFile();
				Bukkit.getServer().getConsoleSender().sendMessage(Options.message.createPlayerFile+a);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
        if(Functions.Config.gb("tpWarp.energy.reset", 
				  Options.plugin.dir + Options.files.config) || !c.exists()){
	        // mỗi khi ng` chơi login thì reset về ban đầu
	        try {
				 FileWriter d = new FileWriter(Options.plugin.dir + "energydata/"+a+".yml");
				 d.write("name: "+e.getPlayer().getName()+"\nenergy: "+Functions.Config.gi("tpWarp.energy.maxEnergy", 
						  Options.plugin.dir + Options.files.config));
				 d.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}