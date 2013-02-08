package org.gestern.shapedborders;

import static org.gestern.shapedborders.Configuration.CONF;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public class BorderCheckTask implements Runnable
{
	private transient Server server = null;

	public BorderCheckTask(Server theServer) {
		this.server = theServer;
	}

	public void run() {
		if (server == null) return;

		// if knockback is set to 0, simply return
		if (CONF.knockback == 0.0) return;

		for (Player player : server.getOnlinePlayers())
		    checkPlayer(player);
	}

	// set targetLoc only if not current player location; set returnLocationOnly to true to have new Location returned if they need to be moved to one, instead of directly handling it
	/**
	 * Checks a player's location, and move them to a valid location if they are outside the world border.
	 * @param player
	 * @param targetLoc
	 * @param returnLocationOnly
	 * @return
	 */
	private Location checkPlayer(Player player) {
	    
		if (player == null || !player.isOnline()) return null;

		Location loc = player.getLocation();
		
		// allow players with bypass permission to bypass world borders
        if (player.hasPermission("shapedborders.bypass")) return null;
		
		Location newLoc = Util.newLocation(loc);
		if (newLoc == null) return null;
		
		// whoosh effect!
		whoosh(loc);

		if (!player.isInsideVehicle()) {
			player.teleport(newLoc);
	    } else {
			Entity ride = player.getVehicle();
			if (ride != null)
			{	// vehicles need to be offset vertically and have velocity stopped
				double vertOffset = (ride instanceof LivingEntity) ? 0 : ride.getLocation().getY() - loc.getY();
				newLoc.setY(newLoc.getY() + vertOffset);
				ride.setVelocity(new Vector(0, 0, 0));
				Util.teleport(ride, newLoc);
			}
			else
			{	// if player.getVehicle() returns null (when riding a pig on older Bukkit releases, for instance), player has to be ejected
				player.leaveVehicle();
				Util.teleport(player, newLoc);
			}
		}

		return null;
	}

	private void whoosh(Location loc) {
	    if (CONF.whooshEffect) {   // give some particle and sound effects where the player was beyond the border
	        World world = loc.getWorld();
	        world.playEffect(loc, Effect.ENDER_SIGNAL, 0);
	        world.playEffect(loc, Effect.ENDER_SIGNAL, 0);
	        world.playEffect(loc, Effect.SMOKE, 4);
	        world.playEffect(loc, Effect.SMOKE, 4);
	        world.playEffect(loc, Effect.SMOKE, 4);
	        world.playEffect(loc, Effect.GHAST_SHOOT, 0);
	    }
	}
}
