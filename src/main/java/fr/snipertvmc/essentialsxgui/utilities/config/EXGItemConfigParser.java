package fr.snipertvmc.essentialsxgui.utilities.config;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.MCServerVersion;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import net.minecraft.server.v1_8_R3.Enchantment;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.Map;

public class EXGItemConfigParser {


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
					"homes.items.noHomesItem",

					"kitsAdminView.items.kitItem",
                    "kitsAdminView.items.noKitsItem",
					"kitsPlayerView.items.kitItem",
                    "kitsPlayerView.items.noKitsItem",
					"kitsPlayerGive.items.playerItem"
			),

			"material", List.of(
					"homeEditing.items.previewHomeItem",
					"kitEditing.items.previewKitItem"
			)
	);


	// -------------------------------------------------- //


	public static boolean isEXGItemConfigValid(InventoryFile inventoryFile, String itemPath, boolean isBorderItem) {


		// Get the inventory configuration
		YamlConfiguration config = inventoryFile.getYamlConfiguration();
		String inventoryName = Main.getInstance().getFilesManager().getInventoryName(inventoryFile);


		// Retrieve item properties
		Object enabled = config.get(itemPath + ".enabled");

		Object slots = config.get(itemPath + ".slots");
		Object slot = config.get(itemPath + ".slot");

		Object material = config.get(itemPath + ".material");
		Object data = config.get(itemPath + ".data");
		Object amount = config.get(itemPath + ".amount");

		Object displayName = config.get(itemPath + ".displayName");
		Object lore = config.get(itemPath + ".lore");

		Object itemFlags = config.get(itemPath + ".itemFlags");
		Object enchantments = config.get(itemPath + ".enchantments");


		return isEnabled(enabled, itemPath) &&

				isSlotValid(slot, slots, itemPath, inventoryName, isBorderItem) &&

				isMaterialValid(material, itemPath) &&
				isDataValid(data, itemPath) &&
				isAmountValid(amount, itemPath) &&

				isDisplayNameValid(displayName, itemPath) &&
				isLoreValid(lore, itemPath) &&

				areEnchantmentsValid(enchantments, itemPath) &&
				areItemFlagsValid(itemFlags, itemPath);
	}


	// -------------------------------------------------- //


	private static boolean isEnabled(Object enabled, String itemPath) {
		return ignoredPaths.get("enabled").contains(itemPath) || (enabled instanceof Boolean && (Boolean) enabled);
	}


	private static boolean isSlotValid(Object slot, Object slots, String itemPath, String inventoryName, boolean isBorderItem) {

		Object slotObject = (isBorderItem ? slots : slot);
		String slotDisplayName = (isBorderItem ? "slots" : "slot");
		Class<?> slotType = (isBorderItem ? List.class : Integer.class);

		if (isIgnored(itemPath, (isBorderItem ? "slots" : "slot"))) {
			return true;
		}

		if (!isMissing(slotObject, itemPath, slotDisplayName, true) &&
				isNotTypeRequired(slotObject, itemPath, slotDisplayName, slotType)) {
			return false;
		}

		if (isBorderItem) {

			List<Integer> slotList = (List<Integer>) slots;
			if (slotList.isEmpty()) {
				ConsoleLogger.error("Invalid slots for item '" + itemPath + "': slots list cannot be empty.");
				return false;
			}

		} else {

			int slotValue = ((Number) slot).intValue();

			InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);
			int rows = inventoryFile.getRows();
			int maxSlots = (rows * 9) - 1;

			if (slotValue < 0 || slotValue > maxSlots) {
				ConsoleLogger.error("Invalid slot for item '" + itemPath + "': it must be between 0 and " + maxSlots + ".");
				return false;
			}
		}

		return true;
	}


	private static boolean isMaterialValid(Object material, String itemPath) {

		if (isIgnored(itemPath, "material")) {
			return true;
		}

		if (isMissing(material, itemPath, "material", false) ||
				isNotTypeRequired(material, itemPath, "material", String.class)) {
			return false;
		}

		try {

			if ((material.toString().startsWith("{") && material.toString().endsWith("}")) ||
					material.toString().startsWith("PLAYER_HEAD:")) {
				return true; // Variable or custom item, no need to validate Material
			}

			Material.valueOf(material.toString());
			return true;

		} catch (IllegalArgumentException e) {
			ConsoleLogger.error("Invalid material for item '" + itemPath + "': '" + material + "' is not valid.");
			return false;
		}
	}


	private static boolean isDataValid(Object data, String itemPath) {

		if (Main.getInstance().getMCServerVersion().isHigherThan(MCServerVersion.v1_12_2)) {
			return true; // Data is not used in versions > 1.12.2
		}

		return isMissing(data, itemPath, "data", true) ||
				!isNotTypeRequired(data, itemPath, "data", Number.class);
	}


	private static boolean isAmountValid(Object amount, String itemPath) {

		return isMissing(amount, itemPath, "amount", true) ||
				!isNotTypeRequired(amount, itemPath, "amount", Number.class);
	}


	private static boolean isDisplayNameValid(Object displayName, String itemPath) {

		return isMissing(displayName, itemPath, "displayName", true) ||
				!isNotTypeRequired(displayName, itemPath, "displayName", String.class);
	}


	private static boolean isLoreValid(Object lore, String itemPath) {

		if (isMissing(lore, itemPath, "lore", true))  {
			return true;
		}

		if (!(lore instanceof List) || ((List<?>) lore).stream().anyMatch(line -> !(line instanceof String))) {
			ConsoleLogger.error("Invalid lore for item '" + itemPath + "': lore must be a list of strings.");
			return false;
		}

		return true;
	}


	private static boolean areEnchantmentsValid(Object enchantments, String itemPath) {

		if (isMissing(enchantments, itemPath, "enchantments", true))  {
			return true;
		}

		if (!(enchantments instanceof List) || ((List<?>) enchantments).stream().anyMatch(enchantment -> !(enchantment instanceof String))) {
			ConsoleLogger.error("Invalid enchantments list for item '" + itemPath + "': enchantments list must contain strings.");
			return false;
		}

		for (Object enchantment : (List<?>) enchantments) {

			String[] parts = enchantment.toString().split(":");
			if (parts.length != 2) {
				ConsoleLogger.error("Invalid enchantment format for item '" + itemPath + "': " + enchantment + " is not in the format 'EnchantmentName:Level'.");
				return false;
			}

			try {
				Enchantment enchantmentValue = Enchantment.getByName(parts[0]);
				Integer.parseInt(parts[1]);

				if (enchantmentValue == null) {
					ConsoleLogger.error("Invalid enchantment name for item '" + itemPath + "': '" + parts[0] + "' is not a valid enchantment.");
					return false;
				}

			} catch (NumberFormatException ex) {
				ConsoleLogger.error("Invalid enchantment level for item '" + itemPath + "': '" + parts[1] + "' is not a valid integer.");
				return false;
			}
		}

		return true;
	}


	private static boolean areItemFlagsValid(Object itemFlags, String itemPath) {

		if (isMissing(itemFlags, itemPath, "itemFlags", true))  {
			return true;
		}

		if (!(itemFlags instanceof List) || ((List<?>) itemFlags).stream().anyMatch(itemFlag -> !(itemFlag instanceof String))) {
			ConsoleLogger.error("Invalid item flags list for item '" + itemPath + "': list must contain strings.");
			return false;
		}

		for (Object itemFlag : (List<?>) itemFlags) {

			try {
				ItemFlag.valueOf(itemFlag.toString());

			} catch (IllegalArgumentException e) {
				ConsoleLogger.error("Invalid item flag for item '" + itemPath + "': '" + itemFlag + "' is not a valid item flag.");
				return false;
			}
		}

		return true;
	}


	// -------------------------------------------------- //


	private static boolean isIgnored(String itemPath, String propertyName) {
		return ignoredPaths.containsKey(propertyName) && ignoredPaths.get(propertyName).contains(itemPath);
	}


	private static boolean isMissing(Object value, String itemPath, String propertyName, boolean silence) {
		if (value == null) {
			if (!silence) {
				ConsoleLogger.error("Invalid value for item '" + itemPath + "': '" + propertyName + "' is missing.");
			}
			return true;
		}
		return false;
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
