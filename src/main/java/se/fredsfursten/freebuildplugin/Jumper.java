package se.fredsfursten.jumppadplugin;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class Jumper implements Listener {
	private static Jumper singleton = null;

	private HashMap<UUID, UUID> playersThatHasBeenInformedToReadTheRules = null;
	private HashMap<UUID, JumpPadInfo> playersInJumpUp = null;
	private HashMap<UUID, UUID> playersWithTemporaryJumpPause = null;
	private AllJumpPads allJumpPads = null;

	private Jumper() {
		this.allJumpPads = AllJumpPads.get();
	}

	static Jumper get()
	{
		if (singleton == null) {
			singleton = new Jumper();
		}
		return singleton;
	}

	void enable(){
		this.playersThatHasBeenInformedToReadTheRules = new HashMap<UUID, UUID>();
		this.playersWithTemporaryJumpPause = new HashMap<UUID, UUID>();
		this.playersInJumpUp = new HashMap<UUID, JumpPadInfo>();
	}

	void disable() {
	}

	void maybeJumpUp(Player player, Location location) {
		JumpPadInfo info = this.allJumpPads.getByLocation(location);
		if (info == null) {
			mustReadRules(player, true);
			playerCanJump(player, true);
			return;
		}
		if (!hasReadRules(player)) {
			maybeTellPlayerToReadTheRules(player);
			return;
		}
		if (hasTemporaryJumpPause(player)) return;

		jumpUp(player, info);
	}

	private void jumpUp(Player player, JumpPadInfo info) {
		Vector upwards = new Vector(0.0, info.getVelocity().getY(), 0.0);
		player.setVelocity(upwards);
		this.playersInJumpUp.put(player.getUniqueId(), info);
	}

	boolean maybeShootForward(Player player, Location from, Location to) {
		if (!isInAir(player)) return false;
		if (!isGoingDown(from, to)) return false;
		shootForward(player);
		return true;
	}

	boolean isGoingDown(Location from, Location to) {
		return to.getY() < from.getY();
	}

	private void shootForward(Player player) {
		JumpPadInfo info = this.playersInJumpUp.get(player.getUniqueId());
		this.playersInJumpUp.remove(player.getUniqueId());
		Vector velocity = new Vector(info.getVelocity().getX(), player.getVelocity().getY(), info.getVelocity().getZ());
		player.setVelocity(velocity);
	}

	private void maybeTellPlayerToReadTheRules(Player player) {
		if (shouldReadRules(player)) {
			player.sendMessage("Please read the global rules (/rules) to get access to the jump pads.");
			mustReadRules(player, true);
		}
	}

	void playerCanJump(Player player, boolean canJump) {
		if (canJump){
			if (hasTemporaryJumpPause(player)) {
				this.playersWithTemporaryJumpPause.remove(player.getUniqueId());
			}
		} else {
			if (!hasTemporaryJumpPause(player)) {
				this.playersWithTemporaryJumpPause.put(player.getUniqueId(), player.getUniqueId());
			}
		}
	}

	private void mustReadRules(Player player, boolean mustReadRules) {
		if (mustReadRules) {
			if (!shouldReadRules(player)) {
				this.playersThatHasBeenInformedToReadTheRules.put(player.getUniqueId(), player.getUniqueId());
			}
		} else {
			if (shouldReadRules(player)) {
				this.playersThatHasBeenInformedToReadTheRules.remove(player.getUniqueId());
			}
		}
	}

	private boolean shouldReadRules(Player player) {
		return !this.playersThatHasBeenInformedToReadTheRules.containsKey(player.getUniqueId());
	}

	private boolean hasTemporaryJumpPause(Player player) {
		return this.playersWithTemporaryJumpPause.containsKey(player.getUniqueId());
	}

	private boolean hasReadRules(Player player) {
		return player.hasPermission("jumppad.jump");
	}

	boolean isInAir(Player player) {
		return this.playersInJumpUp.containsKey(player.getUniqueId());
	}
}
