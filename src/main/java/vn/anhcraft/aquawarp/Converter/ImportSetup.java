package vn.anhcraft.aquawarp.Converter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import vn.anhcraft.aquawarp.AquaWarpsMessages;
import vn.anhcraft.aquawarp.Util.Strings;

import java.util.HashMap;
import java.util.UUID;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class ImportSetup implements Listener {
    static HashMap<Player, ConverterStageTypes> cache = new HashMap<>();
    HashMap<UUID, ConverterTypes> types = new HashMap<>();
    HashMap<UUID, Boolean> keep = new HashMap<>();

    public Boolean isPlayerHasCache(Player pq){
        Boolean r = false;
        for(Player p : cache.keySet()){
            if(p.equals(pq)){
                r = true;
                break;
            }
        }
        return r;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void chayConverterSetup(AsyncPlayerChatEvent ev){
        Player p = ev.getPlayer();
        UUID u = p.getUniqueId();
        if(isPlayerHasCache(p)) {
            ev.setCancelled(true);
            if(cache.get(p).equals(ConverterStageTypes.SelectTypes)) {
                if(ev.getMessage().toLowerCase().equals("cancel")) {
                    cache.remove(p);
                    Strings.sendPlayer(AquaWarpsMessages.get("Utils.Converter.Exited"), p);
                } else {
                    types.put(u, ConverterTypes.valueOf(ev.getMessage().toUpperCase()));
                    cache.put(p, ConverterStageTypes.KeepOldWarp);
                    Strings.sendPlayer(AquaWarpsMessages.get("Utils.Converter.SecondStage").replace("<type>",
                            ev.getMessage()), p);
                }
            } else if(cache.get(p).equals(ConverterStageTypes.KeepOldWarp)) {
                ev.setCancelled(true);
                if(ev.getMessage().toLowerCase().equals("cancel")) {
                    cache.remove(p);
                    types.remove(u);
                    addPlayer(p);
                } else {
                    keep.put(u, Boolean.valueOf(ev.getMessage().toUpperCase()));
                    cache.put(p, ConverterStageTypes.SelectSaveData);
                    Strings.sendPlayer(AquaWarpsMessages.get("Utils.Converter.ThirdStage").replace("<type>",
                            ev.getMessage()), p);
                }
            } else if(cache.get(p).equals(ConverterStageTypes.SelectSaveData)) {
                ev.setCancelled(true);
                if(ev.getMessage().toLowerCase().equals("cancel")) {
                    cache.put(p, ConverterStageTypes.KeepOldWarp);
                    Strings.sendPlayer(AquaWarpsMessages.get("Utils.Converter.SecondStage").replace("<type>",
                            ev.getMessage()), p);
                } else {
                    if(ev.getMessage().toLowerCase().equals("mysql")) {
                        if(types.get(u).equals(ConverterTypes.ESSENTIALS)) {
                            Strings.sendPlayer(AquaWarpsMessages.get("Utils.Converter.Starting").replace("<type>",
                                    types.get(u).name()), p);
                            EssentialsWarp.hook();
                            try {
                                if(EssentialsWarp.importWithMySQL(keep.get(u))){
                                    Strings.sendPlayer(AquaWarpsMessages.get("Utils.Converter.Complete"), p);
                                }
                                types.remove(u);
                                cache.remove(p);
                                keep.remove(u);

                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if(ev.getMessage().toLowerCase().equals("file")) {
                        if(types.get(u).equals(ConverterTypes.ESSENTIALS)) {
                            Strings.sendPlayer(AquaWarpsMessages.get("Utils.Converter.Starting").replace("<type>",
                                    types.get(u).name()), p);
                            EssentialsWarp.hook();

                            try {

                                if(EssentialsWarp.importWithFiles(keep.get(u))){
                                    Strings.sendPlayer(AquaWarpsMessages.get("Utils.Converter.Complete"), p);
                                }
                                types.remove(u);
                                cache.remove(p);
                                keep.remove(u);
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public static void addPlayer(Player p){
        String allTypes = "";
        for(ConverterTypes c : ConverterTypes.values()){
            allTypes += ", "+c.name();
        }
        cache.put(p, ConverterStageTypes.SelectTypes);
        Strings.sendPlayer(AquaWarpsMessages.get("Utils.Converter.FirstStage"),p);
        Strings.sendPlayerNoPrefix("&c"+allTypes.replace(", ",""), p);
    }
}
