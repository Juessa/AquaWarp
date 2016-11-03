package vn.anhcraft.aquawarp.API;

import vn.anhcraft.aquawarp.AquaWarp;
import vn.anhcraft.aquawarp.Options;
import org.bukkit.entity.Player;
import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;

public class MVdWPlaceholderAPI {

	public MVdWPlaceholderAPI(AquaWarp plugin) {
		PlaceholderAPI.registerPlaceholder(plugin, "{aquawarp_player_current_energy}", 
			new PlaceholderReplacer(){
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
				Player p = e.getPlayer();
				if(p != null){
					return Integer.toString(Functions.Energy.get(p.getUniqueId().toString()));
				} else {
					return null;
				}
			}
		});
		
		PlaceholderAPI.registerPlaceholder(plugin, "{aquawarp_maximum_energy}", 
				new PlaceholderReplacer(){
				@Override
				public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
					Player p = e.getPlayer();
					if(p != null){
						return Integer.toString(Functions.Config.gi("tpWarp.energy.maxEnergy",
								   Options.plugin.dir + Options.files.config));
					} else {
						return null;
					}
				}
		});
	}

	public static String replace(Player p, String a) {
		return PlaceholderAPI.replacePlaceholders(p, a);
	}
}
