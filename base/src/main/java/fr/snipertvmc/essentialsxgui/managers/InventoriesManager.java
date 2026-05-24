package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.*;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.EXGHomeEditingInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.EXGHomesInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.*;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.others.EXGDataEntryGUInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps.EXGWarpEditingInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps.EXGWarpPlayerTeleportInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps.EXGWarpsAdminViewInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps.EXGWarpsPlayerViewInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.whois.EXGWhoisPlayersInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.whois.EXGWhoisViewInventoryConfig;

import java.util.List;

public class InventoriesManager {


	// -------------------------------------------------- //


	private EXGHomesInventoryConfig homesInventoryConfig;
	private EXGHomeEditingInventoryConfig homeEditingInventoryConfig;

	private EXGKitsAdminViewInventoryConfig kitsAdminViewInventoryConfig;
	private EXGKitsPlayerViewInventoryConfig kitsPlayerViewInventoryConfig;
	private EXGKitPreviewInventoryConfig kitPreviewInventoryConfig;
	private EXGKitPlayerGiveInventoryConfig kitPlayerGiveInventoryConfig;
	private EXGKitEditingInventoryConfig kitEditingInventoryConfig;
	private EXGKitEditorInventoryConfig kitEditorInventoryConfig;

	private EXGWarpEditingInventoryConfig warpEditingInventoryConfig;
	private EXGWarpPlayerTeleportInventoryConfig warpPlayerTeleportInventoryConfig;
	private EXGWarpsAdminViewInventoryConfig warpsAdminViewInventoryConfig;
	private EXGWarpsPlayerViewInventoryConfig warpsPlayerViewInventoryConfig;

	private EXGWhoisPlayersInventoryConfig whoisPlayersInventoryConfig;
	private EXGWhoisViewInventoryConfig whoisViewInventoryConfig;

	private EXGBalanceTopInventoryConfig balanceTopInventoryConfig;
	private EXGEcoActionInventoryConfig ecoActionInventoryConfig;
	private EXGEcoAmountInventoryConfig ecoAmountInventoryConfig;
	private EXGEcoPlayersInventoryConfig  ecoPlayersInventoryConfig;
	private EXGSellInventoryConfig sellInventoryConfig;
	private EXGWorthInventoryConfig worthInventoryConfig;
	private EXGWorthAllInventoryConfig worthAllInventoryConfig;
	private EXGWorthInventoryInventoryConfig worthInventoryInventoryConfig;

	private EXGDataEntryGUInventoryConfig dataEntryGUIInventoryConfig;


	// -------------------------------------------------- //


	public int loadInventory(String inventoryName) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		String title = inventoryFile.getTitle();
		int rows = inventoryFile.getRows();
		EXGItemConfig borderItem = inventoryFile.getBorderItem();
		int[] borderSlots = inventoryFile.getBorderSlots();

