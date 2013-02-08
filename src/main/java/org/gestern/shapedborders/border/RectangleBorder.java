package org.gestern.shapedborders.border;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import static org.gestern.shapedborders.Configuration.CONF;

public class RectangleBorder extends AbstractBorder {

    private final String world;
    private final double xMin, xMax, zMin, zMax;
    
    RectangleBorder(String world, double xMin, double xMax, double zMin, double zMax) {
        this.world = world;
        this.xMin = xMin;
        this.xMax = xMax;
        this.zMin = zMin;
        this.zMax = zMax;
    }
    
    @Override
    public boolean inside(Location loc) {
        double x = loc.getX(), z = loc.getZ();
        return x > xMin && x < xMax && z > zMin && z < zMax;
    }

    @Override
    public Location reposition(Location loc) {
        
        if (inside(loc)) return loc;
        
        double xLoc = loc.getX(), zLoc = loc.getZ();
        
        if (xLoc <= xMin)
            xLoc = xMin + CONF.knockback;
        else if (xLoc >= xMax)
            xLoc = xMax - CONF.knockback;
        if (zLoc <= zMin)
            zLoc = zMin + CONF.knockback;
        else if (zLoc >= zMax)
            zLoc = zMax - CONF.knockback;

        return safeLocation(new Location(loc.getWorld(), xLoc, loc.getY(), zLoc));
    }

    @SuppressWarnings("serial")
    @Override
    public Map<String, Object> serialize() {
        return new HashMap<String,Object>() {{
            put("world", world);
            put("xMin", xMin);
            put("xMax", xMax);
            put("zMin", zMin);
            put("zMax", zMax);
        }};
    }
    
    public static RectangleBorder deserialize(Map<String, Object> data) {
        return new RectangleBorder(
                (String)data.get("world"), 
                (Double)data.get("xMin"),
                (Double)data.get("xMax"), 
                (Double)data.get("zMin"), 
                (Double)data.get("zMax"));
    }

}
