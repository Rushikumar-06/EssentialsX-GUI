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


		put("updateItemInterval",  List.of(

				// ECONOMY
				"balanceTop.items.forceUpdateItem"
		));


		put("amountValues", List.of(

				// ECONOMY
				"ecoAmount.items.addItem",
				"ecoAmount.items.removeItem"
		));


		put("slots", List.of(

				// ECONOMY
				"ecoAmount.items.addItem",
				"ecoAmount.items.removeItem"
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
		return areClickActionsValid(itemPath, config) &&

				isUpdateItemIntervalValid(itemPath, config) &&

				areAmountValuesValid(itemPath, config);
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


	public static boolean isUpdateItemIntervalValid(String itemPath, YamlConfiguration config) {

		Object updateItemIntervalValue = config.get(itemPath + ".updateItemInterval");

		if (!isRequired(itemPath, "updateItemInterval")) {
			return true;
		}

		if (updateItemIntervalValue == null) {
			ConsoleLogger.error("Invalid update item interval for item '" + itemPath + "': 'updateItemInterval' is missing.");
			return false;
		}

		return hasValidIntegerRange(updateItemIntervalValue, itemPath, "updateItemInterval", 0, 600);
	}


	public static boolean areAmountValuesValid(String itemPath, YamlConfiguration config) {

		Object amountValues = config.get(itemPath + ".amountValues");
		Object slots = config.get(itemPath + ".slots");

		if (!isRequired(itemPath, "amountValues")) {
			return true;
		}

		if (amountValues == null) {
			ConsoleLogger.error("Invalid amount values for item '" + itemPath + "': 'amountValues' is missing.");
			return false;
		}

		return hasSameSize(amountValues, slots, itemPath, "amountValues");
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


	private static boolean hasValidIntegerRange(Object value, String itemPath, String propertyName, int min, int max) {
		if (value instanceof Integer intValue) {
			if (intValue < min || intValue > max) {
				ConsoleLogger.error("Invalid " + propertyName + " for item '" + itemPath + "': '" + intValue + "' is not between " + min + " and " + max + ".");
				return false;
			}
			return true;

		} else {
			ConsoleLogger.error("Invalid " + propertyName + " for item '" + itemPath + "': '" + value + "' is not an integer.");
			return false;
		}
	}


	private static boolean hasSameSize(Object firstValue,  Object secondValue, String itemPath, String propertyName) {
		if (firstValue instanceof List<?> firstList && secondValue instanceof List<?> secondList) {
			if (firstList.size() != secondList.size()) {
				ConsoleLogger.error("Invalid " + propertyName + " for item '" + itemPath + "': the lists do not have the same size.");
				return false;
			}
			return true;

		} else {
			ConsoleLogger.error("Invalid " + propertyName + " for item '" + itemPath + "': both values must be lists.");
			return false;
		}
	}


	// -------------------------------------------------- //
}
