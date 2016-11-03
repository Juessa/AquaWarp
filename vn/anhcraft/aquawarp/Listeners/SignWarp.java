package vn.anhcraft.aquawarp.Listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.API.Functions;
import vn.anhcraft.aquawarp.Commands.TpWarp;

public class SignWarp implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSign(PlayerInteractEvent e){
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
		    Block block = e.getClickedBlock();
		    Player p = e.getPlayer();
		    ItemStack item = p.getItemInHand();
		    
	        if(block.getType().equals(Material.WALL_SIGN) || block.getType().equals(Material.SIGN_POST)) {
	        	Sign sign = (Sign) block.getState();
	        	String[] header = sign.getLines();
	        	
				if(header[0].toLowerCase().equals("[aquawarp]")){
					if(p.hasPermission("aquawarp.*") ||
					   p.hasPermission("aquawarp.signwarp.*") ||
					   p.hasPermission("aquawarp.signwarp.create") ||
					   p.isOp()){
						sign.setLine(0, "§a-=[Aqua Warp]=-");
						sign.setLine(1, "§5"+header[1]);
						sign.setLine(2, header[2].replace("&", "§"));
						sign.setLine(3, header[3].replace("&", "§"));
						sign.update();
						p.sendMessage(Functions.Config.gs("signWarpCreateSuccess", 
								Options.plugin.dir + Options.files.messages));
						e.setCancelled(true);
					} else {
						p.sendMessage(Functions.Config.gs("doesNotHavePerm", Options.plugin.dir + Options.files.messages));
					}
				}
				
				else if(header[0].toLowerCase().equals("§a-=[aqua warp]=-")){
					if((
						p.hasPermission("aquawarp.*") ||
						p.hasPermission("aquawarp.signwarp.*") ||
						p.hasPermission("aquawarp.signwarp.remove") ||
						p.isOp()
					  )
					   && p.getGameMode().equals(GameMode.CREATIVE)
					   && item.getType().equals(Material.DIAMOND_AXE)
					   ){
					  p.sendMessage(Functions.Config.gs("signWarpRemoveSuccess", 
							  Options.plugin.dir + Options.files.messages));
					  e.setCancelled(false);
					}
					
					else {
						if(
							p.hasPermission("aquawarp.*") ||
							p.hasPermission("aquawarp.signwarp.*") ||
							p.hasPermission("aquawarp.signwarp.use") ||
							p.isOp()
						){
							e.setCancelled(true);
							String wn = header[1].replaceFirst("§5", "");
							TpWarp.run(wn, p.getName(), p, false);
						} else {
							p.sendMessage(Functions.Config.gs("senderNotHavePerm",
									Options.plugin.dir + Options.files.messages));
						}
					}
				}
	        }
		} else {
			e.setCancelled(false);
		}
	}
}
