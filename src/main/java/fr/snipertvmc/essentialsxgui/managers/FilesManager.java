package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.ConfigurationFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.MessagesFile;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.config.EXGInventoryConfigParser;
import fr.snipertvmc.essentialsxgui.utilities.data.MapUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			"kitsPlayerView", "inventories/kits/kitsPlayerView",
			"kitsPreview", "inventories/kits/kitsPreview",
			"kitsPlayerGive", "inventories/kits/kitsPlayerGive",
			"kitEditing", "inventories/kits/kitEditing"
	);

	private final Map<String, String> filesVersions = Map.of(
			"configuration", "1.2", // Latest plugin version known before update file: 1.0.2
			"messages", "1.2", // Latest plugin version known before update file: 1.0.2

			"homeEditing", "1.1", // Latest plugin version known before update file: 1.0.1
			"homes", "1.3", // Latest plugin version known before update file: 1.0.2

			"kitsAdminView", "1.2", // Latest plugin version known before update file: 1.0.2
			"kitsPlayerView", "1.2", // Latest plugin version known before update file: 1.0.2
			"kitsPreview", "1.0",
			"kitsPlayerGive", "1.0",
			"kitEditing", "1.1" // Latest plugin version known before update file: 1.0.1
	);


	// -------------------------------------------------- //


	public void loadFiles() {
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Loading files...");

		loadAndCheckConfiguration(false);
		loadAndCheckMessages(false);
		loadAndCheckInventories(false);

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Files loading §fcompleted§7.");
	}


	public void reloadFiles() {

		long startTime = System.currentTimeMillis();

		ConsoleLogger.console("");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Reloading files...");

		loadAndCheckConfiguration(true);
		loadAndCheckMessages(true);
		loadAndCheckInventories(true);

		long endTime = System.currentTimeMillis();
		long loadingTime = endTime - startTime;

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Files reloading §fcompleted§7 in §f" + loadingTime + "ms§7.");
		ConsoleLogger.console("");
	}


	// -------------------------------------------------- //


	private void loadYAMLFile(String fileName) {

		String filePath = filesPaths.get(fileName);
		File file = new File(Main.getInstance().getDataFolder(), filePath + ".yml");

		if (!file.exists()) {
			file.getParentFile().mkdirs();
			Main.getInstance().saveResource(filePath + ".yml", false);
		}

		YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(file);

		switch (fileName) {

			case "configuration" -> configurationFile = new ConfigurationFile(yamlFile);
			case "messages" -> messagesFile = new MessagesFile(yamlFile);
			default -> inventoriesFiles.put(fileName, new InventoryFile(yamlFile, fileName));
		}

		patchFilePlaceholders(fileName);
		patchFileKeys(fileName);
	}


	private boolean createBackupYAMLFile(String fileName) {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String formattedDate = dateFormat.format(currentDate);

		String filePath = filesPaths.get(fileName);
		File fileToBackup = new File(Main.getInstance().getDataFolder(), filePath + ".yml");
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


	public void checkUpdateForFile(String fileName) {

		String latestVersion = filesVersions.get(fileName);

		String fileVersion = switch (fileName) {
			case "configuration" -> getConfiguration().getConfigVersion();
			case "messages" -> getMessages().getMessagesVersion();
			default -> getInventory(fileName).getInventoryVersion();
		};

		if (!latestVersion.equals(fileVersion)) {
			Main.getInstance().getFilesManager().createBackupYAMLFile(fileName);
		}
	}


	// -------------------------------------------------- //


	public void patchFilePlaceholders(String fileName) {

		String filePath = filesPaths.get(fileName);
		File file = new File(Main.getInstance().getDataFolder(), filePath + ".yml");

		try {

			String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
			boolean hasBeenUpdated = false;

			Map<String, String> placeholders = switch (fileName) {
				case "configuration" -> configurationFile.getPlaceholders();
				case "messages" -> messagesFile.getPlaceholders();
				default -> getInventory(fileName).getPlaceholders();
			};

			for (Map.Entry<String, String> entry : placeholders.entrySet()) {
				String placeholder = "%%" + entry.getKey() + "%%";
				String replacement = entry.getValue();

				if (content.contains(placeholder)) {
					hasBeenUpdated = true;
				}
				content = content.replace(placeholder, replacement);
			}

			Files.writeString(file.toPath(), content, StandardCharsets.UTF_8);

			if (!hasBeenUpdated) {
				return;
			}

			YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(file);

			switch (fileName) {

				case "configuration" -> configurationFile = new ConfigurationFile(yamlFile);
				case "messages" -> messagesFile = new MessagesFile(yamlFile);
				default -> inventoriesFiles.put(fileName, new InventoryFile(yamlFile, fileName));
			}

		} catch (IOException e) {
			ConsoleLogger.error("Error while reading file: " + filePath + ".yml");
		}
	}


	public void patchFileKeys(String fileName) {

		String filePath = filesPaths.get(fileName);
		File file = new File(Main.getInstance().getDataFolder(), filePath + ".yml");

		try {
			List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
			List<String> updatedLines = new ArrayList<>();
			boolean hasBeenUpdated = false;

			Pattern keyPattern = Pattern.compile("^\\s*([^:#]+):.*$");

			List<String> keysToRemove = switch (fileName) {
				case "configuration" -> configurationFile.getKeysToRemove();
				 case "messages" -> messagesFile.getKeysToRemove();
				 default -> getInventory(fileName).getKeysToRemove();
			};

			for (String line : lines) {
				Matcher matcher = keyPattern.matcher(line);
				if (matcher.matches()) {
					String matchedKey = matcher.group(1).trim();
					if (keysToRemove.contains(matchedKey)) {
						hasBeenUpdated = true;
						continue; // on saute cette ligne
					}
				}
				updatedLines.add(line);
			}

			Files.write(file.toPath(), updatedLines, StandardCharsets.UTF_8);

			if (!hasBeenUpdated) {
				return;
			}

			YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(file);

			switch (fileName) {

				case "configuration" -> configurationFile = new ConfigurationFile(yamlFile);
				case "messages" -> messagesFile = new MessagesFile(yamlFile);
				default -> inventoriesFiles.put(fileName, new InventoryFile(yamlFile, fileName));
			}

		} catch (IOException e) {
			ConsoleLogger.error("Error while reading file: " + filePath + ".yml");
		}
	}


	// -------------------------------------------------- //


	public void loadAndCheckConfiguration(boolean reload) {
		loadYAMLFile("configuration");
		checkUpdateForFile("configuration");
		String label = reload ? "Reloaded" : "Loaded";
		ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §fconfiguration.yml: §a" + label);
	}


	public void loadAndCheckMessages(boolean reload) {
		loadYAMLFile("messages");
		checkUpdateForFile("messages");
		String label = reload ? "Reloaded" : "Loaded";
		ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §fmessages.yml: §a" + label);
	}


	public void loadAndCheckInventories(boolean reload) {

		for (String inventoryName : Main.getInstance().getInventoriesManager().getInventoryNames()) {
			loadYAMLFile(inventoryName);
			checkUpdateForFile(inventoryName);
			boolean isValid = EXGInventoryConfigParser.isEXGInventoryConfigValid(getInventory(inventoryName));
			Main.getInstance().getInventoriesManager().loadInventory(inventoryName);

			String label = reload ? "Reloaded" : "Loaded";
			label = isValid ? label : "§4Not valid, please resolve the above errors";

			ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §f" + inventoryName + ".yml: §a" + label);
		}
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


	public String getInventoryName(InventoryFile inventoryFile) {
		return MapUtils.getKeyWithValue(inventoriesFiles, inventoryFile).toString();
	}


	// -------------------------------------------------- //
}
