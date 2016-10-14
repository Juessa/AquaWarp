package vn.anhcraft.aquawarp.event;

import java.sql.ResultSet;
import java.sql.SQLException;

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

import net.milkbowl.vault.economy.EconomyResponse;
import vn.anhcraft.aquawarp.api.Functions;
import vn.anhcraft.aquawarp.api.MySQLFuncs;
import vn.anhcraft.aquawarp.command.LockWarp;
import vn.anhcraft.aquawarp.command.TpWarp;
import vn.anhcraft.aquawarp.main.AquaWarp;
import vn.anhcraft.aquawarp.main.Options;

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
					if(p.hasPermission(Options.perm._GLOBAL) || p.hasPermission(Options.perm.SignWarpCreate)){
						sign.setLine(0, "§a-=[Aqua Warp]=-");
						sign.setLine(1, "§5"+header[1]);
						sign.setLine(2, header[2].replace("&", "§"));
						sign.setLine(3, header[3].replace("&", "§"));
						sign.update();
						p.sendMessage(Options.message.signWarpCreateSuccess);
						e.setCancelled(true);
					}
				}
				
				else if(header[0].toLowerCase().equals("§a-=[aqua warp]=-")){
					if((p.hasPermission(Options.perm._GLOBAL) || p.hasPermission(Options.perm.SignWarpRemove))
					   && p.getGameMode().equals(GameMode.CREATIVE)
					   && item.getType().equals(Material.DIAMOND_AXE)
					   ){
					  p.sendMessage(Options.message.signWarpRemoveSuccess);
					  e.setCancelled(false);
					} else {
						if(p.hasPermission(Options.perm._GLOBAL) || p.hasPermission(Options.perm.SignWarpUse)){
							e.setCancelled(true);
							String wn = header[1].replaceFirst("§5", "");
							ResultSet rd = MySQLFuncs.exeTable("SELECT * FROM "+Options.mysql.FeeTpWarp+" WHERE name='"+wn+"';");
							Boolean ib;
							
							try {
								ib = rd.next();
								if(Options.cmd.serviceCharge && !LockWarp.islocked(wn)){
									if(AquaWarp.EcoReady){
										if(ib){
											
											if(Functions.strToDouble(Functions.reSpecial(rd.getString("unlock_money")))
												<= AquaWarp.economy.getBalance(p)){
												EconomyResponse xc = AquaWarp.economy.withdrawPlayer(p, 
												Functions.strToDouble(Functions.reSpecial(rd.getString("unlock_money"))));
									            if(xc.transactionSuccess()) {
									            	TpWarp.run(wn, p.getName(), p, true);
									            } else {
									            	p.sendMessage(xc.errorMessage);
									            }
											} else {
												p.sendMessage(Options.message.lackMoney);
											}
											
										} else {
											TpWarp.run(wn, p.getName(), p, true);
										}
									} else {
										p.sendMessage(Options.message.requireVault);
									}
								} else {
									TpWarp.run(wn, p.getName(), p, true);
								}	
								rd.close();
							} catch (SQLException edw) {
								edw.printStackTrace();
							}
							
						} else {
							p.sendMessage(Options.message.doesNotHavePerm);
						}
					}
				}
				
				else {
					e.setCancelled(false);
				}
	        }
		}
	}
}
