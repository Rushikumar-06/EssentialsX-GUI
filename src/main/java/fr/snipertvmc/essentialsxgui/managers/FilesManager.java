package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.ConfigurationFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoriesFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.MessagesFile;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FilesManager {


	// -------------------------------------------------- //


	private ConfigurationFile configurationFile;
	private MessagesFile messagesFile;
	private InventoriesFile inventoriesFile;


	// -------------------------------------------------- //


	public void loadFiles() {
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Loading files...");

		loadConfiguration();
		checkUpdateForConfiguration();
		ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §fConfiguration: §aLoaded");

		loadMessages();
		checkUpdateForMessages();
		ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §fMessages: §aLoaded");

		loadInventories();
		checkUpdateForInventories();
		ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §fInventories: §aLoaded");

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Files loading §fcompleted§7.");
	}


	// -------------------------------------------------- //


	private void loadYAMLFile(String fileName) {

		File file = new File(Main.getInstance().getDataFolder(), fileName + ".yml");

		if (!file.exists()) {
			file.getParentFile().mkdirs();
			Main.getInstance().saveResource(fileName + ".yml", false);
		}

		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

		switch (fileName) {
			case "configuration" -> configurationFile = new ConfigurationFile(yamlConfiguration);
			case "messages" -> messagesFile = new MessagesFile(yamlConfiguration);
			case "inventories" -> inventoriesFile = new InventoriesFile(yamlConfiguration);
		}
	}


	// -------------------------------------------------- //


	private boolean createBackupYAMLFile(String fileName) {
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
						loadYAMLFile(fileName);
						return true;
					}

				} catch (SecurityException e) {
					throw new RuntimeException("Error while creating backup file: " + e.getMessage());
				}
			}

		} else {
			loadYAMLFile(fileName);
		}
		return false;
	}


	// -------------------------------------------------- //


	public void checkUpdateForConfiguration() {

		String configVersion = Main.getInstance().getFilesManager().getConfiguration().getConfigVersion();

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Checking for configuration file version...");

		if (Main.getInstance().getFilesManager().getConfiguration().getConfigVersion().equals(configVersion)) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §7Your configuration file is §aup to date§7.");
			return;
		}

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7A new version is §favailable §7for your configuration file.");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §6Updating of the configuration file...");

		if (Main.getInstance().getFilesManager().backupConfiguration()) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §aYour configuration file has been updated. §7§o(Backup created)");
			return;
		}

		ConsoleLogger.console("§cAn error has occurred while saving your current configuration file.");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7- §ePlease save your current configuration file, delete it,");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7- §eand finally restart the plugin to create a new one.");
	}


	public void checkUpdateForMessages() {

		String messagesVersion = Main.getInstance().getFilesManager().getMessages().getMessagesVersion();

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Checking for messages file version...");

		if (Main.getInstance().getFilesManager().getMessages().getMessagesVersion().equals(messagesVersion)) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §7Your messages file is §aup to date§7.");
			return;
		}

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7A new version is §favailable §7for your messages file.");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §6Updating of the messages file...");

		if (Main.getInstance().getFilesManager().backupMessages()) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §aYour messages file has been updated. §7§o(Backup created)");
			return;
		}

		ConsoleLogger.console("§cAn error has occurred while saving your current messages file.");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7- §ePlease save your current messages file, delete it,");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7- §eand finally restart the plugin to create a new one.");
	}


	public void checkUpdateForInventories() {

		String inventoriesVersion = Main.getInstance().getFilesManager().getInventories().getInventoriesVersion();

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Checking for inventories file version...");

		if (Main.getInstance().getFilesManager().getInventories().getInventoriesVersion().equals(inventoriesVersion)) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §7Your inventories file is §aup to date§7.");
			return;
		}

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7A new version is §favailable §7for your inventories file.");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §6Updating of the inventories file...");

		if (Main.getInstance().getFilesManager().backupInventories()) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §aYour inventories file has been updated. §7§o(Backup created)");
			return;
		}

		ConsoleLogger.console("§cAn error has occurred while saving your current inventories file.");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7- §ePlease save your current inventories file, delete it,");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7- §eand finally restart the plugin to create a new one.");
	}


	// -------------------------------------------------- //


	public ConfigurationFile getConfiguration() {
		return configurationFile;
	}
	public MessagesFile getMessages() {
		return messagesFile;
	}
	public InventoriesFile getInventories() {
		return inventoriesFile;
	}


	// -------------------------------------------------- //


	private void loadConfiguration() {
		loadYAMLFile("configuration");
	}
	private void loadMessages() {
		loadYAMLFile("messages");
	}
	private void loadInventories() {
		loadYAMLFile("inventories");
	}

	private boolean backupConfiguration() {
		return createBackupYAMLFile("configuration");
	}
	private boolean backupMessages() {
		return createBackupYAMLFile("messages");
	}
	private boolean backupInventories() {
		return createBackupYAMLFile("inventories");
	}


	// -------------------------------------------------- //
}
