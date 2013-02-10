package org.gestern.shapedborders;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
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
	
	/** Unique configuration instance. */
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
        ConfigurationSection borderSection = savedConfig.getConfigurationSection("borders");
        if (borderSection!=null)
            borders.putAll((Map<String, ? extends Border>) borderSection.getValues(false));
        
        this.knockback = savedConfig.getDouble("knockback");
        this.whooshEffect = savedConfig.getBoolean("whooshEffect", true);
        this.timerTicks = savedConfig.getInt("timerTicks", 5);
    }
    
    /**
     * Write config values to a configuration object.
     * @param config
     */
    public void writeConfig(FileConfiguration config) {
        config.set("borders", borders);
        
        config.set("knockback", knockback);
        config.set("timerTicks", timerTicks);
        config.set("whooshEffect", whooshEffect);
    }
    
    
}
