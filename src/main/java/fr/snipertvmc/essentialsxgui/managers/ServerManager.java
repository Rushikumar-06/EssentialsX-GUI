package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGServer;
import fr.snipertvmc.essentialsxgui.utilities.data.DataConverter;

import java.util.Map;

public class ServerManager {


	// -------------------------------------------------- //


	private final EXGServer exgServer;


	// -------------------------------------------------- //


	public EXGServer getEXGServer() {
		return exgServer;
	}


	// -------------------------------------------------- //


	public ServerManager() {

		EXGServer exgServer = new EXGServer();
		this.exgServer = exgServer;


		// KITS LOADING
		Map<String, Object> kitsRaw;

		// Legacy data conversion if needed
		if (DataConverter.isLegacyServerDataFilePresent()) {
			kitsRaw = (Map<String, Object>) DataConverter.getLegacyServerData().get("kits");
			DataConverter.deleteLegacyServerDataFile();
			DataConverter.tryToRemoveLegacyDataFolders();

		} else {
			kitsRaw = Main.getInstance().getDatabaseManager().getKitsTableManager().fetchKits();
			if (kitsRaw.isEmpty()) {
				kitsRaw = Main.getInstance().getServerDataManager().generateDefaultKitsData();
			}
		}

		exgServer.setKitsRaw(kitsRaw);
	}


	public void save() {

		EXGServer exgServer = Main.getInstance().getEXGServer();
		if (exgServer == null) {
			return;
		}


		// KITS SAVING
		Map<String, Object> kitsRaw = exgServer.getKitsRaw();
		Main.getInstance().getDatabaseManager().getKitsTableManager().updateKits(kitsRaw);
	}


	// -------------------------------------------------- //
}
