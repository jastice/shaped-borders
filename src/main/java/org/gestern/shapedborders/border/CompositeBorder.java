package org.gestern.shapedborders.border;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;

/**
 * Border composed of several other borders. The border outline is defined by adding the outline of the consitutent borders.
 * This means, any point within one of the borders is also within the composite border. 
 * If a point is outside of all of the borders, it is also outside of the composite border.
 * @author jast
 *
 */
public class CompositeBorder extends AbstractBorder {
    
    private final List<Border> borders;
    
    public CompositeBorder(Border... borders) {
        this.borders = Arrays.asList(borders);
    }

    @Override
    public boolean inside(Location loc) {
        for (Border border : borders)
            if (border.inside(loc)) return true;
        return false;
    }

    @Override
    public Location reposition(Location loc) {
        // get all the repositionings, find closest to original location
        Location newLoc = null;
        double dSquared = Double.MAX_VALUE;
        for (Border border : borders) {
            Location bLoc = border.reposition(loc);
            double bLocDist = bLoc.distanceSquared(loc);
            if (newLoc==null || bLocDist < dSquared) {
                newLoc = bLoc;
                dSquared = bLocDist;
            }
        }
        return newLoc;
    }

    @SuppressWarnings("serial")
    @Override
    public Map<String, Object> serialize() {
        return new HashMap<String, Object>(borders.size()) {{
            put("borders", borders);
        }};
    }
}
