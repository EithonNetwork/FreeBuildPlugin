package se.fredsfursten.jumppadplugin;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class AllJumpPads implements Listener {
	private static final String FILE_PATH = "plugins/JumpPad/jumppad_locations.bin";
	private static AllJumpPads singleton = null;

	private HashMap<String, JumpPadInfo> jumpPadsByBlock = null;
	private HashMap<String, JumpPadInfo> jumpPadsByName = null;
	private JavaPlugin _plugin = null;

	private AllJumpPads() {
	}

	static AllJumpPads get() {
		if (singleton == null) {
			singleton = new AllJumpPads();
		}
		return singleton;
	}

	void add(JumpPadInfo info) {
		this.jumpPadsByBlock.put(info.getBlockHash(), info);
		this.jumpPadsByName.put(info.getName(), info);
	}

	void remove(JumpPadInfo info) {
		this.jumpPadsByName.remove(info.getName());
		this.jumpPadsByBlock.remove(info.getBlockHash());
	}

	Collection<JumpPadInfo> getAll() {
		return this.jumpPadsByName.values();
	}

	JumpPadInfo getByLocation(Location location) {
		if (this.jumpPadsByBlock == null) return null;
		String position = JumpPadInfo.toBlockHash(location);
		if (!this.jumpPadsByBlock.containsKey(position)) return null;
		return this.jumpPadsByBlock.get(position);
	}

	JumpPadInfo getByName(String name) {
		if (!this.jumpPadsByName.containsKey(name)) return null;
		return this.jumpPadsByName.get(name);
	}

	void load(JavaPlugin plugin) {
		this._plugin = plugin;

		this.jumpPadsByBlock = new HashMap<String, JumpPadInfo>();
		this.jumpPadsByName = new HashMap<String, JumpPadInfo>();

		ArrayList<StorageModel> jumpPadStorageList = loadData(plugin);
		if (jumpPadStorageList == null) return;
		rememberAllData(jumpPadStorageList);
		this._plugin.getLogger().info(String.format("Loaded %d JumpPads", jumpPadStorageList.size()));
	}

	private ArrayList<StorageModel> loadData(JavaPlugin plugin) {
		ArrayList<StorageModel> jumpPadStorageList = null;
		try {
			jumpPadStorageList = SavingAndLoadingBinary.load(FILE_PATH);
		} catch (FileNotFoundException e) {
			plugin.getLogger().info("No jump pad data file found.");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			plugin.getLogger().info("Failed to load data.");
			return null;
		}
		return jumpPadStorageList;
	}

	private void rememberAllData(ArrayList<StorageModel> storageModelList) {
		for (StorageModel storageModel : storageModelList) {
			this.add(JumpPadInfo.createJumpPadInfo(storageModel));
		}
	}

	void save() {
		ArrayList<StorageModel> jumpPadStorageList = getAllData();
		boolean success = saveData(jumpPadStorageList);
		if (success) {
			this._plugin.getLogger().info(String.format("Saved %d JumpPads", jumpPadStorageList.size()));
		} else {
			this._plugin.getLogger().info("Failed to save data.");			
		}
	}

	private ArrayList<StorageModel> getAllData() {
		ArrayList<StorageModel> jumpPadStorageList = new ArrayList<StorageModel>();
		for (JumpPadInfo jumpPadInfo : getAll()) {
			jumpPadStorageList.add(jumpPadInfo.getStorageModel());
		}
		return jumpPadStorageList;
	}

	private boolean saveData(ArrayList<StorageModel> jumpPadStorageList) {
		try {
			SavingAndLoadingBinary.save(jumpPadStorageList, FILE_PATH);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
