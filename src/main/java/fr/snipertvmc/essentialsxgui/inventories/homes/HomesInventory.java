package fr.snipertvmc.essentialsxgui.inventories.homes;

import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.EXGHomesInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;

import java.util.*;
import java.util.stream.Collectors;

public class HomesInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGHomesInventoryConfig config = Main.getInstance().getInventoriesManager().getHomesInventoryConfig().copy();


	// -------------------------------------------------- //


	public HomesInventory(Player player) {
		super(
				Main.getInstance().getInventoriesManager().getHomesInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getHomesInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(Map.of(
						"player", player.getName()))
						.getTitle()
		);


		Main.getInstance().getPlayerDataManager().cleanPlayerData(player.getUniqueId().toString());


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


		Set<EXGHome> homes = Main.getInstance().getPlayerManager().getPlayer(player.getUniqueId().toString()).getHomes()
				.stream()
				.sorted(Comparator.comparing(EXGHome::getName))
				.collect(Collectors.toCollection(LinkedHashSet::new));

		for (EXGHome home : homes) {

			EXGItemConfig homeItem = config.getHomeItem().duplicate();
			homeItem.setMaterial(home.getMaterial().name());

			addContent(homeItem
					.updateVariables(
							Map.of("homeDisplayName", home.getDisplayName(),
									"homeName", home.getName()))
					.build(), e -> {

				if (e.getClick().isLeftClick()) {
					player.performCommand("essentials:home " + home.getName());

				} else if (e.getClick().isRightClick()) {
					new HomeEditingInventory(player, home).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);

				} else if (e.getAction().equals(InventoryAction.DROP_ONE_SLOT)) {
					new HomeEditingInventory(player, home).deleteHome(player, home);
				}
			});
		}


		if (homes.isEmpty()) {
			addContent(config.getNoHomesItem().build());
		}


		if (config.getCreateHomeItem().isEnabled()) {
			setItem(config.getCreateHomeItem().getSlot(), config.getCreateHomeItem()
					.build(), e -> {

				if (player.hasPermission("essentials.sethome")) {
					createNewHome(player);
					return;
				}

				player.sendMessage(MessagesUtils.get(EXGMessage.NO_PERMISSION, null));
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
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


	private void createNewHome(Player player) {

		player.closeInventory();
		player.sendMessage(MessagesUtils.get(EXGMessage.ENTER_NEW_HOME_NAME, null));
		Main.getInstance().getChatManager().addChat(player.getUniqueId(), result -> {

			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {

				if (result.equalsIgnoreCase("cancel")) {
					player.sendMessage(MessagesUtils.get(EXGMessage.ACTION_CANCELED, null));
					new HomesInventory(player).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				if (result.isEmpty() || result.length() > 32) {
					player.sendMessage(MessagesUtils.get(EXGMessage.LENGTH_LIMIT, Map.of("min", "1", "max", "32")));
					new HomesInventory(player).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				Set<EXGHome> homes = Main.getInstance().getPlayerManager().getPlayer(player.getUniqueId().toString()).getHomes();

				if (homes.stream().anyMatch(home -> home.getName().equalsIgnoreCase(result))) {
					player.sendMessage(MessagesUtils.get(EXGMessage.HOME_NAME_ALREADY_EXISTS, null));
					new HomesInventory(player).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				Main.getInstance().getEssentials().getUser(player).setHome(result, player.getLocation());
				player.sendMessage(MessagesUtils.get(EXGMessage.HOME_CREATED, Map.of("homeName", result)));
				new HomesInventory(player).open(player);
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
