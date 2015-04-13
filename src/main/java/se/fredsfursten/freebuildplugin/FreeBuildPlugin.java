package se.fredsfursten.freebuildplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import se.fredsfursten.plugintools.Misc;

public final class FreeBuildPlugin extends JavaPlugin {


	@Override
	public void onEnable() {
		Misc.enable(this);	
		Commands.get().enable(this);
		getServer().getPluginManager().registerEvents(Events.get(), this);	
	}

	@Override
	public void onDisable() {
		Commands.get().disable();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be a player!");
			return false;
		}
		if (args.length < 1) {
			sender.sendMessage("Incomplete command...");
			return false;
		}

		Player player = (Player) sender;

		String command = args[0].toLowerCase();
		if (command.equals("on")) {
			Commands.get().onCommand(player, args);
		} else if (command.equals("off")) {
			Commands.get().offCommand(player, args);
		} else {
			sender.sendMessage("Could not understand command.");
			return false;
		}
		return true;
	}
}
