package vn.anhcraft.aquawarp.Util;

import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.iceapi.MySQL.IceMySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class ConnectDatabase {
    static IceMySQL i = null;

    public static void setup(){
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("mysql")) {
            i = new IceMySQL(
                    Configuration.config.getString("mysql.host"),
                    Configuration.config.getInt("mysql.port"),
                    Configuration.config.getString("mysql.database"),
                    Configuration.config.getString("mysql.user"),
                    Configuration.config.getString("mysql.pass")
            );
            String cl = Configuration.config.getString("mysql.table.collate");

            createIfNotExists(""
                    + "`name` varchar(255) COLLATE " + cl + " DEFAULT NULL UNIQUE,"
                    + "`yaw` float COLLATE " + cl + " DEFAULT NULL,"
                    + "`pitch` float COLLATE " + cl + " DEFAULT NULL,"
                    + "`x` float COLLATE " + cl + " DEFAULT NULL,"
                    + "`y` float COLLATE " + cl + " DEFAULT NULL,"
                    + "`z` float COLLATE " + cl + " DEFAULT NULL,"
                    + "`world` varchar(255) COLLATE " + cl + " DEFAULT NULL", Options.mysql_table_warp);

            createIfNotExists(""
                    + "`name` varchar(255) COLLATE " + cl + " DEFAULT NULL UNIQUE,"
                    + "`pass` varchar(255) COLLATE " + cl + " DEFAULT NULL", Options.mysql_table_protection);

            createIfNotExists(""
                    + "`name` varchar(255) COLLATE " + cl + " DEFAULT NULL UNIQUE,"
                    + "`lock` float(53) COLLATE " + cl + " DEFAULT NULL,"
                    + "`unlock` float(53) COLLATE " + cl + " DEFAULT NULL", Options.mysql_table_service_charges);
        }
    }

    public static void createIfNotExists(String str, String tb){
        try {
            i.connect();
            Statement statement = i.statement();
            statement.executeUpdate(""
                    + "CREATE TABLE IF NOT EXISTS " + tb + " (" + str + ") "
                    + "ENGINE=" + Configuration.config.getString("mysql.table.engine") + " "
                    + "DEFAULT CHARSET=" + Configuration.config.getString("mysql.table.charset") + " "
                    + "COLLATE=" + Configuration.config.getString("mysql.table.collate") + ";");
            statement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void exeu(String cmd){
        setup();
        try {
            i.connect();
            Statement statement = i.statement();
            statement.executeUpdate(cmd);
            statement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet exeq(String cmd){
        setup();
        try {
            i.connect();
            ResultSet o;
            Statement statement = i.statement();
            o = statement.executeQuery(cmd);
            return o;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int rowsize(String t){
        int a = 0;
        try {
            ResultSet r = exeq(""
                    + "SELECT * FROM "+t+";");

            while (r.next()){
                a += 1;
            }

            r.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }
}
