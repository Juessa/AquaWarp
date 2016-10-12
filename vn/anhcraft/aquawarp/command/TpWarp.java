package vn.anhcraft.aquawarp.command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.anhcraft.aquawarp.api.Functions;
import vn.anhcraft.aquawarp.api.MySQL;
import vn.anhcraft.aquawarp.api.MySQLFuncs;
import vn.anhcraft.aquawarp.event.TpWarpPasswordInput;
import vn.anhcraft.aquawarp.main.Cache;
import vn.anhcraft.aquawarp.main.Options;

public class TpWarp {
	static MySQL MySQL = new MySQL(
		    Options.mysql._CONNECT.host,
		    Options.mysql._CONNECT.port,
		    Options.mysql._CONNECT.dtbs,
		    Options.mysql._CONNECT.user,
		    Options.mysql._CONNECT.pass);
		 Connection c = null;
	
	public static void run(String warp, String player, CommandSender sender, Boolean isTpOther){
		Player p = Bukkit.getServer().getPlayerExact(player);
		Boolean m = false;
		for(Player x : Bukkit.getServer().getOnlinePlayers()) {
			if(x.getName().equals(player)){
				m = true;
			}
		}
		if(m){
			try {
				MySQLFuncs.setup();
				Statement statement = MySQL.openConnection().createStatement();
				// kiểm tra và tạo bảng
				ResultSet r = statement.executeQuery(""
			    		+ "SELECT * FROM "+Options.mysql.Warps+" WHERE name='"+Functions.reSpecial(warp)+"';");
				Boolean i = r.next();
				
				if(i){
					float x = Functions.stringToFloat(Functions.reSpecial(r.getString("x")));
					float y = Functions.stringToFloat(Functions.reSpecial(r.getString("y")));
					float z = Functions.stringToFloat(Functions.reSpecial(r.getString("z")));
					String w = r.getString("world");
					float yaw = Functions.stringToFloat(Functions.reSpecial(r.getString("yaw")));
					World world = Bukkit.getServer().getWorld(w);
					if(world != null){
						if(!LockWarp.islocked(warp)){	
							Location l = new Location(world, x, y, z);
							l.setYaw(yaw);
							
							Boolean g = p.teleport(l);
							
							if(g) {
								Date dt = new Date();
								String xg = "{\n"
										+ "    'player_name': '" + p.getName() + "',\n"
										+ "    'player_x': '" + p.getLocation().getX() + "',\n"
										+ "    'player_y': '" + p.getLocation().getY() + "',\n"
										+ "    'player_z': '" + p.getLocation().getZ() + "',\n"
										+ "    'player_yaw': '" + p.getLocation().getYaw() + "',\n"
										+ "    'player_world': '" + p.getLocation().getWorld().getName() + "',\n"
										+ "\n"
										+ "    'warp_name': '" + warp + "',\n"
										+ "    'warp_x': " + r.getString("x") + ",\n"
										+ "    'warp_y': " + r.getString("y") + ",\n"
										+ "    'warp_z': " + r.getString("z") + ",\n"
										+ "    'warp_yaw': " + r.getString("yaw") + ",\n"
										+ "    'warp_world': '" + w + "',\n"
										+ "    'warp_locked': false,\n"
										+ "\n"
										+ "    'date': '"+ dt.toString() +"'\n"
										+ "}";
								Cache.del("tpwarp_"+p.getUniqueId().toString());
								Cache.set("tpwarp_"+p.getUniqueId().toString(), xg);
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
									message = message.replaceAll("@player", player);
								} else {
									message = message.replaceAll("@player", Options.message.tpWarpSuccessReTpOther);
								}
								sender.sendMessage(message);
							}
						} else {
							TpWarpPasswordInput.main(warp, p, sender, isTpOther);
						}
					} else {
						sender.sendMessage(Options.message.worldNull);
					}
				} else {
					sender.sendMessage(Options.message.doNotHaveWarp);
				}
				r.close();
				statement.close();
			} catch (SQLException | ClassNotFoundException e) {
	            e.printStackTrace();
	        }
		} else {
			sender.sendMessage(Options.message.playerNull);
		}
	}
}
