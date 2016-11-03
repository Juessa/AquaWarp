package vn.anhcraft.aquawarp.API;

import java.text.Normalizer;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vn.anhcraft.aquawarp.Options;

public class Functions {
	public static class Energy {
		public static final int get(String uuid){
			File e = new File(Options.plugin.dir + "energydata/"+uuid+".yml");
			YamlConfiguration a = YamlConfiguration.loadConfiguration(e);
			return a.getInt("energy");
		}
		public static final void set(String uuid, int energy) throws IOException{
			File e = new File(Options.plugin.dir + "energydata/"+uuid+".yml");
			YamlConfiguration a = YamlConfiguration.loadConfiguration(e);
			a.set("energy", energy);
			a.save(e);
		}
	}
	
	public static final class Config {
		// lấy dữ liệu
		public static final String gs(String path, String file){
			if(file.equals(Options.plugin.dir + Options.files.messages)){
				String a = Files.messages.getString(path);
				return Functions.reword(a);
			}
			else if(file.equals(Options.plugin.dir + Options.files.config)){
				return Files.config.getString(path);
			} else {
				return null;
			}
		}
		
		public static final int gi(String path, String file){
			if(file.equals(Options.plugin.dir + Options.files.config)){
				return Files.config.getInt(path);
			} else {
				return -1;
			}
		}
		
		public static Material gmaterial(String path, String file) {
			if(file.equals(Options.plugin.dir + Options.files.messages)){
				return Material.getMaterial(Files.messages.getString(path).toUpperCase());
			}
			else if(file.equals(Options.plugin.dir + Options.files.config)){
				return Material.getMaterial(Files.config.getString(path).toUpperCase());
			} else {
				return null;
			}
		}
		
		public static final String[] gls(String path, String file){
			return gals(path, file).toArray(new String[gals(path, file).size()]);
		}
		
		public static final Boolean gb(String path, String file){
			if(file.equals(Options.plugin.dir + Options.files.messages)){
				return Files.messages.getBoolean(path);
			}
			else if(file.equals(Options.plugin.dir + Options.files.config)){
				return Files.config.getBoolean(path);
			} else {
				return null;
			}
		}
		
		public static final ArrayList<String> gals(String path, String file){
			if(file.equals(Options.plugin.dir + Options.files.messages)){
				return new ArrayList<String>(Files.config.getStringList(path));
			}
			else if(file.equals(Options.plugin.dir + Options.files.config)){
				return new ArrayList<String>(Files.config.getStringList(path));
			} else {
				return null;
			}
		}
		
		// thêm dữ liệu
		public static final void s(String path, Object value, String file){
			FileConfiguration f;
			if(file.equals(Options.plugin.dir + Options.files.messages)){
				f = Files.messages;
			}
			else if(file.equals(Options.plugin.dir + Options.files.config)){
				f = Files.config;
			} else {
				f = null;
			}
			f.set(path,value);
			// lưu và cập nhật
			Files.saveAll();
			Files.reload(null);
		}
	}
	
	public static final double strToDouble(String s){
		return Double.parseDouble(s.replace(",","."));
	}
	
	public static final int strToInt(String s){
		double d = Double.parseDouble(s);
		return (int) d;
	}
	
	public static final int doubleToInt(double i){
		int r = (int) i;
		return r;
	}
	
	public static final float strToFloat(String s){
	    try {
	      float f = Float.valueOf(s.trim()).floatValue();
	      return f;
	    }
	    catch (NumberFormatException nfe){
	      System.out.println("NumberFormatException: " + nfe.getMessage());
	      return 0;
	    }
	}
	
	public static final String reSpecial(String s) {
		s = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\-\\+\\.\\,]*");
	    Matcher matcher = pattern.matcher(s);
	    s = matcher.replaceAll("");
		return s;
	}
	
	public static final String reword(String a){
		a = a.replace("%plugin_name%",Options.plugin.name);
		a = a.replace("%plugin_version%",Options.plugin.version);
		a = a.replace("%plugin_author%",Options.plugin.author);
		a = a.replace("%contributor%", Options.plugin.contributor);
		a = a.replace("&", "§");
		return a;
	}
	
	public static final String hash(String pass){
		String type = (Functions.Config.gs("hash", Options.plugin.dir + Options.files.config)).toLowerCase();
		if(type.equals("md5")){
			return md5(pass);
		}
		else if(type.equals("sha256")){
			return sha256(pass);	
		}
		else if(type.equals("sha512")){
			return sha512(pass);
		}
		else if(type.equals("plain_text")){
			return pass;
		}
		else if(type.equals("base64")){
			return base64(pass);
		}
		else {
			return pass;
		}
	}

	public static String base64(String pass) {
        final byte[] authBytes = pass.getBytes(StandardCharsets.UTF_8);
        final String e = Base64.getEncoder().encodeToString(authBytes);
        return e;
	}

	public static final String md5(String pass) {
        MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(pass.getBytes());

	        byte byteData[] = md.digest();
	        StringBuffer hexString = new StringBuffer();
	    	for (int i = 0; i < byteData.length; i++) {
	    		String hex = Integer.toHexString(0xff & byteData[i]);
	   	     	if(hex.length() == 1) {
	   	     		hexString.append('0');
	   	     	}
	   	     	hexString.append(hex);
	    	}
	    	return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static final String sha256(String pass){
	    try {
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(pass.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1){
	            	hexString.append('0');
	            }
	            hexString.append(hex);
	        }
	        return hexString.toString();
	    } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static final String sha512(String pass){
		String salt = Options.plugin.name;
		String generatedPassword = null;
	    try {
	         MessageDigest md = MessageDigest.getInstance("SHA-512");
	         md.update(salt.getBytes("UTF-8"));
	         byte[] bytes = md.digest(pass.getBytes("UTF-8"));
	         StringBuilder sb = new StringBuilder();
	         for(int i = 0; i < bytes.length ; i++){
	            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	         }
	         generatedPassword = sb.toString();
	    } 
	    catch (NoSuchAlgorithmException | UnsupportedEncodingException e){
	       e.printStackTrace();
	    }
	    return generatedPassword;
	}
}
