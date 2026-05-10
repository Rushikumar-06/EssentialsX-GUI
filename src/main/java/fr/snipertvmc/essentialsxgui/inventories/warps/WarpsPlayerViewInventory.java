package fr.snipertvmc.essentialsxgui.inventories.warps;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGPermission;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGWarp;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps.EXGWarpsPlayerViewInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.DataEntryUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class WarpsPlayerViewInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGWarpsPlayerViewInventoryConfig config = Main.getInstance().getInventoriesManager().getWarpsPlayerViewInventoryConfig().copy();


	// -------------------------------------------------- //


	public WarpsPlayerViewInventory(Player player, String warpSearch, Set<EXGWarp> definedWarps) {
		super(
				Main.getInstance().getInventoriesManager().getWarpsPlayerViewInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getWarpsPlayerViewInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(
								Map.of("player", player.getName()))
						.getTitle(player)
		);


		Main.getInstance().getServerDataManager().updateServerWarps();

		Set<EXGWarp> warps = warpSearch != null ? definedWarps :

				Main.getInstance().getEXGServer().getWarps()
						.stream()
						.filter(warp -> player.hasPermission("essentials.warps." + warp.getName()))
						.sorted(Comparator.comparing(EXGWarp::getName))
						.collect(Collectors.toCollection(LinkedHashSet::new));


		initializeGeneralInventory(player);
		InventoriesUtils.initializePaginatedInventory(player, config, this);
		defineWarpsItems(player, warpSearch, warps);
		defineSwitchToAdminModeItem(player);
		defineSearchWarpItem(player, warpSearch, warps);
	}


	// -------------------------------------------------- //


	private void defineWarpsItems(Player player, String warpSearch, Set<EXGWarp> warps) {

		for (EXGWarp warp : warps) {

			EXGItemConfig warpItem = config.getWarpItem().duplicate();
			ItemStack warpItemStack = InventoriesUtils.getWarpItemStack(warpItem, warp, player);

			addContent(warpItemStack, e -> {
				e.getWhoClicked().closeInventory();
				player.performCommand("essentials:warp " + warp.getName());
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}

		if (warps.isEmpty()) {

			if (warpSearch == null) {
				addContent(config.getNoWarpsItem().build(player));

			} else {
				addContent(config.getNoSearchWarpResultsItem()
						.updateVariables(
								Map.of("warpSearch", warpSearch))
						.build(player));
			}
		}
	}


	private void defineSwitchToAdminModeItem(Player player) {

		if (config.getSwitchToAdminModeItem().isEnabled() && player.hasPermission(EXGPermission.WARPS_ADMIN.get())) {
			setItem(config.getSwitchToAdminModeItem().getSlot(), config.getSwitchToAdminModeItem().build(player), e -> {

				new WarpsAdminViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}
	}


	private void defineSearchWarpItem(Player player, String warpSearch, Set<EXGWarp> warps) {

		if (warpSearch == null) {
			if (config.getSearchWarpItem().isEnabled() && !warps.isEmpty()) {
				setItem(config.getSearchWarpItem().getSlot(), config.getSearchWarpItem()
						.build(player), e -> {

					searchWarp(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}

		} else {
			if (config.getCancelSearchWarpItem().isEnabled()) {
				setItem(config.getCancelSearchWarpItem().getSlot(), config.getCancelSearchWarpItem()
						.build(player), e -> {

					new WarpsPlayerViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}
		}
	}


	private void initializeGeneralInventory(Player player) {

		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem().build(player));
		}


		if (config.getCloseItem().isEnabled()) {
			setItem(config.getCloseItem().getSlot(), config.getCloseItem().build(player), e -> {

				e.getWhoClicked().closeInventory();
				SoundsUtils.playSound(player, EXGSound.GUI_CLOSE);
			});
		}


		config.getInventoryScheme().apply(this);
	}


	// -------------------------------------------------- //


	private void searchWarp(Player player) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getFilesManager().getConfiguration().getEntryType("warps", "searchWarpEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.SEARCH_WARP_CHAT));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.SEARCH_WARP))
				.setMinLength(1)
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					Set<EXGWarp> searchWarps = Main.getInstance().getEXGServer().getWarps()
							.stream()
							.filter(warp -> player.hasPermission("essentials.warps." + warp.getName()))
							.filter(warp -> warp.getDisplayName().toLowerCase().contains(result.getLeft().toLowerCase()) ||
									warp.getName().toLowerCase().contains(result.getLeft().toLowerCase()))
							.sorted(Comparator.comparing(EXGWarp::getName))
							.collect(Collectors.toCollection(LinkedHashSet::new));

					if (searchWarps.isEmpty()) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.NO_WARP_FOUND));
						new WarpsPlayerViewInventory(player, result.getLeft(), searchWarps).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					new WarpsPlayerViewInventory(player, result.getLeft(), searchWarps).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new WarpsPlayerViewInventory(player, null, null).open(player)
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
