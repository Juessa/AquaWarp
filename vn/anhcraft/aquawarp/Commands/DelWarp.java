package vn.anhcraft.aquawarp.Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.command.CommandSender;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.API.Functions;
import vn.anhcraft.aquawarp.API.MySQLFuncs;

public class DelWarp {
	public static void run(String name, CommandSender s) {
		name = Functions.reSpecial(name);
		if(s.hasPermission("aquawarp.delwarp") ||
		   s.hasPermission("aquawarp.delwarp.*") ||
		   s.hasPermission("aquawarp.delwarp."+name) ||
		   s.hasPermission("aquawarp.*") ||
		   s.isOp()){
			Boolean r;
			try {
				ResultSet rq = MySQLFuncs.exeq("SELECT * FROM "+ Options.plugin.mysql._Warps +" WHERE name='"+name+"';");
				r = rq.next();
				if(r){
			    	if(LockWarp.islocked(name)){
			    		if(Functions.Config.gb("delWarp.unlockIfDeleteWarp", 
			    				Options.plugin.dir + Options.files.config)){
			    			UnLockWarp.run(s, name);
			    		}
			    	}
			    	MySQLFuncs.exeu("DELETE FROM "+ Options.plugin.mysql._Warps +" WHERE name='"+name+"'; ");
			    	s.sendMessage((Functions.Config.gs("warpDeletedSuccess",
			    			Options.plugin.dir + Options.files.messages)).replace("@warp", name));
			    } else {
			    	s.sendMessage(Functions.Config.gs("warpDidNotCreate",
			    			Options.plugin.dir + Options.files.messages));
			    }
				rq.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		} else {
			s.sendMessage(Functions.Config.gs("doesNotHavePerm",
					Options.plugin.dir + Options.files.messages));
		}
	}
}
