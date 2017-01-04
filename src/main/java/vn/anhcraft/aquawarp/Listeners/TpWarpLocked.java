package vn.anhcraft.aquawarp.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import vn.anhcraft.aquawarp.API.WarpMethods;
import vn.anhcraft.aquawarp.Commands.TpWarp;
import vn.anhcraft.aquawarp.Exceptions.WarpNotFound;
import vn.anhcraft.aquawarp.Exceptions.WrongSaveDataType;
import vn.anhcraft.aquawarp.Util.Configuration;
import vn.anhcraft.aquawarp.Util.Strings;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class TpWarpLocked extends WarpMethods implements Listener {
    public static ArrayList<String> players = new ArrayList();
    public static ArrayList<CommandSender> senders = new ArrayList();
    public static ArrayList<String> warps = new ArrayList();

    public static void tpWarpLocked(String warp, String player, CommandSender sender) {
        Player p = Bukkit.getServer().getPlayerExact(player);
        Boolean m = false;
        for(Player x : Bukkit.getServer().getOnlinePlayers()) {
            if(x.getName().equals(player)){
                m = true;
            }
        }
        if(m){
            Strings.sendSender(Configuration.message.getString("require_enter_password").replace("@warp",warp), sender);
            warps.add(warp);
            players.add(p.getName());
            senders.add(sender);
        } else {
            Strings.sendSender(Configuration.message.getString("player_require_online"), sender);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void enterPassword(PlayerChatEvent e){
        Player p = e.getPlayer();
        String ns = p.getName();
        Boolean uq = false;
        int uo = -1;
        for(int v = 0; v < players.size(); v++){
            String px = players.get(v);
            if(px.equals(ns)){
                uq = true;
                uo = v;
                break;
            } else {
                continue;
            }
        }

        if(uq){
            e.setCancelled(true);
            String pass = e.getMessage();
            String warp = warps.get(uo);
            String player = players.get(uo);
            CommandSender sender = senders.get(uo);
            String hashPass = Strings.hashAutoType(pass);
            warps.remove(warp);
            players.remove(p.getName());
            senders.remove(sender);
            try {
                if(hashPass.equals(getPasswordAutoSaveDataType(warp))){
                    TpWarp.teleport(warp, player, sender);
                } else {
                   Strings.sendSender(Configuration.message.getString("wrong_password").replace("@warp",warp), sender);
                }
            } catch(WarpNotFound | SQLException | WrongSaveDataType es) {
                es.printStackTrace();
            }
        }
    }
}
