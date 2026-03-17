package fr.snipertvmc.essentialsxgui.inventories.warps;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGWarp;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps.EXGWarpsAdminViewInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.DataEntryUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class WarpsAdminViewInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGWarpsAdminViewInventoryConfig config = Main.getInstance().getInventoriesManager().getWarpsAdminViewInventoryConfig().copy();


	// -------------------------------------------------- //


	public WarpsAdminViewInventory(Player player, String warpSearch, Set<EXGWarp> definedWarps) {
		super(
				Main.getInstance().getInventoriesManager().getWarpsAdminViewInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getWarpsAdminViewInventoryConfig().getEXGTitle()
						.duplicate()
						.getTitle(player)
		);


		Main.getInstance().getServerDataManager().updateServerWarps();

		Set<EXGWarp> warps = warpSearch != null ? definedWarps :

				Main.getInstance().getEXGServer().getWarps()
						.stream()
						.sorted(Comparator.comparing(EXGWarp::getName))
						.collect(Collectors.toCollection(LinkedHashSet::new));


		initializeGeneralInventory(player);
		defineWarpsItems(player, warps, warpSearch);
		defineSwitchToPlayerModeItem(player);
		defineCreateWarpItem(player);
		defineSearchWarpItem(player, warps, warpSearch);
	}


	// -------------------------------------------------- //


	private void defineWarpsItems(Player player, Set<EXGWarp> warps, String warpSearch) {

		for (EXGWarp warp : warps) {

			EXGItemConfig warpItem = config.getWarpItem().duplicate();
			warpItem.setMaterial(warp.getMaterial().name());
			warpItem.setData(warp.getData());

			ItemStack warpItemStack;

			if (warp.getCustomItemStack() != null) {
				warpItemStack = warp.getCustomItemStack().clone();
				ItemMeta meta = warpItemStack.getItemMeta();

				meta.setDisplayName(warpItem.getDisplayName()
						.replace("{warpDisplayName}", warp.getDisplayName())
						.replace("{warpName}", warp.getName()));

				meta.setLore(warpItem.getLore().stream()
						.map(line -> line
								.replace("{warpDisplayName}", warp.getDisplayName())
								.replace("{warpName}", warp.getName()))
						.collect(Collectors.toList()));

				warpItemStack.setItemMeta(meta);

			} else {
				warpItemStack = warpItem
						.updateVariables(
								Map.of("warpDisplayName", warp.getDisplayName(),
										"warpName", warp.getName()))
						.build(player);
			}

			addContent(warpItemStack, e -> {

				if (warpItem.isCorrectClick(e.getClick(), "teleportWarp")) {
					new WarpPlayerTeleportInventory(player, warp).open(player);

				} else if (warpItem.isCorrectClick(e.getClick(), "editWarp")) {
					new WarpEditingInventory(player, warp).open(player);

				} else if (warpItem.isCorrectClick(e.getClick(), "deleteWarp")) {
					new WarpEditingInventory(player, warp).deleteWarp(player, warp);
				}

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


	private void defineSwitchToPlayerModeItem(Player player) {

		if (config.getSwitchToPlayerModeItem().isEnabled()) {
			setItem(config.getSwitchToPlayerModeItem().getSlot(), config.getSwitchToPlayerModeItem().build(player), e -> {

				new WarpsPlayerViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}
	}


	private void defineCreateWarpItem(Player player) {

		if (config.getCreateWarpItem().isEnabled()) {
			setItem(config.getCreateWarpItem().getSlot(), config.getCreateWarpItem()
					.build(player), e -> {

				if (player.hasPermission("essentials.setwarp")) {
					createNewWarp(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
					return;
				}

				TextUtils.sendComponentToCommandSender(player, MessagesUtils.getComponent(EXGMessage.NO_PERMISSION, null));
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			});
		}
	}


	private void defineSearchWarpItem(Player player, Set<EXGWarp> warps, String warpSearch) {

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

					new WarpsAdminViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}
		}
	}


	private void initializeGeneralInventory(Player player) {

		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem().build(player));
		}


		previousPageItem(config.getPreviousPageItem().getSlot(), p -> config.getPreviousPageItem().duplicate()
				.updateVariables(
						Map.of("currentPage", String.valueOf(p + 1),
								"previousPage", String.valueOf(p)))
				.build(player));


		nextPageItem(config.getNextPageItem().getSlot(), p -> config.getNextPageItem().duplicate()
				.updateVariables(
						Map.of("currentPage", String.valueOf(p - 1),
								"nextPage", String.valueOf(p)))
				.build(player));


		if (config.getCloseItem().isEnabled()) {
			setItem(config.getCloseItem().getSlot(), config.getCloseItem().build(player), e -> {

				e.getWhoClicked().closeInventory();
				SoundsUtils.playSound(player, EXGSound.GUI_CLOSE);
			});
		}


		config.getInventoryScheme().apply(this);
	}


	// -------------------------------------------------- //


	private void createNewWarp(Player player) {

		if (Main.getInstance().getConfiguration().skipDataEntryProcess()) {
			String instantCreationDefaultWarpName = Main.getInstance().getConfiguration().getInstantCreationDefaultWarpName();
			int warpNumber = 1;

			if (instantCreationDefaultWarpName.contains("%number%")) {

				List<String> warpsName = Main.getInstance().getServerManager().getEXGServer().getWarps().stream()
						.map(EXGWarp::getName)
						.toList();

				while (warpsName.contains(instantCreationDefaultWarpName.replace("%number%", String.valueOf(warpNumber)))) {
					warpNumber++;
				}
			}

			String finalWarpName = instantCreationDefaultWarpName
					.replace("%number%", String.valueOf(warpNumber))
					.replace(" ", "_");

			try {
				Main.getInstance().getHookManager().getEssentialsHook().createWarpWithPlayer(player, finalWarpName);
				TextUtils.sendComponentToCommandSender(player, MessagesUtils.getComponent(EXGMessage.WARP_CREATED, Map.of("warpName", finalWarpName)));
				new WarpsAdminViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

			} catch (Exception e) {
				TextUtils.sendComponentToCommandSender(player, MessagesUtils.getComponent(EXGMessage.WARP_CREATION_ERROR));
				new WarpsAdminViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			}
			return;
		}

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getFilesManager().getConfiguration().getEntryType("warps", "createNewWarpEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendComponentToCommandSender(player, MessagesUtils.getComponent(EXGMessage.ENTER_NEW_WARP_NAME_CHAT));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.ENTER_NEW_WARP_NAME))
				.setMinLength(Main.getInstance().getConfiguration().getMinNameLength())
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					String warpName = result.getLeft();

					try {
						Main.getInstance().getHookManager().getEssentialsHook().createWarpWithPlayer(player, warpName.replace(" ", "_"));
						TextUtils.sendComponentToCommandSender(player, MessagesUtils.getComponent(EXGMessage.WARP_CREATED, Map.of("warpName", warpName)));
						new WarpsAdminViewInventory(player, null, null).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

					} catch (Exception e) {
						TextUtils.sendComponentToCommandSender(player, MessagesUtils.getComponent(EXGMessage.WARP_CREATION_ERROR));
						new WarpsAdminViewInventory(player, null, null).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					}

				}, entry -> new WarpsAdminViewInventory(player, null, null).open(player)
		);
	}


	private void searchWarp(Player player) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getFilesManager().getConfiguration().getEntryType("warps", "searchWarpEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendComponentToCommandSender(player, MessagesUtils.getComponent(EXGMessage.SEARCH_WARP_CHAT));
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
							.filter(warp -> warp.getDisplayName().toLowerCase().contains(result.getLeft().toLowerCase()) ||
									warp.getName().toLowerCase().contains(result.getLeft().toLowerCase()))
							.sorted(Comparator.comparing(EXGWarp::getName))
							.collect(Collectors.toCollection(LinkedHashSet::new));

					if (searchWarps.isEmpty()) {
						TextUtils.sendComponentToCommandSender(player, MessagesUtils.getComponent(EXGMessage.NO_WARP_FOUND));
						new WarpsAdminViewInventory(player, result.getLeft(), searchWarps).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					new WarpsAdminViewInventory(player, result.getLeft(), searchWarps).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new WarpsAdminViewInventory(player, null, null).open(player)
		);
	}


	// -------------------------------------------------- //


	@Override
	protected void onPageChange(int page) {

		Player player = this.getInventory().getViewers().isEmpty() ? null : (Player) this.getInventory().getViewers().get(0);

		setItem(config.getCurrentPageItem().getSlot(), config.getCurrentPageItem()
				.updateVariables(
						Map.of("currentPage", String.valueOf(this.currentPage()),
								"totalPages", String.valueOf(this.lastPage()),
								"previousPage", String.valueOf(this.currentPage() - 1),
								"nextPage", String.valueOf(this.currentPage() + 1)))
				.build(player));

		SoundsUtils.playSound(player, EXGSound.GUI_PAGE_CHANGE);
	}


	// -------------------------------------------------- //
}
