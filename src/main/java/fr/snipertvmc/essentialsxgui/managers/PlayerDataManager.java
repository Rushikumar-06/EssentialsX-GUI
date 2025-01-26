package fr.snipertvmc.essentialsxgui.managers;

import com.earth2me.essentials.User;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;
import fr.snipertvmc.essentialsxgui.utilities.data.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class PlayerDataManager {


	// -------------------------------------------------- //


	private final File dataFolder;


	// -------------------------------------------------- //


	public PlayerDataManager() {

		this.dataFolder = new File(Main.getInstance().getDataFolder(), "data");
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
	}


	// -------------------------------------------------- //


	public Map<String, Object> generatePlayerData(String uuid) {

		Map<String, Object> playerData = new HashMap<>();
		Map<String, Object> playerDataHomes = new HashMap<>();

		List<String> essentialsHomes = Main.getInstance().getEssentials().getUser(UUID.fromString(uuid)).getHomes();

		for (String homeName : essentialsHomes) {
			playerDataHomes.put(homeName, new HashMap<>() {{
				put("displayName", homeName);
				put("material", "GRASS");
			}});
		}

		playerData.put("homes", playerDataHomes);

		return playerData;
	}


	// -------------------------------------------------- //


	public Map<String, Object> loadPlayerData(String uuid) {

		File file = new File(dataFolder, uuid + ".json");
		if (!file.exists()) {
			return generatePlayerData(uuid);
		}

		try {
			String json = Files.readString(file.toPath());
			return JsonUtils.jsonToMap(json);

		} catch (IOException e) {
			throw new RuntimeException("Error when reading data for the player: " + uuid, e);
		}
	}


	public void savePlayerData(String uuid, Map<String, Object> data) {

		File file = new File(dataFolder, uuid + ".json");

		try {
			String json = JsonUtils.mapToJson(data);
			Files.writeString(file.toPath(), json);

		} catch (IOException e) {
			throw new RuntimeException("Error when saving data for the player: " + uuid, e);
		}
	}


	// -------------------------------------------------- //


	public void cleanPlayerData(String uuid) {

		EXGPlayer exgPlayer = Main.getInstance().getPlayerManager().getPlayer(uuid);
		User user = Main.getInstance().getEssentials().getUser(UUID.fromString(uuid));


		// HOMES CLEANING
		List<String> essentialsHomes = user.getHomes();
		Set<EXGHome> playerDataHomes = exgPlayer.getHomes();

		Set<EXGHome> cleanedHomes = new HashSet<>();

		for (EXGHome home : playerDataHomes) {
			if (essentialsHomes.contains(home.getName())) {
				cleanedHomes.add(home);
			}
		}

		for (String homeName : essentialsHomes) {
			if (playerDataHomes.stream().noneMatch(home -> home.getName().equals(homeName))) {
				cleanedHomes.add(new EXGHome(homeName));
			}
		}

		exgPlayer.setHomes(cleanedHomes);
	}


	// -------------------------------------------------- //
}
