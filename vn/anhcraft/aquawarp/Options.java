package vn.anhcraft.aquawarp;

import org.bukkit.Bukkit;

import vn.anhcraft.aquawarp.API.Functions;

public class Options {
	public static class plugin {
		public static final String name = "AquaWarp";
		public static final String version = "1.3.3";
		public static final String author = "Anh Craft";
		public static final String contributor = "Banbeucmas"; // ex: a, b, c, d
		public static final Boolean checkUpdate = Functions.Config.gb("checkUpdate", 
											Options.plugin.dir + Options.files.config);
		public static final String serverCheckUpdate = "aquawarp.dev.ecraftvn.tk";
		
		public static final String[] messageOnEnable = {
			"&5",
			"&5",
			"&c[+]-[+]-[+]-[+]-[+]-[+]-[+]-[+]-[+]-[+]-[+]",
			"&5",
			"&a|-=[&r &5%plugin_name%&r &a]=-|&r",
			"&bv%plugin_version%&r",
			"&5",
			"&aBukkit "+Bukkit.getServer().getBukkitVersion(),
			"&5",
			"&6(c) Copyright %plugin_name% by %plugin_author%&r",
			"&5",
			"&2Contributor: %contributor%",
			"&5",
			"&c[+]-[+]-[+]-[+]-[+]-[+]-[+]-[+]-[+]-[+]-[+]",
			"&5",
			"&5"
		};
		public static final long timeToMessageOnEnable = 20L;
		public static final String dir = "plugins/AquaWarp/";
		public static final String[] langs = {
			"vi",
			"en",
			"de",
			"ru"
		};
		
		public static class mysql {
			public static final String prefix = Functions.Config.gs("mysql.prefix", 
					Options.plugin.dir + Options.files.config);
			
			public static String host = Functions.Config.gs("mysql.host", 
					Options.plugin.dir + Options.files.config);
			public static String port = Functions.Config.gs("mysql.port",
					Options.plugin.dir + Options.files.config);
			public static String user = Functions.Config.gs("mysql.user",
					Options.plugin.dir + Options.files.config);
			public static String pass = Functions.Config.gs("mysql.pass",
					Options.plugin.dir + Options.files.config);
			public static String dtbs = Functions.Config.gs("mysql.database", 
					Options.plugin.dir + Options.files.config);
			
			public static final String collate = Functions.Config.gs("mysql.table.collate",
					Options.plugin.dir + Options.files.config);
			public static final String charset = Functions.Config.gs("mysql.table.charset", 
					Options.plugin.dir + Options.files.config);
			public static final String engine = Functions.Config.gs("mysql.table.engine",
					Options.plugin.dir + Options.files.config);
			public static final String _Warps = prefix + "warps";
			public static final String _Protection = prefix + "protection";
			public static final String _FeeTpWarp = prefix + "fee_tpwarp";
		}
	}
	
	public static class message {
		public static final String enable_bc = Functions.reword("&5%plugin_name%&r &aThe plugin has been enabled!");
		public static final String disable_bc = Functions.reword("&5%plugin_name%&r &cThe plugin has been disable!");
	}
	
	public static class files{
		public static final String config = "config.yml";
		public static final String warps = "warps.yml";
		public static final String messages = "messages_@lang.yml";
		public static final String aquawarphtml = "aquawarp.html";
	}
}
