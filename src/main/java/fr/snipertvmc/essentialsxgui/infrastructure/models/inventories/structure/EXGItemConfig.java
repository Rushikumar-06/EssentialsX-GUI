package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XItemFlag;
import com.cryptomorin.xseries.XMaterial;
import com.earth2me.essentials.utils.VersionUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.ItemBuilder;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
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

	private final List<Pair<XEnchantment, Integer>> enchantments;
	private List<XItemFlag> itemFlags;

	private Map<String, String> variables = new HashMap<>();

	private Map<String, ClickType> clickActions = new HashMap<>();

	private int customModelData = 0;

	private int updateItemInterval = 0;
	private int amountValue = 0;


	// -------------------------------------------------- //


	public EXGItemConfig(boolean enabled,
	                     short slot,
	                     String materialName, int amount, byte data,
	                     String displayName, List<String> lore,
	                     List<Pair<XEnchantment, Integer>> enchantments, List<XItemFlag> itemFlags,
	                     Map<String, ClickType> clickActions,
	                     int customModelData,
	                     int updateItemInterval, int amountValue) {

		this.enabled = enabled;

		this.slot = slot;

		this.materialName = materialName;
		this.amount = amount;
		this.data = data;

		this.displayName = displayName;
		this.lore = lore;

		this.enchantments = enchantments;
		this.itemFlags = itemFlags;

		this.clickActions = clickActions;

		this.customModelData = customModelData;

		this.updateItemInterval = updateItemInterval;
		this.amountValue = amountValue;
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

		this.variables = new HashMap<>(itemConfig.getVariables());

		this.clickActions = new HashMap<>(itemConfig.getClickActions());

		this.customModelData = itemConfig.getCustomModelData();

		this.updateItemInterval = itemConfig.getUpdateItemInterval();
		this.amountValue = itemConfig.getAmountValue();
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
	public XMaterial getMaterial() {
		Optional<XMaterial> possibleMaterial = XMaterial.matchXMaterial(materialName);
		if (possibleMaterial.isEmpty()) {
			ConsoleLogger.error("Material " + materialName + " not found.");
			return XMaterial.GRASS_BLOCK;
		}
		return possibleMaterial.get();
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

	public List<Pair<XEnchantment, Integer>> getEnchantments() {
		return enchantments;
	}
	public List<XItemFlag> getItemFlags() {
		return itemFlags;
	}

	public Map<String, String> getVariables() {
		return variables;
	}

	public Map<String, ClickType> getClickActions() {
		return clickActions;
	}

	public int getCustomModelData() {
		return customModelData;
	}

	public boolean hasUpdateItemInterval() {
		return updateItemInterval > 0;
	}
	public int getUpdateItemInterval() {
		return updateItemInterval;
	}
	public int getAmountValue() {
		return amountValue;
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


	public EXGItemConfig setAmount(int amount) {
		this.amount = amount;
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


	public EXGItemConfig setItemFlags(List<XItemFlag> itemFlags) {
		this.itemFlags = itemFlags;
		return this;
	}


	public EXGItemConfig setCustomModelData(int customModelData) {
		this.customModelData = customModelData;
		return this;
	}


	public EXGItemConfig setUpdateItemInterval(int updateItemInterval) {
		this.updateItemInterval = updateItemInterval;
		return this;
	}


	public EXGItemConfig setAmountValue(int amountValue) {
		this.amountValue = amountValue;
		return this;
	}


	// -------------------------------------------------- //


	public ItemStack build(Player player) {

		ItemBuilder itemBuilder = getBaseItem();

		if (!enabled) {
			return new ItemStack(Material.AIR);
		}

		if (amount < 0 || amount > 64) {
			amount = 1;
		}
		itemBuilder.amount(amount);

		if (VersionUtil.getServerBukkitVersion().isLowerThanOrEqualTo(VersionUtil.BukkitVersion.fromString("1.12.2-R0.1-SNAPSHOT"))) {
			if (data < 0 || data > 15) {
				data = 0;
			}
			itemBuilder.data(data);
		}

		applyDisplayName(player, itemBuilder);
		applyLore(player, itemBuilder);

		applyEnchantments(itemBuilder);
		applyItemFlags(itemBuilder);

		return itemBuilder.build();
	}


	// -------------------------------------------------- //


	private void applyDisplayName(Player player, ItemBuilder itemBuilder) {
		if (displayName != null) {
			if (Main.getInstance().getLoadingManager().isPlaceholderAPISupported()) {
				itemBuilder.name(PlaceholderAPI.setPlaceholders(player, displayName));
			} else {
				itemBuilder.name(displayName);
			}

		} else {
			itemBuilder.name(this.getMaterial().name());
		}
	}


	private void applyLore(Player player, ItemBuilder itemBuilder) {
		if (lore != null) {
			if (Main.getInstance().getLoadingManager().isPlaceholderAPISupported()) {
				itemBuilder.lore(new ArrayList<>(PlaceholderAPI.setPlaceholders(player, lore)));
			} else {
				itemBuilder.lore(new ArrayList<>(lore));
			}
		}
	}


	private void applyEnchantments(ItemBuilder itemBuilder) {
		if (enchantments != null) {
			for (Pair<XEnchantment, Integer> enchantment : enchantments) {
				itemBuilder.enchant(enchantment.getLeft(), enchantment.getRight());
			}
		}
	}


	private void applyItemFlags(ItemBuilder itemBuilder) {
		if (itemFlags != null) {
			for (XItemFlag itemFlag : itemFlags) {
				itemBuilder.flags(itemFlag);
			}
		}
	}


	private @NotNull ItemBuilder getBaseItem() {

		ItemBuilder itemBuilder;

		if (!enabled) {
			return new ItemBuilder(XMaterial.AIR);
		}

		if (materialName == null || materialName.equals("AIR")) {
			itemBuilder = new ItemBuilder(XMaterial.GRASS_BLOCK);

		} else if (materialName.startsWith("PLAYER_HEAD:")) {

			String playerHeadName = materialName.substring("PLAYER_HEAD:".length());
			XMaterial material = XMaterial.PLAYER_HEAD;

			itemBuilder = new ItemBuilder(material);
			itemBuilder.meta(itemMeta -> {
				SkullMeta skullMeta = (SkullMeta) itemMeta;
				skullMeta.setOwner(playerHeadName);
			});

		} else if (getMaterial().get() == null || !getMaterial().get().isItem()) {
			return new ItemBuilder(XMaterial.BARRIER);

		} else {
			itemBuilder = new ItemBuilder(getMaterial());
		}

		if (VersionUtil.getServerBukkitVersion().isHigherThan(VersionUtil.BukkitVersion.fromString("1.13.2-R0.1-SNAPSHOT"))) {
			itemBuilder.meta(meta -> meta.setCustomModelData(customModelData));
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


	public boolean isCorrectClick(ClickType playerClickType, String actionName) {
		if (!clickActions.containsKey(actionName)) {
			return false;
		}
		return playerClickType.name().equals(clickActions.get(actionName).name());
	}


	// -------------------------------------------------- //
}
