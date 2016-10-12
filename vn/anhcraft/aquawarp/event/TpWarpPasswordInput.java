package vn.anhcraft.aquawarp.event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import vn.anhcraft.aquawarp.api.Functions;
import vn.anhcraft.aquawarp.api.MySQLFuncs;
import vn.anhcraft.aquawarp.command.LockWarp;
import vn.anhcraft.aquawarp.main.Cache;
import vn.anhcraft.aquawarp.main.Options;

public class TpWarpPasswordInput implements Listener {
	private static String message;
	private static Player player;
	private static Boolean ready = false;
	private static Boolean isTpOther;
	private static CommandSender sender;
	private static String warp;
	
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e){
		message = e.getMessage();
		if(ready){
			ready = false;
			warp = Functions.reSpecial(warp);
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
							message = Functions.reSpecial(Functions.md5(message));
							if(rs.getString("pass").equals(message)){
								float x = Functions.stringToFloat(Functions.reSpecial(r.getString("x")));
								float y = Functions.stringToFloat(Functions.reSpecial(r.getString("y")));
								float z = Functions.stringToFloat(Functions.reSpecial(r.getString("z")));
								String w = r.getString("world");
								float yaw = Functions.stringToFloat(Functions.reSpecial(r.getString("yaw")));
								World world = Bukkit.getServer().getWorld(w);
								
								Location l = new Location(world, x, y, z);
								l.setYaw(yaw);
								Boolean g = player.teleport(l);
								if(g) {
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
									String message = Options.message.tpWarpSuccess.replaceAll("@warp", warp);
									if(isTpOther){
										message = message.replaceAll("@player", player.getName());
									} else {
										message = message.replaceAll("@player", Options.message.tpWarpSuccessReTpOther);
									}
									sender.sendMessage(message);
								}
							} else {
								sender.sendMessage(Options.message.tpLockedWarpWrongPass.replaceAll("@warp", warp));
							}
						}
					}
				} else {
					sender.sendMessage(Options.message.warpDidNotCreate);
				}
				e.setCancelled(true);				
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		} else {
			e.setCancelled(false);
		}
	}

	public static void main(String warp_, Player p_, CommandSender sender_, Boolean isTpOther_) {
		sender_.sendMessage(Options.message.tpLockedWarpMessage.replaceAll("@warp", warp_));
		ready = true;
		warp = warp_;
		player = p_;
		sender = sender_;
		isTpOther = isTpOther_;
	}
}
