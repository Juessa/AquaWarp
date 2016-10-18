package vn.anhcraft.aquawarp.Commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.CommandSender;

import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.API.Functions;
import vn.anhcraft.aquawarp.API.MySQLFuncs;

public class ListWarp {

	public static void run(CommandSender s) {
		if(s.hasPermission("aquawarp.listwarp") ||
		   s.hasPermission("aquawarp.*") ||
		   s.isOp()){
			try {
				ResultSet r = MySQLFuncs.exeq("SELECT * FROM "+Options.plugin.mysql._Warps+";");
				s.sendMessage((Functions.Config.gs("warpListMessage", Options.plugin.dir + Options.files.messages))
					.replace("@size", Integer.toString(MySQLFuncs.rowsize(Options.plugin.mysql._Warps)))
				);
				
				
				int a = 1;
				while (r.next()){
					String status = "";
					if(LockWarp.islocked(r.getString("name"))){
						status = Functions.Config.gs("warpStatusLocked", Options.plugin.dir + Options.files.messages);
					}
					s.sendMessage((Functions.Config.gs("warpListEach", Options.plugin.dir + Options.files.messages))
						.replace("@num", Integer.toString(a))
						.replace("@name", r.getString("name"))
						.replace("@status", status)
					);
					a += 1;
				}
				
				r.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			s.sendMessage(Functions.Config.gs("doesNotHavePerm", Options.plugin.dir + Options.files.messages));
		}
	}

}
