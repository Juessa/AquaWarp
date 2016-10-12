package vn.anhcraft.aquawarp.main;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import vn.anhcraft.aquawarp.api.Functions;

public class Options {
	public static final class plugin {
		public static final String name = "AquaWarp";
		public static final String version = "1.2.0";
		public static final String author = "Anh Craft";
		
		public static final Boolean disable_stop = Config.getboolean("plugin.disable_serverStop", Default.plugin.disable_stop, files.file_config);
		public static final Boolean checkUpdate = Config.getboolean("plugin.checkUpdate", Default.plugin.checkUpdate, files.file_config);
		public static final String serverCheckUpdate = "s1-aquawarp.anhcraft.tk";
		
		public static final String[] messageOnEnable = {
			"&5",
			"&5",
			"&c<=><=><=><=><=><=><=><=><=><=><=><=><=><=><=>",
			"&5",
			"&a|-=[&r &5%plugin_name%&r &a]=-|&r",
			"&bv%plugin_version%&r",
			"&5",
			"&aBukkit "+Bukkit.getServer().getBukkitVersion(),
			"&5",
			"&6(c) Copyright %plugin_name% by %plugin_author%&r",
			"&5",
			"&c<=><=><=><=><=><=><=><=><=><=><=><=><=><=><=>",
			"&5",
			"&5"
		};
		public static final long timeToMessageOnEnable = 20L;
	}
	
	public static final class cmd {
		public static final String Warp = "warp";
		public static final String Warps = "warps";
		public static final String SetWarp = "set";
		public static final String DelWarp = "del";
		public static final String CheckWarp = "check";
		public static final int CheckWarpGroud = Config.getint("checkWarp.maxArea", Default.cmd.CheckWarpGroud, files.file_config);
		public static final int CheckWarpMaxSafe = Config.getint("checkWarp.maxAreaSafe", Default.cmd.CheckWarpMaxSafe, files.file_config);
		public static final String EditWarp = "edit";
		public static final String ListWarp = "list";
		public static final String LockWarp = "lock";
		public static final String[] UnSafePassword = Config.getstringlist("lockWarp.unsafePassword", Default.cmd.UnSafePassword, files.file_config).stream().toArray(String[]::new);
		public static final String UnLockWarp = "unlock";
		public static final boolean UnlockIfDeleteWarp = Config.getboolean("delWarp.unlockIfDeleteWarp", Default.cmd.UnlockIfDeleteWarp, files.file_config);
	}
	
	public static final class files {
		public static final String plugin_dir = "plugins/AquaWarp/";
		public static final String cache_dir = "cache/";
		public static final String cache_filetype = ".json";
		public static final String file_config = "config.yml";
		public static final String file_perm = "permissions.yml";
		public static final String file_message = "messages.yml";
	}
	
	public static final class message {
		public static final String enable_bc = Functions.reword("&5[%plugin_name%]&r &aPlugin đã bật!");
		public static final String disable_bc = Functions.reword("&5[%plugin_name%]&r &cPlugin đã tắt!");
		
		public static final String invalidSender = Functions.reword(Config.getstring("senderNotPlayer", Default.message.invalidSender, files.file_message));
		public static final String invalidCmd = Functions.reword(Config.getstring("unknownCommand", Default.message.invalidCmd, files.file_message));
		public static final String doesNotHavePerm = Functions.reword(Config.getstring("senderNotHavePerm", Default.message.doesNotHavePerm, files.file_message));
		public static final String doNotHaveWarp = Functions.reword(Config.getstring("unknownWarp", Default.message.doNotHaveWarp, files.file_message));
		public static final String playerNull = Functions.reword(Config.getstring("unknownPlayer", Default.message.playerNull, files.file_message));
		public static final String requireName = Functions.reword(Config.getstring("requireWarpName", Default.message.requireName, files.file_message));
		public static final String worldNull = Functions.reword(Config.getstring("unknownWorld", Default.message.worldNull, files.file_message));
		public static final String warpDidNotCreate = Functions.reword(Config.getstring("warpNotCreated", Default.message.warpDidNotCreate, files.file_message));
		public static final String warpCreated = Functions.reword(Config.getstring("warpCreated", Default.message.warpCreated, files.file_message));
		
		public static final String[] help = {
			"&c---------------------------------&r",
			"&a-=[ &r&5%plugin_name%&r&a &bv%plugin_version%&r&a ]=-&r",
			"&5",
			"&6/warp <name> [<player>]&r &a[+] Teleport you or other player to a warp&r",
			"&6/warps&r &a[+] All commands of this plugin (Help)&r",
			"&6/warps set <name> [<x> <y> <z> <yaw> <world>]&r &a[+] Create new warp&r",
			"&6/warps del <name>&r &a[+] Delete a warp&r",
			"&6/warps check <name>&r &a[+] Check a warp&r",
			"&6/warps list&r &a[+] List all warps&r",
			"&6/warps lock <name> <pass>&r &a[+] Lock a warp&r",
			"&6/warps unlock <name>&r &a[+] Unlock a warp&r",
			"&c---------------------------------&r"
		};
		
