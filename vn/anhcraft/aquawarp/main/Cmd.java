package vn.anhcraft.aquawarp.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.anhcraft.aquawarp.api.Functions;
import vn.anhcraft.aquawarp.command.CheckWarp;
import vn.anhcraft.aquawarp.command.DelWarp;
import vn.anhcraft.aquawarp.command.EditWarp;
import vn.anhcraft.aquawarp.command.FeeTp;
import vn.anhcraft.aquawarp.command.ListWarp;
import vn.anhcraft.aquawarp.command.LockWarp;
import vn.anhcraft.aquawarp.command.SetWarp;
import vn.anhcraft.aquawarp.command.TpWarp;
import vn.anhcraft.aquawarp.command.UnLockWarp;

public class Cmd implements CommandExecutor {
	@SuppressWarnings("unused")
	private final AquaWarp plugin;
	
	public Cmd(AquaWarp plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (cmd.getName().equalsIgnoreCase(Options.cmd.Warp)) {
			if(sender.hasPermission(Options.perm._GLOBAL) ||
			   sender.hasPermission(Options.perm.TpWarp) ||
			   sender.isOp()){
				if (!(sender instanceof Player)) {
					if(1 < args.length){
						TpWarp.run(args[0],args[1],sender,true);
					}
					else if(args.length == 1){
						sender.sendMessage(Options.message.invalidSender);
					}
					else {
						sender.sendMessage(Options.message.requireName);
					}
				} else {				
					if(1 < args.length){
						TpWarp.run(args[0],args[1],sender,true);
					}
					else if(args.length == 1){
						TpWarp.run(args[0],sender.getName(),sender,false);
					}
					else {
						sender.sendMessage(Options.message.requireName);
					}
				}
			} else {
				sender.sendMessage(Options.message.doesNotHavePerm);
			}
		}
		
		/** MOST COMMAND
		 *  
		 *  /warps <sub> <arg...>
		 *  
		**/
		if (cmd.getName().equalsIgnoreCase(Options.cmd.Warps)) {
			if(args.length < 1){
				if(sender.hasPermission(Options.perm._GLOBAL) ||
				   sender.hasPermission(Options.perm.HelpWarp) ||
				   sender.isOp()){
					for(String help : Options.message.help){
						sender.sendMessage(Functions.reword(help));
					}
				} else {
					sender.sendMessage(Options.message.doesNotHavePerm);
				}
			} else {
				
				if(args[0].equals(Options.cmd.SetWarp)){
					if(sender.hasPermission(Options.perm._GLOBAL) ||
					   sender.hasPermission(Options.perm.SetWarp) ||
					   sender.isOp()){
						if(6 < args.length){
							SetWarp.runAdvanced(args[1],args[2],args[3],args[4],args[5],args[6],sender);
						} else {
							if(!(sender instanceof Player)){
								sender.sendMessage(Options.message.invalidSender);
							} else {
								if(args.length < 2){
									sender.sendMessage(Options.message.requireName);
								} else {
									SetWarp.run(args[1], sender);
								}
							}
						}
					} else {
						sender.sendMessage(Options.message.doesNotHavePerm);
					}
				}
				
				else if(args[0].equals(Options.cmd.DelWarp)){
					if(sender.hasPermission(Options.perm._GLOBAL) ||
					   sender.hasPermission(Options.perm.DelWarp) ||
					   sender.isOp()){
						if(args.length < 2){
							sender.sendMessage(Options.message.requireName);
						} else {
							DelWarp.run(args[1], sender);
						}
					} else {
						sender.sendMessage(Options.message.doesNotHavePerm);
					}
				}
				
				else if(args[0].equals(Options.cmd.CheckWarp)){
					if(sender.hasPermission(Options.perm._GLOBAL) ||
					   sender.hasPermission(Options.perm.CheckWarp) ||
					   sender.isOp()){
						if(args.length < 2){
							sender.sendMessage(Options.message.requireName);
						} else {
							CheckWarp.run(args[1], sender, Options.cmd.CheckWarpGroud);
						}
					} else {
						sender.sendMessage(Options.message.doesNotHavePerm);
					}
				}
				
				else if(args[0].equals(Options.cmd.EditWarp)){
					if(sender.hasPermission(Options.perm._GLOBAL) ||
					   sender.hasPermission(Options.perm.EditWarp) ||
					   sender.isOp()){
						if(args.length < 2){
							sender.sendMessage(Options.message.requireName);
						} else {
							EditWarp.run(args[1], sender);
						}
					} else {
						sender.sendMessage(Options.message.doesNotHavePerm);
					}
				}
				
				else if(args[0].equals(Options.cmd.ListWarp)){
					if(sender.hasPermission(Options.perm._GLOBAL) ||
					   sender.hasPermission(Options.perm.ListWarp) ||
					   sender.isOp()){
					   ListWarp.run(sender);
					} else {
						sender.sendMessage(Options.message.doesNotHavePerm);
					}
				}
				
				/** 1.2.0 **/
				else if(args[0].equals(Options.cmd.LockWarp)){
					if(1 < args.length){
						if(2 < args.length){
							if(sender.hasPermission(Options.perm._GLOBAL) ||
							   sender.hasPermission(Options.perm.LockWarp) ||
							   sender.isOp()){
							   LockWarp.run(sender,args[1],args[2]);
							} else {
								sender.sendMessage(Options.message.doesNotHavePerm);
							}
						} else {
							sender.sendMessage(Options.message.requirePass);
						}
				 	} else {
				 		sender.sendMessage(Options.message.requireName);
				 	}
				}
				else if(args[0].equals(Options.cmd.UnLockWarp)){
					if(1 < args.length){
						if(sender.hasPermission(Options.perm._GLOBAL) ||
						   sender.hasPermission(Options.perm.UnLockWarp) ||
						   sender.isOp()){
						   UnLockWarp.run(sender,args[1]);
						} else {
							sender.sendMessage(Options.message.doesNotHavePerm);
						}
				 	} else {
				 		sender.sendMessage(Options.message.requireName);
				 	}
				}
				/**********/
				
				/** 1.2.2 **/
				else if(args[0].equals(Options.cmd.FeeTp)){
					if(1 < args.length){
						if(2 < args.length){
							if(3 < args.length){
								if(sender.hasPermission(Options.perm._GLOBAL) ||
								   sender.hasPermission(Options.perm.FeeTp) ||
								   sender.isOp()){
								   FeeTp.run(sender,args[1],args[2],args[3]);
								} else {
									sender.sendMessage(Options.message.doesNotHavePerm);
								}
							} else {
								sender.sendMessage(Options.message.requireWarpUnLockedAmount);
							}
						} else {
							sender.sendMessage(Options.message.requireWarpLockedAmount);
						}
				 	} else {
				 		sender.sendMessage(Options.message.requireName);
				 	}
				}
				/**********/
				
				else {
					sender.sendMessage(Options.message.invalidCmd);
				}
			}
		}
		return false;
	}
}