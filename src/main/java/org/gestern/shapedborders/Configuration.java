package org.gestern.shapedborders;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.gestern.shapedborders.border.Border;

/**
 * Singleton for global configuration information. 
 * Values are initialized when the plugin is enabled.
 * 
 * @author jast
 *
 */
public enum Configuration {
	
	/** Central configuration instance. */
    CONF;
    
    @SuppressWarnings("unused")
    private final Logger log = ShapedBorders.P.getLogger();
    
    /** Knockback when trying to move past world border. */
    public double knockback = 3;
    
    /** Whether to show a whoosh effect when knocking player back. */
    public boolean whooshEffect = true;
    
    /** How many ticks to wait between worldborder checks */
    public int timerTicks = 5;

    /** Maps worlds to border. */
    public Map<String,Border> borders = new HashMap<String, Border>();
    
    /**
     * Set configuration from values in a file configuration.
     * @param savedConf
     */
    @SuppressWarnings("unchecked")
    public void readConfig(FileConfiguration savedConfig) {
        borders.putAll((Map<String, ? extends Border>) savedConfig.getValues(false));
    }
    
    
}