		public static final String tpWarpSuccess = Functions.reword(Config.getstring("tpSuccess", Default.message.tpWarpSuccess, files.file_message));
		public static final String tpWarpSuccessReTpOther = Config.getstring("tpSuccessReName", Default.message.tpWarpSuccessReTpOther, files.file_message);
		public static final String warpCreateSuccess = Functions.reword(Config.getstring("createdSuccess", Default.message.warpCreateSuccess, files.file_message));
		public static final String warpDeletedSuccess = Functions.reword(Config.getstring("deletedSuccess", Default.message.warpDeletedSuccess, files.file_message));
		public static final String checkWarpSuccess = Functions.reword(Config.getstring("checkedSuccess", Default.message.checkWarpSuccess, files.file_message));
		public static final String warpIsDanger = Functions.reword(Config.getstring("warpDanger", Default.message.warpIsDanger, files.file_message));
		public static final String warpBlockDangerLength = Functions.reword(Config.getstring("warpDangerBlock", Default.message.warpBlockDangerLength, files.file_message));
		public static final String warpIsSafe = Functions.reword(Config.getstring("warpSafe", Default.message.warpIsSafe, files.file_message));
		public static final String editWarpSuccess = Functions.reword(Config.getstring("editedSuccess", Default.message.editWarpSuccess, files.file_message));
		public static final String newUpdateAvailable = Functions.reword("&5[%plugin_name%]&r &6There is a newer version available!");
		public static final String versionNewest = Functions.reword("&5[%plugin_name%]&r &aThis is the newest version!");
		public static final String warpListMessage = Functions.reword(Config.getstring("warpListMessage", Default.message.warpListMessage, files.file_message));
		public static final String warpListEach = Functions.reword(Config.getstring("warpListEach", Default.message.warpListEach, files.file_message));
		public static final String requirePass = Functions.reword(Config.getstring("requirePass", Default.message.requirePass, files.file_message));
		public static final String warpLocked = Functions.reword(Config.getstring("warpLocked", Default.message.warpLocked, files.file_message));
		public static final String passwordUnSafe = Functions.reword(Config.getstring("passwordUnSafe", Default.message.passwordUnSafe, files.file_message));
		public static final String warpLockSuccess = Functions.reword(Config.getstring("warpLockSuccess", Default.message.warpLockSuccess, files.file_message));
		public static final String warpStatusLocked = Functions.reword(Config.getstring("warpStatusLocked", Default.message.warpStatusLocked, files.file_message));
		public static final String warpUnLocked = Functions.reword(Config.getstring("warpUnLocked", Default.message.warpUnLocked, files.file_message));
		public static final String warpUnLockSuccess = Functions.reword(Config.getstring("warpUnLockSuccess", Default.message.warpUnLockSuccess, files.file_message));
		public static final String tpLockedWarpWrongPass = Functions.reword(Config.getstring("tpLockedWarpWrongPass", Default.message.tpLockedWarpWrongPass, files.file_message));
		public static final String tpLockedWarpMessage = Functions.reword(Config.getstring("tpLockedWarpMessage", Default.message.tpLockedWarpMessage, files.file_message));
	}

	public static final class mysql {
		public static final String _GOLBAL = Config.getstring("mysql.prefix", Default.mysql._GOLBAL, files.file_config);
		public static final class _INFO {
			public static final String collate = Config.getstring("mysql.table.collate", Default.mysql._INFO.collate, files.file_config);
			public static final String charset = Config.getstring("mysql.table.charset", Default.mysql._INFO.charset, files.file_config);
			public static final String engine = Config.getstring("mysql.table.engine", Default.mysql._INFO.engine, files.file_config);
		}
		public static final class _CONNECT {
			public static final String host = Config.getstring("mysql.host", Default.mysql._CONNECT.host, files.file_config);
			public static final String port = Config.getstring("mysql.port", Default.mysql._CONNECT.port, files.file_config);
			public static final String user = Config.getstring("mysql.user", Default.mysql._CONNECT.user, files.file_config);
			public static final String pass = Config.getstring("mysql.pass", Default.mysql._CONNECT.pass, files.file_config);
			public static final String dtbs = Config.getstring("mysql.database", Default.mysql._CONNECT.dtbs, files.file_config);
		}
		public static final String Warps = _GOLBAL + "warps";
		public static final String Protection = _GOLBAL + "protection";
	}

	public static final class perm {
		public static final String _GLOBAL = Config.getstring("ALL", Default.perm._GLOBAL, files.file_perm);
		public static final String TpWarp = Config.getstring("tpWarp", Default.perm.TpWarp, files.file_perm);
		public static final String HelpWarp = Config.getstring("helpWarp", Default.perm.HelpWarp, files.file_perm);
		public static final String SetWarp = Config.getstring("setWarp", Default.perm.SetWarp, files.file_perm);
		public static final String DelWarp = Config.getstring("delWarp", Default.perm.DelWarp, files.file_perm);
		public static final String CheckWarp = Config.getstring("checkWarp", Default.perm.CheckWarp, files.file_perm);
		public static final String EditWarp = Config.getstring("editWarp", Default.perm.EditWarp, files.file_perm);
		public static final String ListWarp = Config.getstring("listWarp", Default.perm.ListWarp, files.file_perm);
		public static final String LockWarp = Config.getstring("lockWarp", Default.perm.LockWarp, files.file_perm);
		public static final String UnLockWarp = Config.getstring("unLockWarp", Default.perm.UnLockWarp, files.file_perm);
	}

	public static final class effect {
		public static final boolean _ENABLE = Config.getboolean("other.effectEnable", Default.effect._ENABLE, files.file_config);
		public static final Effect TpWarp = Effect.ENDER_SIGNAL;
	}
	
	public static final class sound {
		public static final boolean _ENABLE = Config.getboolean("other.soundEnable", Default.sound._ENABLE, files.file_config);
		public static final Sound TpWarp = Sound.ENTITY_ENDERMEN_TELEPORT;
	}
}