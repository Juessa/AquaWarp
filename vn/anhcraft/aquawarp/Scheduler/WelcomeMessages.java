package vn.anhcraft.aquawarp.Scheduler;

import org.bukkit.Bukkit;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.API.Functions;

public class WelcomeMessages {
	public WelcomeMessages() {
		for(String m : Options.plugin.messageOnEnable){
	    	Bukkit.getConsoleSender().sendMessage(Functions.reword(m));
	    }
	}
}
