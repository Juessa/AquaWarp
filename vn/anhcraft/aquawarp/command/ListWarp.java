package vn.anhcraft.aquawarp.command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.command.CommandSender;
import vn.anhcraft.aquawarp.api.MySQL;
import vn.anhcraft.aquawarp.api.MySQLFuncs;
import vn.anhcraft.aquawarp.main.Options;

public class ListWarp {
	static MySQL MySQL = new MySQL(
		    Options.mysql._CONNECT.host,
		    Options.mysql._CONNECT.port,
		    Options.mysql._CONNECT.dtbs,
		    Options.mysql._CONNECT.user,
		    Options.mysql._CONNECT.pass);
		 Connection c = null;
		 
	public static void run(CommandSender s) {
		try {
			Statement statement = MySQL.openConnection().createStatement();
			ResultSet r = statement.executeQuery(""
					+ "SELECT * FROM "+Options.mysql.Warps+";");
			s.sendMessage(Options.message.warpListMessage
				.replaceAll("@size", Integer.toString(MySQLFuncs.rowsize(Options.mysql.Warps)))
			);
			
			
			int a = 1;
			while (r.next()){
				String status = "";
				if(LockWarp.islocked(r.getString("name"))){
					status = Options.message.warpStatusLocked;
				}
				s.sendMessage(Options.message.warpListEach
					.replaceAll("@num", Integer.toString(a))
					.replaceAll("@name", r.getString("name"))
					.replaceAll("@status", status)
				);
				a += 1;
			}
			
			r.close();
			statement.close();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
