package se.fredsfursten.freebuildplugin;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class FreeBuilders {
	private static final String FILE_PATH = "plugins/JumpPad/jumppad_locations.bin";
	private static FreeBuilders singleton = null;

	private HashMap<UUID, FreeBuilderInfo> freeBuilders = null;

	private FreeBuilders() {
		this.freeBuilders = new HashMap<UUID, FreeBuilderInfo>();
	}

	static FreeBuilders get() {
		if (singleton == null) {
			singleton = new FreeBuilders();
		}
		return singleton;
	}

	void add(Player player) {
		UUID id = player.getUniqueId();
		if (this.freeBuilders.containsKey(id)) return;
		this.freeBuilders.put(player.getUniqueId(), new FreeBuilderInfo(player));
	}

	void remove(Player player) {
		UUID id = player.getUniqueId();
		if (!this.freeBuilders.containsKey(id)) return;
		this.freeBuilders.remove(id);
	}

	FreeBuilderInfo get(Player player) {
		UUID id = player.getUniqueId();
		if (!this.freeBuilders.containsKey(id)) return null;
		return this.freeBuilders.get(id);
	}

	boolean isFreeBuilder(Player player) {
		return get(player) != null;
	}
}
