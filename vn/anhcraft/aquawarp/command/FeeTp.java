package vn.anhcraft.aquawarp.command;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.CommandSender;

import vn.anhcraft.aquawarp.api.MySQLFuncs;
import vn.anhcraft.aquawarp.main.Options;

public class FeeTp {

	public static void run(CommandSender s, String w, String m1, String m2) {
		try {
			ResultSet rq = MySQLFuncs.exeTable("SELECT * FROM "+ Options.mysql.Warps +" WHERE name='"+w+"';");
			Boolean rt = rq.next();
			if(rt){
				Boolean r = MySQLFuncs.exeTable("SELECT * FROM "+ Options.mysql.FeeTpWarp +" WHERE name='"+w+"';").next();
				if(r){
				   	MySQLFuncs.execUpdate("UPDATE "+ Options.mysql.FeeTpWarp +" SET `unlock_money`='"+m1+"' WHERE `name`='"+w+"';");
				   	MySQLFuncs.execUpdate("UPDATE "+ Options.mysql.FeeTpWarp +" SET `lock_money`='"+m2+"' WHERE `name`='"+w+"';");
				} else {
				    MySQLFuncs.execUpdate("INSERT INTO "+ Options.mysql.FeeTpWarp +" (`name`,`lock_money`,`unlock_money`,`group`) VALUES ("
					+ "'"+w+"', "
					+ "'"+m2+"', "
					+ "'"+m1+"', "
					+ "''"
					+ ");");
				}
				s.sendMessage(Options.message.updateMoneySuccess.replaceAll("@warp", w));
			} else {
				s.sendMessage(Options.message.warpDidNotCreate);
			}
			rq.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
