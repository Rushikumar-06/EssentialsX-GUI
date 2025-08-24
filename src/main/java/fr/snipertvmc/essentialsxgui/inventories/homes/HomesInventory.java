package fr.snipertvmc.essentialsxgui.inventories.homes;

import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.EXGHomesInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class HomesInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGHomesInventoryConfig config = Main.getInstance().getInventoriesManager().getHomesInventoryConfig().copy();


	// -------------------------------------------------- //


	public HomesInventory(Player player, String homeSearch, Set<EXGHome> definedHomes) {
		super(
				Main.getInstance().getInventoriesManager().getHomesInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getHomesInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(
								Map.of("player", player.getName()))
						.getTitle()
		);


		Main.getInstance().getPlayerDataManager().cleanPlayerData(player.getUniqueId().toString());

		Set<EXGHome> homes = homeSearch != null ? definedHomes :

				Main.getInstance().getPlayerManager().getPlayer(player.getUniqueId().toString()).getHomes()
						.stream()
						.sorted(Comparator.comparing(EXGHome::getName))
						.collect(Collectors.toCollection(LinkedHashSet::new));


		initializeInventory(player);
		addHomesItems(player, homes, homeSearch);
		addBedHomeItem(player);
		addCreateHomeItem(player);
		addSearchHomeItem(player, homes, homeSearch);
	}


	// -------------------------------------------------- //


	private void addHomesItems(Player player, Set<EXGHome> homes, String homeSearch) {

		for (EXGHome home : homes) {

			EXGItemConfig homeItem = config.getHomeItem().duplicate();
			homeItem.setMaterial(home.getMaterial().name());
			homeItem.setData(home.getData());

			addContent(homeItem
					.updateVariables(
							Map.of("homeDisplayName", home.getDisplayName(),
									"homeName", home.getName()))
					.build(), e -> {

				if (homeItem.isCorrectClick(e.getClick(), "teleportToHome")) {
					player.performCommand("essentials:home " + home.getName());

				} else if (homeItem.isCorrectClick(e.getClick(), "editHome")) {
					new HomeEditingInventory(player, home).open(player);

				} else if (homeItem.isCorrectClick(e.getClick(), "deleteHome")) {
					new HomeEditingInventory(player, home).deleteHome(player, home);
				}

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}

		if (homes.isEmpty()) {

			if (homeSearch == null) {
				addContent(config.getNoHomesItem().build());

			} else {
				addContent(config.getNoSearchHomeResultsItem()
						.updateVariables(
								Map.of("homeSearch", homeSearch))
						.build());
			}
		}
	}


	private void addBedHomeItem(Player player) {

		String[] bedHomeMaterialParts = getBedHomeMaterialAndData(player).split(":");
		String bedHomeMaterialName = bedHomeMaterialParts[0];
		byte bedHomeData = Byte.parseByte(bedHomeMaterialParts[1]);
		if (player.hasPermission("essentials.home.bed") && config.getBedHomeItem().isEnabled()) {
			setItem(config.getBedHomeItem().getSlot(), config.getBedHomeItem()
					.updateVariables(Map.of("bedHomeWorldDisplayName", getBedHomeWorldDisplayName(player)))
					.setMaterial(bedHomeMaterialName)
					.setData(bedHomeData)
					.build(), e -> {

				if (player.hasPermission("essentials.home.bed")) {
					player.performCommand("essentials:home bed");
					return;
				}

				player.sendMessage(MessagesUtils.get(EXGMessage.NO_PERMISSION, null));
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			});
		}
	}


	private void addCreateHomeItem(Player player) {

		if (config.getCreateHomeItem().isEnabled()) {
			setItem(config.getCreateHomeItem().getSlot(), config.getCreateHomeItem()
					.build(), e -> {

				if (player.hasPermission("essentials.sethome")) {
					createNewHome(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
					return;
				}

				player.sendMessage(MessagesUtils.get(EXGMessage.NO_PERMISSION, null));
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			});
		}
	}


	private void addSearchHomeItem(Player player, Set<EXGHome> homes, String homeSearch) {

		if (homeSearch == null) {
			if (config.getSearchHomeItem().isEnabled() && !homes.isEmpty()) {
				setItem(config.getSearchHomeItem().getSlot(), config.getSearchHomeItem()
						.build(), e -> {

					searchHome(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}

		} else {
			if (config.getCancelSearchHomeItem().isEnabled()) {
				setItem(config.getCancelSearchHomeItem().getSlot(), config.getCancelSearchHomeItem()
						.build(), e -> {

					new HomesInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}
		}
	}


	private void initializeInventory(Player player) {

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


	private void createNewHome(Player player) {

		if (!Main.getInstance().getChatManager().canDoChat(player.getUniqueId())) {
			return;
		}

		player.closeInventory();
		player.sendMessage(MessagesUtils.get(EXGMessage.ENTER_NEW_HOME_NAME, null));
		Main.getInstance().getChatManager().addChat(player.getUniqueId(), result -> {

			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {

				if (result.equalsIgnoreCase("cancel")) {
					player.sendMessage(MessagesUtils.get(EXGMessage.ACTION_CANCELED, null));
					new HomesInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_CANCELED);
					return;
				}

				if (result.isEmpty() || result.length() > 32) {
					player.sendMessage(MessagesUtils.get(EXGMessage.LENGTH_LIMIT, Map.of("min", "1", "max", "32")));
					new HomesInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				Set<EXGHome> homes = Main.getInstance().getPlayerManager().getPlayer(player.getUniqueId().toString()).getHomes();

				if (homes.stream().anyMatch(home -> home.getName().equalsIgnoreCase(result))) {
					player.sendMessage(MessagesUtils.get(EXGMessage.HOME_NAME_ALREADY_EXISTS, null));
					new HomesInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				if (!Main.getInstance().getHookManager().getEssentialsHook().canCreateHome(player)) {
					player.sendMessage(MessagesUtils.get(EXGMessage.HOME_LIMIT_REACHED, null));
					new HomesInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				Main.getInstance().getEssentials().getUser(player).setHome(result, player.getLocation());
				player.sendMessage(MessagesUtils.get(EXGMessage.HOME_CREATED, Map.of("homeName", result)));
				new HomesInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
			});

		}, 10);
	}


	private void searchHome(Player player) {

		if (!Main.getInstance().getChatManager().canDoChat(player.getUniqueId())) {
			return;
		}

		player.closeInventory();
		player.sendMessage(MessagesUtils.get(EXGMessage.SEARCH_HOME, null));
		Main.getInstance().getChatManager().addChat(player.getUniqueId(), result -> {

			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {

				if (result.equalsIgnoreCase("cancel")) {
					player.sendMessage(MessagesUtils.get(EXGMessage.ACTION_CANCELED, null));
					new HomesInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_CANCELED);
					return;
				}

				Set<EXGHome> searchHomes = Main.getInstance().getPlayerManager().getPlayer(player.getUniqueId().toString()).getHomes()
						.stream()
						.filter(home -> MessagesUtils.removeColorCodes(home.getDisplayName()).toLowerCase().startsWith(result.toLowerCase()))
						.collect(Collectors.toCollection(LinkedHashSet::new));

				if (searchHomes.isEmpty()) {
					player.sendMessage(MessagesUtils.get(EXGMessage.NO_HOME_FOUND, null));
					new HomesInventory(player, result, searchHomes).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				new HomesInventory(player, result, searchHomes).open(player);
				SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
			});

		}, 10);
	}


	private String getBedHomeMaterialAndData(Player player) {
		if (player.getBedSpawnLocation() == null) {
			return config.getBedHomeItemNotSetMaterial();

		} else if (player.getBedSpawnLocation().getWorld().getName().endsWith("_nether")) {
			return config.getBedHomeItemNetherMaterial();
		}

		return config.getBedHomeItemOverworldMaterial();
	}


	private String getBedHomeWorldDisplayName(Player player) {
		if (player.getBedSpawnLocation() == null) {
			return config.getBedHomeItemNotSetDisplayName();

		} else if (player.getBedSpawnLocation().getWorld().getName().endsWith("_nether")) {
			return config.getBedHomeItemNetherDisplayName();
		}

		return config.getBedHomeItemOverworldDisplayName();
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
