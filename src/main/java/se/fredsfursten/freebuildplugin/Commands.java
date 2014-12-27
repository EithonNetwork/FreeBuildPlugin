package se.fredsfursten.freebuildplugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Commands implements Listener {
	private static Commands singleton = null;
	private static final String ON_COMMAND = "/freebuild on";
	private static final String OFF_COMMAND = "/freebuild off";

	private JavaPlugin plugin = null;
	private FreeBuilders allJumpPads = null;

	private Commands() {
		this.allJumpPads = FreeBuilders.get();
	}

	static Commands get()
	{
		if (singleton == null) {
			singleton = new Commands();
		}
		return singleton;
	}

	void enable(JavaPlugin plugin){
		this.plugin = plugin;
	}

	void disable() {
	}

	void onCommand(Player player, String[] args)
	{
		if (!verifyPermission(player, "freebuild.on")) return;
		if (!arrayLengthIsWithinInterval(args, 1, 1)) {
			player.sendMessage(ON_COMMAND);
			return;
		}
		
		FreeBuilders freeBuilders = FreeBuilders.get();
		if (freeBuilders.isFreeBuilder(player))
		{
			player.sendMessage("You already have FreeBuilder ON.");
			return;
		}
		
		freeBuilders.add(player);
	}
	
	void offCommand(Player player, String[] args)
	{
		if (!verifyPermission(player, "freebuild.off")) return;
		if (!arrayLengthIsWithinInterval(args, 1, 1)) {
			player.sendMessage(OFF_COMMAND);
			return;
		}
		
		FreeBuilders freeBuilders = FreeBuilders.get();
		if (!freeBuilders.isFreeBuilder(player))
		{
			player.sendMessage("You already have FreeBuilder OFF.");
			return;
		}
		
		FreeBuilderInfo info = freeBuilders.get(player);
		
		// TODO: Add cold down period
		
		freeBuilders.remove(player);
	}

	private boolean verifyPermission(Player player, String permission)
	{
		if (player.hasPermission(permission)) return true;
		player.sendMessage("You must have permission " + permission);
		return false;
	}

	private boolean arrayLengthIsWithinInterval(Object[] args, int min, int max) {
		return (args.length >= min) && (args.length <= max);
	}
}
