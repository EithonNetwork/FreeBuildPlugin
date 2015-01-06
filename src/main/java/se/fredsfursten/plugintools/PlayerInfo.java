package se.fredsfursten.plugintools;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PlayerInfo<T extends Object> {
	private HashMap<UUID, T> playerInfo = null;

	public PlayerInfo() {
		this.playerInfo = new HashMap<UUID, T>();
	}
	
	public void put(Player player, T info) {
		UUID id = player.getUniqueId();
		this.playerInfo.put(id, info);
	}
	
	public T get(Player player) {
		UUID id = player.getUniqueId();
		return this.playerInfo.get(id);
	}
	
	public boolean hasInformation(Player player) {
		UUID id = player.getUniqueId();
		return this.playerInfo.containsKey(id);
	}
	
	public void remove(Player player) {
		UUID id = player.getUniqueId();
		this.playerInfo.remove(id);
	}
}