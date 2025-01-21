package fr.snipertvmc.essentialsxgui.managers;

import fr.mrmicky.fastinv.FastInvManager;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.RegisterUtils;

public class LoadingManager {


	// -------------------------------------------------- //


	private boolean pluginReady = false;


	// -------------------------------------------------- //


	public void loadPlugin(boolean detailedLoading) {


		// CHECK IF THE PLUGIN IS READY
		if (pluginReady) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cThe plugin is already loaded.");
			return;
		}


		// SERVER CONFIGURATION ANALYSIS
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Analyzing server configuration...");
		if (!isServerReady()) {
			return;
		}
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Server configuration analysis §fcompleted§7.");

		// GLOBAL DATA INITIALIZATION
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Initialisation of global data..."); }
		FastInvManager.register(Main.getInstance());
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Initialization of global data §fcompleted§7."); }


		// TASKS INITIALIZATION
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Initialising tasks..."); }
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Tasks initialisation §fcompleted§7."); }


		// COMMANDS REGISTRATION
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Registering commands..."); }
		int registeredCommands = RegisterUtils.registerCommands("fr.snipertvmc.essentialsxgui.commands");
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Registration of §f" + registeredCommands + " commands§7."); }


		// EVENTS REGISTRATION
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Registration of events..."); }
		int registeredEvents = RegisterUtils.registerEvents("fr.snipertvmc.essentialsxgui.events");
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Registration of §f" + registeredEvents + " events§7."); }


		// PLUGIN LOADING COMPLETED
		pluginReady = true;
	}


	// -------------------------------------------------- //


	public void unloadPlugin(boolean detailedLoading) {


		// FINAL DATA SAVING
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Final data saving..."); }
		Main.getInstance().getPlayerManager().saveAll();
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Final data saving §fcompleted§7."); }
	}


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


	public boolean isPluginReady() {
		return pluginReady;
	}


	// -------------------------------------------------- //
}
