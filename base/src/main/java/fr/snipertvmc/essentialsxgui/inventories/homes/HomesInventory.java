package fr.snipertvmc.essentialsxgui.inventories.homes;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.EXGHomesInventoryConfig;
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
import java.util.regex.Pattern;
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
						.getTitle(player)
		);


		InventoriesUtils.initializeInventoryWithClose(player, config, this, config.getCloseItem());
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		EXGPlayer exgPlayer = Main.getInstance().getPlayerManager().getPlayer(player);
		Main.getInstance().getPlayerDataManager().updatePlayerHomes(exgPlayer);

		Set<EXGHome> homes = homeSearch != null ? definedHomes :

				exgPlayer.getHomes()
						.stream()
						.sorted(Comparator.comparing(EXGHome::getName))
						.collect(Collectors.toCollection(LinkedHashSet::new));


		addHomesItems(player, homes, homeSearch);
		addBedHomeItem(player);
		addCreateHomeItem(player);
		addSearchHomeItem(player, homes, homeSearch);
	}


	// -------------------------------------------------- //


	private void addHomesItems(Player player, Set<EXGHome> homes, String homeSearch) {

		for (EXGHome home : homes) {

			EXGItemConfig homeItem = config.getHomeItem().duplicate();
			ItemStack homeItemStack = InventoriesUtils.getHomeItemStack(homeItem, home, player);

			addContent(homeItemStack, e -> {

				if (homeItem.isCorrectClick(e.getClick(), "teleportToHome")) {
					e.getWhoClicked().closeInventory();
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
				addContent(config.getNoHomesItem().build(player));

			} else {
				addContent(config.getNoSearchHomeResultsItem()
						.updateVariables(
								Map.of("homeSearch", homeSearch))
						.build(player));
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
					.build(player), e -> {

				if (player.hasPermission("essentials.home.bed")) {
					e.getWhoClicked().closeInventory();
					player.performCommand("essentials:home bed");
					return;
				}

				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.NO_PERMISSION, null));
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			});
		}
	}


	private void addCreateHomeItem(Player player) {

		if (config.getCreateHomeItem().isEnabled()) {
			setItem(config.getCreateHomeItem().getSlot(), config.getCreateHomeItem()
					.build(player), e -> {

				if (player.hasPermission("essentials.sethome")) {
					createNewHome(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
					return;
				}

				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.NO_PERMISSION, null));
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			});
		}
	}


	private void addSearchHomeItem(Player player, Set<EXGHome> homes, String homeSearch) {

		if (homeSearch == null) {
			if (config.getSearchHomeItem().isEnabled() && !homes.isEmpty()) {
				setItem(config.getSearchHomeItem().getSlot(), config.getSearchHomeItem()
						.build(player), e -> {

					searchHome(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}

		} else {
			if (config.getCancelSearchHomeItem().isEnabled()) {
				setItem(config.getCancelSearchHomeItem().getSlot(), config.getCancelSearchHomeItem()
						.build(player), e -> {

					new HomesInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}
		}
	}


	// -------------------------------------------------- //


	private void createNewHome(Player player) {

		if (Main.getInstance().getConfiguration().skipDataEntryProcess()) {
			String instantCreationDefaultHomeName = Main.getInstance().getConfiguration().getInstantCreationDefaultHomeName();
			int homeNumber = 1;

			if (instantCreationDefaultHomeName.contains("%number%")) {

				List<String> homesName = Main.getInstance().getPlayerManager().getPlayer(player).getHomes().stream()
						.map(EXGHome::getName)
						.toList();

				while (homesName.contains(instantCreationDefaultHomeName.replace("%number%", String.valueOf(homeNumber)))) {
					homeNumber++;
				}
			}

			String finalHomeName = instantCreationDefaultHomeName
					.replace("%number%", String.valueOf(homeNumber))
					.replace(" ", "_");

			Main.getInstance().getEssentials().getUser(player).setHome(finalHomeName, player.getLocation());
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.HOME_CREATED, Map.of("homeName", finalHomeName)));
			new HomesInventory(player, null, null).open(player);
			SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
			return;
		}

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getFilesManager().getConfiguration().getEntryType("homes", "createNewHomeEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ENTER_NEW_HOME_NAME_CHAT));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.ENTER_NEW_HOME_NAME))
				.setMinLength(Main.getInstance().getConfiguration().getMinNameLength())
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					Pattern pattern = Pattern.compile("^[a-zA-Z0-9 _-]+$");
					if (!pattern.matcher(result.getLeft()).matches()) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.INVALID_NAME));
						new HomesInventory(player, null, null).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					Set<EXGHome> homes = Main.getInstance().getPlayerManager().getPlayer(player).getHomes();
					String homeName = result.getLeft().toLowerCase().replace(" ", "_").replace("-", "_");

					if (homes.stream().anyMatch(home -> home.getName().equalsIgnoreCase(homeName))) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.HOME_NAME_ALREADY_EXISTS));
						new HomesInventory(player, null, null).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					if (!Main.getInstance().getEssentialsManager().canCreateHome(player)) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.HOME_LIMIT_REACHED));
						new HomesInventory(player, null, null).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					Main.getInstance().getEssentials().getUser(player).setHome(homeName, player.getLocation());
					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.HOME_CREATED, Map.of("homeName", result.getLeft())));
					new HomesInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new HomesInventory(player, null, null).open(player)
		);
	}


	private void searchHome(Player player) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getFilesManager().getConfiguration().getEntryType("homes", "searchHomeEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.SEARCH_HOME_CHAT));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.SEARCH_HOME))
				.setMinLength(1)
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					Set<EXGHome> searchHomes = Main.getInstance().getPlayerManager().getPlayer(player).getHomes()
							.stream()
							.filter(home -> home.getDisplayName().toLowerCase().contains(result.getLeft().toLowerCase()) ||
									home.getName().toLowerCase().contains(result.getLeft().toLowerCase()))
							.collect(Collectors.toCollection(LinkedHashSet::new));

					if (searchHomes.isEmpty()) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.NO_HOME_FOUND));
						new HomesInventory(player, result.getLeft(), searchHomes).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					new HomesInventory(player, result.getLeft(), searchHomes).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new HomesInventory(player, null, null).open(player)
		);
	}


	// -------------------------------------------------- //


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
		Player player = this.getInventory().getViewers().isEmpty() ? null : (Player) this.getInventory().getViewers().get(0);
		InventoriesUtils.updateCurrentPageItem(player, config, this);
		SoundsUtils.playSound(player, EXGSound.GUI_PAGE_CHANGE);
	}


	// -------------------------------------------------- //
}
