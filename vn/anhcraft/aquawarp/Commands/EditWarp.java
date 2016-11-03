package vn.anhcraft.aquawarp.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.API.Functions;

public class EditWarp {
	public static void run(String warp, CommandSender s) {
		warp = Functions.reword(warp);
		if(s.hasPermission("aquawarp.editwarp") ||
	       s.hasPermission("aquawarp.editwarp.*") ||
		   s.hasPermission("aquawarp.editwarp."+warp) ||
		   s.hasPermission("aquawarp.*") ||
		   s.isOp()){
			Player p = (Player) s;
			Bukkit.getServer().dispatchCommand(p, "warps del " + warp);
			Bukkit.getServer().dispatchCommand(p, "warps set " + warp);
		} else {
			s.sendMessage(Functions.Config.gs("doesNotHavePerm",
					Options.plugin.dir + Options.files.messages));
		}
	}
}
