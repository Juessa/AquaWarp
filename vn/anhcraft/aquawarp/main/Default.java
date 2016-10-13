package vn.anhcraft.aquawarp.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Default {
	public static class plugin {
		public static Boolean disable_stop = false;
		public static Boolean checkUpdate = true;
	}
	
	public static class cmd {
		public static int CheckWarpGroud = 256;
		public static int CheckWarpMaxSafe = 1;
		public static List<String> UnSafePassword = new ArrayList<String>(Arrays.asList( new String[]{
			"123456",
			"123456789",
			"123123",
			"1234",
			"123",
			"admin",
			"password",
			"abc123",
			"abcxyz",
			"login"
		}));
		public static List<String> cmdListBeforeWarping = new ArrayList<String>(Arrays.asList( new String[]{
			"tell {player_name} Teleporting you to {warp_name} warp..."
		}));
		public static Boolean UnlockIfDeleteWarp = true;
		public static List<String> cmdListAfterWarping = new ArrayList<String>(Arrays.asList( new String[]{
			"give {player_name} {player_iteminhand_name} 1"
		}));
		public static Boolean serviceCharge = false;
	}
	
	public static class message {
		public static String invalidSender = "&5[%plugin_name%]&r &cYou must be a player!";
		public static String invalidCmd = "&5[%plugin_name%]&r &cYour command incorrectly!";
		public static String doesNotHavePerm = "&5[%plugin_name%]&r &cYou don't have permission to use this action!";
		public static String doNotHaveWarp = "&5[%plugin_name%]&r &cUnable to find warp you need!";
		public static String playerNull = "&5[%plugin_name%]&r &cPlayers that you want isn't available or not online!";
		public static String requireName = "&5[%plugin_name%]&r &cYou must enter the name of this warp!";
		public static String worldNull = "&5[%plugin_name%]&r &cCan't find the world of warp you need!";
		public static String warpDidNotCreate = "&5[%plugin_name%]&r &cWarp hasn't been created!";
		public static String warpCreated = "&5[%plugin_name%]&r &cThis warp was created!";
		
		public static String tpWarpSuccess = "&5[%plugin_name%]&r &aTeleported @player to @warp warp successfully ! (Service amount: @money)";
		public static String tpWarpSuccessReTpOther = "you";
		public static String warpCreateSuccess = "&5[%plugin_name%]&r &a@warp warp created successfully!";
		public static String warpDeletedSuccess = "&5[%plugin_name%]&r &a@warp warp deleted successfully!";
		public static String checkWarpSuccess = "&5[%plugin_name%]&r &b@warp warp checked successfully with @module module !";
		public static String warpIsDanger = "&5[%plugin_name%]&r &c@module: This is dangerous warp!";
		public static String warpBlockDangerLength = "&5[%plugin_name%]&r &6@module: @length @type block(s) from the warp!";
		public static String warpIsSafe = "&5[%plugin_name%]&r &a@module: This is a safe warp!";
		public static String editWarpSuccess = "&5[%plugin_name%]&r &6@warp warp edited successfully!";
		public static String warpListMessage = "&5[%plugin_name%]&r &a@size warp(s):";
		public static String warpListEach = "&b@num. @name @status";
		public static String requirePass = "&5[%plugin_name%]&r &cYou must enter the password!";
		public static String warpLocked = "&5[%plugin_name%]&r &cThis warp has been locked!";
		public static String passwordUnSafe = "&5[%plugin_name%]&r &cThe password is unsafe!";
		public static String warpLockSuccess = "&5[%plugin_name%]&r &a@warp warp locked successfully!";
		public static String warpStatusLocked = " &c[LOCKED]";
		public static String warpUnLocked = "&5[%plugin_name%]&r &cThis warp has been unlocked!";
		public static String warpUnLockSuccess = "&5[%plugin_name%]&r &a@warp warp unlocked successfully!";
		public static String tpLockedWarpMessage = "&5[%plugin_name%]&r &b@warp warp has been locked, enter the password to continue:";
		public static String tpLockedWarpWrongPass = "&5[%plugin_name%]&r &cWrong password! Use:&r &6/warp @warp&r &c to retype another password.";
		public static String requireWarpUnLockedAmount = "&5[%plugin_name%]&r &cYou must enter an amount of money paid if the player teleports to an unlocked warp.";
		public static String requireWarpLockedAmount = "&5[%plugin_name%]&r &cYou must enter an amount of money paid if the player teleports to a locked warp.";
		public static String updateMoneySuccess = "&5[%plugin_name%]&r &aUpdate the amount of money successfully!";
	}

	public static class mysql {
		public static String _GOLBAL = "aquawarp_";
		public static class _INFO {
			public static String collate = "utf8_unicode_ci";
			public static String charset = "utf8";
			public static String engine = "InnoDB";
		}
		public static class _CONNECT {
			public static String host = "localHost";
			public static String port = "3306";
			public static String user = "root";
			public static String pass = "";
			public static String dtbs = "minecraft";
		}
	}

	public static class perm {
		public static String _GLOBAL = "aquawarp.*";
		public static String TpWarp = "aquawarp.tpwarp";
		public static String HelpWarp = "aquawarp.helpwarp";
		public static String SetWarp = "aquawarp.setwarp";
		public static String DelWarp = "aquawarp.delwarp";
		public static String CheckWarp = "aquawarp.checkwarp";
		public static String EditWarp = "aquawarp.editwarp";
		public static String ListWarp = "aquawarp.listwarp";
		public static String LockWarp = "aquawarp.lockwarp";
		public static String UnLockWarp = "aquawarp.unlockwarp";
		public static String FeeTp = "aquawarp.feetp";
	}

	public static class effect {
		public static boolean _ENABLE = true;
	}
	
	public static class sound {
		public static boolean _ENABLE = true;
	}
}
