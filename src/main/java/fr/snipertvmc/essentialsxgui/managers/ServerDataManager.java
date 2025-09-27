package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGServer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerDataManager {


	// -------------------------------------------------- //


	public Map<String, Object> generateDefaultKitsData() {

		Map<String, Object> serverKits = new HashMap<>();

		Set<String> essentialsKits = Main.getInstance().getEssentials().getKits().getKitKeys();

		for (String kitName : essentialsKits) {
			serverKits.put(kitName, new HashMap<>() {{
				put("displayName", kitName);
				put("material", "CHEST");
				put("data", "0");
				put("customItemStack", null);
			}});

			if (!Main.getInstance().getDatabaseManager().getKitsTableManager().isKitExists(kitName)) {
				Main.getInstance().getDatabaseManager().getKitsTableManager().insertKit(kitName, (Map<String, Object>) serverKits.get(kitName));
			}
		}

		return serverKits;
	}


	public void updateServerKits() {

		EXGServer exgServer = Main.getInstance().getEXGServer();

		Set<String> essentialsKits = Main.getInstance().getEssentials().getKits().getKitKeys();
		Set<EXGKit> serverKits = exgServer.getKits();

		Set<EXGKit> updatedKits = new HashSet<>();

		for (EXGKit kit : serverKits) {
			if (essentialsKits.contains(kit.getName())) {
				updatedKits.add(kit);
			}
		}

		for (String kitName : essentialsKits) {
			if (serverKits.stream().noneMatch(home -> home.getName().equals(kitName))) {
				updatedKits.add(new EXGKit(kitName));
			}
		}

		exgServer.setKits(updatedKits);
	}


	// -------------------------------------------------- //
}
