package se.fredsfursten.freebuildplugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import se.fredsfursten.plugintools.PlayerCollection;

public class Commands {
	private static Commands singleton = null;
	private static final String ON_COMMAND = "/freebuild on";
	private static final String OFF_COMMAND = "/freebuild off";

	private PlayerCollection<FreeBuilderInfo> freeBuilders = new PlayerCollection<FreeBuilderInfo>(new FreeBuilderInfo());

	private Commands() {
	}

	static Commands get()
	{
		if (singleton == null) {
			singleton = new Commands();
		}
		return singleton;
	}

	void enable(JavaPlugin plugin){
		this.freeBuilders = new PlayerCollection<FreeBuilderInfo>(new FreeBuilderInfo());
	}

	void disable() {
		this.freeBuilders = null;
	}
	
	boolean hasInformation(Player player) {
		return this.freeBuilders.hasInformation(player);
	}
	
	public boolean isFreeBuilder(Player player)
	{
		return this.freeBuilders.get(player) != null;
	}

	void onCommand(Player player, String[] args)
	{
		if (!verifyPermission(player, "freebuild.on")) return;
		if (!arrayLengthIsWithinInterval(args, 1, 1)) {
			player.sendMessage(ON_COMMAND);
			return;
		}
		
		if (this.freeBuilders.hasInformation(player))
		{
			player.sendMessage("You already have FreeBuilder ON.");
			return;
		}
		
		this.freeBuilders.put(player, new FreeBuilderInfo(player));
	}
	
	void offCommand(Player player, String[] args)
	{
		if (!verifyPermission(player, "freebuild.off")) return;
		if (!arrayLengthIsWithinInterval(args, 1, 1)) {
			player.sendMessage(OFF_COMMAND);
			return;
		}
		
		if (!this.freeBuilders.hasInformation(player))
		{
			player.sendMessage("You already have FreeBuilder OFF.");
			return;
		}
		
		this.freeBuilders.get(player);
		
		// TODO: Add cool down period
		
		this.freeBuilders.remove(player);
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
