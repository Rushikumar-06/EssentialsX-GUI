package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGServer;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;

import java.util.Map;

public class ServerManager {


	// -------------------------------------------------- //


	public EXGServer initialize() {

		EXGServer exgServer = new EXGServer();

		Map<String, Object> kitsRaw = Main.getInstance().getDatabaseManager().getKitsTableManager().fetchAllKits();
		if (kitsRaw.isEmpty()) {
			kitsRaw = Main.getInstance().getServerDataManager().generateServerKits();
		}

		exgServer.setKitsRaw(kitsRaw);
		return exgServer;
	}


	public void save() {

		EXGServer exgServer = Main.getInstance().getEXGServer();

		if (exgServer == null) {
			return;
		}

		Map<String, Object> kitsRaw = exgServer.getKitsRaw();
		Main.getInstance().getDatabaseManager().getKitsTableManager().updateKits(kitsRaw);
	}


	// -------------------------------------------------- //
}
