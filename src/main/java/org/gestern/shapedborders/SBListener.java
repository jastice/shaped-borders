package org.gestern.shapedborders;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import static org.gestern.shapedborders.Configuration.CONF;
import static org.gestern.shapedborders.ShapedBorders.P;

public class SBListener implements Listener {
    
    Logger log = ShapedBorders.P.getLogger();
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        // if knockback is set to 0, simply return
        if (CONF.knockback == 0.0) return;

        Location newLoc = Util.newLocation(event.getTo()); 
        if (newLoc != null) event.setTo(newLoc);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkLoad(ChunkLoadEvent event) {
        // make sure our border monitoring task is still running like it should
        if (P.borderTimerRunning()) return;

        log.warning("Border-checking task was not running! Something on your server apparently killed it. It will now be restarted.");
        P.startBorderTimer();
    }
}
