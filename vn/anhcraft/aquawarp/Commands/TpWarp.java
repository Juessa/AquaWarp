package vn.anhcraft.aquawarp.Commands;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import net.milkbowl.vault.economy.EconomyResponse;
import vn.anhcraft.aquawarp.AquaWarp;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.API.Functions;
import vn.anhcraft.aquawarp.API.MySQLFuncs;
import vn.anhcraft.aquawarp.Listeners.TpWarpPasswordInput;

public class TpWarp {
	@SuppressWarnings("deprecation")
	public static void tp(String warp, String player, CommandSender sender, Boolean isTpOther){
		// kiểm tra người chơi này có on ko
		Player p = Bukkit.getServer().getPlayerExact(player);
		Boolean m = false;
		for(Player x : Bukkit.getServer().getOnlinePlayers()) {
			if(x.getName().equals(player)){
				m = true;
			}
		}
		if(m || !(sender instanceof Player)){
			try {
				// kết nối database
				ResultSet r = MySQLFuncs.exeq("SELECT * FROM "+Options.plugin.mysql._Warps+" WHERE name='"+warp+"';");
				Boolean i = r.next();
				
				if(i){
					// chuyển vị trí từ str sang float
					float x = Math.round(Functions.strToFloat(Functions.reSpecial(r.getString("x"))));
					float y = Math.round(Functions.strToFloat(Functions.reSpecial(r.getString("y"))));
					float z = Math.round(Functions.strToFloat(Functions.reSpecial(r.getString("z"))));
					String w = Functions.reSpecial(r.getString("world"));
					float yaw = Math.round(Functions.strToFloat(Functions.reSpecial(r.getString("yaw"))));
					World world = Bukkit.getServer().getWorld(w);
					
					//  kiểm tra world có tồn tại ko
					if(world != null){
						
						// warp chưa bị khóa hoặc sender là Console ([+] Console ko cần nhập mật khẩu)
						if(!LockWarp.islocked(warp) || 
						   !(sender instanceof Player)){
							// lấy số tiền phải trả
							String money = Integer.toString(Functions.Config.gi(
									"tpWarp.defaultServiceCost", Options.plugin.dir + Options.files.config));
							ResultSet rd = MySQLFuncs.exeq("SELECT * FROM "+Options.plugin.mysql._FeeTpWarp+" WHERE name='"+warp+"';");					
							if (rd.next()) {
								money = Functions.reSpecial(rd.getString("lock_money"));
							}
							
							Location l = new Location(world, x, y, z);
							l.setYaw(yaw);
							ConsoleCommandSender s = Bukkit.getServer().getConsoleSender();
							
							// lệnh trước khi tp
							String[] cmdListBeforeWarping = Functions.Config.gals(
									"tpWarp.exeCmdBeforeWarping", Options.plugin.dir + Options.files.config)
									.stream().toArray(String[]::new);
							for(String psc : cmdListBeforeWarping){
								Bukkit.getServer().dispatchCommand(s, psc
									.replace("{warp_name}", warp)
									.replace("{warp_loc_x}", Functions.reSpecial(r.getString("x")))
									.replace("{warp_loc_y}", Functions.reSpecial(r.getString("y")))
									.replace("{warp_loc_z}", Functions.reSpecial(r.getString("z")))
									.replace("{warp_loc_yaw}", Functions.reSpecial(r.getString("yaw")))
									.replace("{warp_loc_world}", w)
									.replace("{player_name}", p.getName())
									.replace("{player_exp}", Float.toString(p.getExp()))
									.replace("{player_level}", Integer.toString(p.getLevel()))
									.replace("{player_iteminhand_name}", p.getItemInHand().getType().name())
								);
							}
							
							Boolean canTp = true;
							
							if(Functions.Config.gb("tpWarp.serviceCharge",
									Options.plugin.dir + Options.files.config) &&
									sender instanceof Player){
								if(Functions.strToDouble(money)
									<= AquaWarp.economy.getBalance(((Player) sender).getPlayer())){
									
									EconomyResponse xc = AquaWarp.economy.withdrawPlayer(((Player) sender).getPlayer(), 
									Functions.strToDouble(money));
									
						            if(!xc.transactionSuccess()) {
						            	sender.sendMessage(xc.errorMessage);
						            	canTp = false;
						            }
								} else {
									sender.sendMessage(Functions.Config.gs("lackMoney", 
											Options.plugin.dir + Options.files.messages));
									canTp = false;
								}
							}
							
							// energy
							if(Functions.Config.gb("tpWarp.energy.enable",
									Options.plugin.dir + Options.files.config) &&
									sender instanceof Player && canTp){
								String uuid = ((Player) sender).getUniqueId().toString();
								int a = Functions.Config.gi("tpWarp.energy.energyNeededWarpUnLocked",
										Options.plugin.dir + Options.files.config);
								int b = Functions.Config.gi("tpWarp.energy.energyConsumptionWarpUnLocked",
										Options.plugin.dir + Options.files.config);
								if(a <= Functions.Energy.get(uuid)){
									Functions.Energy.set(uuid, Functions.Energy.get(uuid)-b);
									((Player) sender).sendMessage(
											Functions.Config.gs("lostEnergy", Options.plugin.dir + Options.files.messages)
											.replace("@lostEnergy", Integer.toString(b))
											.replace("@maxEnergy", Integer.toString(Functions.Config.gi("tpWarp.energy.maxEnergy",
											   Options.plugin.dir + Options.files.config)))
											.replace("@energy", Integer.toString(Functions.Energy.get(uuid))));
								} else {
									((Player) sender).sendMessage(
											Functions.Config.gs("haveNotEnoughEnergyRequire", Options.plugin.dir + Options.files.messages)
											.replace("@energyRequire", Integer.toString(a))
											.replace("@energy", Integer.toString(Functions.Energy.get(uuid))));
									canTp = false;
								}
							}
							//--------------
							
							if(canTp){
								Boolean g = p.teleport(l);
								if(g) {
									// lệnh sau khi tp
									String[] cmdListAfterWarping = Functions.Config.gals(
											"tpWarp.exeCmdAfterWarping", Options.plugin.dir + Options.files.config)
											.stream().toArray(String[]::new);
									for(String psc : cmdListAfterWarping){
										Bukkit.getServer().dispatchCommand(s, psc
											.replace("{warp_name}", warp)
											.replace("{warp_loc_x}", Functions.reSpecial(r.getString("x")))
											.replace("{warp_loc_y}", Functions.reSpecial(r.getString("y")))
											.replace("{warp_loc_z}", Functions.reSpecial(r.getString("z")))
											.replace("{warp_loc_yaw}", Functions.reSpecial(r.getString("yaw")))
											.replace("{warp_loc_world}", w)
											.replace("{player_name}", p.getName())
											.replace("{player_exp}", Float.toString(p.getExp()))
											.replace("{player_level}", Integer.toString(p.getLevel()))
											.replace("{player_iteminhand_name}", p.getItemInHand().getType().name())
										);
									}
									
									// nhạc
									if(Functions.Config.gb("tpWarp.sound", 
											Options.plugin.dir + Options.files.config)){
										for(int c = 0; c < 5; c++){
											world.playSound(l, Sound.ENTITY_ENDERMEN_TELEPORT, 3.0F, 0.5F);
										}
									}
									
									// hiệu ứng
									if(Functions.Config.gb("tpWarp.effect", 
											Options.plugin.dir + Options.files.config)){
										for(int c = 0; c < 20; c++){
											world.playEffect(l, Effect.ENDER_SIGNAL, 0);
										}
										l.add(0D, 1D, 0D);
										for(int c = 0; c < 20; c++){
											world.playEffect(l, Effect.ENDER_SIGNAL, 0);
										}
									}
									// thông báo
									String message = (Functions.Config.gs(
											"tpWarpSuccess", Options.plugin.dir + Options.files.messages))
											.replace("@warp", warp)
											.replace("@money", money);
									if(isTpOther){
										message = message.replace("@player", player);
									} else {
										message = message.replace("@player", Functions.Config.gs(
											"tpWarpSuccessReTpOther", Options.plugin.dir + Options.files.messages));
									}
									
									sender.sendMessage(message);
								}
								
								rd.close();
							}
						} else {
							TpWarpPasswordInput.main(warp, p, sender, isTpOther);
						}
					} else {
						sender.sendMessage(Functions.Config.gs(
							"worldNull", Options.plugin.dir + Options.files.messages));
					}
					r.close();
				} else {
					sender.sendMessage(Functions.Config.gs("doNotHaveWarp", Options.plugin.dir + Options.files.messages));
				}
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
		} else {
			sender.sendMessage(Functions.Config.gs("playerNull", Options.plugin.dir + Options.files.messages));
		}
	}
	
	public static void run(String warp, String player, CommandSender sender, Boolean isTpOther){
		// bỏ kí tự đặc biệt
		warp = Functions.reSpecial(warp);
		// tự dịch chuyển
		if( (sender.hasPermission("aquawarp.tpwarp") ||
			sender.hasPermission("aquawarp.tpwarp.*") ||
			sender.hasPermission("aquawarp.tpwarp."+warp) ||
			sender.hasPermission("aquawarp.guiwarp") ||
			sender.hasPermission("aquawarp.tpwarpmulti") ||
			sender.hasPermission("aquawarp.tpwarpmulti.*") ||
			sender.hasPermission("aquawarp.tpwarpmulti."+warp) ||
			sender.hasPermission("aquawarp.signwarp.use") ||
			sender.hasPermission("aquawarp.*") ||
			sender.isOp()) && !isTpOther
		){
			tp(warp, player, sender, false);
		} // dịch chuyển người khác
		else if( (sender.hasPermission("aquawarp.tpwarpmulti") ||
			sender.hasPermission("aquawarp.tpwarpmulti.*") ||
			sender.hasPermission("aquawarp.tpwarpmulti."+warp) ||
			sender.hasPermission("aquawarp.*") ||
			sender.isOp()) && isTpOther
		){
			tp(warp, player, sender, true);
		} else {
			sender.sendMessage(Functions.Config.gs("doesNotHavePerm", Options.plugin.dir + Options.files.messages));
		}
	}
}
