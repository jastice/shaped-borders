package org.gestern.shapedborders.border;

import java.util.Arrays;
import java.util.LinkedHashSet;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public abstract class AbstractBorder implements Border, ConfigurationSerializable {

    /**
     * Find a location which is safe for players to stand in, while preferring to stay in the same x/z coordinates.
     * @return
     */
    protected Location safeLocation(Location loc) {
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        
        // artificial height limit of 127 added for Nether worlds since CraftBukkit still incorrectly returns 255 for their max height, leading to players sent to the "roof" of the Nether
        final int limTop = (world.getEnvironment() == World.Environment.NETHER) ? 125 : world.getMaxHeight() - 2;
        final int limBot = 1;
        
        // Expanding Y search method adapted from Acru's code in the Nether plugin
        for(int y1 = y, y2 = y; (y1 > limBot) || (y2 < limTop); y1--, y2++){
            // Look below.
            if(y1 > limBot)
            {
                if (isSafeSpot(world, x, y1, z)) {
                    loc.setY(y1);
                    return loc;
                }
            }

            // Look above.
            if(y2 < limTop && y2 != y1)
            {
                if (isSafeSpot(world, x, y2, z)) {
                    loc.setY(y2);
                    return loc;
                }
            }
        }

        // no safe Y location?!?!? Must be a rare spot in a Nether world or something
        // oh well, just return the original location
        return loc;    

    }
    
    /** These material IDs are acceptable for places to teleport player; breathable blocks and water. */
    public static final LinkedHashSet<Integer> safeOpenBlocks = new LinkedHashSet<Integer>(Arrays.asList(
         new Integer[] {0, 6, 8, 9, 27, 28, 30, 31, 32, 37, 38, 39, 40, 50, 55, 59, 63, 64, 65, 66, 68, 69, 70, 71, 72, 75, 76, 77, 78, 83, 90, 93, 94, 96, 104, 105, 106, 115, 131, 132, 141, 142}
    ));

    /** These material IDs are ones we don't want to drop the player onto, like cactus or lava or fire or activated Ender portal. */
    public static final LinkedHashSet<Integer> painfulBlocks = new LinkedHashSet<Integer>(Arrays.asList(
         new Integer[] {10, 11, 51, 81, 119}
    ));

    /**
     * Check if a particular spot consists of 2 breathable blocks over something relatively solid.
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    protected boolean isSafeSpot(World world, int x, int y, int z) {
        Integer below = (Integer)world.getBlockTypeIdAt(x, y - 1, z);
        return (safeOpenBlocks.contains((Integer)world.getBlockTypeIdAt(x, y, z))       // target block open and safe
             && safeOpenBlocks.contains((Integer)world.getBlockTypeIdAt(x, y + 1, z))   // above target block open and safe
             && (!safeOpenBlocks.contains(below) || below == 8 || below == 9)           // below target block not open and safe (probably solid), or is water
             && !painfulBlocks.contains(below)                                          // below target block not painful
            );
    }
}
