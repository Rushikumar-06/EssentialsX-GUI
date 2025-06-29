package fr.snipertvmc.essentialsxgui;

import com.earth2me.essentials.Essentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.MCServerVersion;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGServer;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.ConfigurationFile;
import fr.snipertvmc.essentialsxgui.managers.*;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {


	// -------------------------------------------------- //


	private static Main instance;

	private ObjectMapper objectMapper;

	private EXGServer exgServer;

	private ChatManager chatManager;
	private FilesManager filesManager;
	private HookManager hookManager;
	private InventoriesManager inventoriesManager;
	private LoadingManager loadingManager;
	private PlayerDataManager playerDataManager;
	private PlayerManager playerManager;
	private ServerDataManager serverDataManager;
	private ServerManager serverManager;

	private MCServerVersion mcServerVersion;


	// -------------------------------------------------- //


	@Override
	public void onEnable() {


		// PLUGIN LOADING
		long startTime = System.currentTimeMillis();

		ConsoleLogger.console("");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Plugin loading...");


		// GLOBAL VARIABLES INITIALIZATION
		instance = this;

		objectMapper = new ObjectMapper();

		chatManager = new ChatManager();
		hookManager = new HookManager();
		inventoriesManager = new InventoriesManager();
		filesManager = new FilesManager();
		loadingManager = new LoadingManager();
		playerDataManager = new PlayerDataManager();
		playerManager = new PlayerManager();
		serverDataManager = new ServerDataManager();
		serverManager = new ServerManager();

		mcServerVersion = MCServerVersion.getMCServerVersion();


		// FILES LOADING
		filesManager.loadFiles();


		// LOAD PLUGIN
		boolean cancelLoading = false;
		if (!loadingManager.loadPlugin(filesManager.getConfiguration().isDetailedLoading())) {
			cancelLoading = true;
		}


		// SERVER INITIALIZATION
		exgServer = serverManager.initialize();


		// PLUGIN LOADING COMPLETED
		long endTime = System.currentTimeMillis();
		long loadingTime = endTime - startTime;

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7The plugin has been §floaded §7correctly in §f" + loadingTime + "ms§7.");
		ConsoleLogger.console("");

		if (cancelLoading) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cPlugin will be disabled due to loading errors.");
			ConsoleLogger.console("");
			getServer().getPluginManager().disablePlugin(this);
		}
	}


	// -------------------------------------------------- //


	@Override
	public void onDisable() {


		// PLUGIN UNLOADING
		long startTime = System.currentTimeMillis();

		ConsoleLogger.console("");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Plugin unloading...");


		// UNLOAD PLUGIN
		loadingManager.unloadPlugin(filesManager.getConfiguration().isDetailedLoading());


		// PLUGIN UNLOADING COMPLETED
		long endTime = System.currentTimeMillis();
		long unloadingTime = endTime - startTime;

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7The plugin has been §funloaded §7correctly in §f" + unloadingTime + "ms§7.");
		ConsoleLogger.console("");
	}


	// -------------------------------------------------- //


	public static Main getInstance() {
		return instance;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public EXGServer getEXGServer() {
		return exgServer;
	}

	public ChatManager getChatManager() {
		return chatManager;
	}
	public FilesManager getFilesManager() {
		return filesManager;
	}
	public HookManager getHookManager() {
		return hookManager;
	}
	public InventoriesManager getInventoriesManager() {
		return inventoriesManager;
	}
	public LoadingManager getLoadingManager() {
		return loadingManager;
	}
	public PlayerDataManager getPlayerDataManager() {
		return playerDataManager;
	}
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	public ServerDataManager getServerDataManager() {
		return serverDataManager;
	}
	public ServerManager getServerManager() {
		return serverManager;
	}


	// -------------------------------------------------- //


	// SHORTCUTS

	public Essentials getEssentials() {
		return hookManager.getEssentialsHook().getEssentials();
	}
	public ConfigurationFile getConfiguration() {
		return filesManager.getConfiguration();
	}


	// CONSTANTS VARIABLES

	public MCServerVersion getMCServerVersion() {
		return mcServerVersion;
	}


	// -------------------------------------------------- //
}