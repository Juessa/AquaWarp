package vn.anhcraft.aquawarp.API;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import vn.anhcraft.aquawarp.Options;

public class MySQLFuncs {
	 static MySQL MySQL = new MySQL(
		Functions.reSpecial(Options.plugin.mysql.host),
		Functions.reSpecial(Options.plugin.mysql.port),
		Functions.reSpecial(Options.plugin.mysql.dtbs),
		Functions.reSpecial(Options.plugin.mysql.user),
		Options.plugin.mysql.pass);
	 Connection c = null;
	 
	 
	public static int rowsize(String t){
		int a = 0;
		try {
			Statement statement = MySQL.openConnection().createStatement();
			ResultSet r = statement.executeQuery(""
					+ "SELECT * FROM "+t+";");
			
			while (r.next()){
				a += 1;
			}
			
			r.close();
			statement.close();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return a;
	} 
	
	public static void setup() {
		createIfIsNotExists(""
				+ "`name` longtext COLLATE "+Options.plugin.mysql.collate+" DEFAULT NULL,"
				+ "`yaw` longtext COLLATE "+Options.plugin.mysql.collate+" DEFAULT NULL,"
				+ "`x` longtext COLLATE "+Options.plugin.mysql.collate+" DEFAULT NULL,"
				+ "`y` longtext COLLATE "+Options.plugin.mysql.collate+" DEFAULT NULL,"
				+ "`z` longtext COLLATE "+Options.plugin.mysql.collate+" DEFAULT NULL,"
				+ "`world` longtext COLLATE "+Options.plugin.mysql.collate+" DEFAULT NULL",Options.plugin.mysql._Warps);
		
		createIfIsNotExists(""
				+ "`name` longtext COLLATE "+Options.plugin.mysql.collate+" DEFAULT NULL,"
				+ "`pass` longtext COLLATE "+Options.plugin.mysql.collate+" DEFAULT NULL,"
				+ "`group` longtext COLLATE "+Options.plugin.mysql.collate+" DEFAULT NULL,"
				+ "`from` longtext COLLATE "+Options.plugin.mysql.collate+" DEFAULT NULL,"
				+ "`to` longtext COLLATE "+Options.plugin.mysql.collate+" DEFAULT NULL",Options.plugin.mysql._Protection);
		
		createIfIsNotExists(""
				+ "`name` longtext COLLATE "+Options.plugin.mysql.collate+" DEFAULT NULL,"
				+ "`lock_money` longtext COLLATE "+Options.plugin.mysql.collate+" DEFAULT NULL,"
				+ "`unlock_money` longtext COLLATE "+Options.plugin.mysql.collate+" DEFAULT NULL,"
				+ "`group` longtext COLLATE "+Options.plugin.mysql.collate+" DEFAULT NULL",Options.plugin.mysql._FeeTpWarp);
	}
	
	public static void createIfIsNotExists(String str, String tb){
		try {
			Statement statement = MySQL.openConnection().createStatement();
		    statement.executeUpdate(""
		    		+ "CREATE TABLE IF NOT EXISTS "+tb+" ("+str+") "
		    		+ "ENGINE="+Options.plugin.mysql.engine+" "
		    		+ "DEFAULT CHARSET="+Options.plugin.mysql.charset+" "
		    		+ "COLLATE="+Options.plugin.mysql.collate+";");
		    statement.close();
		    
		} catch (SQLException | ClassNotFoundException e) {
           e.printStackTrace();
       }
	}
	
	public static ResultSet exeq(String name){
		try {
			setup();
			
			Statement statement = MySQL.openConnection().createStatement();
		    ResultSet r = statement.executeQuery(name);
		   
		    return r;
		    
		} catch (SQLException | ClassNotFoundException e) {
           e.printStackTrace();
           return null;
       }
	}
	
	public static void exeu(String s){
		try {
			setup();
			
			Statement statement = MySQL.openConnection().createStatement();
			statement.executeUpdate(s);
		    statement.close();
		    
		} catch (SQLException | ClassNotFoundException e) {
           e.printStackTrace();
       }
	}
}