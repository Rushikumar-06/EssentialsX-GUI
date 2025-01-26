package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGServer;

import java.util.*;

public class ServerManager {


	// -------------------------------------------------- //


	public EXGServer initialize() {

		EXGServer exgServer = new EXGServer();

		Map<String, Object> serverData = Main.getInstance().getServerDataManager().loadServerData();
		Map<String, Object> kits = (Map<String, Object>) serverData.get("kits");
		exgServer.setKitsRaw(kits);

		return exgServer;
	}


	public void save() {

		EXGServer exgServer = Main.getInstance().getEXGServer();

		Map<String, Object> kits = exgServer.getKitsRaw();
		Map<String, Object> serverData = new HashMap<>() {{
			put("kits", kits);
		}};
		Main.getInstance().getServerDataManager().saveServerData(serverData);
	}


	// -------------------------------------------------- //
}
