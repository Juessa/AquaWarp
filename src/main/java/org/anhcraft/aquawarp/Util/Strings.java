package org.anhcraft.aquawarp.Util;

import org.anhcraft.aquawarp.Options;
import org.anhcraft.spaciouslib.Hash.SHash;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class Strings {
    public static String hashAutoType(String str){
        String hash = "";
        SHash i = new SHash();
        if(Configuration.config.getString("hash").equals("md5")){
            try {
                hash = i.md5(str);
            } catch(NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if(Configuration.config.getString("hash").equals("plains")){
            hash = str;
        } else if(Configuration.config.getString("hash").equals("sha256")){
            try {
                hash = i.sha256(str);
            } catch(NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if(Configuration.config.getString("hash").equals("sha512")){
            try {
                hash = i.sha512(str);
            } catch(NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if(Configuration.config.getString("hash").equals("base64")){
            try {
                hash = i.base64(str);
            } catch(NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            hash = str;
        }
        return hash;
    }

	public static String reword(String a){
		java.lang.String b = a.toString()
            .replace("{plugin_name}",Options.pluginName)
		    .replace("{plugin_version}",Options.pluginVersion)
		    .replace("{plugin_author}",Options.author)
		    .replace("{contributor}", Options.contributor)
		    .replace("&", "§");
		return b;
	}

	public static void sendSender(String a) {
        Bukkit.getConsoleSender().sendMessage(reword(Configuration.config.getString("prefix") + a));
    }

    public static void sendSenderNoPrefix(String a) {
        Bukkit.getServer().getConsoleSender().sendMessage(reword(a));
    }

    public static void sendPlayer(String a, Player p) {
        p.sendMessage(reword(Configuration.config.getString("prefix") + a));
    }

    public static void sendPlayerNoPrefix(String a, Player p) {
        p.sendMessage(reword(a));
    }

    public static void sendSender(String a, CommandSender s) {
        s.sendMessage(reword(Configuration.config.getString("prefix") + a));
    }

    public static void sendSenderNoPrefix(String a, CommandSender s) {
        s.sendMessage(reword(a));
    }

    public static void sendAllPlayers(String a) {
        for(Player p: Bukkit.getServer().getOnlinePlayers()) {
             p.sendMessage(reword(Configuration.config.getString("prefix") + a));
        }
    }

    public static void sendAllPlayersNoPrefix(String a) {
        for(Player p: Bukkit.getServer().getOnlinePlayers()) {
            p.sendMessage(reword(a));
        }
    }

    public static void sendGlobal(String a) {
        Bukkit.getServer().broadcastMessage(reword(Configuration.config.getString("prefix")
                + a));
    }

    public static void sendGlobalNoPrefix(String a) {
        Bukkit.getServer().broadcastMessage(reword(a));
    }

    public static void sendGlobal(String a, World w){
        for(Player p: w.getPlayers()){
            sendPlayer(a, p);
        }
    }
}
