package org.gestern.shapedborders.border;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import static org.gestern.shapedborders.Configuration.CONF;
import static org.gestern.shapedborders.Util.*;

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
        double x = Math.abs(xCenter - xLoc);
        double z = Math.abs(zCenter - zLoc);

        if (x < definiteSquare && z < definiteSquare)
            return true;    // Definitely inside
        else return (x * x + z * z < radiusSquared); // goold old hypothenuse
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

    @SuppressWarnings("serial")
    @Override
    public Map<String, Object> serialize() {
        return new HashMap<String, Object>(5) {{
            put("xCenter", xCenter);
            put("zCenter", zCenter);
            put("radius", radius);
        }};
    }
    
    public static CircleBorder deserialize(Map<String, Object> source) {
        return new CircleBorder(
                asDouble(source.get("xCenter")), 
                asDouble(source.get("zCenter")), 
                asDouble(source.get("radius")) );
    }

}
