package org.anhcraft.aquawarp.Scheduler;

import org.bukkit.Bukkit;
import org.anhcraft.aquawarp.AquaWarp;
import org.anhcraft.aquawarp.Options;
import org.anhcraft.aquawarp.Util.Strings;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class WelcomeMessages {
    private Boolean sendAllPlayers = true;
    private String[] customMessages = Options.listWelcomeMessages;
    private float customTimeOut = 20F;

    public WelcomeMessages(){}

    public WelcomeMessages(Boolean sendAllPlayers_){
        this.sendAllPlayers = sendAllPlayers_;
    }

    public WelcomeMessages(Boolean sendAllPlayers_, float customTimeOut_){
        this.sendAllPlayers = sendAllPlayers_;
        this.customTimeOut = customTimeOut_;
    }
    public WelcomeMessages(Boolean sendAllPlayers_, float customTimeOut_, String[] customMessages_){
        this.sendAllPlayers = sendAllPlayers_;
        this.customTimeOut = customTimeOut_;
        this.customMessages = customMessages_;
    }
    public void start(){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AquaWarp.plugin, new Runnable() {
            @Override
            public void run() {
                if(sendAllPlayers){
                    for(String s: customMessages){
                        Strings.sendAllPlayersNoPrefix(s);
                    }
                }
                for(String s: customMessages){
                    Strings.sendSenderNoPrefix(s);
                }
            }
        }, 20L);
    }
}
