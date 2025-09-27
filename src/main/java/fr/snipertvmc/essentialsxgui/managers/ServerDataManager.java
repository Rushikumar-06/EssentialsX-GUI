package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGServer;
import fr.snipertvmc.essentialsxgui.utilities.serializers.ItemStackSerializer;
import fr.snipertvmc.essentialsxgui.utilities.type.JsonUtils;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerDataManager {


	// -------------------------------------------------- //


	public Map<String, Object> generateServerKits() {

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


	// -------------------------------------------------- //


	public void cleanServerKits() {

		EXGServer exgServer = Main.getInstance().getEXGServer();


		// HOMES CLEANING
		Set<String> essentialsKits = Main.getInstance().getEssentials().getKits().getKitKeys();
		Set<EXGKit> serverKits = exgServer.getKits();

		Set<EXGKit> cleanedKits = new HashSet<>();

		for (EXGKit kit : serverKits) {
			if (essentialsKits.contains(kit.getName())) {
				cleanedKits.add(kit);
			}
		}

		for (String kitName : essentialsKits) {
			if (serverKits.stream().noneMatch(home -> home.getName().equals(kitName))) {
				cleanedKits.add(new EXGKit(kitName));
			}
		}

		exgServer.setKits(cleanedKits);
	}


	// -------------------------------------------------- //
}
