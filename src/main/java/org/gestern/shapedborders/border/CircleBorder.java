package org.gestern.shapedborders.border;

import java.util.Map;

import org.bukkit.Location;

import static org.gestern.shapedborders.Configuration.CONF;

/**
 * Represents a circle shaped  world border.
 * 
 * @author jast
 *
 */
public class CircleBorder extends AbstractBorder {
    
    private final double xCenter, zCenter, radius, radiusSquared, definiteSquare; 
    
    public CircleBorder(double x, double z, double radius) {
        this.xCenter = x;
        this.zCenter = z;
        this.radius = radius;
        
        this.radiusSquared = radius * radius;
        this.definiteSquare = Math.sqrt(.5 * this.radiusSquared);
    }

    @Override
    public boolean inside(Location loc) {
        
        double xLoc = loc.getX();
        double zLoc = loc.getZ();
        
        // elegant round border checking algorithm is from rBorder by Reil with almost no changes, all credit to him for it
        double x = Math.abs(xLoc - xLoc);
        double z = Math.abs(zLoc - zLoc);

        if (x < definiteSquare && z < definiteSquare)
            return true;    // Definitely inside
        else if (x >= radius || z >= radius)
            return false;   // Definitely outside
        else  
            return (x * x + z * z < radiusSquared); // goold old hypothenuse
    }

    @Override
    public Location reposition(Location loc) {
        double xLoc = loc.getX();
        double zLoc = loc.getZ();
        
        // algorithm from: http://stackoverflow.com/questions/300871/best-way-to-find-a-point-on-a-circle-closest-to-a-given-point
        double vX = xLoc - xCenter;
        double vZ = zLoc - zCenter;
        double magV = Math.sqrt(vX*vX + vZ*vZ);
        xLoc = xCenter + vX / magV * (radius - CONF.knockback);
        zLoc = zCenter + vZ / magV * (radius - CONF.knockback);
        
        Location target = new Location(loc.getWorld(), xLoc, loc.getY(), zLoc) ;
        
        return safeLocation(target);
    }

    @Override
    public Map<String, Object> serialize() {
        // TODO Auto-generated method stub
        return null;
    }

}
