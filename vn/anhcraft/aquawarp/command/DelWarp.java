package vn.anhcraft.aquawarp.command;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.CommandSender;
import vn.anhcraft.aquawarp.api.Functions;
import vn.anhcraft.aquawarp.api.MySQLFuncs;
import vn.anhcraft.aquawarp.main.Options;

public class DelWarp {
	public static void run(String name, CommandSender s){
		name = Functions.reSpecial(name);
		Boolean r;
		try {
			ResultSet rq = MySQLFuncs.exeTable("SELECT * FROM "+ Options.mysql.Warps +" WHERE name='"+name+"';");
			r = rq.next();
			if(r){
		    	if(LockWarp.islocked(name)){
		    		if(Options.cmd.UnlockIfDeleteWarp){
		    			UnLockWarp.run(s, name);
		    		}
		    	}
		    	MySQLFuncs.execUpdate("DELETE FROM "+ Options.mysql.Warps +" WHERE name='"+name+"'; ");
		    	s.sendMessage(Options.message.warpDeletedSuccess.replaceAll("@warp", name));
		    } else {
		    	s.sendMessage(Options.message.warpDidNotCreate);
		    }
			rq.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
