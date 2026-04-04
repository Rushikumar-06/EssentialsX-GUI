package fr.snipertvmc.essentialsxgui.inventories.kits;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitsAdminViewInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
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

public class KitsAdminViewInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGKitsAdminViewInventoryConfig config = Main.getInstance().getInventoriesManager().getKitsAdminViewInventoryConfig().copy();


	// -------------------------------------------------- //


	public KitsAdminViewInventory(Player player, String kitSearch, Set<EXGKit> definedKits) {
		super(
				Main.getInstance().getInventoriesManager().getKitsAdminViewInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getKitsAdminViewInventoryConfig().getEXGTitle()
						.duplicate()
						.getTitle(player)
		);


		Main.getInstance().getServerDataManager().updateServerKits();

		Set<EXGKit> kits = kitSearch != null ? definedKits :

				Main.getInstance().getEXGServer().getKits()
						.stream()
						.sorted(Comparator.comparing(EXGKit::getName))
						.collect(Collectors.toCollection(LinkedHashSet::new));


		initializeGeneralInventory(player);
		InventoriesUtils.initializePaginatedInventory(player, config, this);
		defineKitsItems(player, kits, kitSearch);
		defineSwitchToPlayerModeItem(player);
		defineCreateKitItem(player);
		defineSearchKitItem(player, kits, kitSearch);
	}


	// -------------------------------------------------- //


	private void defineKitsItems(Player player, Set<EXGKit> kits, String kitSearch) {

		for (EXGKit kit : kits) {

			EXGItemConfig kitItem = config.getKitItem().duplicate();
			ItemStack kitItemStack = InventoriesUtils.getKitItemStack(kitItem, kit, player);

			addContent(kitItemStack, e -> {

				if (kitItem.isCorrectClick(e.getClick(), "giveKit")) {
					new KitPlayerGiveInventory(player, kit).open(player);

				} else if (kitItem.isCorrectClick(e.getClick(), "editKit")) {
					new KitEditingInventory(player, kit).open(player);

				} else if (kitItem.isCorrectClick(e.getClick(), "deleteKit")) {
					new KitEditingInventory(player, kit).deleteKit(player, kit);
				}

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}

		if (kits.isEmpty()) {

			if (kitSearch == null) {
				addContent(config.getNoKitsItem().build(player));

			} else {
				addContent(config.getNoSearchKitResultsItem()
						.updateVariables(
								Map.of("kitSearch", kitSearch))
						.build(player));
			}
		}
	}


	private void defineSwitchToPlayerModeItem(Player player) {

		if (config.getSwitchToPlayerModeItem().isEnabled()) {
			setItem(config.getSwitchToPlayerModeItem().getSlot(), config.getSwitchToPlayerModeItem().build(player), e -> {

				new KitsPlayerViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}
	}


	private void defineCreateKitItem(Player player) {

		if (config.getCreateKitItem().isEnabled()) {
			setItem(config.getCreateKitItem().getSlot(), config.getCreateKitItem()
					.build(player), e -> {

				if (player.hasPermission("essentials.createkit")) {
					createNewKitName(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
					return;
				}

				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.NO_PERMISSION, null));
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			});
		}
	}


	private void defineSearchKitItem(Player player, Set<EXGKit> kits, String kitSearch) {

		if (kitSearch == null) {
			if (config.getSearchKitItem().isEnabled() && !kits.isEmpty()) {
				setItem(config.getSearchKitItem().getSlot(), config.getSearchKitItem()
						.build(player), e -> {

					searchKit(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}

		} else {
			if (config.getCancelSearchKitItem().isEnabled()) {
				setItem(config.getCancelSearchKitItem().getSlot(), config.getCancelSearchKitItem()
						.build(player), e -> {

					new KitsAdminViewInventory(player, null, null).open(player);
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


	private void createNewKitName(Player player) {

		if (Main.getInstance().getConfiguration().skipDataEntryProcess()) {
			String instantCreationDefaultKitName = Main.getInstance().getConfiguration().getInstantCreationDefaultKitName();
			int kitNumber = 1;

			if (instantCreationDefaultKitName.contains("%number%")) {

				List<String> kitsName = Main.getInstance().getServerManager().getEXGServer().getKits().stream()
						.map(EXGKit::getName)
						.toList();

				while (kitsName.contains(instantCreationDefaultKitName.replace("%number%", String.valueOf(kitNumber)))) {
					kitNumber++;
				}
			}

			String finalKitName = instantCreationDefaultKitName
					.replace("%number%", String.valueOf(kitNumber))
					.replace(" ", "_");

			long delay = Main.getInstance().getConfiguration().getInstantCreationDefaultKitDelay();

			Main.getInstance().getHookManager().getEssentialsHook().createKitWithPlayer(player, finalKitName, delay);
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.KIT_CREATED, Map.of("kitName", finalKitName, "kitDelay", String.valueOf(delay))));
			new KitsAdminViewInventory(player, null, null).open(player);
			SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
			return;
		}

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getFilesManager().getConfiguration().getEntryType("kits", "createNewKitNameEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ENTER_NEW_KIT_NAME));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.ENTER_NEW_KIT_NAME))
				.setMinLength(Main.getInstance().getConfiguration().getMinNameLength())
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					Set<EXGKit> kits = Main.getInstance().getEXGServer().getKits();

					if (kits.stream().anyMatch(kit -> kit.getName().equalsIgnoreCase(result.getLeft()))) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.KIT_NAME_ALREADY_EXISTS, null));
						new KitsAdminViewInventory(player, null, null).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					createNewKitDelay(player, result.getLeft());

				}, entry -> new KitsAdminViewInventory(player, null, null).open(player)
		);
	}


	private void createNewKitDelay(Player player, String kitName) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getFilesManager().getConfiguration().getEntryType("kits", "createNewKitDelayEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ENTER_NEW_KIT_DELAY_CHAT));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.ENTER_NEW_KIT_DELAY))
				.setMustBeNumber(true);

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					long delay = Long.parseLong(result.getLeft());

					Main.getInstance().getHookManager().getEssentialsHook().createKitWithPlayer(player, kitName.replace(" ", "_"), delay);
					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.KIT_CREATED, Map.of("kitName", kitName, "kitDelay", String.valueOf(delay))));
					new KitsAdminViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new KitsAdminViewInventory(player, null, null).open(player)
		);
	}


	private void searchKit(Player player) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getFilesManager().getConfiguration().getEntryType("kits", "searchKitEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.SEARCH_KIT_CHAT));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.SEARCH_KIT))
				.setMinLength(1)
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					Set<EXGKit> searchKits = Main.getInstance().getEXGServer().getKits()
							.stream()
							.filter(kit -> kit.getDisplayName().toLowerCase().contains(result.getLeft().toLowerCase()) ||
									kit.getName().toLowerCase().contains(result.getLeft().toLowerCase()))
							.sorted(Comparator.comparing(EXGKit::getName))
							.collect(Collectors.toCollection(LinkedHashSet::new));

					if (searchKits.isEmpty()) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.NO_KIT_FOUND, null));
						new KitsAdminViewInventory(player, result.getLeft(), searchKits).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					new KitsAdminViewInventory(player, result.getLeft(), searchKits).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new KitsAdminViewInventory(player, null, null).open(player)
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
