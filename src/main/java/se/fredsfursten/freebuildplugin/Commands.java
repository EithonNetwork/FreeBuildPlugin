package se.fredsfursten.freebuildplugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import se.fredsfursten.plugintools.PlayerCollection;

public class Commands {
	private static Commands singleton = null;
	private static final String ON_COMMAND = "/freebuild on";
	private static final String OFF_COMMAND = "/freebuild off";

	private JavaPlugin plugin = null;
	private PlayerCollection<FreeBuilderInfo> freeBuilders;

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
		this.plugin = plugin;
		this.freeBuilders = new PlayerCollection<FreeBuilderInfo>();
	}

	void disable() {
		this.freeBuilders = null;
	}
	
	boolean hasInformation(Player player) {
		return this.freeBuilders.hasInformation(player);
	}
	
	private FreeBuilderInfo getInfo(Player player)
	{
		return this.freeBuilders.get(player);
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
		
		FreeBuilderInfo info = this.freeBuilders.get(player);
		
		// TODO: Add cold down period
		
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
