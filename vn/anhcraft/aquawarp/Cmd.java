package vn.anhcraft.aquawarp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.anhcraft.aquawarp.API.Files;
import vn.anhcraft.aquawarp.API.Functions;
import vn.anhcraft.aquawarp.Commands.DelWarp;
import vn.anhcraft.aquawarp.Commands.EditWarp;
import vn.anhcraft.aquawarp.Commands.FeeTp;
import vn.anhcraft.aquawarp.Commands.ListWarp;
import vn.anhcraft.aquawarp.Commands.LockWarp;
import vn.anhcraft.aquawarp.Commands.SetWarp;
import vn.anhcraft.aquawarp.Commands.TpWarp;
import vn.anhcraft.aquawarp.Commands.UnLockWarp;
import vn.anhcraft.aquawarp.GUI.WarpGUI;

public class Cmd implements CommandExecutor {
	private final AquaWarp plugin;
	
	public Cmd(AquaWarp plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("warp")) {
			if (!(sender instanceof Player)) {
				if(1 < args.length){
					TpWarp.run(args[0],args[1],sender,true);
				}
				else if(args.length == 1){
					sender.sendMessage(Functions.Config.gs("invalidSender",
							Options.plugin.dir + Options.files.messages));
				}
				else {
					sender.sendMessage(Functions.Config.gs("requireName", 
							Options.plugin.dir + Options.files.messages));
				}
			} else {
				if(1 < args.length){
					TpWarp.run(args[0],args[1],sender,true);
				}
				else if(args.length == 1){
					TpWarp.run(args[0],sender.getName(),sender,false);
				}
				else {
					sender.sendMessage(Functions.Config.gs("requireName", 
							Options.plugin.dir + Options.files.messages));
				}
			}
		}
		
		/** MOST COMMAND
		 *  
		 *  /warps <sub> <arg...>
		 *  
		**/
		if (cmd.getName().equals("warps")) {
			if(args.length < 1){
				for(String help : Options.message.help){
					sender.sendMessage(Functions.reword(help));
				}
			} else {
				if(args[0].equals("set")){
					if(6 < args.length){
						SetWarp.runAdvanced(args[1],args[2],args[3],args[4],args[5],args[6],sender);
					} else {
						if(!(sender instanceof Player)){
							sender.sendMessage(Functions.Config.gs("invalidSender", 
									Options.plugin.dir + Options.files.messages));
						} else {
							if(args.length < 2){
								sender.sendMessage(Functions.Config.gs("requireName", 
										Options.plugin.dir + Options.files.messages));
							} else {
								SetWarp.run(args[1], sender);
							}
						}
					}
				}
				
				else if(args[0].equals("del")){
					if(args.length < 2){
						sender.sendMessage(Functions.Config.gs("requireName", 
								Options.plugin.dir + Options.files.messages));
					} else {
						DelWarp.run(args[1], sender);
					}
				}
				
				else if(args[0].equals("edit")){
					if(sender instanceof Player){
						if(args.length < 2){
							sender.sendMessage(Functions.Config.gs("requireName", 
							Options.plugin.dir + Options.files.messages));
						} else {
							EditWarp.run(args[1], sender);
						}
					} else {
						sender.sendMessage(Functions.Config.gs("invalidSender", 
								Options.plugin.dir + Options.files.messages));
					}
				}
				
				else if(args[0].equals("list")){
					ListWarp.run(sender);
				}
				
				/** 1.2.0 **/
				else if(args[0].equals("lock")){
					if(1 < args.length){
						if(2 < args.length){
							LockWarp.run(sender,args[1],args[2]);
						} else {
							sender.sendMessage(Functions.Config.gs("requirePass", 
									Options.plugin.dir + Options.files.messages));
						}
				 	} else {
				 		sender.sendMessage(Functions.Config.gs("requireName", 
								Options.plugin.dir + Options.files.messages));
				 	}
				}
				else if(args[0].equals("unlock")){
					if(1 < args.length){
						UnLockWarp.run(sender,args[1]);
				 	} else {
				 		sender.sendMessage(Functions.Config.gs("requireName", 
								Options.plugin.dir + Options.files.messages));
				 	}
				}
				/**********/
				
				/** 1.2.2 **/
				else if(args[0].equals("feetp")){
					if(1 < args.length){
						if(2 < args.length){
							if(3 < args.length){
								FeeTp.run(sender,args[1],args[2],args[3]);
							} else {
								sender.sendMessage(Functions.Config.gs("requireWarpUnLockedAmount", 
										Options.plugin.dir + Options.files.messages));
							}
						} else {
							sender.sendMessage(Functions.Config.gs("requireWarpLockedAmount", 
									Options.plugin.dir + Options.files.messages));
						}
				 	} else {
				 		sender.sendMessage(Functions.Config.gs("requireName", 
								Options.plugin.dir + Options.files.messages));
				 	}
				}
				/**********/
				
				/** 1.3.0 **/
				else if(args[0].equals("gui")){
					if(sender instanceof Player){
						WarpGUI.register(sender);
					} else {
						sender.sendMessage(Functions.Config.gs("invalidSender", 
							Options.plugin.dir + Options.files.messages));
					}
				}
				/**********/
				
				else {
					sender.sendMessage(Functions.Config.gs("invalidCmd",
							Options.plugin.dir + Options.files.messages));
				}
			}
		}
		
		/**
		 * 1.3.0
		 * Warp Admin
		 */
		if (cmd.getName().equals("warpadmin") ||
			cmd.getName().equals("aquawarp")) {
			/** 1.3.0 **/
			if(0 < args.length){
				if(args[0].equals("reload")){
					if(sender.hasPermission("aquawarp.admin.reload") ||
					   sender.hasPermission("aquawarp.*") ||
					   sender.isOp()){
						Files.reload(plugin);
						Files.reload(plugin);
						sender.sendMessage(Functions.Config.gs("reloadSuccess",
						Options.plugin.dir + Options.files.messages));
						
					} else {
						sender.sendMessage(Functions.Config.gs("senderNotHavePerm",
								Options.plugin.dir + Options.files.messages));
					}
				} else {
					sender.sendMessage(Functions.Config.gs("invalidCmd",
							Options.plugin.dir + Options.files.messages));
				}
			}
			
			else {
				sender.sendMessage("Use: /warps");
			}
		}
		
		return false;
	}
}
