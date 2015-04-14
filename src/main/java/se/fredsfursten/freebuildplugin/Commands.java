package se.fredsfursten.freebuildplugin;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sun.org.apache.xpath.internal.axes.HasPositionalPredChecker;

import se.fredsfursten.plugintools.ConfigurableFormat;
import se.fredsfursten.plugintools.CoolDown;
import se.fredsfursten.plugintools.Misc;
import se.fredsfursten.plugintools.PlayerCollection;
import se.fredsfursten.plugintools.PluginConfig;

public class Commands {
	private static Commands singleton = null;
	private static final String ON_COMMAND = "/freebuild on";
	private static final String OFF_COMMAND = "/freebuild off";
	private ConfigurableFormat mustBeInFreebuildWordMessage;
	private ConfigurableFormat waitForCoolDownMessage;
	private List<String> applicableWorlds;
	private int _coolDownTimeInMinutes;
	private CoolDown _coolDown;

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
		this._coolDownTimeInMinutes = config.getInt("CoolDownTimeInMinutes", 30);
		this.mustBeInFreebuildWordMessage = config.getConfigurableFormat(
				"messages.MustBeInFreebuildWordMessage", 0, 
				"You can only switch between survival and freebuild in the SurvivalFreebuild world.");
		this.waitForCoolDownMessage = config.getConfigurableFormat(
				"messages.WaitForCoolDownMessage", 2, 
				"The remaining cool down period for switching Freebuild mode is %d minutes and %d seconds.");
		this._coolDown = new CoolDown("freebuild", this._coolDownTimeInMinutes*60);
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
		Misc.debugInfo("  canUseFreebuilderProtection: Return false if the current player is not a freebuilder.");
		if (!isFreeBuilder(player)) return false;
		Misc.debugInfo("  canUseFreebuilderProtection: Return false if the player's current world is not a freebuilder world.");
		if (!inFreebuildWorld(player, warnIfNotInFreebuildWorld)) return false;
		Misc.debugInfo("  canUseFreebuilderProtection: Returns true.");
		return true;
	}

	boolean isFreeBuilder(Player player)
	{
		return this.freeBuilders.get(player) != null;
	}

	boolean inFreebuildWorld(Player player, boolean mustBeInFreeBuildWord) {
		String currentWorldName = player.getWorld().getName();
		for (String worldName : this.applicableWorlds) {
			if (currentWorldName.equalsIgnoreCase(worldName)) return true;
		}
		if (mustBeInFreeBuildWord) this.mustBeInFreebuildWordMessage.sendMessage(player);
		return false;
	}

	void onCommand(Player player, String[] args)
	{
		if (!Misc.verifyPermission(player, "freebuild.on")) return;
		if (!arrayLengthIsWithinInterval(args, 1, 1)) {
			player.sendMessage(ON_COMMAND);
			return;
		}

		if (isFreeBuilder(player))
		{
			player.sendMessage("Freebuild mode is already active.");
			return;
		}

		if (!inFreebuildWorld(player, true)) {
			this.mustBeInFreebuildWordMessage.sendMessage(player);
			return;
		}

		if (!verifyCoolDown(player)) return;

		this.freeBuilders.put(player, new FreeBuilderInfo(player));
		player.sendMessage("Freebuild mode is now active.");	
	}

	private boolean verifyCoolDown(Player player) {
		if (player.hasPermission("freebuild.nocooldown")) return true;

		int secondsLeft = this._coolDown.secondsLeft(player);
		if (secondsLeft == 0) return true;

		int minutes = secondsLeft/60;
		int seconds = secondsLeft - 60 * minutes;
		this.waitForCoolDownMessage.sendMessage(player, minutes, seconds);
		return false;
	}

	void offCommand(Player player, String[] args)
	{
		if (!Misc.verifyPermission(player, "freebuild.off")) return;
		if (!arrayLengthIsWithinInterval(args, 1, 1)) {
			player.sendMessage(OFF_COMMAND);
			return;
		}

		if (!isFreeBuilder(player))
		{
			player.sendMessage("Survival mode is already active (freebuild is OFF).");
			return;
		}

		if (!verifyCoolDown(player)) return;

		// TODO: Add cool down period

		this.freeBuilders.remove(player);
		player.sendMessage("Survival mode is now active (freebuild is OFF).");		
	}


	private boolean arrayLengthIsWithinInterval(Object[] args, int min, int max) {
		return (args.length >= min) && (args.length <= max);
	}
}
