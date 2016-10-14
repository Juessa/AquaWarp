package vn.anhcraft.aquawarp.main;

import java.io.File;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vn.anhcraft.aquawarp.main.Options;

public class Config {
	public static FileConfiguration getConfigFile(String fs) {
		File pf = new File(Options.files.plugin_dir + fs);
        FileConfiguration f = null;
        if (isExists(fs)) {
    		f = YamlConfiguration.loadConfiguration(pf);
        }
        return f;
    }
	
	public static boolean isExists(String fn){
		File pf = new File(Options.files.plugin_dir + fn);
        if (!pf.exists()) {
        	return false;
        } else {
        	return true;
        }
	}
	
	
	public static List<String> getstringlist(String path, List<String> def, String fn) {
		List<String> o = def;
		if (isExists(fn)) {
        	o = getConfigFile(fn).getStringList(path);
		}
        return o;
	}
	
	public static String getstring(String path, String def, String fn) {
		String o = def;
		if (isExists(fn)) {
        	o = getConfigFile(fn).getString(path);
		}
        return o;
	}
	
	public static int getint(String path, int def, String fn) {
		int o = def;
		if (isExists(fn)) {
        	o = getConfigFile(fn).getInt(path);
		}
        return o;
	}
	
	public static Boolean getboolean(String path, Boolean def, String fn) {
		Boolean o = def;
		if (isExists(fn)) {
        	o = getConfigFile(fn).getBoolean(path);
		}
        return o;
	}
}
