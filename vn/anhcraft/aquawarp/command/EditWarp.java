package vn.anhcraft.aquawarp.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vn.anhcraft.aquawarp.main.Options;

public class EditWarp {
	public static void run(String warp, CommandSender sender) {
		if(sender instanceof Player){
			Player s = (Player) sender;
			Bukkit.getServer().dispatchCommand(s, "warps del " + warp);
			Bukkit.getServer().dispatchCommand(s, "warps set " + warp);
			s.sendMessage(Options.message.editWarpSuccess.replaceAll("@warp", warp));
		} else {
			sender.sendMessage(Options.message.invalidSender);
		}
	}
}
