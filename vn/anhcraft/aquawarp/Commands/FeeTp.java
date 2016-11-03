package vn.anhcraft.aquawarp.Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.command.CommandSender;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.API.Functions;
import vn.anhcraft.aquawarp.API.MySQLFuncs;

public class FeeTp {
	public static void run(CommandSender s, String w, String m1, String m2) {
		w = Functions.reword(w);
		if(s.hasPermission("aquawarp.feetp") ||
		   s.hasPermission("aquawarp.feetp.*") ||
		   s.hasPermission("aquawarp.feetp."+w) ||
		   s.hasPermission("aquawarp.*") ||
		   s.isOp()){
			try {
				ResultSet rq = MySQLFuncs.exeq("SELECT * FROM "+ Options.plugin.mysql._Warps +" WHERE name='"+w+"';");
				Boolean rt = rq.next();
				if(rt){
					Boolean r = MySQLFuncs.exeq("SELECT * FROM "+ Options.plugin.mysql._FeeTpWarp +" WHERE name='"+w+"';").next();
					if(r){
					   	MySQLFuncs.exeu("UPDATE "+ Options.plugin.mysql._FeeTpWarp +" SET `unlock_money`='"+m1+"' WHERE `name`='"+w+"';");
					   	MySQLFuncs.exeu("UPDATE "+ Options.plugin.mysql._FeeTpWarp +" SET `lock_money`='"+m2+"' WHERE `name`='"+w+"';");
					} else {
					    MySQLFuncs.exeu("INSERT INTO "+ Options.plugin.mysql._FeeTpWarp +" (`name`,`lock_money`,`unlock_money`,`group`) VALUES ("
						+ "'"+w+"', "
						+ "'"+m2+"', "
						+ "'"+m1+"', "
						+ "''"
						+ ");");
					}
					s.sendMessage((Functions.Config.gs("updateMoneySuccess", 
							Options.plugin.dir + Options.files.messages)).replaceAll("@warp", w));
				} else {
					s.sendMessage(Functions.Config.gs("warpDidNotCreate",
							Options.plugin.dir + Options.files.messages));
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
