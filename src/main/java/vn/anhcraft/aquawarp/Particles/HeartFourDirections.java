package vn.anhcraft.aquawarp.Particles;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class HeartFourDirections {
    public HeartFourDirections(float yFrom,
            float xFrom, float zFrom,
            float distanceX, float distanceZ, World world){

        float xF1 = xFrom;
        for(int t = 0; t < distanceX; t++){
            xF1 += 1;
            world.playEffect(new Location(world, xF1, yFrom, zFrom), Effect.HEART, 1);
        }
        float zF1 = zFrom;
        for(int t = 0; t < distanceZ; t++){
            zF1 += 1;
            world.playEffect(new Location(world, xFrom, yFrom, zF1), Effect.HEART, 1);
        }

        float xF2 = xFrom;
        for(int t = 0; t < distanceX; t++){
            xF2 -= 1;
            world.playEffect(new Location(world, xF2, yFrom, zFrom), Effect.HEART, 1);
        }

        float zF2 = zFrom;
        for(int t = 0; t < distanceZ; t++){
            zF2 -= 1;
            world.playEffect(new Location(world, xFrom, yFrom, zF2), Effect.HEART, 1);
        }
    }
}
