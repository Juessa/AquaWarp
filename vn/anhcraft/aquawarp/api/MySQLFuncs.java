package vn.anhcraft.aquawarp.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import vn.anhcraft.aquawarp.main.Options;

public class MySQLFuncs {
	 static MySQL MySQL = new MySQL(
		Functions.reSpecial(Options.mysql._CONNECT.host),
		Functions.reSpecial(Options.mysql._CONNECT.port),
		Functions.reSpecial(Options.mysql._CONNECT.dtbs),
		Functions.reSpecial(Options.mysql._CONNECT.user),
	    Options.mysql._CONNECT.pass);
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
				+ "`name` text COLLATE "+Options.mysql._INFO.collate+" DEFAULT NULL,"
				+ "`yaw` text COLLATE "+Options.mysql._INFO.collate+" DEFAULT NULL,"
				+ "`x` text COLLATE "+Options.mysql._INFO.collate+" DEFAULT NULL,"
				+ "`y` text COLLATE "+Options.mysql._INFO.collate+" DEFAULT NULL,"
				+ "`z` text COLLATE "+Options.mysql._INFO.collate+" DEFAULT NULL,"
				+ "`world` text COLLATE "+Options.mysql._INFO.collate+" DEFAULT NULL",Options.mysql.Warps);
		
		createIfIsNotExists(""
				+ "`name` text COLLATE "+Options.mysql._INFO.collate+" DEFAULT NULL,"
				+ "`pass` text COLLATE "+Options.mysql._INFO.collate+" DEFAULT NULL,"
				+ "`group` text COLLATE "+Options.mysql._INFO.collate+" DEFAULT NULL,"
				+ "`from` text COLLATE "+Options.mysql._INFO.collate+" DEFAULT NULL,"
				+ "`to` text COLLATE "+Options.mysql._INFO.collate+" DEFAULT NULL",Options.mysql.Protection);
	}
	
	public static void createIfIsNotExists(String str, String tb){
		try {
			Statement statement = MySQL.openConnection().createStatement();
		    statement.executeUpdate(""
		    		+ "CREATE TABLE IF NOT EXISTS "+tb+" ("+str+") "
		    		+ "ENGINE="+Options.mysql._INFO.engine+" "
		    		+ "DEFAULT CHARSET="+Options.mysql._INFO.charset+" "
		    		+ "COLLATE="+Options.mysql._INFO.collate+";");
		    statement.close();
		    
		} catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
	}
	
	public static ResultSet exeTable(String name){
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
	
	public static void execUpdate(String s){
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
