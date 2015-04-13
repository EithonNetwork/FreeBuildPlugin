package se.fredsfursten.freebuildplugin;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sun.org.apache.xpath.internal.axes.HasPositionalPredChecker;

import se.fredsfursten.plugintools.ConfigurableFormat;
import se.fredsfursten.plugintools.Misc;
import se.fredsfursten.plugintools.PlayerCollection;
import se.fredsfursten.plugintools.PluginConfig;

public class Commands {
	private static Commands singleton = null;
	private static final String ON_COMMAND = "/freebuild on";
	private static final String OFF_COMMAND = "/freebuild off";
	private ConfigurableFormat mustBeInFreebuildWordMessage;
	private List<String> applicableWorlds;

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
		PluginConfig config = PluginConfig.get(plugin);
		this.applicableWorlds = config.getStringList("FreebuildWorldNames");
		this.mustBeInFreebuildWordMessage = config.getConfigurableFormat(
				"messages.MustBeInFreebuildWordMessage", 0, 
				"You can only switch between survival and freebuild in the SurvivalFreebuild world.");
	}

	void disable() {
		this.freeBuilders = null;
	}
	
	public boolean canUseFreebuilderProtection(Player player)
	{
		return canUseFreebuilderProtection(player, false);
	}
	
	public boolean canUseFreebuilderProtection(Player player, boolean warnIfNotInFreebuildWorld)
	{
		return this.freeBuilders.hasInformation(player) && inFreebuildWorld(player, warnIfNotInFreebuildWorld);
	}
	
	private boolean isFreeBuilder(Player player)
	{
		return this.freeBuilders.get(player) != null;
	}

	void onCommand(Player player, String[] args)
	{
		if (!Misc.verifyPermission(player, "freebuild.on")) return;
		if (!arrayLengthIsWithinInterval(args, 1, 1)) {
			player.sendMessage(ON_COMMAND);
			return;
		}
		
		if (this.freeBuilders.hasInformation(player))
		{
			player.sendMessage("You already have FreeBuilder ON.");
			return;
		}
		
		if (!inFreebuildWorld(player, true)) {
			this.mustBeInFreebuildWordMessage.sendMessage(player);
			return;
		}
		
		this.freeBuilders.put(player, new FreeBuilderInfo(player));
	}
	
	private boolean inFreebuildWorld(Player player, boolean mustBeInFreeBuildWord) {
		if (player.hasPermission("freebuild.inallworlds")) return true;
		String currentWorldName = player.getWorld().getName();
		for (String worldName : this.applicableWorlds) {
			if (currentWorldName.equalsIgnoreCase(worldName)) return true;
		}
		if (mustBeInFreeBuildWord) this.mustBeInFreebuildWordMessage.sendMessage(player);
		return false;
	}

	void offCommand(Player player, String[] args)
	{
		if (!Misc.verifyPermission(player, "freebuild.off")) return;
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


	private boolean arrayLengthIsWithinInterval(Object[] args, int min, int max) {
		return (args.length >= min) && (args.length <= max);
	}
}
