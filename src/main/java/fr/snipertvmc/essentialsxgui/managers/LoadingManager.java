package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.MCServerVersion;
import fr.snipertvmc.essentialsxgui.libraries.bstats.Metrics;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInvManager;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.RegisterUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.UpdateUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

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


		// DATABASES CONNECTION
		Main.getInstance().getDatabaseManager().connectAllDatabases();


		// SERVER CONFIGURATION ANALYSIS
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Analyzing server configuration...");
		if (!isServerReady()) {
			return false;
		}
		checkForServerVersionSupport();
		startUpdateCheckerTask();
		checkForUpdates(false);
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Server configuration analysis §fcompleted§7."); }


		// GLOBAL DATA INITIALIZATION
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Initialisation of global data..."); }
		FastInvManager.register(Main.getInstance());
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Initialization of global data §fcompleted§7."); }


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


		// CHECK IF THE PLUGIN IS NOT READY
		if (!pluginReady) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cThe plugin is already unloaded.");
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cWell... it's impossible to unload an unloaded plugin ¯\\_(ツ)_/¯");
			return;
		}


		// STOP TASKS
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Stopping tasks..."); }
		stopUpdateCheckerTask();
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Tasks stopping §fcompleted§7."); }


		// FINAL DATA SAVING
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Data saving..."); }
		Main.getInstance().getPlayerManager().saveAll();
		Main.getInstance().getServerManager().save();
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Data saving §fcompleted§7."); }


		// DATABASES DISCONNECTION
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Disconnecting databases..."); }
		Main.getInstance().getDatabaseManager().disconnectAllDatabases();
		if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7Databases disconnection §fcompleted§7."); }
	}


	// -------------------------------------------------- //


	public boolean isServerReady() {

		String essentialsVersionRequired = Main.getInstance().getHookManager().getEssentialsHook().getMinimumVersionRequired();

		if (Main.getInstance().getEssentials() == null) {

			ConsoleLogger.console("\t§6EssentialsX-GUI: §cEssentialsX is not installed on the server.");
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cPlease install EssentialsX (" + essentialsVersionRequired + ") to use this plugin.");
			return false;
		}

		boolean essentialsVersionSupported = Main.getInstance().getHookManager().getEssentialsHook().isEssentialsVersionSupported();
		String version = Main.getInstance().getEssentials().getDescription().getVersion();
		String essentialsVersionColor = essentialsVersionSupported ? "§a" : "§c";

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7EssentialsX version found: " + essentialsVersionColor + version);

		if (!essentialsVersionSupported) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cCurrent version is not supported. §4(Minimum version required: §4" + essentialsVersionRequired + ")");
			return false;
		}

		ConsoleLogger.console("\t§6EssentialsX-GUI: §aThis EssentialsX version is supported by EssentialsX-GUI.");
		return true;
	}


	public void checkForServerVersionSupport() {

		MCServerVersion serverVersion = Main.getInstance().getMCServerVersion();
		String serverVersionColor = serverVersion.isFullySupported() ? "§a" : "§6";
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


	// -------------------------------------------------- //


	private boolean updateAvailable = false;
	private String latestVersionAvailable = null;


	public void checkForUpdates(boolean dontFlood) {

		String currentVersion = Main.getInstance().getDescription().getVersion();
		if (currentVersion.contains("-dev") && !dontFlood) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §5You are using a development version of EssentialsX-GUI.");
			ConsoleLogger.console("\t§6EssentialsX-GUI: §dSome features may not work as expected, and bugs may be present.");
			return;
		}

		if (!Main.getInstance().getConfiguration().checkForUpdates()) {
			return;
		}

		String latestVersionAvailable = UpdateUtils.getLatestVersionTag();
		if (latestVersionAvailable == null) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cFailed to check for updates.");
			return;
		}

		if (currentVersion.equals(latestVersionAvailable)) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §7You are using the §alatest §7version of EssentialsX-GUI.");
			updateAvailable = false;
			this.latestVersionAvailable = null;

		} else {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §eA new version of EssentialsX-GUI is available: §f" + latestVersionAvailable);
			ConsoleLogger.console("\t§6EssentialsX-GUI: §6Please update to the latest version for new features and bug fixes.");

			updateAvailable = true;
			this.latestVersionAvailable = latestVersionAvailable;
		}
	}


	public void alertPlayerForUpdate(Player player) {

		if (!updateAvailable) {
			return;
		}

		if (!Main.getInstance().getConfiguration().canReceiveUpdateAlert(player)) {
			return;
		}

		player.sendMessage(MessagesUtils.get(EXGMessage.ALERT_UPDATE_AVAILABLE, Map.of(
				"currentVersion", Main.getInstance().getDescription().getVersion(),
				"latestVersion", latestVersionAvailable
		)));
	}


	// -------------------------------------------------- //


	private BukkitTask checkForUpdatesTask = null;


	public void startUpdateCheckerTask() {

		if (checkForUpdatesTask != null) {
			return;
		}

		long checkIntervalTicks = 20L * 60L * 60L;

		checkForUpdatesTask = Main.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(
				Main.getInstance(),
				() -> checkForUpdates(true),
				checkIntervalTicks,
				checkIntervalTicks
		);
	}


	public void stopUpdateCheckerTask() {

		if (checkForUpdatesTask == null) {
			return;
		}

		checkForUpdatesTask.cancel();
		checkForUpdatesTask = null;
	}


	// -------------------------------------------------- //


	private void loadMetricsCharts() {


		// EssentialsX Version Chart
		Main.getInstance().getMetrics().addCustomChart(
				new Metrics.SimplePie("essentialsx_version", () -> Main.getInstance().getEssentials() != null ?
						Main.getInstance().getEssentials().getDescription().getVersion() : "Other")
		);


		// Storage Type Chart
		Main.getInstance().getMetrics().addCustomChart(
				new Metrics.SimplePie("storage_type", () -> Main.getInstance().getConfiguration().getStorageType() != null ?
						Main.getInstance().getConfiguration().getStorageType() : "Other")
		);
	}


	// -------------------------------------------------- //


	public boolean isPluginReady() {
		return pluginReady;
	}


	// -------------------------------------------------- //
}
