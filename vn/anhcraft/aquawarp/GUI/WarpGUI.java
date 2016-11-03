package vn.anhcraft.aquawarp.GUI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.API.Functions;
import vn.anhcraft.aquawarp.API.MySQLFuncs;

public class WarpGUI {
	public static int warpguinvrow = 27; // 6 row (tối thiểu: 18)
	
	// CACHE
	public static ArrayList<String> pname = new ArrayList<String>();
	// vị trí bắt đầu (+warpguinvrow)
	public static ArrayList<Integer> plimit = new ArrayList<Integer>();
	//***
	
	public static Inventory warpguinv = Bukkit.createInventory(
			null, 
			warpguinvrow,
			Functions.reword("&5-=[&r&2WarpGui&r&5]=-&r"));
	
	public static void register(CommandSender sender) {
		Player p = (Player) sender;
		if(p.hasPermission("aquawarp.guiwarp") ||
		   p.hasPermission("aquawarp.*") ||
		   p.isOp()){
			
			int ps = -1;
			if(0 < pname.size()){
				for(int h = 0; h < pname.size();){
					if(pname.get(h).equals(p.getName())){
						ps = h;
						break;
					} 
					else if(h == (pname.size()-1)){
						pname.add(p.getName());
						plimit.add(0);
						ps = h;
					}
					else {
						continue;
					}
					
				}
			} else {
				pname.add(p.getName());
				plimit.add(0);
				ps = 0;
			}
			
			try {
				warpguinv.clear();
				// trừ 9 để bỏ hàng cuối cùng là footer
				ResultSet r = MySQLFuncs.exeq(""
						+ "SELECT * FROM `"+Options.plugin.mysql._Warps+"` "
							+ "LIMIT "+plimit.get(ps)+", "
							+ ""+(plimit.get(ps)+warpguinvrow-9)+";");
				
				
				int z = 0;
				while (r.next()){
					if(z < (warpguinvrow-9)){
						ItemStack a = new ItemStack(Material.MAP, 1);
						ItemMeta k = a.getItemMeta();
						k.setDisplayName("§a"+r.getString("name"));
						List<String> s = Functions.Config.gals("warpGUI.warpLore", Options.plugin.dir + Options.files.config);
						List<String> s_ = new ArrayList<String>();
						for(String sp : s){
							s_.add(Functions.reword(sp.replace("@warp", r.getString("name"))));
						}
						k.setLore(s_);
						k.addEnchant(Enchantment.LUCK, 1000, true);
						a.setItemMeta(k);
						warpguinv.setItem(z, a);
						z += 1;
						continue;
					} else {
						break;
					}
				}
				for(int y = 0; y < (warpguinvrow-z-9); y++){
					warpguinv.setItem(y+z, new ItemStack(Material.PAPER, 1));
				}
				
				ItemStack xa = new ItemStack(Material.DIAMOND, 1);
				ItemMeta na = xa.getItemMeta();
				na.setDisplayName("§6<--");
				xa.setItemMeta(na);
				warpguinv.setItem(warpguinvrow-9, xa);
				
				ItemStack xb = new ItemStack(Material.DIAMOND, 1);
				ItemMeta nb = xb.getItemMeta();
				nb.setDisplayName("§6-->");
				xb.setItemMeta(nb);
				warpguinv.setItem(warpguinvrow-1, xb);
				
				for(int y = 0; y < 7; y++){
					if(y == 3){
						ItemStack xas = new ItemStack(Material.THIN_GLASS, 1);
						ItemMeta nas = xas.getItemMeta();
						nas.setDisplayName("§c"+Integer.toString(plimit.get(ps)));
						xas.setItemMeta(nas);
						warpguinv.setItem(warpguinvrow-y-2, xas);
					} else {
						warpguinv.setItem(warpguinvrow-y-2, new ItemStack(Material.THIN_GLASS, 1));
					}
				}
				
				r.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			p.openInventory(warpguinv);
		} else {
			p.sendMessage(Functions.Config.gs("doesNotHavePerm",
					Options.plugin.dir + Options.files.messages));
		}
	}
}
