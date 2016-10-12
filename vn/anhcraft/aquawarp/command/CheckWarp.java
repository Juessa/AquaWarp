package vn.anhcraft.aquawarp.command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import vn.anhcraft.aquawarp.api.Functions;
import vn.anhcraft.aquawarp.api.MySQL;
import vn.anhcraft.aquawarp.main.Options;

public class CheckWarp {
	static MySQL MySQL = new MySQL(
    Options.mysql._CONNECT.host,
    Options.mysql._CONNECT.port,
    Options.mysql._CONNECT.dtbs,
    Options.mysql._CONNECT.user,
    Options.mysql._CONNECT.pass);
	Connection c = null;
	
	public static void run(String warp, CommandSender sender, int j) {
		try {
			Statement statement = MySQL.openConnection().createStatement();
			// kiểm tra và tạo bảng
			ResultSet r = statement.executeQuery(""
		    		+ "SELECT * FROM "+Options.mysql.Warps+" WHERE name='"+Functions.reSpecial(warp)+"';");
			Boolean i = r.next();
			
			if(i){
				float x_ = Functions.stringToFloat(Functions.reSpecial(r.getString("x")));
				float y_ = Functions.stringToFloat(Functions.reSpecial(r.getString("y")));
				float z_ = Functions.stringToFloat(Functions.reSpecial(r.getString("z")));
				// chuyển float -> double -> int (vd: 3.14F -> 3.14 -> 3)
				int x = Functions.doubleToInt((double) x_);
				int y = Functions.doubleToInt((double) y_);
				int z = Functions.doubleToInt((double) z_);
				String w = r.getString("world");
				World world = Bukkit.getServer().getWorld(w);
				
				if(world != null){
					// giảm độ cao để lấy block ngay dưới chân player
					Block b = world.getBlockAt(x, y-1, z);
					SetupCheckWarp(b,sender,j,warp);
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
	}

	private static void SetupCheckWarp(Block b, CommandSender sender, int j, String warp) {
		Module_Air(b, sender, j, warp);
		Module_Lava(b, sender, j, warp);
	}

	private static void Module_Lava(Block spawn, CommandSender sender, int to, String w) {
		Material type = spawn.getType();
		int len = 0;
		sender.sendMessage(Options.message.checkWarpSuccess.replaceAll("@warp", w).replaceAll("@module", "Module_Lava"));
		if(type.equals(Material.LAVA)){
			Block q = spawn;
			len = 1;
			for(int i = 0; i < to; i++){
				Block io = q.getRelative(BlockFace.DOWN);
				if(io.getType().equals(Material.LAVA)){
					q = io;
					len += 1;
				} else {
					q = spawn;
					break;
				}
			}
			for(int i = 0; i < to; i++){
				Block io = q.getRelative(BlockFace.UP);
				if(io.getType().equals(Material.LAVA)){
					q = io;
					len += 1;
				} else {
					q = spawn;
					break;
				}
			}
		} else {}
		
		sender.sendMessage(Options.message.warpBlockDangerLength.replaceAll("@type", "lava").replaceAll("@warp", w).replaceAll("@module", "Module_Lava").replaceAll("@length", Integer.toString(len)));
		if(Options.cmd.CheckWarpMaxSafe < len){
			sender.sendMessage(Options.message.warpIsDanger.replaceAll("@warp", w).replaceAll("@module", "Module_Lava"));
		} else {
			sender.sendMessage(Options.message.warpIsSafe.replaceAll("@warp", w).replaceAll("@module", "Module_Lava"));
		}
	}

	private static void Module_Air(Block spawn, CommandSender sender, int to, String w) {
		Material type = spawn.getType();
		int len = 0;
		sender.sendMessage(Options.message.checkWarpSuccess.replaceAll("@warp", w).replaceAll("@module", "Module_Air"));
		if(type.equals(Material.AIR)){
			Block q = spawn;
			len = 1;
			for(int i = 0; i < to; i++){
				Block io = q.getRelative(BlockFace.DOWN);
				if(io.getType().equals(Material.AIR)){
					q = io;
					len += 1;
				} else {
					break;
				}
			}
		} else {}
		
		sender.sendMessage(Options.message.warpBlockDangerLength.replaceAll("@type", "air").replaceAll("@warp", w).replaceAll("@module", "Module_Air").replaceAll("@length", Integer.toString(len)));
		if(Options.cmd.CheckWarpMaxSafe < len){
			sender.sendMessage(Options.message.warpIsDanger.replaceAll("@warp", w).replaceAll("@module", "Module_Air"));
		} else {
			sender.sendMessage(Options.message.warpIsSafe.replaceAll("@warp", w).replaceAll("@module", "Module_Air"));
		}
	}
}