		return switch (inventoryName) {
			case "homes" -> loadHomesInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "homeEditing" -> loadHomeEditingInventory(title, rows, inventoryName, borderItem, borderSlots);

			case "kitsAdminView" -> loadKitsAdminViewInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "kitsPlayerView" -> loadKitsPlayerViewInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "kitPreview" -> loadKitPreviewInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "kitPlayerGive" -> loadKitPlayerGiveInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "kitEditing" -> loadKitEditingInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "kitEditor" -> loadKitEditorInventory(title, rows, inventoryName, borderItem, borderSlots);

			case "warpsAdminView" -> loadWarpsAdminViewInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "warpsPlayerView" -> loadWarpsPlayerViewInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "warpPlayerTeleport" -> loadWarpPlayerTeleportInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "warpEditing" -> loadWarpEditingInventory(title, rows, inventoryName, borderItem, borderSlots);

			case "whoisPlayers" -> loadWhoisPlayersInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "whoisView" -> loadWhoisViewInventory(title, rows, inventoryName, borderItem, borderSlots);

			case "balanceTop" -> loadBalanceTopInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "ecoAction" -> loadEcoActionInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "ecoAmount" -> loadEcoAmountInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "ecoPlayers" -> loadEcoPlayersInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "sell" -> loadSellInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "worth" -> loadWorthInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "worthAll" -> loadWorthAllInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "worthInventory" -> loadWorthInventoryInventory(title, rows, inventoryName, borderItem, borderSlots);

			case "dataEntryGUI" -> loadDataEntryGUIInventory(title, rows, inventoryName, borderItem, borderSlots);
			default -> 0;
		};
	}


	// -------------------------------------------------- //


	//
 	// HOMES INVENTORIES
	//


	private int loadHomesInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		homesInventoryConfig = new EXGHomesInventoryConfig(title, rows, borderItem, borderSlots);

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		homesInventoryConfig.setHomeItem(inventoryFile.getItem(
				"homeItem", homesInventoryConfig));
		homesInventoryConfig.setBedHomeItem(inventoryFile.getItem(
				"bedHomeItem", homesInventoryConfig));
		homesInventoryConfig.setNoHomesItem(inventoryFile.getItem(
				"noHomesItem", homesInventoryConfig));

		homesInventoryConfig.setCreateHomeItem(inventoryFile.getItem(
				"createHomeItem", homesInventoryConfig));
		homesInventoryConfig.setSearchHomeItem(inventoryFile.getItem(
				"searchHomeItem", homesInventoryConfig));
		homesInventoryConfig.setCancelSearchHomeItem(inventoryFile.getItem(
				"cancelSearchHomeItem", homesInventoryConfig));
		homesInventoryConfig.setNoSearchHomeResultsItem(inventoryFile.getItem(
				"noSearchHomeResultsItem", homesInventoryConfig));

		homesInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem", homesInventoryConfig));
		homesInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem", homesInventoryConfig));
		homesInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem", homesInventoryConfig));

		homesInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem", homesInventoryConfig));

		homesInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());

		homesInventoryConfig.setBedHomeItemOverworldMaterial(inventoryFile.getFullBedHomeItemMaterial("overworld"),
				inventoryFile.getBedHomeItemWorldDisplayName("overworld"));
		homesInventoryConfig.setBedHomeItemNetherMaterial(inventoryFile.getFullBedHomeItemMaterial("nether"),
				inventoryFile.getBedHomeItemWorldDisplayName("nether"));
		homesInventoryConfig.setBedHomeItemNotSetMaterial(inventoryFile.getFullBedHomeItemMaterial("notSet"),
				inventoryFile.getBedHomeItemWorldDisplayName("notSet"));

		return homesInventoryConfig.getConfigurationErrors();
	}

	private int loadHomeEditingInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		homeEditingInventoryConfig = new EXGHomeEditingInventoryConfig(title, rows, borderItem, borderSlots);

		homeEditingInventoryConfig.setPreviewHomeItem(inventoryFile.getItem(
				"previewHomeItem", homeEditingInventoryConfig));

		homeEditingInventoryConfig.setChangeDisplayNameItem(inventoryFile.getItem(
				"changeDisplayNameItem", homeEditingInventoryConfig));
		homeEditingInventoryConfig.setChangeIconItem(inventoryFile.getItem(
				"changeIconItem", homeEditingInventoryConfig));
		homeEditingInventoryConfig.setDeleteHomeItem(inventoryFile.getItem(
				"deleteHomeItem", homeEditingInventoryConfig));

		homeEditingInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem", homeEditingInventoryConfig));

		return homeEditingInventoryConfig.getConfigurationErrors();
	}


	//
	// KITS INVENTORIES
	//


	private int loadKitsAdminViewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		kitsAdminViewInventoryConfig = new EXGKitsAdminViewInventoryConfig(title, rows, borderItem, borderSlots);

		kitsAdminViewInventoryConfig.setKitItem(inventoryFile.getItem(
				"kitItem", kitsAdminViewInventoryConfig));
		kitsAdminViewInventoryConfig.setNoKitsItem(inventoryFile.getItem(
				"noKitsItem", kitsAdminViewInventoryConfig));

		kitsAdminViewInventoryConfig.setCreateKitItem(inventoryFile.getItem(
				"createKitItem", kitsAdminViewInventoryConfig));
		kitsAdminViewInventoryConfig.setSwitchToPlayerModeItem(inventoryFile.getItem(
				"switchToPlayerModeItem", kitsAdminViewInventoryConfig));
		kitsAdminViewInventoryConfig.setSearchKitItem(inventoryFile.getItem(
				"searchKitItem", kitsAdminViewInventoryConfig));
		kitsAdminViewInventoryConfig.setCancelSearchKitItem(inventoryFile.getItem(
				"cancelSearchKitItem", kitsAdminViewInventoryConfig));
		kitsAdminViewInventoryConfig.setNoSearchKitResultsItem(inventoryFile.getItem(
				"noSearchKitResultsItem", kitsAdminViewInventoryConfig));

		kitsAdminViewInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem", kitsAdminViewInventoryConfig));
		kitsAdminViewInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem", kitsAdminViewInventoryConfig));
		kitsAdminViewInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem", kitsAdminViewInventoryConfig));

		kitsAdminViewInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem", kitsAdminViewInventoryConfig));

		kitsAdminViewInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());

		return kitsAdminViewInventoryConfig.getConfigurationErrors();
	}

	private int loadKitsPlayerViewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		kitsPlayerViewInventoryConfig = new EXGKitsPlayerViewInventoryConfig(title, rows, borderItem, borderSlots);

		kitsPlayerViewInventoryConfig.setKitItem(inventoryFile.getItem(
				"kitItem", kitsPlayerViewInventoryConfig));
		kitsPlayerViewInventoryConfig.setNoKitsItem(inventoryFile.getItem(
				"noKitsItem", kitsPlayerViewInventoryConfig));

		kitsPlayerViewInventoryConfig.setSwitchToAdminModeItem(inventoryFile.getItem(
				"switchToAdminModeItem", kitsPlayerViewInventoryConfig));
		kitsPlayerViewInventoryConfig.setSearchKitItem(inventoryFile.getItem(
				"searchKitItem", kitsPlayerViewInventoryConfig));
		kitsPlayerViewInventoryConfig.setCancelSearchKitItem(inventoryFile.getItem(
				"cancelSearchKitItem", kitsPlayerViewInventoryConfig));
		kitsPlayerViewInventoryConfig.setNoSearchKitResultsItem(inventoryFile.getItem(
				"noSearchKitResultsItem", kitsPlayerViewInventoryConfig));

		kitsPlayerViewInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem", kitsPlayerViewInventoryConfig));
		kitsPlayerViewInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem", kitsPlayerViewInventoryConfig));
		kitsPlayerViewInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem", kitsPlayerViewInventoryConfig));

		kitsPlayerViewInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem", kitsPlayerViewInventoryConfig));

		kitsPlayerViewInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());

		return kitsPlayerViewInventoryConfig.getConfigurationErrors();
	}

	private int loadKitPreviewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		kitPreviewInventoryConfig = new EXGKitPreviewInventoryConfig(title, rows, borderItem, borderSlots);

		kitPreviewInventoryConfig.setTitle(title);
		kitPreviewInventoryConfig.setRows(rows);
		kitPreviewInventoryConfig.setBorderItem(borderItem);
		kitPreviewInventoryConfig.setBorderSlots(borderSlots);

		kitPreviewInventoryConfig.setKitItem(inventoryFile.getItem(
				"kitItem", kitPreviewInventoryConfig));
		kitPreviewInventoryConfig.setEmptyKitItem(inventoryFile.getItem(
				"emptyKitItem", kitPreviewInventoryConfig));

		kitPreviewInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem", kitPreviewInventoryConfig));
		kitPreviewInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem", kitPreviewInventoryConfig));
		kitPreviewInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem", kitPreviewInventoryConfig));

		kitPreviewInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem", kitPreviewInventoryConfig));

		kitPreviewInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());

		return kitPreviewInventoryConfig.getConfigurationErrors();
	}

	private int loadKitPlayerGiveInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		kitPlayerGiveInventoryConfig = new EXGKitPlayerGiveInventoryConfig(title, rows, borderItem, borderSlots);

		kitPlayerGiveInventoryConfig.setPlayerItem(inventoryFile.getItem(
				"playerItem", kitPlayerGiveInventoryConfig));

		kitPlayerGiveInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem", kitPlayerGiveInventoryConfig));
		kitPlayerGiveInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem", kitPlayerGiveInventoryConfig));
		kitPlayerGiveInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem", kitPlayerGiveInventoryConfig));

		kitPlayerGiveInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem", kitPlayerGiveInventoryConfig));

		kitPlayerGiveInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());

		return kitPlayerGiveInventoryConfig.getConfigurationErrors();
	}

	private int loadKitEditingInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		kitEditingInventoryConfig = new EXGKitEditingInventoryConfig(title, rows, borderItem, borderSlots);

		kitEditingInventoryConfig.setPreviewKitItem(inventoryFile.getItem(
				"previewKitItem", kitEditingInventoryConfig));

		kitEditingInventoryConfig.setChangeDisplayNameItem(inventoryFile.getItem(
				"changeDisplayNameItem", kitEditingInventoryConfig));
		kitEditingInventoryConfig.setChangeIconItem(inventoryFile.getItem(
				"changeIconItem", kitEditingInventoryConfig));
		kitEditingInventoryConfig.setDeleteKitItem(inventoryFile.getItem(
				"deleteKitItem", kitEditingInventoryConfig));
		kitEditingInventoryConfig.setEditKitContentsItem(inventoryFile.getItem(
				"editKitContentsItem", kitEditingInventoryConfig));

		kitEditingInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem", kitEditingInventoryConfig));

		return kitEditingInventoryConfig.getConfigurationErrors();
	}


	private int loadKitEditorInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		kitEditorInventoryConfig = new EXGKitEditorInventoryConfig(title, rows, borderItem, borderSlots);

		kitEditorInventoryConfig.setSaveKitItem(inventoryFile.getItem(
				"saveKitItem", kitEditorInventoryConfig));
		kitEditorInventoryConfig.setCancelChangesItem(inventoryFile.getItem(
				"cancelChangesItem", kitEditorInventoryConfig));

		return kitEditorInventoryConfig.getConfigurationErrors();
	}


	//
	// WARPS INVENTORIES
	//


	private int loadWarpEditingInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		warpEditingInventoryConfig = new EXGWarpEditingInventoryConfig(title, rows, borderItem, borderSlots);

		warpEditingInventoryConfig.setPreviewWarpItem(inventoryFile.getItem(
				"previewWarpItem", warpEditingInventoryConfig));

		warpEditingInventoryConfig.setChangeDisplayNameItem(inventoryFile.getItem(
				"changeDisplayNameItem", warpEditingInventoryConfig));
		warpEditingInventoryConfig.setChangeIconItem(inventoryFile.getItem(
				"changeIconItem", warpEditingInventoryConfig));
		warpEditingInventoryConfig.setDeleteWarpItem(inventoryFile.getItem(
				"deleteWarpItem", warpEditingInventoryConfig));

		warpEditingInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem", warpEditingInventoryConfig));

		return warpEditingInventoryConfig.getConfigurationErrors();
	}

	private int loadWarpPlayerTeleportInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		warpPlayerTeleportInventoryConfig = new EXGWarpPlayerTeleportInventoryConfig(title, rows, borderItem, borderSlots);

		warpPlayerTeleportInventoryConfig.setPlayerItem(inventoryFile.getItem(
				"playerItem", warpPlayerTeleportInventoryConfig));

		warpPlayerTeleportInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem", warpPlayerTeleportInventoryConfig));
		warpPlayerTeleportInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem", warpPlayerTeleportInventoryConfig));
		warpPlayerTeleportInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem", warpPlayerTeleportInventoryConfig));

		warpPlayerTeleportInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem", warpPlayerTeleportInventoryConfig));

		warpPlayerTeleportInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());

		return warpPlayerTeleportInventoryConfig.getConfigurationErrors();
	}

	private int loadWarpsAdminViewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		warpsAdminViewInventoryConfig = new EXGWarpsAdminViewInventoryConfig(title, rows, borderItem, borderSlots);

		warpsAdminViewInventoryConfig.setWarpItem(inventoryFile.getItem(
				"warpItem", warpsAdminViewInventoryConfig));
		warpsAdminViewInventoryConfig.setNoWarpsItem(inventoryFile.getItem(
				"noWarpsItem", warpsAdminViewInventoryConfig));

		warpsAdminViewInventoryConfig.setCreateWarpItem(inventoryFile.getItem(
				"createWarpItem", warpsAdminViewInventoryConfig));
		warpsAdminViewInventoryConfig.setSwitchToPlayerModeItem(inventoryFile.getItem(
				"switchToPlayerModeItem", warpsAdminViewInventoryConfig));
		warpsAdminViewInventoryConfig.setSearchWarpItem(inventoryFile.getItem(
				"searchWarpItem", warpsAdminViewInventoryConfig));
		warpsAdminViewInventoryConfig.setCancelSearchWarpItem(inventoryFile.getItem(
				"cancelSearchWarpItem", warpsAdminViewInventoryConfig));
		warpsAdminViewInventoryConfig.setNoSearchWarpResultsItem(inventoryFile.getItem(
				"noSearchWarpResultsItem", warpsAdminViewInventoryConfig));

		warpsAdminViewInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem", warpsAdminViewInventoryConfig));
		warpsAdminViewInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem", warpsAdminViewInventoryConfig));
		warpsAdminViewInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem", warpsAdminViewInventoryConfig));

		warpsAdminViewInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem", warpsAdminViewInventoryConfig));

		warpsAdminViewInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());

		return warpsAdminViewInventoryConfig.getConfigurationErrors();
	}

	private int loadWarpsPlayerViewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		warpsPlayerViewInventoryConfig = new EXGWarpsPlayerViewInventoryConfig(title, rows, borderItem, borderSlots);

		warpsPlayerViewInventoryConfig.setWarpItem(inventoryFile.getItem(
				"warpItem", warpsPlayerViewInventoryConfig));
		warpsPlayerViewInventoryConfig.setNoWarpsItem(inventoryFile.getItem(
				"noWarpsItem", warpsPlayerViewInventoryConfig));

		warpsPlayerViewInventoryConfig.setSwitchToAdminModeItem(inventoryFile.getItem(
				"switchToAdminModeItem", warpsPlayerViewInventoryConfig));
		warpsPlayerViewInventoryConfig.setSearchWarpItem(inventoryFile.getItem(
				"searchWarpItem", warpsPlayerViewInventoryConfig));
		warpsPlayerViewInventoryConfig.setCancelSearchWarpItem(inventoryFile.getItem(
				"cancelSearchWarpItem", warpsPlayerViewInventoryConfig));
		warpsPlayerViewInventoryConfig.setNoSearchWarpResultsItem(inventoryFile.getItem(
				"noSearchWarpResultsItem", warpsPlayerViewInventoryConfig));

		warpsPlayerViewInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem", warpsPlayerViewInventoryConfig));
		warpsPlayerViewInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem", warpsPlayerViewInventoryConfig));
		warpsPlayerViewInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem", warpsPlayerViewInventoryConfig));

		warpsPlayerViewInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem", warpsPlayerViewInventoryConfig));

		warpsPlayerViewInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());

		return warpsPlayerViewInventoryConfig.getConfigurationErrors();
	}


	//
	// WHOIS INVENTORIES
	//


	private int loadWhoisPlayersInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		whoisPlayersInventoryConfig = new EXGWhoisPlayersInventoryConfig(title, rows, borderItem, borderSlots);

		whoisPlayersInventoryConfig.setPlayerItem(inventoryFile.getItem(
				"playerItem", whoisPlayersInventoryConfig));

		whoisPlayersInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem", whoisPlayersInventoryConfig));
		whoisPlayersInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem", whoisPlayersInventoryConfig));
		whoisPlayersInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem", whoisPlayersInventoryConfig));

		whoisPlayersInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem", whoisPlayersInventoryConfig));

		whoisPlayersInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());

		return whoisPlayersInventoryConfig.getConfigurationErrors();
	}

	private int loadWhoisViewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		whoisViewInventoryConfig = new EXGWhoisViewInventoryConfig(title, rows, borderItem, borderSlots);

		whoisViewInventoryConfig.setPlayerIdentificationItem(inventoryFile.getItem(
				"playerIdentificationItem", whoisViewInventoryConfig));
		whoisViewInventoryConfig.setPlayerStatisticsItem(inventoryFile.getItem(
				"playerStatisticsItem", whoisViewInventoryConfig));
		whoisViewInventoryConfig.setPlayerWorldItem(inventoryFile.getItem(
				"playerWorldItem", whoisViewInventoryConfig));
		whoisViewInventoryConfig.setPlayerServerDataItem(inventoryFile.getItem(
				"playerServerDataItem", whoisViewInventoryConfig));
		whoisViewInventoryConfig.setPlayerPunishmentsItem(inventoryFile.getItem(
				"playerPunishmentsItem", whoisViewInventoryConfig));

		whoisViewInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem", whoisViewInventoryConfig));
		whoisViewInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem", whoisViewInventoryConfig));
		whoisViewInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem", whoisViewInventoryConfig));

		whoisViewInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem", whoisViewInventoryConfig));

		whoisViewInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());

		whoisViewInventoryConfig.setOnlyUsePlaceholderAPI(inventoryFile.getYamlConfiguration()
				.getBoolean("onlyUsePlaceholderAPI"));

		return whoisViewInventoryConfig.getConfigurationErrors();
	}


	//
	// ECONOMY INVENTORIES
	//


	private int loadBalanceTopInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		balanceTopInventoryConfig = new EXGBalanceTopInventoryConfig(title, rows, borderItem, borderSlots);

		balanceTopInventoryConfig.setPlayerRankingItem(inventoryFile.getItem(
				"playerRankingItem", balanceTopInventoryConfig));
		balanceTopInventoryConfig.setForceUpdateItem(inventoryFile.getItem(
				"forceUpdateItem", balanceTopInventoryConfig));

		balanceTopInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem", balanceTopInventoryConfig));

		balanceTopInventoryConfig.setRankingItems(inventoryFile.getItemsSection(
				"rankingItems", balanceTopInventoryConfig));

		balanceTopInventoryConfig.setRankingRange(inventoryFile.getRankingRange());

		return balanceTopInventoryConfig.getConfigurationErrors();
	}


	private int loadEcoActionInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		ecoActionInventoryConfig = new EXGEcoActionInventoryConfig(title, rows, borderItem, borderSlots);

		ecoActionInventoryConfig.setPlayerItem(inventoryFile.getItem(
				"playerItem", ecoActionInventoryConfig));

		ecoActionInventoryConfig.setAddBalanceItem(inventoryFile.getItem(
				"addBalanceItem", ecoActionInventoryConfig));
		ecoActionInventoryConfig.setTakeBalanceItem(inventoryFile.getItem(
				"takeBalanceItem", ecoActionInventoryConfig));
		ecoActionInventoryConfig.setSetBalanceItem(inventoryFile.getItem(
				"setBalanceItem", ecoActionInventoryConfig));
		ecoActionInventoryConfig.setResetBalanceItem(inventoryFile.getItem(
				"resetBalanceItem", ecoActionInventoryConfig));

		ecoActionInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem", ecoActionInventoryConfig));

		return ecoActionInventoryConfig.getConfigurationErrors();
	}


	private int loadEcoAmountInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		ecoAmountInventoryConfig = new EXGEcoAmountInventoryConfig(title, rows, borderItem, borderSlots);

		ecoAmountInventoryConfig.setAddItems(inventoryFile.getItems(
				"addItem", true, ecoAmountInventoryConfig));
		ecoAmountInventoryConfig.setRemoveItems(inventoryFile.getItems(
				"removeItem", true, ecoAmountInventoryConfig));

		ecoAmountInventoryConfig.setConfirmActionItem(inventoryFile.getItem(
				"confirmActionItem", ecoAmountInventoryConfig));
		ecoAmountInventoryConfig.setCancelActionItem(inventoryFile.getItem(
				"cancelActionItem", ecoAmountInventoryConfig));

		return ecoAmountInventoryConfig.getConfigurationErrors();
	}


	private int loadEcoPlayersInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		ecoPlayersInventoryConfig = new EXGEcoPlayersInventoryConfig(title, rows, borderItem, borderSlots);

		ecoPlayersInventoryConfig.setPlayerItem(inventoryFile.getItem(
				"playerItem", ecoPlayersInventoryConfig));

		ecoPlayersInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem", ecoPlayersInventoryConfig));
		ecoPlayersInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem", ecoPlayersInventoryConfig));
		ecoPlayersInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem", ecoPlayersInventoryConfig));

		ecoPlayersInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem", ecoPlayersInventoryConfig));

		ecoPlayersInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());

		return ecoPlayersInventoryConfig.getConfigurationErrors();
	}


	private int loadSellInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		sellInventoryConfig = new EXGSellInventoryConfig(title, rows, borderItem, borderSlots);

		sellInventoryConfig.setConfirmSellItem(inventoryFile.getItem(
				"confirmSellItem", sellInventoryConfig));

		sellInventoryConfig.setCancelSellItem(inventoryFile.getItem(
				"cancelSellItem", sellInventoryConfig));

		return sellInventoryConfig.getConfigurationErrors();
	}


	private int loadWorthInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		worthInventoryConfig = new EXGWorthInventoryConfig(title, rows, borderItem, borderSlots);

		worthInventoryConfig.setAllItem(inventoryFile.getItem(
				"allItem", worthInventoryConfig));
		worthInventoryConfig.setHandItem(inventoryFile.getItem(
				"handItem", worthInventoryConfig));
		worthInventoryConfig.setInventoryItem(inventoryFile.getItem(
				"inventoryItem", worthInventoryConfig));

		worthInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem", worthInventoryConfig));

		return worthInventoryConfig.getConfigurationErrors();
	}


	private int loadWorthAllInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		worthAllInventoryConfig = new EXGWorthAllInventoryConfig(title, rows, borderItem, borderSlots);

		worthAllInventoryConfig.setWorthItem(inventoryFile.getItem(
				"worthItem", worthAllInventoryConfig));
		worthAllInventoryConfig.setNoWorthItem(inventoryFile.getItem(
				"noWorthItem", worthAllInventoryConfig));

		worthAllInventoryConfig.setSearchWorthItem(inventoryFile.getItem(
				"searchWorthItem", worthAllInventoryConfig));
		worthAllInventoryConfig.setCancelSearchWorthItem(inventoryFile.getItem(
				"cancelSearchWorthItem", worthAllInventoryConfig));
		worthAllInventoryConfig.setNoSearchWorthResultsItem(inventoryFile.getItem(
				"noSearchWorthResultsItem", worthAllInventoryConfig));

		worthAllInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem", worthAllInventoryConfig));
		worthAllInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem", worthAllInventoryConfig));
		worthAllInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem", worthAllInventoryConfig));

		worthAllInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem", worthAllInventoryConfig));

		worthAllInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());

		return worthAllInventoryConfig.getConfigurationErrors();
	}


	private int loadWorthInventoryInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		worthInventoryInventoryConfig = new EXGWorthInventoryInventoryConfig(title, rows, borderItem, borderSlots);

		worthInventoryInventoryConfig.setWorthItem(inventoryFile.getItem(
				"worthItem", worthInventoryInventoryConfig));
		worthInventoryInventoryConfig.setEmptyInventoryItem(inventoryFile.getItem(
				"emptyInventoryItem", worthInventoryInventoryConfig));

		worthInventoryInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem", worthInventoryInventoryConfig));
		worthInventoryInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem", worthInventoryInventoryConfig));
		worthInventoryInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem", worthInventoryInventoryConfig));

		worthInventoryInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem", worthInventoryInventoryConfig));

		worthInventoryInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());

		return worthInventoryInventoryConfig.getConfigurationErrors();
	}


	//
	// OTHERS INVENTORIES
	//


	private int loadDataEntryGUIInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		dataEntryGUIInventoryConfig = new EXGDataEntryGUInventoryConfig(title, rows, borderItem, borderSlots);

		dataEntryGUIInventoryConfig.setMaterialIconItem(inventoryFile.getItem(
				"materialIconItem", dataEntryGUIInventoryConfig));

		dataEntryGUIInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem", dataEntryGUIInventoryConfig));
		dataEntryGUIInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem", dataEntryGUIInventoryConfig));
		dataEntryGUIInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem", dataEntryGUIInventoryConfig));

		dataEntryGUIInventoryConfig.setCancelItem(inventoryFile.getItem(
				"cancelItem", dataEntryGUIInventoryConfig));

		dataEntryGUIInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());

		return dataEntryGUIInventoryConfig.getConfigurationErrors();
	}


	// -------------------------------------------------- //


	public List<String> getInventoryNames() {
		return List.of(
				"homes",
				"homeEditing",

				"kitsAdminView",
				"kitsPlayerView",
				"kitPreview",
				"kitPlayerGive",
				"kitEditing",
				"kitEditor",

				"warpEditing",
				"warpPlayerTeleport",
				"warpsAdminView",
				"warpsPlayerView",

				"whoisPlayers",
				"whoisView",

				"balanceTop",
				"ecoAction",
				"ecoAmount",
				"ecoPlayers",
				"sell",
				"worth",
				"worthAll",
				"worthInventory",

				"dataEntryGUI"
		);
	}


	// -------------------------------------------------- //


	public EXGHomesInventoryConfig getHomesInventoryConfig() {
		return homesInventoryConfig;
	}
	public EXGHomeEditingInventoryConfig getHomeEditingInventoryConfig() {
		return homeEditingInventoryConfig;
	}

	public EXGKitsAdminViewInventoryConfig getKitsAdminViewInventoryConfig() {
		return kitsAdminViewInventoryConfig;
	}
	public EXGKitsPlayerViewInventoryConfig getKitsPlayerViewInventoryConfig() {
		return kitsPlayerViewInventoryConfig;
	}
	public EXGKitPreviewInventoryConfig getKitPreviewInventoryConfig() {
		return kitPreviewInventoryConfig;
	}
	public EXGKitPlayerGiveInventoryConfig getKitPlayerGiveInventoryConfig() {
		return kitPlayerGiveInventoryConfig;
	}
	public EXGKitEditingInventoryConfig getKitEditingInventoryConfig() {
		return kitEditingInventoryConfig;
	}
	public EXGKitEditorInventoryConfig getKitEditorInventoryConfig() {
		return kitEditorInventoryConfig;
	}

	public EXGWarpEditingInventoryConfig getWarpEditingInventoryConfig() {
		return warpEditingInventoryConfig;
	}
	public EXGWarpPlayerTeleportInventoryConfig getWarpPlayerTeleportInventoryConfig() {
		return warpPlayerTeleportInventoryConfig;
	}
	public EXGWarpsAdminViewInventoryConfig getWarpsAdminViewInventoryConfig() {
		return warpsAdminViewInventoryConfig;
	}
	public EXGWarpsPlayerViewInventoryConfig getWarpsPlayerViewInventoryConfig() {
		return warpsPlayerViewInventoryConfig;
	}

	public EXGWhoisPlayersInventoryConfig getWhoisPlayersInventoryConfig() {
		return whoisPlayersInventoryConfig;
	}
	public EXGWhoisViewInventoryConfig getWhoisViewInventoryConfig() {
		return whoisViewInventoryConfig;
	}

	public EXGBalanceTopInventoryConfig getBalanceTopInventoryConfig() {
		return balanceTopInventoryConfig;
	}
	public EXGEcoActionInventoryConfig  getEcoActionInventoryConfig() {
		return ecoActionInventoryConfig;
	}
	public EXGEcoAmountInventoryConfig getEcoAmountInventoryConfig() {
		return ecoAmountInventoryConfig;
	}
	public EXGEcoPlayersInventoryConfig getEcoPlayersInventoryConfig() {
		return ecoPlayersInventoryConfig;
	}
	public EXGSellInventoryConfig getSellInventoryConfig() {
		return sellInventoryConfig;
	}
	public EXGWorthInventoryConfig getWorthInventoryConfig() {
		return worthInventoryConfig;
	}
	public EXGWorthAllInventoryConfig  getWorthAllInventoryConfig() {
		return worthAllInventoryConfig;
	}
	public EXGWorthInventoryInventoryConfig getWorthInventoryInventoryConfig() {
		return worthInventoryInventoryConfig;
	}

	public EXGDataEntryGUInventoryConfig getDataEntryGUIInventoryConfig() {
		return dataEntryGUIInventoryConfig;
	}


	// -------------------------------------------------- //
}
