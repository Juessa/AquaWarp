package vn.anhcraft.aquawarp.Particles;

import org.bukkit.World;
import vn.anhcraft.aquawarp.Util.Configuration;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class PlayParticles {
    public PlayParticles(String particle, float xFrom,
                               float yFrom, float zFrom,
                               float distanceX,  float distanceY, float distanceZ, World world){
        particle = particle.toUpperCase();
        switch(particle) {
            case "HEART_FOUR_DIRECTIONS":
                for(int i = 0; i < (Configuration.config.getInt("tpWarp.effect.repeat")+1); i++) {
                    new HeartFourDirections(yFrom, xFrom, zFrom, distanceX, distanceZ, world);
                }
                break;
            default:
                for(int i = 0; i < (Configuration.config.getInt("tpWarp.effect.repeat")+1); i++) {
                    new HeartFourDirections(yFrom, xFrom, zFrom, distanceX, distanceZ, world);
                }
        }
    }
}
