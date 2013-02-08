package org.gestern.shapedborders.border;

import org.bukkit.Location;

public interface Border {

    /** Check if a location is within a border. */
    boolean inside(Location loc);
    
    /**
     * Get a position within the border closest to the given position which is safe for a player to stand.
     * If the position is already within the border, this will return the location.
     * 
     * @param loc location to find closest location to
     * @return a position within the border closest to the given position
     */
    Location reposition(Location loc);
}
