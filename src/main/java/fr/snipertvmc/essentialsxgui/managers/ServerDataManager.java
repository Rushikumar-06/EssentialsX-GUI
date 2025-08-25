package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGServer;
import fr.snipertvmc.essentialsxgui.utilities.type.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerDataManager {


	// -------------------------------------------------- //


	private final File dataFolder;


	// -------------------------------------------------- //


	public ServerDataManager() {

		this.dataFolder = new File(Main.getInstance().getDataFolder(), "data/");
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
	}


	// -------------------------------------------------- //


	public Map<String, Object> generateServerData() {

		Map<String, Object> serverData = new HashMap<>();
		Map<String, Object> serverKits = new HashMap<>();

		Set<String> essentialsKits = Main.getInstance().getEssentials().getKits().getKitKeys();

		for (String kitName : essentialsKits) {
			serverKits.put(kitName, new HashMap<>() {{
				put("displayName", kitName);
				put("material", "CHEST");
				put("data", "0");
			}});
		}

		serverData.put("kits", serverKits);
		return serverData;
	}


	// -------------------------------------------------- //


	public Map<String, Object> loadServerData() {

		File file = new File(dataFolder, "server.json");
		if (!file.exists()) {
			return generateServerData();
		}

		try {
			String json = Files.readString(file.toPath());
			return JsonUtils.jsonToMap(json);

		} catch (IOException e) {
			throw new RuntimeException("Error when reading data for the server", e);
		}
	}


	public void saveServerData(Map<String, Object> data) {

		File file = new File(dataFolder, "server.json");

		try {
			String json = JsonUtils.mapToJson(data);
			Files.writeString(file.toPath(), json);

		} catch (IOException e) {
			throw new RuntimeException("Error when saving data for the server", e);
		}
	}


	// -------------------------------------------------- //


	public void cleanServerData() {

		EXGServer exgServer = Main.getInstance().getEXGServer();


		// HOMES CLEANING
		Set<String> essentialsKits = Main.getInstance().getEssentials().getKits().getKitKeys();
		Set<EXGKit> serverDataKits = exgServer.getKits();

		Set<EXGKit> cleanedHomes = new HashSet<>();

		for (EXGKit home : serverDataKits) {
			if (essentialsKits.contains(home.getName())) {
				cleanedHomes.add(home);
			}
		}

		for (String kitName : essentialsKits) {
			if (serverDataKits.stream().noneMatch(home -> home.getName().equals(kitName))) {
				cleanedHomes.add(new EXGKit(kitName));
			}
		}

		exgServer.setKits(cleanedHomes);
	}


	// -------------------------------------------------- //
}
