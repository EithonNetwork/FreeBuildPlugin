package se.fredsfursten.freebuildplugin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import se.fredsfursten.plugintools.IJson;
import se.fredsfursten.plugintools.IUuidAndName;
import se.fredsfursten.plugintools.Json;

class FreeBuilderInfo implements IJson<FreeBuilderInfo>, IUuidAndName {
	private UUID playerId;
	private String playerName;
	
	FreeBuilderInfo(Player player)
	{
		this.playerId = player.getUniqueId();
		this.playerName = player.getName();
	}
	
	FreeBuilderInfo() {
	}
	
	Player getPlayer()
	{
		return Bukkit.getServer().getPlayer(this.playerId);
	}
	
	public String getName()
	{
		return this.playerName;
	}

	public String toString() {
		return String.format("%s", this.getName());
	}

	@Override
	public FreeBuilderInfo factory() {
		return new FreeBuilderInfo();
	}

	@Override
	public UUID getUniqueId() {
		return this.playerId;
	}

	@Override
	public Object toJson() {
		return Json.fromPlayer(this.playerId, this.playerName);
	}

	@Override
	public void fromJson(Object json) {
		this.playerId = Json.toPlayerId((JSONObject) json);
		this.playerName = Json.toPlayerName((JSONObject) json);
		
	}
}
