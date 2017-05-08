package org.anhcraft.aquawarp.Util;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import org.bukkit.entity.Player;
import org.anhcraft.aquawarp.API.Energy;
import org.anhcraft.aquawarp.AquaWarp;
import org.anhcraft.aquawarp.Exceptions.EnergyDataNotFound;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class RegisterMVdWPlaceholderAPI {

    public RegisterMVdWPlaceholderAPI(AquaWarp plugin) {
        PlaceholderAPI.registerPlaceholder(plugin, "{aquawarp_player_current_energy}",
            new PlaceholderReplacer(){
                @Override
                public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
                    Player p = e.getPlayer();
                    try {
                        return Integer.toString(Energy.get(p.getUniqueId().toString()));
                    } catch(EnergyDataNotFound energyDataNotFound) {
                        energyDataNotFound.printStackTrace();
                        return null;
                    }
                }
            }
        );

        PlaceholderAPI.registerPlaceholder(plugin, "{aquawarp_maximum_energy}",
            new PlaceholderReplacer(){
                @Override
                public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
                    Player p = e.getPlayer();
                    return Integer.toString(Energy.getMax(p));
                }
            }
        );
    }
}
