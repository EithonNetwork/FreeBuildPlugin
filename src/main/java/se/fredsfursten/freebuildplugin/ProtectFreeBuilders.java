package se.fredsfursten.freebuildplugin;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class ProtectFreeBuilders implements Listener {
	private static ProtectFreeBuilders singleton = null;

	private FreeBuilders freeBuilders = null;

	private ProtectFreeBuilders() {
	}

	static ProtectFreeBuilders get()
	{
		if (singleton == null) {
			singleton = new ProtectFreeBuilders();
		}
		return singleton;
	}

	void enable(){
		freeBuilders = FreeBuilders.get();
	}

	void disable() {
	}
}
