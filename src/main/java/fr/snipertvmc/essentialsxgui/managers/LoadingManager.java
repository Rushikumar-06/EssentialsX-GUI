package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;

public class LoadingManager {


	// -------------------------------------------------- //


	public boolean isServerReady() {

		if (Main.getInstance().getEssentials() == null) {

			ConsoleLogger.console("\t§6EssentialsX-GUI: §cEssentialsX is not installed on the server.");
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cPlease install EssentialsX (2.20.1+) to use this plugin.");
			ConsoleLogger.console("");

			Main.getInstance().getServer().getPluginManager().disablePlugin(Main.getInstance());
			return false;
		}

		String version = Main.getInstance().getEssentials().getDescription().getVersion();

		ConsoleLogger.console("\t§6EssentialsX-GUI: §aEssentialsX is installed on the server.");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §aVersion found: §f" + version);

		return true;
	}


	// -------------------------------------------------- //
}
