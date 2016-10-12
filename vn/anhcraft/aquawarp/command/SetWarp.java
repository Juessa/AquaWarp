package vn.anhcraft.aquawarp.command;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.anhcraft.aquawarp.api.Functions;
import vn.anhcraft.aquawarp.api.MySQLFuncs;
import vn.anhcraft.aquawarp.main.Options;

public class SetWarp {
	public static void run(String name, CommandSender sender){
		name = Functions.reSpecial(name);
		Player s = (Player) sender;
		double locX = s.getLocation().getX();
		double locY = s.getLocation().getY();
		double locZ = s.getLocation().getZ();
		float yaw = s.getLocation().getYaw();
		String locWorld = s.getLocation().getWorld().getName();
		
		Boolean r;
		try {
			r = MySQLFuncs.exeTable("SELECT * FROM "+ Options.mysql.Warps +" WHERE name='"+name+"';").next();
		    if(r){
		    	s.sendMessage(Options.message.warpCreated);
		    } else {
		    	MySQLFuncs.execUpdate("INSERT INTO "+ Options.mysql.Warps +" (`name`,`yaw`,`x`,`y`,`z`,`world`) VALUES ('"+name+"','"+yaw+"','"+locX+"','"+locY+"','"+locZ+"','"+locWorld+"');");
		    	s.sendMessage(Options.message.warpCreateSuccess.replaceAll("@warp", name));
		    }			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void runAdvanced(String name, String x, String y, String z, String yaw,
			String w, CommandSender s) {
		name = Functions.reSpecial(name);
		x = Functions.reSpecial(x);
		y = Functions.reSpecial(y);
		z = Functions.reSpecial(z);
		yaw = Functions.reSpecial(yaw);
		w = Functions.reSpecial(w);
		World h = Bukkit.getServer().getWorld(w);
		
		if(h != null){
			Boolean r;
			try {
				r = MySQLFuncs.exeTable("SELECT * FROM "+ Options.mysql.Warps +" WHERE name='"+name+"';").next();
			    if(r){
			    	s.sendMessage(Options.message.warpCreated);
			    } else {
			    	MySQLFuncs.execUpdate("INSERT INTO "+ Options.mysql.Warps +" (name,yaw,x,y,z,world) VALUES ('"+name+"','"+yaw+"','"+x+"','"+y+"','"+z+"','"+w+"');");
			    	s.sendMessage(Options.message.warpCreateSuccess.replaceAll("@warp", name));
			    }				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			s.sendMessage(Options.message.worldNull);
		}
	}
}
