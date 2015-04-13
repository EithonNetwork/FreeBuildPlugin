package se.fredsfursten.freebuildplugin;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Events implements Listener {
	private static Events singleton = null;

	private Events() {
	}

	static Events get()
	{
		if (singleton == null) {
			singleton = new Events();
		}
		return singleton;
	}

	void enable(JavaPlugin plugin){
	}

	void disable() {
	}

	// Avoid becoming a target
	@EventHandler
	public void onEntityTargetLivingEntityEvent(EntityTargetLivingEntityEvent event) {
		if (event.isCancelled()) return;

		Entity target = event.getTarget();
		if (!(target instanceof Player)) return;
		Player player = (Player) target;

		if (!(event.getEntity() instanceof Monster)) return;

		if (!Commands.get().canUseFreebuilderProtection(player)) return;

		event.setCancelled(true);
	}

	// Don't attack monsters
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) return;

		Entity damager = event.getDamager();
		if (!(damager instanceof Player)) return;
		Player player = (Player) damager;

		if (!(event.getEntity() instanceof Monster)) return;
		Monster monster = (Monster) event.getEntity();

		if (!Commands.get().canUseFreebuilderProtection(player)) return;

		// You can attack monsters that targets you
		if (monster.getTarget() == player) return;

		event.setCancelled(true);
	}

	// No damage
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (event.isCancelled()) return;

		Entity entity = event.getEntity();
		if (!(entity instanceof Player)) return;
		Player player = (Player) entity;

		if (!Commands.get().canUseFreebuilderProtection(player)) return;

		event.setCancelled(true);
	}

	// No potion effects on free build players
	@EventHandler
	public void onPotionSplashEvent(PotionSplashEvent event) {
		if (event.isCancelled()) return;
		Player shooter = null;
		boolean shooterIsFreeBuilder = false;
		if(event.getPotion().getShooter() instanceof Player) {
			shooter = (Player) event.getPotion().getShooter();
			shooterIsFreeBuilder = Commands.get().canUseFreebuilderProtection(shooter);
		}
		for (LivingEntity livingEntity : event.getAffectedEntities()) {
			if (!(livingEntity instanceof Player)) {
				// FreeBuilders can't affect non-players
				if (shooterIsFreeBuilder) event.setIntensity(livingEntity, 0.0);
				continue;
			}
			Player affectedPlayer = (Player) livingEntity;
			boolean shooterAndAffectedAreTheSamePlayer = affectedPlayer.getUniqueId() == shooter.getUniqueId();
			boolean affectedIsFreebuilder = Commands.get().canUseFreebuilderProtection(affectedPlayer);
			// Freebuilders can affect themselves
			if (shooterIsFreeBuilder && shooterAndAffectedAreTheSamePlayer) continue;
			// Non freebuilders can affect each other
			if (!shooterIsFreeBuilder && !affectedIsFreebuilder) continue;
			event.setIntensity(livingEntity, 0.0);
		}
	}
	
	// Survival players can't fly
	@EventHandler
	public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent event) {
		if (event.isCancelled()) return;
		if (!event.isFlying()) return;
		
		// Allow players with permission freebuild.canfly to fly
		if (event.getPlayer().hasPermission("freebuild.canfly")) return;
		
		if (Commands.get().canUseFreebuilderProtection(event.getPlayer())) return;
		event.getPlayer().sendMessage("You are not allowed to fly.");
		event.setCancelled(true);
	}
}
