package se.fredsfursten.jumppadplugin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

class JumpPadInfo {
	private Vector velocity;
	private Location location;
	private String name;
	private UUID creatorId;
	private String creatorName;
	
	JumpPadInfo(String name, Location location, Vector velocity, UUID creatorId, String creatorName)
	{
		this.velocity = velocity;
		this.name = name;
		this.location = location;
		this.creatorId = creatorId;
		this.creatorName = creatorName;
	}
	
	public static JumpPadInfo createJumpPadInfo(StorageModel storageModel)
	{
		Player creator = storageModel.getCreator();
		return new JumpPadInfo(storageModel.getName(), storageModel.getLocation(), storageModel.getVelocity(), creator.getUniqueId(), creator.getName());
	}
	
	Vector getVelocity() {
		return this.velocity;
	}
	
	String getName() {
		return this.name;
	}
	
	Location getLocation() {
		return this.location;
	}
	
	String getBlockHash() {
		return JumpPadInfo.toBlockHash(this.location);
	}

	static String toBlockHash(Location location)
	{
		return toBlockHash(location.getBlock());
	}

	static String toBlockHash(Block block)
	{
		return String.format("%d;%d;%d", block.getX(), block.getY(), block.getZ());
	}
	
	Player getCreator()
	{
		return Bukkit.getServer().getPlayer(this.creatorId);
	}
	
	String getCreatorName() {
		return this.creatorName;
	}
	
	UUID getCreatorId() {
		return this.creatorId;
	}

	StorageModel getStorageModel() {
		return new StorageModel(getName(), getLocation().getBlock(), getVelocity(), getCreatorId(), getCreatorName());

	}

	public String toString() {
		return String.format("%s (%s): from %s with velocity %s", getName(), getCreatorName(), getLocation().getBlock().toString(), getVelocity().toString());
	}
}
