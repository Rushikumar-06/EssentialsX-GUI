/*
 * EssentialsX-GUI - An unofficial GUI addon for EssentialsX
 * Copyright (C) 2025  Sniper_TVmc
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package fr.snipertvmc.essentialsxgui;

import com.earth2me.essentials.Essentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.MCServerVersion;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGServer;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.ConfigurationFile;
import fr.snipertvmc.essentialsxgui.libraries.bstats.Metrics;
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
	private Metrics metrics;


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
		metrics = new Metrics(this, 26314);


		// FILES LOADING
		filesManager.loadFiles();


		// LOAD PLUGIN
		boolean cancelLoading = false;
		if (!loadingManager.loadPlugin(filesManager.getConfiguration().isDetailedLoading())) {
			cancelLoading = true;
		}


		// SERVER INITIALIZATION
		if (!cancelLoading) {
			exgServer = serverManager.initialize();
		}


		// PLUGIN LOADING COMPLETED
		long endTime = System.currentTimeMillis();
		long loadingTime = endTime - startTime;

		if (cancelLoading) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cPlugin will be disabled due to loading errors.");
			ConsoleLogger.console("");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

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
	public Metrics getMetrics() {
		return metrics;
	}


	// -------------------------------------------------- //
}