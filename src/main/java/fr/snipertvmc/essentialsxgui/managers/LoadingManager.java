package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.libraries.bstats.Metrics;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInvManager;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.MCServerVersion;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.RegisterUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.UpdateUtils;

public class LoadingManager {


	// -------------------------------------------------- //


	private boolean pluginReady = false;


	// -------------------------------------------------- //


	public boolean loadPlugin(boolean detailedLoading) {


		// CHECK IF THE PLUGIN IS READY
		if (pluginReady) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cThe plugin is already loaded.");
			return false;
		}


		// SERVER CONFIGURATION ANALYSIS
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Analyzing server configuration...");
		checkForServerVersionSupport();
		if (!isServerReady()) {
			return false;
		}
		checkForUpdates();
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


		// METRICS CHARTS LOADING
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Loading metrics charts..."); }
		loadMetricsCharts();
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Metrics charts loading §fcompleted§7."); }


		// PLUGIN LOADING COMPLETED
		pluginReady = true;
		return true;
	}


	// -------------------------------------------------- //


	public void unloadPlugin(boolean detailedLoading) {


		// FINAL DATA SAVING
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Final data saving..."); }
		Main.getInstance().getPlayerManager().saveAll();
		Main.getInstance().getServerManager().save();
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


	public void checkForServerVersionSupport() {

		MCServerVersion serverVersion = Main.getInstance().getMCServerVersion();
		String serverVersionColor = serverVersion.isFullySupported() ? "§2" : "§6";
		serverVersionColor = serverVersion == MCServerVersion.UnknownVersion ? "§4" : serverVersionColor;

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Server version found: " + serverVersionColor + serverVersion.getVersionName());

		if (serverVersion == MCServerVersion.UnknownVersion) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cBe very careful, the version has not been recognized or is not supported.");
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cPlease check the documentation for more information about this error.");
			return;
		}

		if (serverVersion.isDeprecated()) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §eThis server version is deprecated and could be removed in the future.");
			ConsoleLogger.console("\t§6EssentialsX-GUI: §ePlease consider updating to a newer version.");
			ConsoleLogger.console("\t§6EssentialsX-GUI: §eA list of fully supported versions is available on the plugin page.");
			return;
		}

		if (!serverVersion.isFullySupported()) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §eBe aware that this version of EssentialsX-GUI could");
			ConsoleLogger.console("\t§6EssentialsX-GUI: §enot be fully compatible with your server version.");
			ConsoleLogger.console("\t§6EssentialsX-GUI: §eA list of fully supported versions is available on the plugin page.");
			return;
		}

		ConsoleLogger.console("\t§6EssentialsX-GUI: §aThis server version is fully supported by EssentialsX-GUI.");
		return;
	}


	public void checkForUpdates() {

		if (!Main.getInstance().getConfiguration().checkForUpdates()) {
			return;
		}

		// TODO: Just keep the getLatestPublicVersionTag() method, remove the getLatestReleaseVersionTag() method.
		//       This is a temporary solution to avoid the 404 error while there is no public version available.
		String latestVersionAvailable = UpdateUtils.getLatestPublicVersionTag() != null ? UpdateUtils.getLatestPublicVersionTag() : UpdateUtils.getLatestReleaseVersionTag();
		if (latestVersionAvailable == null) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cFailed to check for updates.");
			return;
		}

		String currentVersion = Main.getInstance().getDescription().getVersion();
		if (currentVersion.equals(latestVersionAvailable)) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §aYou are using the latest version of EssentialsX-GUI.");

		} else {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §6A new version of EssentialsX-GUI is available: §f" + latestVersionAvailable);
			ConsoleLogger.console("\t§6EssentialsX-GUI: §ePlease update to the latest version for new features and bug fixes.");
		}
	}


	// -------------------------------------------------- //


	private void loadMetricsCharts() {


		// EssentialsX Version Chart
		Main.getInstance().getMetrics().addCustomChart(
				new Metrics.SimplePie("essentialsx_version", () -> Main.getInstance().getEssentials() != null ?
						Main.getInstance().getEssentials().getDescription().getVersion() : "Other")
		);
	}


	// -------------------------------------------------- //


	public boolean isPluginReady() {
		return pluginReady;
	}


	// -------------------------------------------------- //
}
