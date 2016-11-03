package vn.anhcraft.aquawarp.Scheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.API.Functions;

public class UpdateEnergy {
	public static ArrayList<String> playerUUIDOnlineChecked = new ArrayList<String>();
	public static ArrayList<String> playerUUIDOfflineChecked = new ArrayList<String>();
	public static int energy = Functions.Config.gi("tpWarp.energy.energyRecoveryAmount", Options.plugin.dir + Options.files.config);
	public static int max = Functions.Config.gi("tpWarp.energy.maxEnergy", Options.plugin.dir + Options.files.config);
    
	public static void forOnlinePlayers(){
		playerUUIDOnlineChecked.clear();
        Collection<? extends Player> pa = Bukkit.getServer().getOnlinePlayers();
        for(Player p : pa){
        	String uuida = p.getUniqueId().toString();
        	File a = new File(Options.plugin.dir + "energydata/"+uuida+".yml");
            if(a.exists()){
            	int e = Functions.Energy.get(p.getUniqueId().toString());
            	// năng lượng bé hơn tối đa
            	if(e < max){
            		int q = max-e;
            		// nếu số lượng năng lượng cần phục hồi bé hơn số năng lượng phục hồi tối đa
            		if(q < energy){
            			try {
							Functions.Energy.set(uuida, max);
						} catch (IOException g) {
							g.printStackTrace();
						}
            		} else {
            			try {
							Functions.Energy.set(uuida, e+energy);
						} catch (IOException g) {
							g.printStackTrace();
						}
            		}
            	}
            	playerUUIDOnlineChecked.add(uuida);
        	}
        }
	}
	
	public static Boolean isPlayerOnline(String uuid){
		Boolean e = false;
		for(int v = 0; v < playerUUIDOnlineChecked.size(); v++){
			String px = playerUUIDOnlineChecked.get(v);
			if(px.equals(uuid)){
				e = true;
				break;
			}
		}
		return e;
	}
	
	public static void forOfflinePlayers(){
		playerUUIDOfflineChecked.clear();
        OfflinePlayer[] pb = Bukkit.getServer().getOfflinePlayers();
        for(OfflinePlayer pg : pb){
        	String uuidb = pg.getUniqueId().toString();
        	File b = new File(Options.plugin.dir + "energydata/"+uuidb+".yml");
            if(b.exists() && !isPlayerOnline(uuidb)){
            	int ee = Functions.Energy.get(pg.getUniqueId().toString());
            	// năng lượng bé hơn tối đa
            	if(ee < max){
            		int qa = max-ee;
            		// nếu số lượng năng lượng cần phục hồi bé hơn số năng lượng phục hồi tối đa
            		if(qa < energy){
            			try {
							Functions.Energy.set(uuidb, max);
						} catch (IOException g) {
							g.printStackTrace();
						}
            		} else {
            			try {
							Functions.Energy.set(uuidb, ee+energy);
						} catch (IOException g) {
							g.printStackTrace();
						}
            		}
            	}
            	playerUUIDOfflineChecked.add(uuidb);
        	}
        }
	}
}
