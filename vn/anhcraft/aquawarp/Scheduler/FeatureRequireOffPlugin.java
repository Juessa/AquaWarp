package vn.anhcraft.aquawarp.Scheduler;

import org.bukkit.Bukkit;
import vn.anhcraft.aquawarp.AquaWarp;
import vn.anhcraft.aquawarp.API.Functions;

public class FeatureRequireOffPlugin {
	public FeatureRequireOffPlugin(String r){
		Bukkit.getServer().broadcastMessage(Functions.reword("&5[%plugin_name%]&r &cA feature of this plugin is enabled and"
	 		+ " it requires installing &r&b"+r+"&r§c (and &r&b"+r+"&r§c has been enabled). "
	 		+ "Because it can't find &r&b"+r+"&r§c, therefore, this plugin will be disabled."));
		Bukkit.getServer().getPluginManager().disablePlugin(AquaWarp.plugin);
	}
}
