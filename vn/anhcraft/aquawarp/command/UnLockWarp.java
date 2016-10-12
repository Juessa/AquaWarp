package vn.anhcraft.aquawarp.command;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.CommandSender;
import vn.anhcraft.aquawarp.api.Functions;
import vn.anhcraft.aquawarp.api.MySQLFuncs;
import vn.anhcraft.aquawarp.main.Options;

public class UnLockWarp {
	public static void run(CommandSender s, String warp) {
		warp = Functions.reSpecial(warp);
		Boolean r;
		try {
			ResultSet rq = MySQLFuncs.exeTable("SELECT * FROM "+ Options.mysql.Warps +" WHERE name='"+warp+"';");
			r = rq.next();
			if(r){
				if(!LockWarp.islocked(warp)){
					s.sendMessage(Options.message.warpUnLocked);
				} else {
					MySQLFuncs.execUpdate("DELETE FROM "+ Options.mysql.Protection +" WHERE name='"+warp+"'; ");
					s.sendMessage(Options.message.warpUnLockSuccess.replaceAll("@warp", warp));
				}
			} else {
				s.sendMessage(Options.message.warpDidNotCreate);
			}
			rq.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
