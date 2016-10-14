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
import org.bukkit.command.ConsoleCommandSender;
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
	
	@SuppressWarnings("deprecation")
	public static void run(String warp, String player, CommandSender sender, Boolean isTpOther){
		warp = Functions.reSpecial(warp);
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
			    		+ "SELECT * FROM "+Options.mysql.Warps+" WHERE name='"+warp+"';");
				Boolean i = r.next();
				
				if(i){
					float x = Functions.stringToFloat(Functions.reSpecial(r.getString("x")));
					float y = Functions.stringToFloat(Functions.reSpecial(r.getString("y")));
					float z = Functions.stringToFloat(Functions.reSpecial(r.getString("z")));
					String w = Functions.reSpecial(r.getString("world"));
					float yaw = Functions.stringToFloat(Functions.reSpecial(r.getString("yaw")));
					World world = Bukkit.getServer().getWorld(w);
					if(world != null){
						if(!LockWarp.islocked(warp) || !(sender instanceof Player)){
							String money = "0";
							ResultSet rd = MySQLFuncs.exeTable("SELECT * FROM "+Options.mysql.FeeTpWarp+" WHERE name='"+warp+"';");
							if (rd.next()) {
								money = rd.getString("unlock_money");
							}
							rd.close();
							
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
									.replace("{player_name}", p.getName())
									.replace("{player_exp}", Float.toString(p.getExp()))
									.replace("{player_level}", Integer.toString(p.getLevel()))
									.replace("{player_iteminhand_name}", p.getItemInHand().getType().name())
								);
							}
							
							Boolean g = p.teleport(l);
							
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
										.replace("{player_name}", p.getName())
										.replace("{player_exp}", Float.toString(p.getExp()))
										.replace("{player_level}", Integer.toString(p.getLevel()))
										.replace("{player_iteminhand_name}", p.getItemInHand().getType().name())
									);
								}
								
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
								String message = Options.message.tpWarpSuccess.replace("@warp", warp).replace("@money", money);
								if(isTpOther){
									message = message.replace("@player", player);
								} else {
									message = message.replace("@player", Options.message.tpWarpSuccessReTpOther);
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
