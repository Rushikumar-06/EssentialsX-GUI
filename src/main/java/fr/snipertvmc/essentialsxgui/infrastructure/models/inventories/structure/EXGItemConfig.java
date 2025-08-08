package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.ItemBuilder;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.data.TypeUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EXGItemConfig {


	// -------------------------------------------------- //


	private boolean enabled = false;

	private short slot;

	private String materialName;
	private int amount;
	private byte data;

	private String displayName;
	private List<String> lore;

	private final List<Pair<Enchantment, Integer>> enchantments;
	private final List<ItemFlag> itemFlags;

	private Map<String, String> variables = new HashMap<>();


	// -------------------------------------------------- //


	public EXGItemConfig(boolean enabled,
	                     short slot,
	                     String materialName, int amount, byte data,
	                     String displayName, List<String> lore,
	                     List<Pair<Enchantment, Integer>> enchantments, List<ItemFlag> itemFlags) {

		this.enabled = enabled;

		this.slot = slot;

		this.materialName = materialName;
		this.amount = amount;
		this.data = data;

		this.displayName = displayName;
		this.lore = lore;

		this.enchantments = enchantments;
		this.itemFlags = itemFlags;
	}


	public EXGItemConfig(EXGItemConfig itemConfig) {
		this.enabled = itemConfig.isEnabled();
		this.slot = itemConfig.getSlot();

		this.materialName = itemConfig.getMaterialName();
		this.amount = itemConfig.getAmount();
		this.data = itemConfig.getData();

		this.displayName = itemConfig.getDisplayName();
		this.lore = itemConfig.getLore();

		this.enchantments = itemConfig.getEnchantments();
		this.itemFlags = itemConfig.getItemFlags();
	}


	// -------------------------------------------------- //


	public boolean isEnabled() {
		return enabled;
	}

	public short getSlot() {
		return slot;
	}

	public String getMaterialName() {
		return materialName;
	}

	public Material getMaterial() {
		Material possibleMaterial = Material.matchMaterial(materialName);
		if (possibleMaterial == null) {
			ConsoleLogger.error("Material " + materialName + " not found.");
			return switch (Main.getInstance().getMCServerVersion()) {
				case v1_8_8, v1_9_4, v1_10_2, v1_11_2, v1_12_2 -> Material.matchMaterial("GRASS");
				default -> Material.matchMaterial("GRASS_BLOCK");
			};
		}
		return possibleMaterial;
	}

	public int getAmount() {
		return amount;
	}

	public byte getData() {
		return data;
	}

	public String getDisplayName() {
		return displayName;
	}

	public List<String> getLore() {
		return lore;
	}

	public List<Pair<Enchantment, Integer>> getEnchantments() {
		return enchantments;
	}

	public List<ItemFlag> getItemFlags() {
		return itemFlags;
	}


	// -------------------------------------------------- //


	public EXGItemConfig setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}


	public EXGItemConfig setSlot(short slot) {
		this.slot = slot;
		return this;
	}


	public EXGItemConfig setMaterial(String materialName) {
		this.materialName = materialName;
		return this;
	}


	public EXGItemConfig setData(byte data) {
		this.data = data;
		return this;
	}


	public EXGItemConfig setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}


	public EXGItemConfig setLore(List<String> lore) {
		this.lore = lore;
		return this;
	}


	// -------------------------------------------------- //


	public ItemStack build() {

		ItemBuilder itemBuilder = getBaseItem();

		if (!enabled) {
			return new ItemStack(Material.AIR);
		}

		if (amount < 0 || amount > 64) {
			amount = 1;
		}
		itemBuilder.amount(amount);

		if (data < 0 || data > 15) {
			data = 0;
		}
		itemBuilder.data(data);

		if (displayName != null) {
			itemBuilder.name(displayName.replace("&", "§"));
		} else {
			itemBuilder.name(this.getMaterial().name());
		}

		if (lore != null) {
			itemBuilder.lore(lore.stream()
					.map(line -> line.replace("&", "§"))
					.collect(Collectors.toList()));
		}

		if (enchantments != null) {
			for (Pair<Enchantment, Integer> enchantment : enchantments) {
				itemBuilder.enchant(enchantment.getLeft(), enchantment.getRight());
			}
		}

		if (itemFlags != null) {
			for (ItemFlag itemFlag : itemFlags) {
				itemBuilder.flags(itemFlag);
			}
		}

		return itemBuilder.build();
	}


	private @NotNull ItemBuilder getBaseItem() {

		ItemBuilder itemBuilder;

		if (!enabled) {
			return new ItemBuilder(Material.AIR);
		}

		if (materialName == null || materialName.equals("AIR")) {
			itemBuilder = new ItemBuilder(Material.GRASS);

		} else if (materialName.startsWith("PLAYER_HEAD:")) {

			String playerHeadName = materialName.substring("PLAYER_HEAD:".length());

			Material material = switch (Main.getInstance().getMCServerVersion()) {
				case v1_8_8, v1_9_4, v1_10_2, v1_11_2, v1_12_2 -> Material.matchMaterial("SKULL_ITEM");
				default -> Material.matchMaterial("PLAYER_HEAD");
			};

			itemBuilder = new ItemBuilder(material);
			itemBuilder.meta(itemMeta -> {
				SkullMeta skullMeta = (SkullMeta) itemMeta;
				skullMeta.setOwner(playerHeadName);
			});

		} else {
			itemBuilder = new ItemBuilder(getMaterial());
		}

		return itemBuilder;
	}


	// -------------------------------------------------- //


	public EXGItemConfig updateVariables(Map<String, String> variables) {

		this.variables = variables;
		variables.forEach((key, value) -> {

			if (materialName != null) {
				materialName = materialName.replace("{" + key + "}", value);
			}

			if (displayName != null) {
				displayName = displayName.replace("{" + key + "}", value);
			}

			if (lore != null) {
				lore = lore.stream()
						.map(line -> line.replace("{" + key + "}", value))
						.collect(Collectors.toList());
			}
		});

		return this;
	}


	// -------------------------------------------------- //


	public EXGItemConfig duplicate() {
		return new EXGItemConfig(this);
	}


	// -------------------------------------------------- //
}
