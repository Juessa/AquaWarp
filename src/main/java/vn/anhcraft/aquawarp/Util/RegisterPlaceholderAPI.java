package vn.anhcraft.aquawarp.Util;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;
import vn.anhcraft.aquawarp.API.Energy;
import vn.anhcraft.aquawarp.AquaWarp;
import vn.anhcraft.aquawarp.Exceptions.EnergyDataNotFound;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class RegisterPlaceholderAPI extends EZPlaceholderHook {

    public RegisterPlaceholderAPI(AquaWarp plugin) {
        super(plugin, "aquawarp");
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (identifier.equals("player_current_energy") && p != null){
            try {
                return Integer.toString(Energy.get(p.getUniqueId().toString()));
            } catch(EnergyDataNotFound energyDataNotFound) {
                energyDataNotFound.printStackTrace();
            }
        }
        if (identifier.equals("maximum_energy")) {
            return Integer.toString(Energy.getMax(p));
        }

        return null;
    }
}
