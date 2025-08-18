package fr.snipertvmc.essentialsxgui.inventories.kits;

import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.inventories.homes.HomeEditingInventory;
import fr.snipertvmc.essentialsxgui.inventories.homes.HomesInventory;
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
import org.bukkit.event.inventory.InventoryAction;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class KitsAdminViewInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGKitsAdminViewInventoryConfig config = Main.getInstance().getInventoriesManager().getKitsAdminInventoryConfig().copy();


	// -------------------------------------------------- //


	public KitsAdminViewInventory(Player player) {
		super(
				Main.getInstance().getInventoriesManager().getKitsAdminInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getKitsAdminInventoryConfig().getEXGTitle()
						.duplicate()
						.getTitle()
		);


		Main.getInstance().getServerDataManager().cleanServerData();


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


		Set<EXGKit> kits = Main.getInstance().getEXGServer().getKits()
				.stream()
				.sorted(Comparator.comparing(EXGKit::getName))
				.collect(Collectors.toCollection(LinkedHashSet::new));

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
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);

				} else if (kitItem.isCorrectClick(e.getClick(), "editKit")) {
					new KitEditingInventory(player, kit).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);

				} else if (kitItem.isCorrectClick(e.getClick(), "deleteKit")) {
					new KitEditingInventory(player, kit).deleteKit(player, kit);
				}

			});
		}

		if (kits.isEmpty()) {
			addContent(config.getNoKitsItem().build());
		}


		if (config.getCreateKitItem().isEnabled()) {
			setItem(config.getCreateKitItem().getSlot(), config.getCreateKitItem()
					.build(), e -> {

				if (player.hasPermission("essentials.createkit")) {
					createNewKitName(player);
					return;
				}

				player.sendMessage(MessagesUtils.get(EXGMessage.NO_PERMISSION, null));
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			});
		}


		if (config.getSwitchToPlayerModeItem().isEnabled()) {
			setItem(config.getSwitchToPlayerModeItem().getSlot(), config.getSwitchToPlayerModeItem().build(), e -> {

				new KitsPlayerViewInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}


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
					new KitsAdminViewInventory(player).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_CANCELED);
					return;
				}

				if (result.isEmpty() || result.length() > 32) {
					player.sendMessage(MessagesUtils.get(EXGMessage.LENGTH_LIMIT, Map.of("min", "1", "max", "32")));
					new KitsAdminViewInventory(player).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				Set<EXGKit> kits = Main.getInstance().getEXGServer().getKits();

				if (kits.stream().anyMatch(kit -> kit.getName().equalsIgnoreCase(result))) {
					player.sendMessage(MessagesUtils.get(EXGMessage.KIT_NAME_ALREADY_EXISTS, null));
					new KitsAdminViewInventory(player).open(player);
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
					new KitsAdminViewInventory(player).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_CANCELED);
					return;
				}

				if (!TypeUtils.isLong(result)) {
					player.sendMessage(MessagesUtils.get(EXGMessage.INVALID_NUMBER, null));
					new KitsAdminViewInventory(player).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				long delay = Long.parseLong(result);

				if (delay < 0 || delay > 999999999) {
					player.sendMessage(MessagesUtils.get(EXGMessage.LENGTH_LIMIT, Map.of("min", "0", "max", "999999999")));
					new KitsAdminViewInventory(player).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				Main.getInstance().getHookManager().getEssentialsHook().createKitWithPlayer(player, kitName, delay);
				player.sendMessage(MessagesUtils.get(EXGMessage.KIT_CREATED, Map.of("kitName", kitName, "kitDelay", String.valueOf(delay))));
				new KitsAdminViewInventory(player).open(player);
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
