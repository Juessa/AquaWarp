package vn.anhcraft.aquawarp.Listeners;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import net.milkbowl.vault.economy.EconomyResponse;
import vn.anhcraft.aquawarp.AquaWarp;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.API.Functions;
import vn.anhcraft.aquawarp.API.MySQLFuncs;
import vn.anhcraft.aquawarp.Commands.LockWarp;

public class TpWarpPasswordInput implements Listener {
	
	//**** CACHE TPWARP ****
	private static ArrayList<Player> playerx = new ArrayList<Player>();
	private static ArrayList<Boolean> isTpOtherx = new ArrayList<Boolean>();
	private static ArrayList<CommandSender> senderx = new ArrayList<CommandSender>();
	private static ArrayList<String> warpx = new ArrayList<String>();
		
	public static void main(String warp_, Player p_, CommandSender sender_, Boolean isTpOther_) {
		sender_.sendMessage(
			(Functions.Config.gs("tpLockedWarpMessage", Options.plugin.dir + Options.files.messages))
			.replace("@warp", warp_)
		);
		// thêm người chơi vào cache
		warpx.add(warp_);
		playerx.add(p_);
		senderx.add(sender_);
		isTpOtherx.add(isTpOther_);
	}

	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e){
		// kiểm tra người chơi có trong cache ko
		Boolean uq = false;
		int uo = -1;
		for(int v = 0; v < playerx.size(); v++){
			Player px = playerx.get(v);
			if(px.getName().equals(e.getPlayer().getName())){
				uq = true;
				uo = v;
				break;
			}
		}
		
		
		if(uq){
			// lấy thông tin
			String pass = e.getMessage();
			Boolean isTpOther = isTpOtherx.get(uo);
			String warp = Functions.reSpecial(warpx.get(uo));
			CommandSender sender = senderx.get(uo);
			Player player = playerx.get(uo);
			
			ResultSet r = MySQLFuncs.exeq("SELECT * FROM "+ Options.plugin.mysql._Warps +" WHERE name='"+warp+"';");
			Boolean rq;
			try {
				rq = r.next();
				if(rq){
					ResultSet rs = MySQLFuncs.exeq("SELECT * FROM "+ Options.plugin.mysql._Protection +" WHERE name='"+warp+"';");
					Boolean rqs = rs.next();
					if(rqs){
						// kiểm tra đã khóa chưa
						if(!LockWarp.islocked(warp)){
							sender.sendMessage(Functions.Config.gs("warpUnLocked", 
									Options.plugin.dir + Options.files.messages));
						} else {
							// mã hóa pass
							pass = Functions.reSpecial(Functions.hash(pass));
							if(rs.getString("pass").equals(pass)){
								// trừ tiền
								String money = Integer.toString(Functions.Config.gi(
										"tpWarp.defaultServiceCost", Options.plugin.dir + Options.files.config));
								ResultSet rd = MySQLFuncs.exeq("SELECT * FROM "+Options.plugin.mysql._FeeTpWarp+" WHERE name='"+warp+"';");
								if (rd.next()) {
									money = Functions.reSpecial(rd.getString("lock_money"));
								}
								rd.close();
								
								gotp(warp, player, r, sender, isTpOther, money);					
							} else {
								sender.sendMessage(Functions.Config.gs("tpLockedWarpWrongPass", 
										Options.plugin.dir + Options.files.messages)
										.replace("@warp", warp));
							}
						}
					}
				} else {
					sender.sendMessage(Functions.Config.gs("warpDidNotCreate", 
						Options.plugin.dir + Options.files.messages));
				}		
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			
			// hủy cache
			warpx.remove(uo);
			playerx.remove(uo);
			senderx.remove(uo);
			isTpOtherx.remove(uo);
			e.setCancelled(true);
			try {
				r.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else {
			e.setCancelled(false);
		}
	}

	@SuppressWarnings("deprecation")
	private void gotp(String warp, Player player, ResultSet r, CommandSender sender, Boolean isTpOther, String money) {
		try {
			// chuyển vị trí từ str sang float
			float x = Math.round(Functions.strToFloat(Functions.reSpecial(r.getString("x"))));
			float y = Math.round(Functions.strToFloat(Functions.reSpecial(r.getString("y"))));
			float z = Math.round(Functions.strToFloat(Functions.reSpecial(r.getString("z"))));
			String w = Functions.reSpecial(r.getString("world"));
			float yaw = Math.round(Functions.strToFloat(Functions.reSpecial(r.getString("yaw"))));
			World world = Bukkit.getServer().getWorld(w);
			
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
					.replace("{player_name}", player.getName())
					.replace("{player_exp}", Float.toString(player.getExp()))
					.replace("{player_level}", Integer.toString(player.getLevel()))
					.replace("{player_iteminhand_name}", player.getItemInHand().getType().name())
				);
			}
			
			Boolean canTp = true;
			
			if(Functions.Config.gb("tpWarp.serviceCharge",
					Options.plugin.dir + Options.files.config) &&
					sender instanceof Player){
				if(Functions.strToDouble(money)
					<= AquaWarp.economy.getBalance(((Player) sender).getPlayer())){
					
					EconomyResponse xc = AquaWarp.economy.withdrawPlayer(
					((Player) sender).getPlayer(), 
					Functions.strToDouble(money));
		            if(!xc.transactionSuccess()){
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
				int a = Functions.Config.gi("tpWarp.energy.energyNeededWarpLocked",
						Options.plugin.dir + Options.files.config);
				int b = Functions.Config.gi("tpWarp.energy.energyConsumptionWarpLocked",
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
				Boolean g = player.teleport(l);
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
							.replace("{player_name}", player.getName())
							.replace("{player_exp}", Float.toString(player.getExp()))
							.replace("{player_level}", Integer.toString(player.getLevel()))
							.replace("{player_iteminhand_name}", player.getItemInHand().getType().name())
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
						message = message.replace("@player", player.getName());
					} else {
						message = message.replace("@player", Functions.Config.gs(
								"tpWarpSuccessReTpOther", Options.plugin.dir + Options.files.messages));
					}
					sender.sendMessage(message);
				}
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
}
