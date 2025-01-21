package fr.snipertvmc.essentialsxgui.utilities.config;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class EXGInventoryYamlParser {


	// -------------------------------------------------- //


	public static boolean isEXGItemConfigValid(String itemPath, boolean isBorderItem) {

		// Get the inventories configuration
		YamlConfiguration config = Main.getInstance().getFilesManager().getInventories().getYamlConfiguration();


		// Retrieve item properties
		Object slots = config.get(itemPath + ".slots");
		Object slot = config.get(itemPath + ".slot");

		Object material = config.get(itemPath + ".material");
		Object amount = config.get(itemPath + ".amount");
		Object data = config.get(itemPath + ".data");

		Object displayName = config.get(itemPath + ".displayName");
		Object lore = config.get(itemPath + ".lore");

		Object itemFlags = config.get(itemPath + ".itemFlags");
		Object enchantments = config.get(itemPath + ".enchantments");

		// Check if item properties are valid
		if (isBorderItem) {
			if (!(slots instanceof List) || ((List<?>) slots).isEmpty()) {
				ConsoleLogger.error("Invalid border item at path '" + itemPath + "': 'slots' is missing or invalid.");
				return false;
			}

		} else {
			if (!(slot instanceof Number) && !itemPath.equals("inventories.homes.items.homeItem")) {
				ConsoleLogger.error("Invalid item at path '" + itemPath + "': 'slot' is missing or not a number.");
				return false;
			}
		}

		if (!(material instanceof String)) {
			ConsoleLogger.error("Invalid item at path '" + itemPath + "': 'material' is missing or not a string.");
			return false;
		}

		if (data != null && !(data instanceof Number)) {
			ConsoleLogger.error("Invalid item at path '" + itemPath + "': 'data' is not a number.");
			return false;
		}

		if (amount != null && !(amount instanceof Number)) {
			ConsoleLogger.error("Invalid item at path '" + itemPath + "': 'amount' is not a number.");
			return false;
		}

		if (displayName != null && !(displayName instanceof String)) {
			ConsoleLogger.error("Invalid item at path '" + itemPath + "': 'displayName' is not a string.");
			return false;
		}

		if (lore != null) {
			if (!(lore instanceof List) || ((List<?>) lore).stream().anyMatch(line -> !(line instanceof String))) {
				ConsoleLogger.error("Invalid item at path '" + itemPath + "': 'lore' must be a list of strings.");
				return false;
			}
		}

		if (enchantments != null) {
			if (!(enchantments instanceof List) || ((List<?>) enchantments).stream().anyMatch(enchantment -> !(enchantment instanceof String))) {
				ConsoleLogger.error("Invalid item at path '" + itemPath + "': 'enchantments' must be a list of strings.");
				return false;
			}
		}

		if (itemFlags != null) {
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
