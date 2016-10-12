package vn.anhcraft.aquawarp.command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.command.CommandSender;
import vn.anhcraft.aquawarp.api.Functions;
import vn.anhcraft.aquawarp.api.MySQL;
import vn.anhcraft.aquawarp.api.MySQLFuncs;
import vn.anhcraft.aquawarp.main.Options;

public class LockWarp {
	static MySQL MySQL = new MySQL(
		    Options.mysql._CONNECT.host,
		    Options.mysql._CONNECT.port,
		    Options.mysql._CONNECT.dtbs,
		    Options.mysql._CONNECT.user,
		    Options.mysql._CONNECT.pass);
		 Connection c = null;
		 
	public static boolean islocked(String warp){
		MySQLFuncs.setup();
		Statement statement;
		Boolean a = false;
		try {
			statement = MySQL.openConnection().createStatement();
			ResultSet r = statement.executeQuery(""
	    		+ "SELECT * FROM "+Options.mysql.Protection+" WHERE name='"+Functions.reSpecial(warp)+"';");
			a = r.next();
			r.close();
			statement.close();
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			a = false;
		}
		return a;
	}
	
	public static void run(CommandSender s, String warp, String pass) {
		warp = Functions.reSpecial(warp);
		Boolean r;
		try {
			ResultSet rq = MySQLFuncs.exeTable("SELECT * FROM "+ Options.mysql.Warps +" WHERE name='"+warp+"';");
			r = rq.next();
			if(r){
				if(islocked(warp)){
					s.sendMessage(Options.message.warpLocked);
				} else {
					String[] unsafePassword = Options.cmd.UnSafePassword;
					Boolean ps = true;
					for(String psc : unsafePassword){
						if(pass.equals(psc)){
							ps = false;
							break;
						}
					}
					if(ps){
						pass = Functions.reSpecial(Functions.md5(pass));
						MySQLFuncs.execUpdate("INSERT INTO "+ Options.mysql.Protection +" (`name`,`pass`,`group`,`from`,`to`) VALUES ('"+warp+"','"+pass+"','','','');");
						s.sendMessage(Options.message.warpLockSuccess.replaceAll("@warp", warp));
					} else {
						s.sendMessage(Options.message.passwordUnSafe);
					}
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
