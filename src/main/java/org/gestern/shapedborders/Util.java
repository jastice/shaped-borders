package org.gestern.shapedborders;

import static org.gestern.shapedborders.Configuration.CONF;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.gestern.shapedborders.border.Border;

public class Util {

    /**
     * Find a suitable location to move, if the given location is not within the world border.
     * World, pitch and yaw of the old location are preserved.
     * @param loc Location to check
     * @return a replacement location, or null if the location should remain unchanged.
     */
    public static Location newLocation(Location loc) {
        if (loc==null) return null;
        
        World world = loc.getWorld();
        if (world == null) return null;
        
        Border border = CONF.borders.get(loc.getWorld().getName());
        if (border == null) return null;

        // nothing to do if player within borders
        if (border.inside(loc)) return null;
        
        Location newLoc = border.reposition(loc);
        newLoc.setWorld(world);
        newLoc.setPitch(loc.getPitch());
        newLoc.setYaw(loc.getYaw());
        
        return newLoc;
    }
    
    /**
     * Do a safe teleport, assuring a chunk is loaded before teleporting.
     * @param ent entity to teleport
     * @param loc location to teleport to
     */
    public static void teleport(Entity ent, Location loc) {
        Chunk chunk = loc.getChunk();
        if (!chunk.isLoaded())
            chunk.load();
        ent.teleport(loc);
    }
    
    /**
     * Convert an object into a double, if possible. Accepted types are Integer, Double, String.
     * If no conversion was possible, return 0.
     * @param number double representation of given object, or 0 if there is none.
     * @return
     */
    public static double asDouble(Object number) {
        if (number instanceof Integer)
            return (Integer)number; // autoboxing conversion thingy yay
        if (number instanceof Double)
            return (Double)number;
        if (number instanceof String)
            return Double.parseDouble((String)number);
        
        return 0;
    }
}
