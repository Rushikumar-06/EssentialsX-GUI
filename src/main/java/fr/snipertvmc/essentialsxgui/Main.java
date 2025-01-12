package fr.snipertvmc.essentialsxgui;

import com.earth2me.essentials.Essentials;
import fr.snipertvmc.essentialsxgui.managers.HookManager;
import fr.snipertvmc.essentialsxgui.managers.LoadingManager;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.RegisterUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {


	// -------------------------------------------------- //


	private static Main instance;

	private HookManager hookManager;
	private LoadingManager loadingManager;


	// -------------------------------------------------- //


	@Override
	public void onEnable() {


		// PLUGIN LOADING
		long startTime = System.currentTimeMillis();

		ConsoleLogger.console("");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Plugin loading...");


		// GLOBAL VARIABLES INITIALIZATION
		instance = this;

		hookManager = new HookManager();
		loadingManager = new LoadingManager();


		// SERVER CONFIGURATION ANALYSIS
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Analyzing server configuration...");
		if (!loadingManager.isServerReady()) {
			return;
		}
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Server configuration analysis §fcompleted§7.");


		// GLOBAL DATA INITIALIZATION
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Initialisation of global data...");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Initialization of global data §fcompleted§7.");


		// TASKS INITIALIZATION
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Initialising tasks...");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Tasks initialisation §fcompleted§7.");


		// COMMANDS REGISTRATION
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Registering commands...");
		int registeredCommands = RegisterUtils.registerCommands("fr.snipertvmc.essentialxgui.commands");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Registration of §f" + registeredCommands + " commands§7.");


		// EVENTS REGISTRATION
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Registration of events...");
		int registeredEvents = RegisterUtils.registerEvents("fr.snipertvmc.essentialxgui.events");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Registration of §f" + registeredEvents + " events§7.");


		// PLUGIN LOADING COMPLETED
		long endTime = System.currentTimeMillis();
		long loadingTime = endTime - startTime;

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7The plugin has been §floaded §7correctly in §f" + loadingTime + "ms§7.");
		ConsoleLogger.console("");

	}


	// -------------------------------------------------- //


	@Override
	public void onDisable() {


		// PLUGIN UNLOADING
		long startTime = System.currentTimeMillis();

		ConsoleLogger.console("");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Plugin unloading...");


		// FINAL DATA SAVING
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Final data saving...");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Final data saving §fcompleted§7.");


		// DÉCHARGEMENT TERMINÉ DU PLUGIN
		long endTime = System.currentTimeMillis();
		long unloadingTime = endTime - startTime;

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7The plugin has §funloaded §7correctly in §f" + unloadingTime + "ms§7.");
		ConsoleLogger.console("");
	}


	// -------------------------------------------------- //


	public static Main getInstance() {
		return instance;
	}

	public HookManager getHookManager() {
		return hookManager;
	}
	public LoadingManager getLoadingManager() {
		return loadingManager;
	}


	// -------------------------------------------------- //


	// SHORTCUTS

	public Essentials getEssentials() {
		return hookManager.getEssentialsHook().getEssentials();
	}


	// -------------------------------------------------- //
}