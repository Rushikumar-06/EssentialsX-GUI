package fr.snipertvmc.essentialsxgui.inventories.economy;

import com.earth2me.essentials.utils.NumberUtil;
import com.earth2me.essentials.utils.VersionUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.EXGWorthAllInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.DataEntryUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorthAllInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGWorthAllInventoryConfig config = Main.getInstance().getInventoriesManager().getWorthAllInventoryConfig().copy();


	// -------------------------------------------------- //


	public WorthAllInventory(Player player, String worthSearch, Map<String, BigDecimal> itemsWorth) {
		super(
				Main.getInstance().getInventoriesManager().getWorthAllInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getWorthAllInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(
								Map.of("player", player.getName()))
						.getTitle(player)
		);


		InventoriesUtils.initializeBorderItem(player, config, this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new WorthInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}


		if (itemsWorth == null) {
			itemsWorth = Main.getInstance().getEXGServer().getWorth().getItemsWorth();
		}

		addWorthItem(player, itemsWorth, worthSearch);
		addSearchWorthItem(player, itemsWorth, worthSearch);
	}


	// -------------------------------------------------- //


	private void addWorthItem(Player player, Map<String, BigDecimal> itemsWorth, String worthSearch) {

		// Without data
		if (VersionUtil.getServerBukkitVersion().isLowerThanOrEqualTo(VersionUtil.BukkitVersion.fromString("1.12.2-R0.1-SNAPSHOT"))) {

			for (Map.Entry<String, BigDecimal> entry : itemsWorth.entrySet()) {

				String materialName = entry.getKey().split(":")[0];
				String dataValue = entry.getKey().split(":")[1];

				Material material = Main.getInstance().getEXGServer().getWorth().getMaterialFromWorthName(materialName);
				byte data = !dataValue.equals("*") ? Byte.valueOf(dataValue) : 0;

				BigDecimal itemPrice = entry.getValue();
				String itemWorth = NumberUtil.displayCurrency(itemPrice, Main.getInstance().getEssentials());

				addContent(config.getWorthItem()
						.duplicate()
						.setMaterial(material.name())
						.setData(data)
						.updateVariables(Map.of(
								"worthItemMaterial", material.name(),
								"worthItemData", dataValue,
								"itemWorth", itemWorth))
						.build(player));
			}

			// Without data
		} else {

			for (Map.Entry<String, BigDecimal> entry : itemsWorth.entrySet()) {

				String materialName = entry.getKey();
				Material material = Main.getInstance().getEXGServer().getWorth().getMaterialFromWorthName(materialName);

				BigDecimal itemPrice = entry.getValue();
				String itemWorth = NumberUtil.displayCurrency(itemPrice, Main.getInstance().getEssentials());

				addContent(config.getWorthItem()
						.duplicate()
						.setMaterial(material.name())
						.updateVariables(Map.of(
								"worthItemMaterial", material.name(),
								"itemWorth", itemWorth))
						.build(player));
			}
		}


		if (itemsWorth.isEmpty()) {

			if (worthSearch == null) {
				addContent(config.getNoWorthItem().build(player));

			} else {
				addContent(config.getNoSearchWorthItemsItem()
						.updateVariables(
								Map.of("worthSearch", worthSearch))
						.build(player));
			}
		}
	}


	private void addSearchWorthItem(Player player, Map<String, BigDecimal> itemsWorth, String worthSearch) {

		if (worthSearch == null) {
			if (config.getSearchWorthItem().isEnabled() && !itemsWorth.isEmpty()) {
				setItem(config.getSearchWorthItem().getSlot(), config.getSearchWorthItem()
						.build(player), e -> {

					searchWorth(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}

		} else {
			if (config.getCancelSearchWorthItem().isEnabled()) {
				setItem(config.getCancelSearchWorthItem().getSlot(), config.getCancelSearchWorthItem()
						.build(player), e -> {

					new WorthAllInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}
		}
	}


	// -------------------------------------------------- //


	private void searchWorth(Player player) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getFilesManager().getConfiguration().getEntryType("economy.worth", "searchWorthEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.SEARCH_WORTH_CHAT));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.SEARCH_WORTH))
				.setMinLength(1)
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					Map<String, BigDecimal> searchItemsWorth = Main.getInstance().getEXGServer().getWorth().getItemsWorth()
							.entrySet().stream()
							.filter(entry -> entry.getKey().toLowerCase().contains(result.getLeft().toLowerCase()))
							.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

					if (searchItemsWorth.isEmpty()) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.NO_WORTH_FOUND));
						new WorthAllInventory(player, result.getLeft(), searchItemsWorth).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					new WorthAllInventory(player, result.getLeft(), searchItemsWorth).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new WorthAllInventory(player, null, null).open(player)
		);
	}


	// -------------------------------------------------- //


	@Override
	protected void onPageChange(int page) {
		Player player = this.getInventory().getViewers().isEmpty() ? null : (Player) this.getInventory().getViewers().get(0);
		InventoriesUtils.updateCurrentPageItem(player, config, this);
		SoundsUtils.playSound(player, EXGSound.GUI_PAGE_CHANGE);
	}


	// -------------------------------------------------- //
}
