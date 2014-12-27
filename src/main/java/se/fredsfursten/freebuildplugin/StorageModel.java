package se.fredsfursten.jumppadplugin;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

class StorageModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private double velocityX;
	private double velocityY;
	private double velocityZ;
	private double blockX;
	private double blockY;
	private double blockZ;
	private UUID worldId;
	private String name;
	private UUID creatorId;
	private String creatorName;
	
	public StorageModel(String name, Block block, Vector velocity, UUID creatorId, String creatorName)
	{
		this.name = name;
		this.blockX =block.getX();
		this.blockY = block.getY();
		this.blockZ = block.getZ();
		this.worldId = block.getWorld().getUID();
		this.velocityX = velocity.getX();
		this.velocityY = velocity.getY();
		this.velocityZ = velocity.getZ();
		this.creatorId = creatorId;
		this.creatorName = creatorName;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public World getWorld()
	{
		return Bukkit.getServer().getWorld(this.worldId);
	}
	
	public Block getBlock()
	{
		return getLocation().getBlock();
	}
	
	public Location getLocation()
	{
		return new Location(getWorld(), this.blockX, this.blockY, this.blockZ);
	}
	
	public Vector getVelocity()
	{
		return new Vector(this.velocityX, this.velocityY, this.velocityZ);
	}
	
	public Player getCreator()
	{
		Player creator = Bukkit.getServer().getPlayer(this.creatorId);
		return creator;
	}
	
	public String getCreatorName()
	{
		Player creator = getCreator();
		if (creator != null){
			this.creatorName = creator.getName();
		}
		return this.creatorName;
	}
}
