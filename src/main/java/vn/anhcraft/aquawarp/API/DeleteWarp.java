package vn.anhcraft.aquawarp.API;

import vn.anhcraft.aquawarp.AquaWarpsMessages;
import vn.anhcraft.aquawarp.Exceptions.WarpNotFound;
import vn.anhcraft.aquawarp.Exceptions.WrongSaveDataType;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.Util.Configuration;
import vn.anhcraft.aquawarp.Util.ConnectDatabase;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public interface DeleteWarp {
    static Boolean DeleteWarpWithMySQL(String warp)
            throws SQLException, WarpNotFound, WrongSaveDataType {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("mysql")){
            ResultSet r = ConnectDatabase.exeq("SELECT * FROM " + Options.mysql_table_warp + " WHERE name='" + warp + "';");
            Boolean i = r.next();
            if(!i) {
                r.close();
                throw new WarpNotFound(AquaWarpsMessages.get("System.Exception.300"));
            } else {
                ConnectDatabase.exeu("DELETE FROM " + Options.mysql_table_warp + "  WHERE `name`='"+warp+"';");
                r.close();
            }
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
        return true;
    }
    static Boolean DeleteWarpWithFile(String warp)
            throws SQLException, WarpNotFound, WrongSaveDataType, IOException {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")) {
            File a = new File(Options.pluginDir + "warps/" + warp + ".yml");
            if(!a.exists()) {
                throw new WarpNotFound(AquaWarpsMessages.get("System.Exception.300"));
            } else {
                a.delete();
            }
        } else {
            throw new WrongSaveDataType(AquaWarpsMessages.get("System.Exception.301"));
        }
        return true;
    }

    static Boolean DeleteWarpAutoSaveDataType(String warp)
            throws SQLException, WrongSaveDataType, IOException, WarpNotFound {
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            return DeleteWarpWithFile(warp);
        } else {
            return DeleteWarpWithMySQL(warp);
        }
    }
}
