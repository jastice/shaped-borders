package org.gestern.shapedborders.border;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

/**
 * An open border, which acts exactly like no border at all.
 * 
 * @author jast
 *
 */
public class OpenBorder extends AbstractBorder {

    @Override
    public boolean inside(Location loc) {
        return true;
    }

    @Override
    public Location reposition(Location loc) {
        return null;
    }

    @Override
    public Map<String, Object> serialize() {
        return new HashMap<String, Object>(0);
    }
}
