package vn.anhcraft.aquawarp.Commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.CommandSender;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.API.Functions;
import vn.anhcraft.aquawarp.API.MySQLFuncs;

public class UnLockWarp {
	public static void run(CommandSender s, String warp) {
		// perm
		if(s.hasPermission("aquawarp.unlockwarp") ||
		   s.hasPermission("aquawarp.unlockwarp.*") ||
		   s.hasPermission("aquawarp.unlockwarp."+warp) ||
		   s.hasPermission("aquawarp.*") ||
		   s.isOp()
		){
			warp = Functions.reSpecial(warp);
			Boolean r;
			try {
				// kiểm tra đả khóa chưa
				ResultSet rq = MySQLFuncs.exeq("SELECT * FROM "+ Options.plugin.mysql._Warps +" WHERE name='"+warp+"';");
				r = rq.next();
				if(r){
					// nếu đã khóa
					if(!LockWarp.islocked(warp)){
						s.sendMessage(Functions.Config.gs("warpUnLocked", Options.plugin.dir + Options.files.messages));
					} else {
						// xóa khỏi dtbs
						MySQLFuncs.exeu("DELETE FROM "+ Options.plugin.mysql._Protection +" WHERE name='"+warp+"'; ");
						s.sendMessage(( Functions.Config.gs("warpUnLockSuccess", Options.plugin.dir + Options.files.messages)).replaceAll("@warp", warp));
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
