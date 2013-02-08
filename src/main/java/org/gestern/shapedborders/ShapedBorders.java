package org.gestern.shapedborders;

import static org.gestern.shapedborders.Configuration.CONF;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

public class ShapedBorders extends JavaPlugin {

	/** The Gringotts plugin instance. */
	public static ShapedBorders P;
	
	private Logger log;
	
	private int borderTask = -1;
    
    @Override
    public void onEnable() {
    	
    	P = this;
    	log = getLogger();
    	
        // load and init configuration
        saveDefaultConfig(); // saves default configuration if no config.yml exists yet
        FileConfiguration savedConfig = getConfig();
        CONF.readConfig(savedConfig);
    	
        CommandExecutor myCommands = new Commands();
    	 
        getCommand("shapedborders").setExecutor(myCommands);
        getServer().getPluginManager().registerEvents(new SBListener(), this);
        startBorderTimer();
        
        setupMetrics();
    }
    
	@Override
    public void onDisable() {
        stopBorderTimer();
    }
    
    private boolean setupMetrics() {
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
            return true;
        } catch (IOException e) {
        	log.info("Failed to submit PluginMetrics stats");
        	return false;
        }
    }
    
    public boolean borderTimerRunning() {
        if (borderTask == -1) return false;
        return (getServer().getScheduler().isQueued(borderTask) || getServer().getScheduler().isCurrentlyRunning(borderTask));
    }

    public void startBorderTimer() {
        stopBorderTimer();

        borderTask = getServer().getScheduler()
                .scheduleSyncRepeatingTask(this, new BorderCheckTask(getServer()), CONF.timerTicks, CONF.timerTicks);

        if (borderTask == -1)
            log.warning("Failed to start timed border-checking task! This will prevent the plugin from working. Try restarting Bukkit.");

        log.config("Border-checking timed task started.");
    }

    public void stopBorderTimer() {
        if (borderTask == -1) return;

        getServer().getScheduler().cancelTask(borderTask);
        borderTask = -1;
        log.config("Border-checking timed task stopped.");
    }


}
