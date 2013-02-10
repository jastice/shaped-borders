package org.gestern.shapedborders;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.gestern.shapedborders.ShapedBorders.P;

public class Commands implements CommandExecutor {

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	    if (args.length == 0) return false;
	    String subcommand = args[0];
	    if (subcommand.equalsIgnoreCase("reload"))
	        P.reloadConfig();
	    return false;
    }

}
