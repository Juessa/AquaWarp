package vn.anhcraft.aquawarp.GUI;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import vn.anhcraft.aquawarp.API.WarpStatus;
import vn.anhcraft.aquawarp.API.WarpStatusTypes;
import vn.anhcraft.aquawarp.Commands.TpWarp;
import vn.anhcraft.aquawarp.Exceptions.WarpNotFound;
import vn.anhcraft.aquawarp.Exceptions.WrongSaveDataType;
import vn.anhcraft.aquawarp.Options;
import vn.anhcraft.aquawarp.Util.Configuration;
import vn.anhcraft.aquawarp.Util.ConnectDatabase;
import vn.anhcraft.aquawarp.Util.PermissionsManager;
import vn.anhcraft.aquawarp.Util.Strings;
import vn.anhcraft.iceapi.Inventory.IceInventory;
import vn.anhcraft.iceapi.Items.IceItems;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class WarpGUI implements Listener {
    public static List<String> getListWarpsWithMySQL(){
        List<String> output = new ArrayList<>();
        try {
            ResultSet r = ConnectDatabase.exeq("SELECT * FROM " + Options.mysql_table_warp + ";");
            while(r.next()) {
                output.add(r.getString("name"));
            }
            r.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static List<String> getListWarpsWithFiles(){
        List<String> output = new ArrayList<>();
        File[] files = new File(Options.pluginDir + "warps").listFiles(
                new FilenameFilter() {
                    @Override public boolean accept(File dir, String name) {
                        return name.endsWith(".yml");
                    }
                }
        );
        for (File file : files) {
            FileConfiguration f = YamlConfiguration.loadConfiguration(file);
            output.add(f.getString("name"));
        }
        return output;
    }

    public static List<String> getListWarpsAutoSaveDataType(){
        if(Configuration.config.getString("saveDataType").toLowerCase().equals("file")){
            return getListWarpsWithFiles();
        } else {
            return getListWarpsWithMySQL();
        }
    }

    static int warpslot = Configuration.config.getInt("warpgui.row") * 9;
    public static int page = 1;
    static Inventory inv;
    static int current = 0;

    public static void show(Player p, int from) {
        current = from;
        inv = new IceInventory().create(warpslot+9,
                Strings.reword(Configuration.config.getString("warpgui.title").replace("@page",
                        Integer.toString(page))));
        if(p.hasPermission(PermissionsManager.getPermission("warps.warpgui_open")[0]) ||
                PermissionsManager.hasAll(p)) {
            try {
                List<String> warps = getListMax(getListWarpsAutoSaveDataType(), warpslot, current);
                for(String warp : warps) {
                    if(WarpStatus.getStatusAutoSaveDataType(warp).equals(WarpStatusTypes.LOCKED)) {
                        List<String> lores1 = new ArrayList<>();
                        for(String l : Configuration.config.getStringList("warpgui.items_warp_lock.lores")) {
                            lores1.add(l.replace("@warp", warp));
                        }
                        ItemStack i = new IceItems().create("&6&l" + warp,
                                Material.PAPER,
                                1, lores1
                        );
                        inv.addItem(i);
                    } else {
                        List<String> lores2 = new ArrayList<>();
                        for(String l : Configuration.config.getStringList("warpgui.items_warp_unlock.lores")) {
                            lores2.add(l.replace("@warp", warp));
                        }
                        ItemStack i = new IceItems().create("&6&l" + warp,
                                Material.PAPER,
                                1, lores2
                        );
                        inv.addItem(i);
                    }
                }
                ItemStack i1 = new IceItems().create(Configuration.config.getString("warpgui.item_prev_page.name"),
                        Material.ARROW,
                        1, Configuration.config.getStringList("warpgui.item_prev_page.lores")
                );

                inv.setItem((warpslot - 7)+9, i1);

                // menu
                ItemStack i2 = new IceItems().create(Configuration.config.getString("warpgui.item_next_page.name"),
                        Material.ARROW,
                        1, Configuration.config.getStringList("warpgui.item_next_page.lores")
                );
                inv.setItem((warpslot - 2)+9, i2);
                new IceInventory(inv).open(p);
            } catch(WarpNotFound | WrongSaveDataType | SQLException e) {
                e.printStackTrace();
            }
        }else {
            Strings.sendPlayer(Configuration.message.getString("require_permission"), p);
        }
    }

    public static List<String> getListMax(List<String> l, int max, int from){
        List<String> x = new ArrayList<>();
        int h = 0;
        for(String s : l){
            h += 1;
            if(from <= h){
                x.add(s);
            } else {
                continue;
            }
        }
        int i = 0;
        List<String> n = new ArrayList<>();
        for(String s : x){
            // <=
            if(i < max){
                i += 1;
                n.add(s);
                continue;
            } else {
                break;
            }
        }
        return n;
    }

    public void prevPage(Player p, int currentPage) {
        if(0 < currentPage) {
            page -= 1;
            show(p, currentPage - warpslot);
        }
    }

    public void nextPage(Player p, int currentPage) {
        page += 1;
        show(p, currentPage + warpslot);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        ItemStack c = event.getCurrentItem();
        Inventory i = event.getClickedInventory();

        if(c != null && !c.getType().equals(Material.AIR)){
            if(i.equals(inv)) {
                event.setCancelled(true);
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
                else if(c.getType().equals(Material.ARROW)){
                    if(c.getItemMeta().getDisplayName().equals(
                            Strings.reword(Configuration.config.getString("warpgui.item_prev_page.name")))){
                        prevPage(p, current);
                    }
                    else if(c.getItemMeta().getDisplayName().equals(
                            Strings.reword(Configuration.config.getString("warpgui.item_next_page.name")))){
                        nextPage(p, current);
                    }
                }
            }
        }
    }
}
