package se.fredsfursten.freebuildplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class FreeBuildPlugin extends JavaPlugin implements Listener {
	
	private FreeBuilders freeBuilders;

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);		
		ProtectFreeBuilders.get().enable();
		Commands.get().enable(this);
		this.freeBuilders = FreeBuilders.get();
	}

	@Override
	public void onDisable() {
		ProtectFreeBuilders.get().disable();
		Commands.get().disable();
		this.freeBuilders = null;
	}

	@EventHandler
	public void avoidBecomingATarget(EntityTargetLivingEntityEvent event) {
		if (event.isCancelled()) return;

		Entity target = event.getTarget();
		if (!(target instanceof Player)) return;
		Player player = (Player) target;
		
		if (!(event.getEntity() instanceof Monster)) return;
		
		if (!this.freeBuilders.isFreeBuilder(player)) return;

		event.setCancelled(true);
	}

	@EventHandler
	public void dontAttackMonsters(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) return;

		Entity damager = event.getDamager();
		if (!(damager instanceof Player)) return;
		Player player = (Player) damager;
		
		if (!(event.getEntity() instanceof Monster)) return;
		Monster monster = (Monster) event.getEntity();
		
		if (!this.freeBuilders.isFreeBuilder(player)) return;
		
		// You can attack monsters that targets you
		if (monster.getTarget() == player) return;

		event.setCancelled(true);
	}


	@EventHandler
	public void noDamage(EntityDamageEvent event) {
		if (event.isCancelled()) return;
		
		Entity entity = event.getEntity();
		if (!(entity instanceof Player)) return;
		Player player = (Player) entity;
		
		if (!this.freeBuilders.isFreeBuilder(player)) return;
		
		event.setCancelled(true);
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
