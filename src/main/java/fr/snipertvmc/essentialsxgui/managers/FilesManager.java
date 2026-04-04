package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.ConfigurationFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.MessagesFile;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.config.EXGInventoryConfigParser;
import fr.snipertvmc.essentialsxgui.utilities.type.MapUtils;
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


	private final Map<String, String> filesPaths = new HashMap<>() {{
		put("configuration", "configuration");
		put("messages", "messages");

		put("homeEditing", "inventories/homes/homeEditing");
		put("homes", "inventories/homes/homes");

		put("kitsAdminView", "inventories/kits/kitsAdminView");
		put("kitsPlayerView", "inventories/kits/kitsPlayerView");
		put("kitPreview", "inventories/kits/kitPreview");
		put("kitPlayerGive", "inventories/kits/kitPlayerGive");
		put("kitEditing", "inventories/kits/kitEditing");
		put("kitEditor", "inventories/kits/kitEditor");

		put("warpEditing", "inventories/warps/warpEditing");
		put("warpPlayerTeleport", "inventories/warps/warpPlayerTeleport");
		put("warpsAdminView", "inventories/warps/warpsAdminView");
		put("warpsPlayerView", "inventories/warps/warpsPlayerView");

		put("whoisPlayers", "inventories/whois/whoisPlayers");
		put("whoisView", "inventories/whois/whoisView");

		put("dataEntryGUI", "inventories/others/dataEntryGUI");
	}};


	private final Map<String, String> filesVersions = new HashMap<>() {{
		put("configuration", "1.7"); // Updated for version: 1.4.1
		put("messages", "1.7"); // Updated for version: 1.4.1

		put("homeEditing", "1.1"); // Updated for version: 1.0.2
		put("homes", "1.3"); // Updated for version: 1.1.0

		put("kitsAdminView", "1.2"); // Updated for version: 1.1.0
		put("kitsPlayerView", "1.2"); // Updated for version: 1.1.0
		put("kitPreview", "1.0");
		put("kitPlayerGive", "1.0");
		put("kitEditing", "1.2"); // Updated for version: 1.1.0
		put("kitEditor", "1.0"); // Updated for version: 1.0.2

		put("warpEditing", "1.0"); // Updated for version: 1.2.0
		put("warpPlayerTeleport", "1.0"); // Updated for version: 1.2.0
		put("warpsAdminView", "1.0"); // Updated for version: 1.2.0
		put("warpsPlayerView", "1.0"); // Updated for version: 1.2.0

		put("whoisPlayers", "1.0"); // Updated for version: 1.3.0
		put("whoisView", "1.0"); // Updated for version: 1.3.0

		put("dataEntryGUI", "1.0"); // Updated for version: 1.1.0
	}};


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
		if (configurationFile.isDetailedLoading()) ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §fconfiguration.yml: §a" + label);
	}


	public void loadAndCheckMessages(boolean reload) {
		loadYAMLFile("messages");
		checkUpdateForFile("messages");
		String label = reload ? "Reloaded" : "Loaded";
		if (configurationFile.isDetailedLoading()) ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §fmessages.yml: §a" + label);
	}


	public void loadAndCheckInventories(boolean reload) {

		for (String inventoryName : Main.getInstance().getInventoriesManager().getInventoryNames()) {
			loadYAMLFile(inventoryName);
			checkUpdateForFile(inventoryName);
			boolean isValid = EXGInventoryConfigParser.isEXGInventoryConfigValid(getInventory(inventoryName));
			Main.getInstance().getInventoriesManager().loadInventory(inventoryName);

			String label = reload ? "Reloaded" : "Loaded";
			label = isValid ? label : "§4Not valid, please resolve the above errors";

			if (configurationFile.isDetailedLoading()) ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §f" + inventoryName + ".yml: §a" + label);
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
