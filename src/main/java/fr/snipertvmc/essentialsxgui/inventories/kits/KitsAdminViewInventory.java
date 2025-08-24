package fr.snipertvmc.essentialsxgui.inventories.kits;

import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitsAdminViewInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.TypeUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class KitsAdminViewInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGKitsAdminViewInventoryConfig config = Main.getInstance().getInventoriesManager().getKitsAdminInventoryConfig().copy();


	// -------------------------------------------------- //


	public KitsAdminViewInventory(Player player, String kitSearch, Set<EXGKit> definedKits) {
		super(
				Main.getInstance().getInventoriesManager().getKitsAdminInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getKitsAdminInventoryConfig().getEXGTitle()
						.duplicate()
						.getTitle()
		);


		Main.getInstance().getServerDataManager().cleanServerData();

		Set<EXGKit> kits = kitSearch != null ? definedKits :

				Main.getInstance().getEXGServer().getKits()
						.stream()
						.sorted(Comparator.comparing(EXGKit::getName))
						.collect(Collectors.toCollection(LinkedHashSet::new));


		initializeGeneralInventory(player);
		defineKitsItems(player, kits, kitSearch);
		defineSwitchToPlayerModeItem(player);
		defineCreateKitItem(player);
		defineSearchKitItem(player, kits, kitSearch);
	}


	// -------------------------------------------------- //


	private void defineKitsItems(Player player, Set<EXGKit> kits, String kitSearch) {

		for (EXGKit kit : kits) {

			EXGItemConfig kitItem = config.getKitItem().duplicate();
			kitItem.setMaterial(kit.getMaterial().name());
			kitItem.setData(kit.getData());

			addContent(kitItem
					.updateVariables(
							Map.of("kitDisplayName", kit.getDisplayName(),
									"kitName", kit.getName()))
					.build(), e -> {

				if (kitItem.isCorrectClick(e.getClick(), "giveKit")) {
					new KitsPlayerGiveInventory(player, kit).open(player);

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
				addContent(config.getNoKitsItem().build());

			} else {
				addContent(config.getNoSearchKitResultsItem()
						.updateVariables(
								Map.of("kitSearch", kitSearch))
						.build());
			}
		}
	}


	private void defineSwitchToPlayerModeItem(Player player) {

		if (config.getSwitchToPlayerModeItem().isEnabled()) {
			setItem(config.getSwitchToPlayerModeItem().getSlot(), config.getSwitchToPlayerModeItem().build(), e -> {

				new KitsPlayerViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}
	}


	private void defineCreateKitItem(Player player) {

		if (config.getCreateKitItem().isEnabled()) {
			setItem(config.getCreateKitItem().getSlot(), config.getCreateKitItem()
					.build(), e -> {

				if (player.hasPermission("essentials.createkit")) {
					createNewKitName(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
					return;
				}

				player.sendMessage(MessagesUtils.get(EXGMessage.NO_PERMISSION, null));
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			});
		}
	}


	private void defineSearchKitItem(Player player, Set<EXGKit> kits, String kitSearch) {

		if (kitSearch == null) {
			if (config.getSearchKitItem().isEnabled() && !kits.isEmpty()) {
				setItem(config.getSearchKitItem().getSlot(), config.getSearchKitItem()
						.build(), e -> {

					searchKit(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}

		} else {
			if (config.getCancelSearchKitItem().isEnabled()) {
				setItem(config.getCancelSearchKitItem().getSlot(), config.getCancelSearchKitItem()
						.build(), e -> {

					new KitsAdminViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}
		}
	}


	private void initializeGeneralInventory(Player player) {

		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem().build());
		}


		previousPageItem(config.getPreviousPageItem().getSlot(), config.getPreviousPageItem()
				.updateVariables(
						Map.of("currentPage", String.valueOf(this.currentPage()),
								"previousPage", String.valueOf(this.currentPage() - 1)))
				.build());


		nextPageItem(config.getNextPageItem().getSlot(), config.getNextPageItem()
				.updateVariables(
						Map.of("currentPage", String.valueOf(this.currentPage()),
								"nextPage", String.valueOf(this.currentPage() + 1)))
				.build());


		if (config.getCloseItem().isEnabled()) {
			setItem(config.getCloseItem().getSlot(), config.getCloseItem().build(), e -> {

				e.getWhoClicked().closeInventory();
				SoundsUtils.playSound(player, EXGSound.GUI_CLOSE);
			});
		}


		config.getInventoryScheme().apply(this);
	}


	// -------------------------------------------------- //


	private void createNewKitName(Player player) {

		if (!Main.getInstance().getChatManager().canDoChat(player.getUniqueId())) {
			return;
		}

		player.closeInventory();
		player.sendMessage(MessagesUtils.get(EXGMessage.ENTER_NEW_KIT_NAME, null));
		Main.getInstance().getChatManager().addChat(player.getUniqueId(), result -> {

			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {

				if (result.equalsIgnoreCase("cancel")) {
					player.sendMessage(MessagesUtils.get(EXGMessage.ACTION_CANCELED, null));
					new KitsAdminViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_CANCELED);
					return;
				}

				if (result.isEmpty() || result.length() > 32) {
					player.sendMessage(MessagesUtils.get(EXGMessage.LENGTH_LIMIT, Map.of("min", "1", "max", "32")));
					new KitsAdminViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				Set<EXGKit> kits = Main.getInstance().getEXGServer().getKits();

				if (kits.stream().anyMatch(kit -> kit.getName().equalsIgnoreCase(result))) {
					player.sendMessage(MessagesUtils.get(EXGMessage.KIT_NAME_ALREADY_EXISTS, null));
					new KitsAdminViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				createNewKitDelay(player, result);
			});

		}, 10);
	}


	private void createNewKitDelay(Player player, String kitName) {

		player.closeInventory();
		player.sendMessage(MessagesUtils.get(EXGMessage.ENTER_NEW_KIT_DELAY, null));
		Main.getInstance().getChatManager().addChat(player.getUniqueId(), result -> {

			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {

				if (result.equalsIgnoreCase("cancel")) {
					player.sendMessage(MessagesUtils.get(EXGMessage.ACTION_CANCELED, null));
					new KitsAdminViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_CANCELED);
					return;
				}

				if (!TypeUtils.isLong(result)) {
					player.sendMessage(MessagesUtils.get(EXGMessage.INVALID_NUMBER, null));
					new KitsAdminViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				long delay = Long.parseLong(result);

				if (delay < 0 || delay > 999999999) {
					player.sendMessage(MessagesUtils.get(EXGMessage.LENGTH_LIMIT, Map.of("min", "0", "max", "999999999")));
					new KitsAdminViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				Main.getInstance().getHookManager().getEssentialsHook().createKitWithPlayer(player, kitName, delay);
				player.sendMessage(MessagesUtils.get(EXGMessage.KIT_CREATED, Map.of("kitName", kitName, "kitDelay", String.valueOf(delay))));
				new KitsAdminViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
			});

		}, 10);
	}


	private void searchKit(Player player) {

		if (!Main.getInstance().getChatManager().canDoChat(player.getUniqueId())) {
			return;
		}

		player.closeInventory();
		player.sendMessage(MessagesUtils.get(EXGMessage.SEARCH_KIT, null));
		Main.getInstance().getChatManager().addChat(player.getUniqueId(), result -> {

			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {

				if (result.equalsIgnoreCase("cancel")) {
					player.sendMessage(MessagesUtils.get(EXGMessage.ACTION_CANCELED, null));
					new KitsAdminViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_CANCELED);
					return;
				}

				Set<EXGKit> searchKits = Main.getInstance().getEXGServer().getKits()
						.stream()
						.filter(kit -> MessagesUtils.removeColorCodes(kit.getDisplayName()).toLowerCase().startsWith(result.toLowerCase()))
						.sorted(Comparator.comparing(EXGKit::getName))
						.collect(Collectors.toCollection(LinkedHashSet::new));

				if (searchKits.isEmpty()) {
					player.sendMessage(MessagesUtils.get(EXGMessage.NO_KIT_FOUND, null));
					new KitsAdminViewInventory(player, result, searchKits).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				new KitsAdminViewInventory(player, result, searchKits).open(player);
				SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
			});

		}, 10);
	}


	// -------------------------------------------------- //


	@Override
	protected void onPageChange(int page) {

		setItem(config.getCurrentPageItem().getSlot(), config.getCurrentPageItem()
				.updateVariables(
						Map.of("currentPage", String.valueOf(this.currentPage()),
								"totalPages", String.valueOf(this.lastPage()),
								"previousPage", String.valueOf(this.currentPage() - 1),
								"nextPage", String.valueOf(this.currentPage() + 1)))
				.build());
	}


	// -------------------------------------------------- //
}
