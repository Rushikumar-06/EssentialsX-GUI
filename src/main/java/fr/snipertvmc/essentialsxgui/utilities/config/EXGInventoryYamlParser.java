package fr.snipertvmc.essentialsxgui.utilities.config;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Map;

public class EXGInventoryYamlParser {


	// -------------------------------------------------- //


	public static boolean isEXGInventoryConfigValid() {

		// Get the inventories configuration
//		YamlConfiguration config = Main.getInstance().getFilesManager().getInventories().getYamlConfiguration();


		// Check if the inventories configuration is valid

		// All sections are valid
		return true;
	}


	// -------------------------------------------------- //


	private static final Map<String, List<String>> ignoredPaths = Map.of(

			"enabled", List.of(
					"homes.items.homeItem",
					"homes.items.previousPageItem",
					"homes.items.nextPageItem",

					"kitsAdminView.items.kitItem",
					"kitsPlayerView.items.kitItem",
					"kitsPlayerGive.items.playerItem"
			),

			"slot", List.of(
					"homes.items.homeItem",

					"kitsAdminView.items.kitItem",
					"kitsPlayerView.items.kitItem",
					"kitsPlayerGive.items.playerItem"
			),

			"material", List.of(
					"homeEditing.items.previewHomeItem"
			),

			"amount", List.of(),

			"data", List.of(),

			"displayName", List.of(),

			"lore", List.of(),

			"enchantments", List.of(),

			"itemFlags", List.of()
	);


	// -------------------------------------------------- //


	public static boolean isEXGItemConfigValid(InventoryFile inventoryFile, String itemPath, boolean isBorderItem) {

		// Get the inventories configuration
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Retrieve item properties
		Object enabled = config.get(itemPath + ".enabled");

		Object slots = config.get(itemPath + ".slots");
		Object slot = config.get(itemPath + ".slot");

		Object material = config.get(itemPath + ".material");
		Object amount = config.get(itemPath + ".amount");
		Object data = config.get(itemPath + ".data");

		Object displayName = config.get(itemPath + ".displayName");
		Object lore = config.get(itemPath + ".lore");

		Object itemFlags = config.get(itemPath + ".itemFlags");
		Object enchantments = config.get(itemPath + ".enchantments");


		// Check if item is enabled
		if (enabled instanceof Boolean && !(Boolean) enabled && !ignoredPaths.get("enabled").contains(itemPath)) {
			return true;
		}


		// Check if item properties are valid
		if (isBorderItem) {
			if (!(slots instanceof List) || ((List<?>) slots).isEmpty()) {
				ConsoleLogger.error("Invalid border item at path '" + itemPath + "': 'slots' is missing or invalid.");
				return false;
			}

		} else {
			if (!(slot instanceof Number) && !ignoredPaths.get("slot").contains(itemPath)) {
				ConsoleLogger.error("Invalid item at path '" + itemPath + "': 'slot' is missing or not a number.");
				return false;
			}
		}

		if (!(material instanceof String) && !ignoredPaths.get("material").contains(itemPath)) {
			ConsoleLogger.error("Invalid item at path '" + itemPath + "': 'material' is missing or not a string.");
			return false;
		}

		if (data != null && !(data instanceof Number) && !ignoredPaths.get("data").contains(itemPath)) {
			ConsoleLogger.error("Invalid item at path '" + itemPath + "': 'data' is not a number.");
			return false;
		}

		if (amount != null && !(amount instanceof Number) && !ignoredPaths.get("amount").contains(itemPath)) {
			ConsoleLogger.error("Invalid item at path '" + itemPath + "': 'amount' is not a number.");
			return false;
		}

		if (displayName != null && !(displayName instanceof String) && !ignoredPaths.get("displayName").contains(itemPath)) {
			ConsoleLogger.error("Invalid item at path '" + itemPath + "': 'displayName' is not a string.");
			return false;
		}

		if (lore != null && !ignoredPaths.get("lore").contains(itemPath)) {
			if (!(lore instanceof List) || ((List<?>) lore).stream().anyMatch(line -> !(line instanceof String))) {
				ConsoleLogger.error("Invalid item at path '" + itemPath + "': 'lore' must be a list of strings.");
				return false;
			}
		}

		if (enchantments != null && !ignoredPaths.get("enchantments").contains(itemPath)) {
			if (!(enchantments instanceof List) || ((List<?>) enchantments).stream().anyMatch(enchantment -> !(enchantment instanceof String))) {
				ConsoleLogger.error("Invalid item at path '" + itemPath + "': 'enchantments' must be a list of strings.");
				return false;
			}
		}

		if (itemFlags != null && !ignoredPaths.get("itemFlags").contains(itemPath)) {
			if (!(itemFlags instanceof List) || ((List<?>) itemFlags).stream().anyMatch(itemFlag -> !(itemFlag instanceof String))) {
				ConsoleLogger.error("Invalid item at path '" + itemPath + "': 'itemFlags' must be a list of strings.");
				return false;
			}
		}

		// All properties are valid
		return true;
	}


	// -------------------------------------------------- //
}
