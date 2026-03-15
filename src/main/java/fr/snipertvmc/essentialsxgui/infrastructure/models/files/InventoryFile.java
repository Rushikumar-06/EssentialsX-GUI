package fr.snipertvmc.essentialsxgui.infrastructure.models.files;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XItemFlag;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.MCServerVersion;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;
import fr.snipertvmc.essentialsxgui.utilities.config.EXGItemConfigParser;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.ClickType;

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
			return new EXGItemConfig(false, (short) 0, Material.AIR.name(), 1, (byte) 0, null, null, null, null, new HashMap<>(), 0);
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
					"<dark_red><bold>Invalid Item",
					List.of("<red>This item configuration is invalid.",
							"  <dark_gray>Please check the <white>" + inventoryName + ".yml <dark_gray>file",
							"  <dark_gray>and fix error(s) seen in the console.",
							"",
							"<gold>Item path involved: ",
							"<dark_gray>- <yello>" + path),
					null, null, new HashMap<>(), 0
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

				(Integer) itemConfiguration.get("customModelData")
		);
	}


	// -------------------------------------------------- //


	public YamlConfiguration getYamlConfiguration() {
		return yamlConfiguration;
	}


	// -------------------------------------------------- //


	public List<String> getKeysToRemove() {

		MCServerVersion serverVersion = Main.getInstance().getMCServerVersion();
		switch (serverVersion) {

			case v1_13_2, v1_14_4, v1_15_2, v1_16_5,
			     v1_17_1, v1_18_2, v1_19_4, v1_20_6, v1_21, v1_21_1, v1_21_2, v1_21_3,
			     v1_21_4, v1_21_5, v1_21_6, v1_21_7, v1_21_8, v1_21_9, v1_21_10, v1_21_11 -> {
				return List.of("data");
			}
		}

		return List.of();
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


	// -------------------------------------------------- //
}
