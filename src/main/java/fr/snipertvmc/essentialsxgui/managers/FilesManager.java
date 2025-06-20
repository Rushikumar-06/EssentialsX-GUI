package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.ConfigurationFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.MessagesFile;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FilesManager {


	// -------------------------------------------------- //


	private ConfigurationFile configurationFile;
	private MessagesFile messagesFile;
	private final Map<String, InventoryFile> inventoriesFiles = new HashMap<>();


	private final Map<String, String> filesPaths = Map.of(
			"configuration", "configuration",
			"messages", "messages",

			"homeEditing", "inventories/homes/homeEditing",
			"homes", "inventories/homes/homes",

			"kitsAdminView", "inventories/kits/kitsAdminView",
			"kitsPlayerView", "inventories/kits/kitsPlayerView"
	);

	private final Map<String, String> filesVersions = Map.of(
			"configuration", "1.0",
			"messages", "1.0",

			"homeEditing", "1.0",
			"homes", "1.0",

			"kitsAdminView", "1.0",
			"kitsPlayerView", "1.0"
	);


	// -------------------------------------------------- //


	public void loadFiles() {
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Loading files...");

		loadConfiguration();
		checkUpdateForFile("configuration");

		loadMessages();
		checkUpdateForFile("messages");

		loadInventories();
		checkUpdateForInventories();
		Main.getInstance().getInventoriesManager().loadInventories();

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Files loading §fcompleted§7.");
	}


	// -------------------------------------------------- //


	private void loadYAMLFile(String fileName, boolean inventoryFile, @Nullable String inventoryName) {

		File file = new File(Main.getInstance().getDataFolder(), fileName + ".yml");

		if (!file.exists()) {
			file.getParentFile().mkdirs();
			Main.getInstance().saveResource(fileName + ".yml", false);
		}

		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

		if (inventoryFile) {
			inventoriesFiles.put(inventoryName, new InventoryFile(yamlConfiguration));
		}

		switch (fileName) {
			case "configuration" -> configurationFile = new ConfigurationFile(yamlConfiguration);
			case "messages" -> messagesFile = new MessagesFile(yamlConfiguration);
			default -> inventoriesFiles.put(inventoryName, new InventoryFile(yamlConfiguration));
		}
	}


	// -------------------------------------------------- //


	private boolean createBackupYAMLFile(String fileName, boolean inventoryFile, @Nullable String inventoryName) {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String formattedDate = dateFormat.format(currentDate);

		File fileToBackup = new File(Main.getInstance().getDataFolder(), fileName + ".yml");
		if (fileToBackup.exists()) {

			File backupFile = new File(fileToBackup.getParent(), formattedDate + "_" + fileName + ".yml");
			if (!backupFile.exists()) {

				try {
					boolean success = fileToBackup.renameTo(backupFile);

					if (success) {
						loadYAMLFile(fileName, inventoryFile, inventoryName);
						return true;
					}

				} catch (SecurityException e) {
					throw new RuntimeException("Error while creating backup file: " + e.getMessage());
				}
			}

		} else {
			loadYAMLFile(fileName, inventoryFile, inventoryName);
		}
		return false;
	}


	// -------------------------------------------------- //


	public void reloadFiles() {

		long startTime = System.currentTimeMillis();

		ConsoleLogger.console("");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Reloading files...");

		loadConfiguration();
		ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §fConfiguration: §aReloaded");

		loadMessages();
		ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §fMessages: §aReloaded");

		loadInventories();
		Main.getInstance().getInventoriesManager().loadInventories();
		ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §fInventories: §aReloaded");

		long endTime = System.currentTimeMillis();
		long loadingTime = endTime - startTime;

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Files reloading §fcompleted§7 in §f" + loadingTime + "ms§7.");
		ConsoleLogger.console("");
	}


	// -------------------------------------------------- //


	public void checkUpdateForFile(String fileName) {

		String latestVersion = filesVersions.get(fileName);

		String fileVersion = switch (fileName) {
			case "configuration" -> getConfiguration().getConfigVersion();
			case "messages" -> getMessages().getMessagesVersion();
			default -> getInventory(fileName).getInventoryVersion();
		};

		if (latestVersion.equals(fileVersion)) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §f" + fileName + ": §aLoaded and up to date");
			return;
		}

		if (Main.getInstance().getFilesManager().backupFile(fileName)) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §f" + fileName + ": §6Loaded but updated");
			return;
		}

		ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §f" + fileName + ": §cLoaded but not updated, check wiki for more information");
	}


	// -------------------------------------------------- //


	public ConfigurationFile getConfiguration() {
		return configurationFile;
	}
	public MessagesFile getMessages() {
		return messagesFile;
	}
	public InventoryFile getInventory(String inventoryName) {
		return inventoriesFiles.get(inventoryName);
	}


	// -------------------------------------------------- //


	private void loadConfiguration() {
		loadYAMLFile("configuration", false, null);
	}
	private void loadMessages() {
		loadYAMLFile("messages", false, null);
	}
	private void loadInventory(String inventoryName) {
		loadYAMLFile(filesPaths.get(inventoryName), true, inventoryName);
	}

	private boolean backupFile(String fileName) {
		return switch (fileName) {
			case "configuration", "messages" -> createBackupYAMLFile(fileName, false, null);
			default -> createBackupYAMLFile(filesPaths.get(fileName), true, fileName);
		};
	}


	// -------------------------------------------------- //


	private void loadInventories() {
		for (String inventoryName : Main.getInstance().getInventoriesManager().getInventoryNames()) {
			loadInventory(inventoryName);
		}
	}

	private void checkUpdateForInventories() {
		for (String inventoryName : Main.getInstance().getInventoriesManager().getInventoryNames()) {
			checkUpdateForFile(inventoryName);
		}
	}


	// -------------------------------------------------- //
}
