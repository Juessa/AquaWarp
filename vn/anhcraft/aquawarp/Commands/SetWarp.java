package vn.anhcraft.aquawarp.Commands;

import java.sql.SQLException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.API.Functions;
import vn.anhcraft.aquawarp.API.MySQLFuncs;

public class SetWarp {
	public static void run(String name, CommandSender sender){
		name = Functions.reSpecial(name);
		Player s = (Player) sender;
		if(sender.hasPermission("aquawarp.setwarp") ||
		   sender.hasPermission("aquawarp.setwarp.*") ||
		   sender.hasPermission("aquawarp.setwarp."+name) ||
		   sender.hasPermission("aquawarp.*") ||
		   sender.isOp()){
			double locX = s.getLocation().getX();
			double locY = s.getLocation().getY();
			double locZ = s.getLocation().getZ();
			float yaw = s.getLocation().getYaw();
			String locWorld = s.getLocation().getWorld().getName();
			
			Boolean r;
			try {
				r = MySQLFuncs.exeq("SELECT * FROM "+ Options.plugin.mysql._Warps +" WHERE name='"+name+"';").next();
			    if(r){
			    	s.sendMessage(Functions.Config.gs("warpCreated", 
			    			Options.plugin.dir + Options.files.messages));
			    } else {
			    	MySQLFuncs.exeu("INSERT INTO "+ Options.plugin.mysql._Warps +" (`name`,`yaw`,`x`,`y`,`z`,`world`) VALUES ('"+name+"','"+yaw+"','"+locX+"','"+locY+"','"+locZ+"','"+locWorld+"');");
			    	s.sendMessage((Functions.Config.gs("warpCreateSuccess",
			    			Options.plugin.dir + Options.files.messages)).replaceAll("@warp", name));
			    }			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			s.sendMessage(Functions.Config.gs("senderNotHavePerm",
					Options.plugin.dir + Options.files.messages));
		}
	}

	public static void runAdvanced(String name, String x, String y, String z, String yaw,
		String w, CommandSender s) {
		name = Functions.reSpecial(name);
		if(s.hasPermission("aquawarp.setwarp+") ||
	       s.hasPermission("aquawarp.setwarp+.*") ||
		   s.hasPermission("aquawarp.setwarp+."+name) ||
		   s.hasPermission("aquawarp.*") ||
		   s.isOp()){
			x = Functions.reSpecial(x);
			y = Functions.reSpecial(y);
			z = Functions.reSpecial(z);
			yaw = Functions.reSpecial(yaw);
			w = Functions.reSpecial(w);
			
			Boolean r;
			try {
				r = MySQLFuncs.exeq("SELECT * FROM "+ Options.plugin.mysql._Warps +" WHERE name='"+name+"';").next();
			    if(r){
			    	s.sendMessage(Functions.Config.gs("warpCreated", 
			    			Options.plugin.dir + Options.files.messages));
			    } else {
			    	MySQLFuncs.exeu("INSERT INTO "+ Options.plugin.mysql._Warps +" (name,yaw,x,y,z,world) VALUES ('"+name+"','"+yaw+"','"+x+"','"+y+"','"+z+"','"+w+"');");
			    	s.sendMessage((Functions.Config.gs("warpCreateSuccess",
			    			Options.plugin.dir + Options.files.messages)).replaceAll("@warp", name));
			    }				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			s.sendMessage(Functions.Config.gs("doesNotHavePerm", Options.plugin.dir + Options.files.messages));
		}
	}
}

