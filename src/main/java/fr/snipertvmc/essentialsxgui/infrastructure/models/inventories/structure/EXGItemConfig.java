package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure;

import fr.mrmicky.fastinv.ItemBuilder;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EXGItemConfig {


	// -------------------------------------------------- //


	private short slot;

	private Material material;
	private int amount;
	private byte data;

	private String displayName;
	private List<String> lore;

	private final List<Pair<Enchantment, Integer>> enchantments;
	private final List<ItemFlag> itemFlags;


	// -------------------------------------------------- //


	public EXGItemConfig(short slot,
	                     Material material, int amount, byte data,
	                     String displayName, List<String> lore,
	                     List<Pair<Enchantment, Integer>> enchantments, List<ItemFlag> itemFlags) {

		this.slot = slot;

		this.material = material;
		this.amount = amount;
		this.data = data;

		this.displayName = displayName;
		this.lore = lore;

		this.enchantments = enchantments;
		this.itemFlags = itemFlags;
	}


	public EXGItemConfig(EXGItemConfig itemConfig) {
		this.slot = itemConfig.getSlot();

		this.material = itemConfig.getMaterial();
		this.amount = itemConfig.getAmount();
		this.data = itemConfig.getData();

		this.displayName = itemConfig.getDisplayName();
		this.lore = itemConfig.getLore();

		this.enchantments = itemConfig.getEnchantments();
		this.itemFlags = itemConfig.getItemFlags();
	}


	// -------------------------------------------------- //


	public short getSlot() {
		return slot;
	}

	public Material getMaterial() {
		return material;
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


	public void setSlot(short slot) {
		this.slot = slot;
	}


	public void setMaterial(Material material) {
		this.material = material;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public void setLore(List<String> lore) {
		this.lore = lore;
	}


	// -------------------------------------------------- //


	public ItemStack build() {

		ItemBuilder itemBuilder;
		if (material == null || material == Material.AIR) {
			itemBuilder = new ItemBuilder(Material.GRASS);
		} else {
			itemBuilder = new ItemBuilder(material);
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


	// -------------------------------------------------- //


	public EXGItemConfig updateVariables(Map<String, String> variables) {


		variables.forEach((key, value) -> {
			if (displayName != null) {
				displayName = displayName.replace(key, value);
			}

			if (lore != null) {
				lore = lore.stream()
						.map(line -> line.replace(key, value))
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
