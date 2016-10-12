package vn.anhcraft.aquawarp.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Cache {
	public static void delAll(){
		File folder = new File(Options.files.plugin_dir + Options.files.cache_dir);
	    File[] files = folder.listFiles();
	    if(files!=null) {
	        for(File f: files) {
	         f.delete();
	        }
	    }
	    folder.delete();
	}
	
	public static boolean set(String filename, String cont){
		String path = Options.files.plugin_dir + Options.files.cache_dir + filename + Options.files.cache_filetype;
		File ie = new File(path);
		if(!ie.exists()) {
			Cache.newFile(filename);
		}
		try {
			BufferedWriter o = new BufferedWriter(new FileWriter(path));
			o.write(cont);
			o.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String get(String filename){
		String path = Options.files.plugin_dir + Options.files.cache_dir + filename + Options.files.cache_filetype;
		File ie = new File(path);
		if(!ie.exists()) {
			Cache.newFile(filename);
		}
		try {
			BufferedReader o = new BufferedReader(new FileReader(path));
			o.close();
			return o.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean del(String filename){
		String path = Options.files.plugin_dir + Options.files.cache_dir + filename + Options.files.cache_filetype;
		File ie = new File(path);
		if(ie.exists()) {
			return ie.delete();
		} else {
			return false;
		}
	}

	public static boolean newFile(String filename) {
		File file = new File(Options.files.plugin_dir + Options.files.cache_dir + filename + Options.files.cache_filetype);
		File dir = new File(Options.files.plugin_dir + Options.files.cache_dir);
		if(!dir.exists()) {
			dir.mkdirs();
		}
        try {
			file.createNewFile();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
