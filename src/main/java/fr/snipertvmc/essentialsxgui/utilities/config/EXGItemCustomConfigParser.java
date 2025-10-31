package fr.snipertvmc.essentialsxgui.utilities.config;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EXGItemCustomConfigParser {


	// -------------------------------------------------- //


	private static final Map<String, List<String>> requiredPaths = new HashMap<>() {{

		put("clickActions", List.of(

				// HOMES
				"homes.items.homeItem",

				// KITS
				"kitsAdminView.items.kitItem",
				"kitsPlayerView.items.kitItem",

				// WARPS
				"warpsAdminView.items.warpItem"
		));
	}};


	private static final Map<String, List<String>> clickActionsPath = new HashMap<>() {{


		// HOMES
		put("homes.items.homeItem", List.of(
				"teleportToHome",
				"editHome",
				"deleteHome"));


		// KITS
		put("kitsAdminView.items.kitItem", List.of(
				"giveKit",
				"editKit",
				"deleteKit"));

		put("kitsPlayerView.items.kitItem", List.of(
				"receiveKit",
				"previewKit"));


		// WARPS
		put("warpsAdminView.items.warpItem", List.of(
				"teleportWarp",
				"editWarp",
				"deleteWarp"));
	}};


	// -------------------------------------------------- //



	public static boolean isEXGItemCustomConfigValid(InventoryFile inventoryFile, String itemPath) {


		// Get the inventory configuration
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Validate item properties
		return areClickActionsValid(itemPath, config);
	}


	// -------------------------------------------------- //


	private static boolean areClickActionsValid(String itemPath, YamlConfiguration config) {

		if (!isRequired(itemPath, "clickActions")) {
			return true;
		}

		Object clickActionsSectionObject = config.get(itemPath + ".clickActions");
		if (clickActionsSectionObject == null) {
			ConsoleLogger.error("Invalid click actions for item '" + itemPath + "': 'clickActions' section is missing.");
			return false;
		}

		if (!(clickActionsSectionObject instanceof ConfigurationSection clickActionsSection)) {
			ConsoleLogger.error("Invalid click actions for item '" + itemPath + "': 'clickActions' section is not a valid configuration section.");
			return false;
		}

		if (clickActionsSection.getKeys(false).isEmpty()) {
			ConsoleLogger.error("Invalid click actions for item '" + itemPath + "': 'clickActions' section is empty.");
			return false;
		}

		boolean areValid = true;
		List<ClickType> usedClickTypes = new ArrayList<>();
		for (String action : clickActionsSection.getKeys(false)) {
			Object clickTypeObject = clickActionsSection.get(action);
			if (!isValidClickAction(action, clickTypeObject, itemPath)) {
				areValid = false;
				continue;
			}

			ClickType clickType = ClickType.valueOf(((String) clickTypeObject).toUpperCase());
			if (usedClickTypes.contains(clickType)) {
				ConsoleLogger.error("Invalid click actions for item '" + itemPath + "': '" + clickType + "' is used more than once.");
				areValid = false;
			} else {
				usedClickTypes.add(clickType);
			}
		}

		return areValid;
	}


	private static boolean isValidClickAction(String action, Object clickTypeObject, String itemPath) {

		if (!clickActionsPath.containsKey(itemPath) || !clickActionsPath.get(itemPath).contains(action)) {
			ConsoleLogger.error("Invalid click action for item '" + itemPath + "': '" + action + "' is not a valid action for this item.");
			return false;
		}

		if (isNotTypeRequired(clickTypeObject, itemPath, action, String.class)) {
			return false;
		}

		try {
			String clickTypeString = (String) clickTypeObject;
			ClickType.valueOf(clickTypeString.toUpperCase());
			return true;

		} catch (IllegalArgumentException e) {
			ConsoleLogger.error("Invalid click action for item '" + itemPath + "': '" + clickTypeObject + "' is not a valid ClickType.");
			return false;
		}
	}



	// -------------------------------------------------- //


	private static boolean isRequired(String itemPath, String propertyName) {
		return requiredPaths.containsKey(propertyName) && requiredPaths.get(propertyName).contains(itemPath);
	}


	private static boolean isNotTypeRequired(Object value, String itemPath, String propertyName, Class<?> requiredType) {
		if (!requiredType.isInstance(value)) {
			ConsoleLogger.error("Invalid " + propertyName + " for item '" + itemPath + "': '" + value + "' is not a " + requiredType.getSimpleName() + ".");
			return true;
		}
		return false;
	}


	// -------------------------------------------------- //
}
