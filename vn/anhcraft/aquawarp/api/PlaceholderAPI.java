package vn.anhcraft.aquawarp.API;

import org.bukkit.entity.Player;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import vn.anhcraft.aquawarp.AquaWarp;
import vn.anhcraft.aquawarp.Options;

public class PlaceholderAPI extends EZPlaceholderHook {
	 
	 public PlaceholderAPI(AquaWarp plugin) {
		super(plugin, "aquawarp");
	}

	@Override
	public String onPlaceholderRequest(Player p, String identifier) {
		String uuid = p.getUniqueId().toString();
        if (identifier.equals("player_current_energy") && p != null){
        	return Integer.toString(Functions.Energy.get(uuid));
        }
        if (identifier.equals("maximum_energy")) {
            return Integer.toString(Functions.Config.gi("tpWarp.energy.maxEnergy",
					   Options.plugin.dir + Options.files.config));
        }
         
        return null;
	}
}
