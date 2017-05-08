package org.anhcraft.aquawarp.Scheduler;

import org.anhcraft.aquawarp.AquaWarp;
import org.anhcraft.aquawarp.AquaWarpsMessages;
import org.anhcraft.aquawarp.Options;
import org.anhcraft.aquawarp.Util.Configuration;
import org.anhcraft.aquawarp.Util.Strings;
import org.anhcraft.aquawarp.Util.URLContent;
import org.anhcraft.spaciouslib.Converter.DataTypes;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class CheckUpdate {
    public CheckUpdate() {
        if(Configuration.config.getBoolean("checkUpdate")) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AquaWarp.plugin, new Runnable() {
                @Override
                public void run() {
                    BufferedReader br = URLContent.get(
                            new StringBuilder()
                                    .append("http://")
                                    .append(Options.serverCheckUpdate)
                                    .append("?v=")
                                    .append(Options.pluginVersion)
                                    .toString());
                    String inputLine;
                    try {
                        inputLine = br.readLine().replace(" ", "");
                        String[] a = inputLine.split("\\/");
                        int o = new DataTypes().toInt(a[0]);
                        if(o == 0) {
                            Strings.sendSender(AquaWarpsMessages.get("System.CheckUpdate.newest"));
                        }
                        if(o == 1) {
                            Strings.sendSender(AquaWarpsMessages.get("System.CheckUpdate.available.1"));
                            Strings.sendSender(AquaWarpsMessages.get("System.CheckUpdate.available.2") + a[1]);
                        }
                        if(o == -1) {
                            Strings.sendSender(AquaWarpsMessages.get("System.Error.1003")
                                    .replace("<v>", Options.pluginVersion));
                        }

                        br.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 25L);
        }
    }
}
