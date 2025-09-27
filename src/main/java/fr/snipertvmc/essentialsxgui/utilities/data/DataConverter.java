package fr.snipertvmc.essentialsxgui.utilities.data;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.type.JsonUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class DataConverter {


	// -------------------------------------------------- //


	/**
	 * Checks if legacy EssentialsX data files are present in the plugin's data folder.
	 * <p>
	 * <strong>Note:</strong> This method will be discontinued as soon as possible.<br>
	 * It allows the conversion of old data to the new structure.<br>
	 * It is possible to do this manually, to avoid any surprises during
	 * a future update, for more information see the wiki :<br>
	 * - <a href="https://sniper-tvmc.gitbook.io/essentialsx-gui/support/troubleshooting">
	 *     https://sniper-tvmc.gitbook.io/essentialsx-gui/support/troubleshooting</a>
	 */
	@Deprecated(since = "1.1.1")
	public static boolean areLegacyPlayerDataFilesPresent() {
		File playersDataFolder = new File(Main.getInstance().getDataFolder(), "data/players");
		if (!playersDataFolder.exists() || !playersDataFolder.isDirectory()) {
			return false;
		}

		File[] jsonFiles = playersDataFolder.listFiles(pathname -> pathname.isFile() && pathname.getName().endsWith(".json"));
		return jsonFiles != null && jsonFiles.length > 0;
	}


	/**
	 * Checks if legacy EssentialsX data files are present in the plugin's data folder.
	 * <p>
	 * <strong>Note:</strong> This method will be discontinued as soon as possible.<br>
	 * It allows the conversion of old data to the new structure.<br>
	 * It is possible to do this manually, to avoid any surprises during
	 * a future update, for more information see the wiki :<br>
	 * - <a href="https://sniper-tvmc.gitbook.io/essentialsx-gui/support/troubleshooting">
	 *     https://sniper-tvmc.gitbook.io/essentialsx-gui/support/troubleshooting</a>
	 */
	@Deprecated(since = "1.1.1")
	public static boolean isLegacyServerDataFilePresent() {
		File serverDataFile = new File(Main.getInstance().getDataFolder(), "data/server.json");
		return serverDataFile.exists() && serverDataFile.isFile();
	}


	/**
	 * Checks if legacy EssentialsX data files are present in the plugin's data folder.
	 * <p>
	 * <strong>Note:</strong> This method will be discontinued as soon as possible.<br>
	 * It allows the conversion of old data to the new structure.<br>
	 * It is possible to do this manually, to avoid any surprises during
	 * a future update, for more information see the wiki :<br>
	 * - <a href="https://sniper-tvmc.gitbook.io/essentialsx-gui/support/troubleshooting">
	 *     https://sniper-tvmc.gitbook.io/essentialsx-gui/support/troubleshooting</a>
	 */
	@Deprecated(since = "1.1.1")
	public static void tryToRemoveLegacyDataFolders() {

		if (!areLegacyPlayerDataFilesPresent() && !isLegacyServerDataFilePresent()) {

			File playersDataFolder = new File(Main.getInstance().getDataFolder(), "data/players/");
			if (playersDataFolder.exists() && playersDataFolder.isDirectory()) {
				playersDataFolder.delete();
			}

			File dataFolder = new File(Main.getInstance().getDataFolder(), "data/");
			if (!dataFolder.exists() || !dataFolder.isDirectory()) {
				return;
			}

			dataFolder.delete();
		}
	}


	// -------------------------------------------------- //


	/**
	 * Checks if legacy EssentialsX data files are present in the plugin's data folder.
	 * <p>
	 * <strong>Note:</strong> This method will be discontinued as soon as possible.<br>
	 * It allows the conversion of old data to the new structure.<br>
	 * It is possible to do this manually, to avoid any surprises during
	 * a future update, for more information see the wiki :<br>
	 * - <a href="https://sniper-tvmc.gitbook.io/essentialsx-gui/support/troubleshooting">
	 *     https://sniper-tvmc.gitbook.io/essentialsx-gui/support/troubleshooting</a>
	 */
	@Deprecated(since = "1.1.1")
	public static Map<String, Object> getLegacyPlayerData(Player player) {

		File playerDataFile = new File(Main.getInstance().getDataFolder(), "data/players/" + player.getUniqueId().toString() + ".json");
		if (!playerDataFile.exists()) {
			return null;
		}

		try {
			String json = Files.readString(playerDataFile.toPath());
			return JsonUtils.jsonToMap(json);

		} catch (IOException e) {
			throw new RuntimeException("Error when reading data for the player: " + player.getUniqueId().toString(), e);
		}
	}


	/**
	 * Checks if legacy EssentialsX data files are present in the plugin's data folder.
	 * <p>
	 * <strong>Note:</strong> This method will be discontinued as soon as possible.<br>
	 * It allows the conversion of old data to the new structure.<br>
	 * It is possible to do this manually, to avoid any surprises during
	 * a future update, for more information see the wiki :<br>
	 * - <a href="https://sniper-tvmc.gitbook.io/essentialsx-gui/support/troubleshooting">
	 *     https://sniper-tvmc.gitbook.io/essentialsx-gui/support/troubleshooting</a>
	 */
	@Deprecated(since = "1.1.1")
	public static void deleteLegacyPlayerDataFile(Player player) {

		File playerDataFile = new File(Main.getInstance().getDataFolder(), "data/players/" + player.getUniqueId().toString() + ".json");
		if (playerDataFile.exists()) {
			if (!playerDataFile.delete()) {
				ConsoleLogger.error("Failed to delete legacy data file for player: " + player.getUniqueId().toString());
				ConsoleLogger.error("Please delete the file manually to avoid any issues: " + playerDataFile.getAbsolutePath());

			} else {
				ConsoleLogger.info("Legacy data file deleted for player: " + player.getUniqueId().toString());
			}
		}
	}


	// -------------------------------------------------- //


	/**
	 * Checks if legacy EssentialsX data files are present in the plugin's data folder.
	 * <p>
	 * <strong>Note:</strong> This method will be discontinued as soon as possible.<br>
	 * It allows the conversion of old data to the new structure.<br>
	 * It is possible to do this manually, to avoid any surprises during
	 * a future update, for more information see the wiki :<br>
	 * - <a href="https://sniper-tvmc.gitbook.io/essentialsx-gui/support/troubleshooting">
	 *     https://sniper-tvmc.gitbook.io/essentialsx-gui/support/troubleshooting</a>
	 */
	@Deprecated(since = "1.1.1")
	public static Map<String, Object> getLegacyServerData() {

		File serverDataFile = new File(Main.getInstance().getDataFolder(), "data/server.json");
		if (!serverDataFile.exists()) {
			return null;
		}

		try {
			String json = Files.readString(serverDataFile.toPath());
			return JsonUtils.jsonToMap(json);

		} catch (IOException e) {
			throw new RuntimeException("Error when reading server data file.", e);
		}
	}


	/**
	 * Checks if legacy EssentialsX data files are present in the plugin's data folder.
	 * <p>
	 * <strong>Note:</strong> This method will be discontinued as soon as possible.<br>
	 * It allows the conversion of old data to the new structure.<br>
	 * It is possible to do this manually, to avoid any surprises during
	 * a future update, for more information see the wiki :<br>
	 * - <a href="https://sniper-tvmc.gitbook.io/essentialsx-gui/support/troubleshooting">
	 *     https://sniper-tvmc.gitbook.io/essentialsx-gui/support/troubleshooting</a>
	 */
	@Deprecated(since = "1.1.1")
	public static void deleteLegacyServerDataFile() {

		File serverDataFile = new File(Main.getInstance().getDataFolder(), "data/server.json");
		if (serverDataFile.exists()) {
			if (!serverDataFile.delete()) {
				ConsoleLogger.error("Failed to delete legacy server data file.");
				ConsoleLogger.error("Please delete the file manually to avoid any issues: " + serverDataFile.getAbsolutePath());

			} else {
				ConsoleLogger.info("Legacy server data file deleted.");
			}
		}
	}


	// -------------------------------------------------- //
}
