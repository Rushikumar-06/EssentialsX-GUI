package fr.snipertvmc.essentialsxgui.infrastructure.models.files;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.MCServerVersion;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.config.EXGItemConfigParser;
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
	private final String inventoryName;


	// -------------------------------------------------- //


	public InventoryFile(YamlConfiguration yamlConfiguration, String inventoryName) {
		this.yamlConfiguration = yamlConfiguration;
		this.inventoryName = inventoryName;
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


	public String getTitle() {
		return yamlConfiguration.getString(inventoryName + ".title");
	}


	public int getRows() {
		return yamlConfiguration.getInt(inventoryName + ".rows");
	}


	public EXGItemConfig getItem(String itemName) {
		EXGItemConfig itemConfig = getItem(inventoryName + ".items." + itemName, false);
		short slot = (short) yamlConfiguration.getInt(inventoryName + ".items." + itemName + ".slot");
		itemConfig.setSlot(slot);
		return itemConfig;
	}


	public EXGItemConfig getBorderItem() {
		return getItem(inventoryName + ".borderItem", true);
	}


	public int[] getBorderSlots() {
		List<Integer> borderSlots = yamlConfiguration.getIntegerList(inventoryName + ".borderItem.slots");
		return borderSlots.stream().mapToInt(i -> i).toArray();
	}


	public InventoryScheme getInventoryScheme() {
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
		if (!EXGItemConfigParser.isEXGItemConfigValid(this, path, isBorderItem)) {

			Object slotValue = yamlConfiguration.get(path + ".slot");
			short slot = 0;

			if (slotValue instanceof Number slotNumber) {
				slot = slotNumber.shortValue();
			}

			return new EXGItemConfig(true,
					slot,
					"BEDROCK", 1, (byte) 0,
					"§4§lInvalid Item",
					List.of("§cThis item configuration is invalid.",
							"  §7Please check the §f" + inventoryName + " §7file",
							"  §7and fix error(s) seen in the console.",
							"",
							"§6Item path involved: ",
							"§8- §e" + path),
					null, null
			);
		}

		return parseItemConfig(path);
	}


	private EXGItemConfig parseItemConfig(String path) {

		Map<String, Object> itemStringValues = new HashMap<>() {{
			put("enabled", yamlConfiguration.get(path + ".enabled"));

			put("slot", yamlConfiguration.getInt(path + ".slot"));

			put("material", yamlConfiguration.getString(path + ".material"));
			put("amount", yamlConfiguration.getInt(path + ".amount", 1));
			put("data", yamlConfiguration.getInt(path + ".data", 0));

			put("displayName", yamlConfiguration.getString(path + ".displayName"));
			put("lore", yamlConfiguration.getStringList(path + ".lore"));

			put("enchantments", yamlConfiguration.getStringList(path + ".enchantments"));
			put("itemFlags", yamlConfiguration.getStringList(path + ".item-flags"));
		}};


		List<String> lore = new ArrayList<>((List<String>) itemStringValues.get("lore"));

		List<Pair<Enchantment, Integer>> enchantments = new ArrayList<>();
		for (Object enchantment : (List<String>) itemStringValues.get("enchantments")) {
			String[] enchantmentSplit = ((String) enchantment).split(":");
			if (Enchantment.getByName(enchantmentSplit[0]) != null) {
				enchantments.add(new ImmutablePair<>(Enchantment.getByName(enchantmentSplit[0]), Integer.parseInt(enchantmentSplit[1])));
			}
		}

		List<ItemFlag> itemFlags = new ArrayList<>();
		for (String itemFlag : (List<String>) itemStringValues.get("itemFlags")) {
			itemFlags.add(ItemFlag.valueOf(itemFlag));
		}

		Object booleanValue = itemStringValues.get("enabled");
		if (!(booleanValue instanceof Boolean)) {
			booleanValue = true;
		}

		return new EXGItemConfig(
				(boolean) booleanValue,
				((Number) itemStringValues.get("slot")).shortValue(),

				(String) itemStringValues.get("material"),
				(Integer) itemStringValues.get("amount"),
				((Number) itemStringValues.get("data")).byteValue(),

				(String) itemStringValues.get("displayName"),
				lore,

				enchantments,
				itemFlags
		);
	}


	// -------------------------------------------------- //


	public YamlConfiguration getYamlConfiguration() {
		return yamlConfiguration;
	}


	// -------------------------------------------------- //


	public Map<String, String> getPlaceholders() {

		MCServerVersion serverVersion = Main.getInstance().getMCServerVersion();
		switch (serverVersion) {

			case v1_8_8, v1_9_4, v1_10_2, v1_11_2, v1_12_2 -> {

				return Map.of(
						"value_materialBorderItem", "STAINED_GLASS_PANE",
						"value_materialHomeItem", "GRASS",
						"value_materialAdminModeItem", "EYE_OF_ENDER"
				);
			}

			case v1_13_2, v1_14_4, v1_15_2, v1_16_5,
			     v1_17_1, v1_18_2, v1_19_4, v1_20_6, v1_21, v1_21_1, v1_21_2, v1_21_3,
			     v1_21_4, v1_21_5, v1_21_6, v1_21_7, v1_21_8 -> {

				return Map.of(
						"value_materialBorderItem", "BLACK_STAINED_GLASS_PANE",
						"value_materialHomeItem", "GRASS_BLOCK",
						"value_materialAdminModeItem", "ENDER_EYE"
				);
			}
		}

		return new HashMap<>();
	}


	public List<String> getKeysToRemove() {

		MCServerVersion serverVersion = Main.getInstance().getMCServerVersion();
		switch (serverVersion) {

			case v1_13_2, v1_14_4, v1_15_2, v1_16_5,
			     v1_17_1, v1_18_2, v1_19_4, v1_20_6, v1_21, v1_21_1, v1_21_2, v1_21_3,
			     v1_21_4, v1_21_5, v1_21_6, v1_21_7, v1_21_8 -> {
				return List.of("data");
			}
		}

		return List.of();
	}


	// -------------------------------------------------- //
}
