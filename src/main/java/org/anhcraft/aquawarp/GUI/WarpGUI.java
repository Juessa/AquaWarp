package org.anhcraft.aquawarp.GUI;

import org.anhcraft.aquawarp.API.ListWarps;
import org.anhcraft.aquawarp.API.WarpStatus;
import org.anhcraft.aquawarp.API.WarpStatusTypes;
import org.anhcraft.aquawarp.Commands.TpWarp;
import org.anhcraft.aquawarp.Exceptions.WarpNotFound;
import org.anhcraft.aquawarp.Exceptions.WrongSaveDataType;
import org.anhcraft.aquawarp.Util.Configuration;
import org.anhcraft.aquawarp.Util.PermissionsManager;
import org.anhcraft.aquawarp.Util.Strings;
import org.anhcraft.spaciouslib.Inventory.InteractItemRunnable;
import org.anhcraft.spaciouslib.Inventory.SInventory;
import org.anhcraft.spaciouslib.Inventory.SItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class WarpGUI implements Listener {
    public static void show(Player p, int pageindex) {
        int max = Configuration.config.getInt("warpgui.row") * 9;
        SInventory inv = new SInventory(Strings.reword(Configuration.config.getString("warpgui.title")
                .replace("@page",
                        Integer.toString(pageindex+1))), max+9);
        if(p.hasPermission(PermissionsManager.getPermission("warps.warpgui_open")[0]) ||
                PermissionsManager.hasAll(p)) {
            try {
                List<String> list = ListWarps.getListWarpsAutoSaveDataType();
                int first = max * pageindex;
                int end = max * pageindex + (max - 1);
                List<String> filter = new ArrayList<>();
                int i = 0;
                for(String v : list){
                    if(first <= i && i <= end){
                        filter.add(v);
                    }
                    i++;
                }
                if(0 < filter.size()){
                    int s = 0;
                    for(String warp : list) {
                        if(WarpStatus.getStatusAutoSaveDataType(warp).equals(WarpStatusTypes.LOCKED)) {
                            List<String> lores1 = new ArrayList<>();
                            for(String l : Configuration.config.getStringList("warpgui.items_warp_lock.lores")) {
                                lores1.add(l.replace("@warp", warp));
                            }
                            SItems item = new SItems("&6&l" + warp,
                                    Material.PAPER,
                                    1, lores1
                            );
                            inv.set(s, item.getItem(), new InteractItemRunnable() {
                                @Override
                                public void run(Player player, ItemStack itemStack, ClickType clickType, int i) {
                                    click(player, itemStack);
                                }
                            });
                        } else {
                            List<String> lores2 = new ArrayList<>();
                            for(String l : Configuration.config.getStringList("warpgui.items_warp_unlock.lores")) {
                                lores2.add(l.replace("@warp", warp));
                            }
                            SItems item = new SItems("&6&l" + warp,
                                    Material.PAPER,
                                    1, lores2
                            );
                            inv.set(s, item.getItem(), new InteractItemRunnable() {
                                @Override
                                public void run(Player player, ItemStack itemStack, ClickType clickType, int i) {
                                    click(player, itemStack);
                                }
                            });
                        }
                        s++;
                    }
                    SItems i1 = new SItems(Configuration.config.getString("warpgui.item_prev_page.name"),
                            Material.ARROW,
                            1, Configuration.config.getStringList("warpgui.item_prev_page.lores")
                    );

                    inv.set(max+2, i1.getItem(), new InteractItemRunnable() {
                        @Override
                        public void run(Player player, ItemStack itemStack, ClickType clickType, int i) {
                            WarpGUI.show(player, pageindex-1);
                        }
                    });

                    // menu
                    SItems i2 = new SItems(Configuration.config.getString("warpgui.item_next_page.name"),
                            Material.ARROW,
                            1, Configuration.config.getStringList("warpgui.item_next_page.lores")
                    );
                    inv.set(max+6, i2.getItem(), new InteractItemRunnable() {
                        @Override
                        public void run(Player player, ItemStack itemStack, ClickType clickType, int i) {
                            WarpGUI.show(player, pageindex+1);
                        }
                    });
                    inv.open(p);
                }
            } catch(WarpNotFound | WrongSaveDataType | SQLException e) {
                e.printStackTrace();
            }
        } else {
            Strings.sendPlayer(Configuration.message.getString("require_permission"), p);
        }
    }

    private static void click(Player p, ItemStack c) {
        if(c.getType().equals(Material.PAPER)){
            String w = c.getItemMeta().getDisplayName().replace("§6§l", "");
            if(p.hasPermission(PermissionsManager.getPermission("warps.warpgui_tp")[0].replace("<name>",w)) ||
               p.hasPermission(PermissionsManager.getPermission("warps.warpgui_tp")[1].replace("<name>",w)) ||
               p.hasPermission(PermissionsManager.getPermission("warps.warpgui_tp")[2].replace("<name>",w)) ||
               PermissionsManager.hasAll(p)) {
                p.closeInventory();
                TpWarp.run(w, p.getName(), p);
            } else {
                Strings.sendPlayer(Configuration.message.getString("require_permission"), p);
            }
        }
    }
}
