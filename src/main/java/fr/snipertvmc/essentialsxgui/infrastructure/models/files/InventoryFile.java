package fr.snipertvmc.essentialsxgui.infrastructure.models.files;

import fr.mrmicky.fastinv.InventoryScheme;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.config.EXGInventoryYamlParser;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryFile {


	// -------------------------------------------------- //


	private final YamlConfiguration yamlConfiguration;


	// -------------------------------------------------- //


	public InventoryFile(YamlConfiguration yamlConfiguration) {
		this.yamlConfiguration = yamlConfiguration;
	}


	// -------------------------------------------------- //


	public String getInventoryVersion() {
		String version = yamlConfiguration.getString("config-version");
		if (version != null) {
			return version;
		}
		return "0.0";
	}


	// -------------------------------------------------- //


	public String getTitle(String inventoryName) {
		return yamlConfiguration.getString(inventoryName + ".title");
	}


	public int getRows(String inventoryName) {
		return yamlConfiguration.getInt(inventoryName + ".rows");
	}


	public EXGItemConfig getItem(String inventoryName, String itemName) {
		EXGItemConfig itemConfig = getItem(inventoryName + ".items." + itemName, false);
		short slot = (short) yamlConfiguration.getInt(inventoryName + ".items." + itemName + ".slot");
		itemConfig.setSlot(slot);
		return itemConfig;
	}


	public EXGItemConfig getBorderItem(String inventoryName) {
		return getItem(inventoryName + ".borderItem", true);
	}


	public int[] getBorderSlots(String inventoryName) {
		List<Integer> borderSlots = yamlConfiguration.getIntegerList(inventoryName + ".borderItem.slots");
		return borderSlots.stream().mapToInt(i -> i).toArray();
	}


	public InventoryScheme getInventoryScheme(String inventoryName) {
		List<String> scheme = yamlConfiguration.getStringList(inventoryName + ".inventoryScheme");
		InventoryScheme inventoryScheme = new InventoryScheme();
		for (String line : scheme) {
			inventoryScheme.mask(line);
		}
		return inventoryScheme.bindPagination('1');
	}


	// -------------------------------------------------- //


	private EXGItemConfig getItem(String path, boolean isBorderItem) {

		Object enabledValue = yamlConfiguration.get(path + ".enabled");
		if (enabledValue instanceof Boolean isEnabled && !isEnabled) {
			return new EXGItemConfig(false, (short) 0, Material.AIR.name(), 1, (byte) 0, null, null, null, null);
		}

		// Check if the item configuration is valid
		if (!EXGInventoryYamlParser.isEXGItemConfigValid(this, path, isBorderItem)) {

			Object slotValue = yamlConfiguration.get(path + ".slot");
			short slot = 0;

			if (slotValue instanceof Number slotNumber) {
				slot = slotNumber.shortValue();
			}

			return new EXGItemConfig(true,
					slot,
					"BEDROCK", 1, (byte) 0,
					"§4Invalid Item",
					List.of("§cThis item configuration is invalid.",
							"§7Please check the configuration at path:",
							"§8- §f" + path,
							"§7and fix the errors seen in the console."),
					null, null
					);
		}

		boolean enabled = false;

		short slot = 0;

		String materialName = null;
		int amount = 1;
		byte data = 0;

		String displayName = null;
		List<String> lore = null;

		List<Pair<Enchantment, Integer>> enchantments = null;
		List<ItemFlag> itemFlags = null;

		Map<String, Object> itemStringValues = new HashMap<>() {{
			put("enabled", yamlConfiguration.get(path + ".enabled"));

			put("slot", yamlConfiguration.get(path + ".slot"));

			put("material", yamlConfiguration.get(path + ".material"));
			put("amount", yamlConfiguration.get(path + ".amount"));
			put("data", yamlConfiguration.get(path + ".data"));

			put("displayName", yamlConfiguration.get(path + ".displayName"));
			put("lore", yamlConfiguration.get(path + ".lore"));

			put("enchantments", yamlConfiguration.get(path + ".enchantments"));
			put("itemFlags", yamlConfiguration.get(path + ".item-flags"));
		}};

		for (Map.Entry<String, Object> entry : itemStringValues.entrySet()) {

			if (entry.getKey().equals("enabled") && entry.getValue() != null && entry.getValue() instanceof Boolean) {
				enabled = (boolean) enabledValue;

			} else if (entry.getKey().equals("slot") && entry.getValue() != null && !isBorderItem && entry.getValue() instanceof Number slotValue) {
				slot = slotValue.shortValue();

			} else if (entry.getKey().equals("material") && entry.getValue() != null && entry.getValue() instanceof String materialValue) {
				materialName = materialValue;

			} else if (entry.getKey().equals("amount") && entry.getValue() != null && entry.getValue() instanceof Number amountValue) {
				amount = amountValue.intValue();

			} else if (entry.getKey().equals("data") && entry.getValue() != null && entry.getValue() instanceof Number dataValue) {
				data = dataValue.byteValue();

			} else if (entry.getKey().equals("displayName") && entry.getValue() != null && entry.getValue() instanceof String displayNameValue) {
				displayName = displayNameValue;

			} else if (entry.getKey().equals("lore") && entry.getValue() != null && entry.getValue() instanceof List<?> loreValue) {
				lore = new ArrayList<>();
				for (Object loreLine : loreValue) {
					lore.add((String) loreLine);
				}

			} else if (entry.getKey().equals("enchantments") && entry.getValue() != null && entry.getValue() instanceof List<?> enchantmentsList) {
				enchantments = new ArrayList<>();
				for (Object enchantment : enchantmentsList) {
					String[] enchantmentSplit = ((String) enchantment).split(":");
					if (Enchantment.getByName(enchantmentSplit[0]) != null) {
						enchantments.add(new ImmutablePair<>(Enchantment.getByName(enchantmentSplit[0]), Integer.parseInt(enchantmentSplit[1])));
					}
				}

			} else if (entry.getKey().equals("itemFlags") && entry.getValue() != null && entry.getValue() instanceof List<?> itemFlagsList) {
				itemFlags = new ArrayList<>();
				for (Object itemFlag : itemFlagsList) {
					try {
						itemFlags.add(ItemFlag.valueOf((String) itemFlag));
					} catch (IllegalArgumentException e) {
						ConsoleLogger.console("§cInvalid ItemFlag: §7" + itemFlag);
					}
				}
			}
		}

		return new EXGItemConfig(enabled, slot, materialName, amount, data, displayName, lore, enchantments, itemFlags);
	}


	// -------------------------------------------------- //


	public YamlConfiguration getYamlConfiguration() {
		return yamlConfiguration;
	}


	// -------------------------------------------------- //
}
