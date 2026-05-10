package fr.snipertvmc.essentialsxgui.infrastructure.models;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.MCServerVersion;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EXGWorth {


	// -------------------------------------------------- //


	private final Map<String, BigDecimal> itemsWorth = new HashMap<>();
	private final Set<String> materials = new HashSet<>();


	// -------------------------------------------------- //


	public EXGWorth() {
		for (Material material : Material.values()) {
			materials.add(material.name().toLowerCase().replace("_", ""));
		}

		loadItemsWorth();
	}


	// -------------------------------------------------- //


	public void loadItemsWorth() {
		itemsWorth.clear();

		File file = Main.getInstance().getEssentials().getWorth().getFile();
		YamlConfiguration worthConfigurationFile = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection worthConfig =  worthConfigurationFile.getConfigurationSection("worth");
		if (worthConfig == null) return;

		for (String key : worthConfig.getKeys(false)) {
			String materialName = key.toLowerCase().replace("_", "");
			if (!materials.contains(materialName)) continue;

			// With data
			boolean greaterThan1_12 = Main.getInstance().getMCServerVersion().isHigherThan(MCServerVersion.v1_12_2);
			if (!greaterThan1_12 && worthConfig.getConfigurationSection(key) != null) {
				ConfigurationSection materialSection = worthConfig.getConfigurationSection(key);
				if (materialSection == null) continue;

				for (String dataKey : materialSection.getKeys(false)) {
					BigDecimal itemWorth = BigDecimal.valueOf(materialSection.getDouble(dataKey));
					itemsWorth.put(materialName + ":" + dataKey, itemWorth);
				}
				continue;
			}

			// Without data
			BigDecimal itemWorth = BigDecimal.valueOf(worthConfig.getDouble(key));
			itemsWorth.put(materialName, itemWorth);
		}
	}


	public BigDecimal getPrice(ItemStack itemStack) {

		String itemName = itemStack.getType().name().toLowerCase().replace("_", "");

		// Without data
		if (Main.getInstance().getMCServerVersion().isHigherThan(MCServerVersion.v1_12_2)) {
			return itemsWorth.getOrDefault(itemName, null);
		}

		// With data
		short itemData = itemStack.getDurability();

		if (itemsWorth.containsKey(itemName + ":" + itemData)) {
			return itemsWorth.get(itemName + ":" + itemData);
		} else return itemsWorth.getOrDefault(itemName + ":*", null);
	}


	// -------------------------------------------------- //
}
