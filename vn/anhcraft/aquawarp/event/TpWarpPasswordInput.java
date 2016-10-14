package vn.anhcraft.aquawarp.event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import net.milkbowl.vault.economy.EconomyResponse;
import vn.anhcraft.aquawarp.api.Functions;
import vn.anhcraft.aquawarp.api.MySQLFuncs;
import vn.anhcraft.aquawarp.command.LockWarp;
import vn.anhcraft.aquawarp.main.AquaWarp;
import vn.anhcraft.aquawarp.main.Cache;
import vn.anhcraft.aquawarp.main.Options;

public class TpWarpPasswordInput implements Listener {
	
	//**** CACHE TPWARP ****
	private static ArrayList<Player> playerx = new ArrayList<Player>();
	private static ArrayList<Boolean> isTpOtherx = new ArrayList<Boolean>();
	private static ArrayList<CommandSender> senderx = new ArrayList<CommandSender>();
	private static ArrayList<String> warpx = new ArrayList<String>();
	
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e){
		Boolean uq = false;
		int uo = -1;
		for(int v = 0; v < playerx.size(); v++){
			Player px = playerx.get(v);
			if(px.getName().equals(e.getPlayer().getName())){
				uq = true;
				uo = v;
			}
		}
		
		
		if(uq){
			String pass = e.getMessage();
			Boolean isTpOther = isTpOtherx.get(uo);
			String warp = Functions.reSpecial(warpx.get(uo));
			CommandSender sender = senderx.get(uo);
			Player player = playerx.get(uo);
			
			ResultSet r = MySQLFuncs.exeTable("SELECT * FROM "+ Options.mysql.Warps +" WHERE name='"+warp+"';");
			Boolean rq;
			try {
				rq = r.next();
				if(rq){
					ResultSet rs = MySQLFuncs.exeTable("SELECT * FROM "+ Options.mysql.Protection +" WHERE name='"+warp+"';");
					Boolean rqs = rs.next();
					if(rqs){
						if(!LockWarp.islocked(warp)){
							sender.sendMessage(Options.message.warpUnLocked);
						} else {
							pass = Functions.reSpecial(Functions.md5(pass));
							if(rs.getString("pass").equals(pass)){
								
								String money = "0";
								ResultSet rd = MySQLFuncs.exeTable("SELECT * FROM "+Options.mysql.FeeTpWarp+" WHERE name='"+warp+"';");
								if (rd.next()) {
									money = rd.getString("lock_money");
									try {
										if(Options.cmd.serviceCharge){
											if(AquaWarp.EcoReady){
												if(Functions.strToDouble(Functions.reSpecial(rd.getString("lock_money")))
													<= AquaWarp.economy.getBalance(((Player) sender).getPlayer())){
													EconomyResponse xc = AquaWarp.economy.withdrawPlayer(((Player) sender).getPlayer(), 
													Functions.strToDouble(Functions.reSpecial(rd.getString("lock_money"))));
										            if(xc.transactionSuccess()) {
										            	gotp(warp, player, r, sender, isTpOther, money);
										            } else {
										            	sender.sendMessage(xc.errorMessage);
										            }
												} else {
													sender.sendMessage(Options.message.lackMoney);
												}
											} else {
												sender.sendMessage(Options.message.requireVault);
											}
										} else {
											gotp(warp, player, r, sender, isTpOther, money);
										}
									} catch (SQLException ev) {
										ev.printStackTrace();
									}
								} else {
									gotp(warp, player, r, sender, isTpOther, money);								
								}
								rd.close();
								
							} else {
								sender.sendMessage(Options.message.tpLockedWarpWrongPass.replaceAll("@warp", warp));
							}
						}
					}
				} else {
					sender.sendMessage(Options.message.warpDidNotCreate);
				}		
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			
			warpx.remove(uo);
			playerx.remove(uo);
			senderx.remove(uo);
			isTpOtherx.remove(uo);
			e.setCancelled(true);
		} else {
			e.setCancelled(false);
		}
	}

	@SuppressWarnings("deprecation")
	private void gotp(String warp, Player player, ResultSet r, CommandSender sender, Boolean isTpOther, String money) {
		try {
			 float x = Functions.stringToFloat(Functions.reSpecial(r.getString("x")));
			 float y = Functions.stringToFloat(Functions.reSpecial(r.getString("y")));
				float z = Functions.stringToFloat(Functions.reSpecial(r.getString("z")));
				String w = r.getString("world");
				float yaw = Functions.stringToFloat(Functions.reSpecial(r.getString("yaw")));
				World world = Bukkit.getServer().getWorld(w);
				
				Location l = new Location(world, x, y, z);
				l.setYaw(yaw);
				ConsoleCommandSender s = Bukkit.getServer().getConsoleSender();
				String[] cmdListBeforeWarping = Options.cmd.cmdListBeforeWarping;
				for(String psc : cmdListBeforeWarping){
					Bukkit.getServer().dispatchCommand(s, psc
						.replace("{warp_name}", warp)
						.replace("{warp_loc_x}", Functions.reSpecial(r.getString("x")))
						.replace("{warp_loc_y}", Functions.reSpecial(r.getString("y")))
						.replace("{warp_loc_z}", Functions.reSpecial(r.getString("z")))
						.replace("{warp_loc_yaw}", Functions.reSpecial(r.getString("yaw")))
						.replace("{warp_loc_world}", w)
						.replace("{player_name}",player.getName())
						.replace("{player_exp}", Float.toString(player.getExp()))
						.replace("{player_level}", Integer.toString(player.getLevel()))
						.replace("{player_iteminhand_name}", player.getItemInHand().getType().name())
					);
				}
				
				Boolean g = player.teleport(l);
				if(g) {
					String[] cmdListAfterWarping = Options.cmd.cmdListAfterWarping;
					for(String psc : cmdListAfterWarping){
						Bukkit.getServer().dispatchCommand(s, psc
							.replace("{warp_name}", warp)
							.replace("{warp_loc_x}", Functions.reSpecial(r.getString("x")))
							.replace("{warp_loc_y}", Functions.reSpecial(r.getString("y")))
							.replace("{warp_loc_z}", Functions.reSpecial(r.getString("z")))
							.replace("{warp_loc_yaw}", Functions.reSpecial(r.getString("yaw")))
							.replace("{warp_loc_world}", w)
							.replace("{player_name}",player.getName())
							.replace("{player_exp}", Float.toString(player.getExp()))
							.replace("{player_level}", Integer.toString(player.getLevel()))
							.replace("{player_iteminhand_name}", player.getItemInHand().getType().name())
						);
					}
				
					Date dt = new Date();
					String xg = "{\n"
							+ "    'player_name': '" + player.getName() + "',\n"
							+ "    'player_x': '" + player.getLocation().getX() + "',\n"
							+ "    'player_y': '" + player.getLocation().getY() + "',\n"
							+ "    'player_z': '" + player.getLocation().getZ() + "',\n"
							+ "    'player_yaw': '" + player.getLocation().getYaw() + "',\n"
							+ "    'player_world': '" + player.getLocation().getWorld().getName() + "',\n"
							+ "\n"
							+ "    'warp_name': '" + warp + "',\n"
							+ "    'warp_x': " + r.getString("x") + ",\n"
							+ "    'warp_y': " + r.getString("y") + ",\n"
							+ "    'warp_z': " + r.getString("z") + ",\n"
							+ "    'warp_yaw': " + r.getString("yaw") + ",\n"
							+ "    'warp_world': '" + w + "',\n"
							+ "    'warp_locked': true,\n"
							+ "\n"
							+ "    'date': '"+ dt.toString() +"'\n"
							+ "}";
					Cache.del("tpwarp_"+player.getUniqueId().toString());
					Cache.set("tpwarp_"+player.getUniqueId().toString(), xg);
					if(Options.sound._ENABLE){
						for(int c = 0; c < 5; c++){
							world.playSound(l, Options.sound.TpWarp, 3.0F, 0.5F);
						}
					}
					if(Options.effect._ENABLE){
						for(int c = 0; c < 20; c++){
							world.playEffect(l, Options.effect.TpWarp, 0);
							world.spawnParticle(Particle.FIREWORKS_SPARK, l, 1, 1, 1, 1, 1);
						}
						l.add(0D, 1D, 0D);
						for(int c = 0; c < 20; c++){
							world.playEffect(l, Options.effect.TpWarp, 0);
							world.spawnParticle(Particle.FIREWORKS_SPARK, l, 0, 0, 0, 0, 1);
						}
					}
					String message = Options.message.tpWarpSuccess.replaceAll("@warp", warp).replace("@money", money);
					if(isTpOther){
						message = message.replaceAll("@player", player.getName());
					} else {
						message = message.replaceAll("@player", Options.message.tpWarpSuccessReTpOther);
					}
					sender.sendMessage(message);
				}
				r.close();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String warp_, Player p_, CommandSender sender_, Boolean isTpOther_) {
		sender_.sendMessage(Options.message.tpLockedWarpMessage.replaceAll("@warp", warp_));
		warpx.add(warp_);
		playerx.add(p_);
		senderx.add(sender_);
		isTpOtherx.add(isTpOther_);
	}
}
