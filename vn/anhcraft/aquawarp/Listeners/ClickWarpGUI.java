package vn.anhcraft.aquawarp.Listeners;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import vn.anhcraft.aquawarp.AquaWarp;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.API.Functions;
import vn.anhcraft.aquawarp.API.MySQLFuncs;
import vn.anhcraft.aquawarp.Commands.TpWarp;
import vn.anhcraft.aquawarp.GUI.WarpGUI;

public class ClickWarpGUI implements Listener {
	@EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
	    Player p = (Player) event.getWhoClicked();
	    if(p.hasPermission("aquawarp.guiwarp") ||
	 	   p.hasPermission("aquawarp.*") ||
	 	   p.isOp()){
		    ItemStack c = event.getCurrentItem();
		    Inventory inv = event.getClickedInventory();
		    
		    if(c != null && c.getType() != Material.AIR){
		    	if(inv.equals(WarpGUI.warpguinv)){
			    	event.setCancelled(true);
			    	
			    	int ps = -1;
					for(int h = 0; h < WarpGUI.pname.size(); h++){
						if(WarpGUI.pname.get(h).equals(p.getName())){
							ps = h;
							break;
						}
						// giả sử đến kết quả cuối ko có
						else if (h == (WarpGUI.pname.size()-1) && !WarpGUI.pname.get(h).equals(p.getName())) {
							WarpGUI.pname.add(p.getName());
							WarpGUI.plimit.add(0);
						} else {
							continue;
						}
					}
			    	
			    	if(c.getType().equals(Material.MAP)){
			    		ItemMeta k = c.getItemMeta();
			    		try {
			    			String wn = k.getDisplayName().replaceFirst("§a", "");
			    			ResultSet r = MySQLFuncs.exeq(""
			    					+ "SELECT * FROM "+Options.plugin.mysql._Warps+" WHERE `name`='"+wn+"';");
			    			if(r.next()){
			    				inv.clear();
			    				p.closeInventory();
								TpWarp.run(wn, p.getName(), p, false);
			    			} else {
			    				p.sendMessage(Functions.Config.gs("warpDidNotCreate",
			    						Options.plugin.dir + Options.files.messages));
			    			}
			    			r.close();
			    		} catch (SQLException e) {
			    			e.printStackTrace();
			    		}
			    	}
			    	if(c.getType().equals(Material.DIAMOND)){
			    		ItemMeta q = c.getItemMeta();
			    		// quay về
			    		if(q.getDisplayName().equals("§6<--")){
			    			// nếu chưa là tối thiểu
			    			if(WarpGUI.plimit.get(ps) >= (WarpGUI.warpguinvrow-9)){
			    				WarpGUI.plimit.set(ps, WarpGUI.plimit.get(ps)-(WarpGUI.warpguinvrow-9));
			    				inv.clear();
			    				p.closeInventory();
			    				new BukkitRunnable() {
			    		            @Override
			    		            public void run() {
			    		                WarpGUI.register(p);
			    		            }
			    		        }.runTaskLater(AquaWarp.getPlugin(), 5L);
			    			}
			    		} 
			    		// típ tục
			    		else {
			    			// nếu chưa là tối đa
			    			if((WarpGUI.plimit.get(ps)+9) <= MySQLFuncs.rowsize(Options.plugin.mysql._Warps)){
			    				WarpGUI.plimit.set(ps, WarpGUI.plimit.get(ps)+WarpGUI.warpguinvrow-9);
			    				inv.clear();
			    				p.closeInventory();
			    				new BukkitRunnable() {
			    		            @Override
			    		            public void run() {
			    		                WarpGUI.register(p);
			    		            }
			    		        }.runTaskLater(AquaWarp.getPlugin(), 5L);
			    			}
			    		}
			    	}
			    }
		   }
	    } else {
			p.sendMessage(Functions.Config.gs("doesNotHavePerm",
					Options.plugin.dir + Options.files.messages));
		}
    }
}
