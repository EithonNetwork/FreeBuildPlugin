package se.fredsfursten.freebuildplugin;

import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

class FreeBuilderInfo {
	private Date date;
	private UUID playerId;
	private String playerName;
	
	FreeBuilderInfo(Player player)
	{
		this.playerId = player.getUniqueId();
		this.playerName = player.getName();
		this.date = new Date();
	}
	
	Date getDate() {
		return this.date;
	}
	
	Player getPlayer()
	{
		return Bukkit.getServer().getPlayer(this.playerId);
	}
	
	String getName()
	{
		return this.playerName;
	}

	public String toString() {
		return String.format("%s: %s", this.getName(), this.date.toString());
	}
}
