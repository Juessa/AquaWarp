package vn.anhcraft.aquawarp.Scheduler;

import org.bukkit.Bukkit;
import vn.anhcraft.aquawarp.AquaWarp;
import vn.anhcraft.aquawarp.AquaWarpsMessages;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.Util.Configuration;
import vn.anhcraft.aquawarp.Util.Strings;
import vn.anhcraft.aquawarp.Util.URLContent;
import vn.anhcraft.iceapi.Converter.IceConverter;
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
                        int o = new IceConverter().toInt(a[0]);
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
