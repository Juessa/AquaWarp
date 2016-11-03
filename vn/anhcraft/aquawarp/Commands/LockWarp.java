package vn.anhcraft.aquawarp.Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.command.CommandSender;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.API.Functions;
import vn.anhcraft.aquawarp.API.MySQLFuncs;

public class LockWarp {
	public static boolean islocked(String warp){
		Boolean a = false;
		try {
			ResultSet r = MySQLFuncs.exeq("SELECT * FROM "+Options.plugin.mysql._Protection+" WHERE name='"+Functions.reSpecial(warp)+"';");
			a = r.next();
			r.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			a = false;
		}
		return a;
	}
	
	public static void run(CommandSender s, String warp, String pass) {
		if( s.hasPermission("aquawarp.lockwarp") ||
			s.hasPermission("aquawarp.lockwarp.*") ||
			s.hasPermission("aquawarp.lockwarp."+warp) ||
			s.hasPermission("aquawarp.*") ||
			s.isOp()
		){
			// xóa kí tự đặc biệt
			warp = Functions.reSpecial(warp);
			Boolean r;
			try {
				// kết nối dtbs
				ResultSet rq = MySQLFuncs.exeq("SELECT * FROM "+ Options.plugin.mysql._Warps +" WHERE name='"+warp+"';");
				r = rq.next();
				if(r){
					// có lock chưa
					if(islocked(warp)){
						s.sendMessage(Functions.Config.gs("warpLocked", Options.plugin.dir + Options.files.messages));
					} else {
						// kiểm tra mật khẩu
						String[] unsafePassword = Functions.Config.gls("lockWarp.unsafePassword", Options.plugin.dir + Options.files.config);
						Boolean ps = true;
						for(String psc : unsafePassword){
							if(pass.equals(psc)){
								ps = false;
								break;
							}
						}
						if(ps){
							// thêm lock
							pass = Functions.reSpecial(Functions.hash(pass));
							MySQLFuncs.exeu("INSERT INTO "+ Options.plugin.mysql._Protection +
							" (`name`,`pass`,`group`,`from`,`to`) "
							+ "VALUES ('"+warp+"','"+pass+"','','','');");
							s.sendMessage((Functions.Config.gs("warpLockSuccess", Options.plugin.dir + Options.files.messages)).replace("@warp", warp));
						} else {
							s.sendMessage(Functions.Config.gs("passwordUnSafe", Options.plugin.dir + Options.files.messages));
						}
					}
				} else {
					s.sendMessage(Functions.Config.gs("warpDidNotCreate", Options.plugin.dir + Options.files.messages));
				}		
				rq.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			s.sendMessage(Functions.Config.gs("doesNotHavePerm", Options.plugin.dir + Options.files.messages));
		}
	}
}
