package fr.snipertvmc.essentialsxgui.infrastructure.models.files;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XItemFlag;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.config.EXGItemConfigParser;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.ClickType;

import java.util.*;

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


	public EXGItemConfig getItem(String itemName, EXGInventoryConfig inventoryConfig) {
		EXGItemConfig itemConfig = getItem(inventoryName + ".items." + itemName, false);
		short slot = (short) yamlConfiguration.getInt(inventoryName + ".items." + itemName + ".slot");
		itemConfig.setSlot(slot);
		if (itemConfig.isMisconfigured()) inventoryConfig.incrementConfigurationErrors();
		return itemConfig;
	}


	public List<EXGItemConfig> getItems(String itemName, EXGInventoryConfig inventoryConfig) {
		EXGItemConfig itemConfig = getItem(inventoryName + ".items." + itemName, true);
		List<Integer> slots = yamlConfiguration.getIntegerList(inventoryName + ".items." + itemName + ".slots");
		List<EXGItemConfig> itemConfigs = new ArrayList<>();
		for (int slot : slots) {
			EXGItemConfig itemConfigCopy = itemConfig.duplicate();
			itemConfigCopy.setSlot((short) slot);
			itemConfigs.add(itemConfigCopy);
			if (itemConfig.isMisconfigured()) inventoryConfig.incrementConfigurationErrors();
		}
		return itemConfigs;
	}


	public List<EXGItemConfig> getItems(String itemName, boolean withAmountValues, EXGInventoryConfig inventoryConfig) {
		if (!withAmountValues) return getItems(itemName, inventoryConfig);
		EXGItemConfig itemConfig = getItem(inventoryName + ".items." + itemName, true);
		List<Integer> slots = yamlConfiguration.getIntegerList(inventoryName + ".items." + itemName + ".slots");
		List<Double> amountValues = yamlConfiguration.getDoubleList(inventoryName + ".items." + itemName + ".amountValues");
		List<EXGItemConfig> itemConfigs = new ArrayList<>();
		for (int i = 0; i < slots.size(); i++) {
			int slot = slots.get(i);
			double amountValue = amountValues.size() > i ? amountValues.get(i) : 0;
			EXGItemConfig itemConfigCopy = itemConfig.duplicate();
			itemConfigCopy.setSlot((short) slot);
			itemConfigCopy.setAmountValue(amountValue);
			itemConfigs.add(itemConfigCopy);
			if (itemConfig.isMisconfigured()) inventoryConfig.incrementConfigurationErrors();
		}
		return itemConfigs;
	}


	public Set<EXGItemConfig> getItemsSection(String sectionPath, EXGInventoryConfig inventoryConfig) {

		ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection(sectionPath);
		if (configurationSection == null) return Collections.emptySet();

		Set<EXGItemConfig> itemConfigs = new HashSet<>();

		for (String key : configurationSection.getKeys(false)) {

			EXGItemConfig itemConfig = getItem(sectionPath + "." + key, false);

			// Range slots
			if (key.contains("-")) {

				int startRange;
				int endRange;

				try {
					startRange = Integer.parseInt(key.split("-")[0]);
					endRange = Integer.parseInt(key.split("-")[1]);

				} catch (NumberFormatException e) {
					ConsoleLogger.error("Invalid slot range format for item '" + key + "' in inventory '" + inventoryName + "'." +
							"Expected format: 'start-end' (e.g., '0-8'). Skipping this item.");
					continue;
				}

				for (int i = startRange; i <= endRange; i++) {
					itemConfig.setSlot((short) i);
					itemConfigs.add(itemConfig);
				}
				continue;
			}


			// Specific slot
			int slot;

			try {
				slot = Integer.parseInt(key.split("-")[0]);

			} catch (NumberFormatException e) {
				ConsoleLogger.error("Invalid slot format for item '" + key + "' in inventory '" + inventoryName + "'." +
						"Expected a number or a range (e.g., '0' or '0-8'). Skipping this item.");
				continue;
			}

			itemConfig.setSlot((short) slot);
			itemConfigs.add(itemConfig);
			if (itemConfig.isMisconfigured()) inventoryConfig.incrementConfigurationErrors();
		}

		return itemConfigs;
	}


	public EXGItemConfig getBorderItem() {
		return getItem(inventoryName + ".borderItem", true);
	}


	public int[] getBorderSlots() {
		List<Short> borderSlots = yamlConfiguration.getShortList(inventoryName + ".borderItem.slots");
		return borderSlots.stream().mapToInt(Short::intValue).toArray();
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


	private EXGItemConfig getItem(String path, boolean multipleSlots) {

		Object enabledValue = yamlConfiguration.get(path + ".enabled", true);
		if (enabledValue instanceof Boolean isEnabled && !isEnabled) {
			return new EXGItemConfig(false, (short) 0, Material.AIR.name(), 1, (byte) 0, null, null, null, null, new HashMap<>(), 0, 0, 0, false);
		}

		// Check if the item configuration is valid
		if (!EXGItemConfigParser.isEXGItemConfigValid(this, path, multipleSlots)) {

			Object slotValue = yamlConfiguration.get(path + ".slot");
			short slot = 0;

			if (slotValue instanceof Number slotNumber) {
				slot = slotNumber.shortValue();
			}

			return new EXGItemConfig(true,
					slot,
					"BEDROCK", 1, (byte) 0,
					"<dark_red><bold>Invalid Item",
					List.of("<red>This item configuration is invalid.",
							"  <dark_gray>Please check the <white>" + inventoryName + ".yml <dark_gray>file",
							"  <dark_gray>and fix error(s) seen in the console.",
							"",
							"<gold>Item path involved: ",
							"<dark_gray>- <yellow>" + path),
					null, null, new HashMap<>(), 0, 0, 0,
					true
			);
		}

		return parseItemConfig(path);
	}


	private EXGItemConfig parseItemConfig(String path) {

		Map<String, Object> itemConfiguration = new HashMap<>() {{
			put("enabled", yamlConfiguration.get(path + ".enabled"));

			put("slot", yamlConfiguration.getInt(path + ".slot"));

			put("material", yamlConfiguration.getString(path + ".material"));
			put("amount", yamlConfiguration.getInt(path + ".amount", 1));
			put("data", yamlConfiguration.getInt(path + ".data", 0));

			put("displayName", yamlConfiguration.getString(path + ".displayName"));
			put("lore", yamlConfiguration.getStringList(path + ".lore"));

			put("enchantments", yamlConfiguration.getStringList(path + ".enchantments"));
			put("itemFlags", yamlConfiguration.getStringList(path + ".itemFlags"));

			put("clickActions", yamlConfiguration.getConfigurationSection(path + ".clickActions"));

			put("customModelData", yamlConfiguration.getInt(path + ".customModelData", 0));

			put("updateItemInterval", yamlConfiguration.getInt(path + ".updateItemInterval", 0));
			put("amountValue", yamlConfiguration.getInt(path + ".amountValue", 0));
		}};


		List<String> lore = new ArrayList<>((List<String>) itemConfiguration.get("lore"));

		List<Pair<XEnchantment, Integer>> enchantments = new ArrayList<>();
		for (Object enchantment : (List<String>) itemConfiguration.get("enchantments")) {
			String[] enchantmentSplit = ((String) enchantment).split(":");
			if (XEnchantment.of(enchantmentSplit[0]).isPresent()) {
				enchantments.add(Pair.of(XEnchantment.of(enchantmentSplit[0]).get(), Integer.parseInt(enchantmentSplit[1])));
			}
		}

		List<XItemFlag> itemFlags = new ArrayList<>();
		for (String itemFlag : (List<String>) itemConfiguration.get("itemFlags")) {
			if (XItemFlag.of(itemFlag).isPresent()) {
				itemFlags.add(XItemFlag.of(itemFlag).get());
			}
		}

		ConfigurationSection clickActionsSection = (ConfigurationSection) itemConfiguration.get("clickActions");
		Map<String, ClickType> clickActions = new HashMap<>();
		if (clickActionsSection != null) {
			for (String action : clickActionsSection.getKeys(false)) {
				String clickTypeString = clickActionsSection.getString(action);
				clickActions.put(action, ClickType.valueOf(clickTypeString.toUpperCase()));
			}
		}

		Object booleanValue = itemConfiguration.get("enabled");
		if (!(booleanValue instanceof Boolean)) {
			booleanValue = true;
		}

		return new EXGItemConfig(
				(boolean) booleanValue,
				((Number) itemConfiguration.get("slot")).shortValue(),

				(String) itemConfiguration.get("material"),
				(Integer) itemConfiguration.get("amount"),
				((Number) itemConfiguration.get("data")).byteValue(),

				(String) itemConfiguration.get("displayName"),
				lore,

				enchantments,
				itemFlags,

				clickActions,

				(Integer) itemConfiguration.get("customModelData"),

				(Integer) itemConfiguration.get("updateItemInterval"),
				(Integer)  itemConfiguration.get("amountValue"),

				false
		);
	}


	// -------------------------------------------------- //


	public YamlConfiguration getYamlConfiguration() {
		return yamlConfiguration;
	}


	// -------------------------------------------------- //


	// CUSTOMS CONFIGURATIONS
	public String getFullBedHomeItemMaterial(String worldName) {
		String materialName = yamlConfiguration.getString("bedHomeItem." + worldName + ".material");
		int materialData = yamlConfiguration.getInt("bedHomeItem." + worldName + ".data", 0);
		return materialName + ":" + materialData;
	}

	public String getBedHomeItemWorldDisplayName(String worldName) {
		return yamlConfiguration.getString("bedHomeItem." + worldName + ".worldDisplayName");
	}

	public Pair<Integer, Integer> getRankingRange() {
		String range = yamlConfiguration.getString("rankingRange", "1-10");
		String[] rangeSplit = range.split("-");
		return Pair.of(Integer.parseInt(rangeSplit[0]), Integer.parseInt(rangeSplit[1]));
	}


	// -------------------------------------------------- //
}
