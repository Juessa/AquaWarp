package vn.anhcraft.aquawarp.api;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import vn.anhcraft.aquawarp.main.Options;

public class Functions {
	public static double strToDouble(String s){
		return Double.parseDouble(s.replace(",","."));
	}
	
	public static int strToInt(String s){
		double d = Double.parseDouble(s);
		return (int) d;
	}
	
	public static int doubleToInt(double i){
		int r = (int) i;
		return r;
	}
	
	public static float stringToFloat(String s){
	    try {
	      float f = Float.valueOf(s.trim()).floatValue();
	      return f;
	    }
	    catch (NumberFormatException nfe){
	      System.out.println("NumberFormatException: " + nfe.getMessage());
	      return 0;
	    }
	}
	
	public static String reSpecial(String s) {
		s = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\-\\+\\.\\,]*");
	    Matcher matcher = pattern.matcher(s);
	    s = matcher.replaceAll("");
	    s = s.replaceAll(",","."); // tự động . thành ,
		return s;
	}
	
	public static String reword(String a){
		a = a.replaceAll("%plugin_name%",Options.plugin.name);
		a = a.replaceAll("%plugin_version%",Options.plugin.version);
		a = a.replaceAll("%plugin_author%",Options.plugin.author);
		a = a.replaceAll("&", "§");
		return a;
	}

	public static String md5(String pass) {
        MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(pass.getBytes());

	        byte byteData[] = md.digest();
	        StringBuffer hexString = new StringBuffer();
	    	for (int i=0;i<byteData.length;i++) {
	    		String hex=Integer.toHexString(0xff & byteData[i]);
	   	     	if(hex.length()==1) hexString.append('0');
	   	     	hexString.append(hex);
	    	}
	    	
	    	return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
