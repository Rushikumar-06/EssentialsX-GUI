package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.EXGBalanceTopInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.EXGWorthAllInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.EXGWorthInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.EXGWorthInventoryInventoryConfig;
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
	private EXGWorthInventoryConfig worthInventoryConfig;
	private EXGWorthAllInventoryConfig worthAllInventoryConfig;
	private EXGWorthInventoryInventoryConfig worthInventoryInventoryConfig;

	private EXGDataEntryGUInventoryConfig dataEntryGUIInventoryConfig;


	// -------------------------------------------------- //


	public void loadInventory(String inventoryName) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		String title = inventoryFile.getTitle();
		int rows = inventoryFile.getRows();
		EXGItemConfig borderItem = inventoryFile.getBorderItem();
		int[] borderSlots = inventoryFile.getBorderSlots();

		switch (inventoryName) {
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
			case "worth" -> loadWorthInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "worthAll" -> loadWorthAllInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "worthInventory" -> loadWorthInventoryInventory(title, rows, inventoryName, borderItem, borderSlots);

			case "dataEntryGUI" -> loadDataEntryGUIInventory(title, rows, inventoryName, borderItem, borderSlots);
		}
	}


	// -------------------------------------------------- //


	//
 	// HOMES INVENTORIES
	//


	private void loadHomesInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		homesInventoryConfig = new EXGHomesInventoryConfig(title, rows, borderItem, borderSlots);

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		homesInventoryConfig.setHomeItem(inventoryFile.getItem(
				"homeItem"));
		homesInventoryConfig.setBedHomeItem(inventoryFile.getItem(
				"bedHomeItem"));
		homesInventoryConfig.setNoHomesItem(inventoryFile.getItem(
				"noHomesItem"));

		homesInventoryConfig.setCreateHomeItem(inventoryFile.getItem(
				"createHomeItem"));
		homesInventoryConfig.setSearchHomeItem(inventoryFile.getItem(
				"searchHomeItem"));
		homesInventoryConfig.setCancelSearchHomeItem(inventoryFile.getItem(
				"cancelSearchHomeItem"));
		homesInventoryConfig.setNoSearchHomeResultsItem(inventoryFile.getItem(
				"noSearchHomeResultsItem"));

		homesInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		homesInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		homesInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));

		homesInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem"));

		homesInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());

		homesInventoryConfig.setBedHomeItemOverworldMaterial(inventoryFile.getFullBedHomeItemMaterial("overworld"),
				inventoryFile.getBedHomeItemWorldDisplayName("overworld"));
		homesInventoryConfig.setBedHomeItemNetherMaterial(inventoryFile.getFullBedHomeItemMaterial("nether"),
				inventoryFile.getBedHomeItemWorldDisplayName("nether"));
		homesInventoryConfig.setBedHomeItemNotSetMaterial(inventoryFile.getFullBedHomeItemMaterial("notSet"),
				inventoryFile.getBedHomeItemWorldDisplayName("notSet"));
	}

	private void loadHomeEditingInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		homeEditingInventoryConfig = new EXGHomeEditingInventoryConfig(title, rows, borderItem, borderSlots);

		homeEditingInventoryConfig.setPreviewHomeItem(inventoryFile.getItem(
				"previewHomeItem"));

		homeEditingInventoryConfig.setChangeDisplayNameItem(inventoryFile.getItem(
				"changeDisplayNameItem"));
		homeEditingInventoryConfig.setChangeIconItem(inventoryFile.getItem(
				"changeIconItem"));
		homeEditingInventoryConfig.setDeleteHomeItem(inventoryFile.getItem(
				"deleteHomeItem"));

		homeEditingInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem"));
	}


	//
	// KITS INVENTORIES
	//


	private void loadKitsAdminViewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		kitsAdminViewInventoryConfig = new EXGKitsAdminViewInventoryConfig(title, rows, borderItem, borderSlots);

		kitsAdminViewInventoryConfig.setKitItem(inventoryFile.getItem(
				"kitItem"));
		kitsAdminViewInventoryConfig.setNoKitsItem(inventoryFile.getItem(
				"noKitsItem"));

		kitsAdminViewInventoryConfig.setCreateKitItem(inventoryFile.getItem(
				"createKitItem"));
		kitsAdminViewInventoryConfig.setSwitchToPlayerModeItem(inventoryFile.getItem(
				"switchToPlayerModeItem"));
		kitsAdminViewInventoryConfig.setSearchKitItem(inventoryFile.getItem(
				"searchKitItem"));
		kitsAdminViewInventoryConfig.setCancelSearchKitItem(inventoryFile.getItem(
				"cancelSearchKitItem"));
		kitsAdminViewInventoryConfig.setNoSearchKitResultsItem(inventoryFile.getItem(
				"noSearchKitResultsItem"));

		kitsAdminViewInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		kitsAdminViewInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		kitsAdminViewInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));

		kitsAdminViewInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem"));

		kitsAdminViewInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());
	}

	private void loadKitsPlayerViewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		kitsPlayerViewInventoryConfig = new EXGKitsPlayerViewInventoryConfig(title, rows, borderItem, borderSlots);

		kitsPlayerViewInventoryConfig.setKitItem(inventoryFile.getItem(
				"kitItem"));
		kitsPlayerViewInventoryConfig.setNoKitsItem(inventoryFile.getItem(
				"noKitsItem"));

		kitsPlayerViewInventoryConfig.setSwitchToAdminModeItem(inventoryFile.getItem(
				"switchToAdminModeItem"));
		kitsPlayerViewInventoryConfig.setSearchKitItem(inventoryFile.getItem(
				"searchKitItem"));
		kitsPlayerViewInventoryConfig.setCancelSearchKitItem(inventoryFile.getItem(
				"cancelSearchKitItem"));
		kitsPlayerViewInventoryConfig.setNoSearchKitResultsItem(inventoryFile.getItem(
				"noSearchKitResultsItem"));

		kitsPlayerViewInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		kitsPlayerViewInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		kitsPlayerViewInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));

		kitsPlayerViewInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem"));

		kitsPlayerViewInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());
	}

	private void loadKitPreviewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		kitPreviewInventoryConfig = new EXGKitPreviewInventoryConfig(title, rows, borderItem, borderSlots);

		kitPreviewInventoryConfig.setTitle(title);
		kitPreviewInventoryConfig.setRows(rows);
		kitPreviewInventoryConfig.setBorderItem(borderItem);
		kitPreviewInventoryConfig.setBorderSlots(borderSlots);

		kitPreviewInventoryConfig.setKitItem(inventoryFile.getItem(
				"kitItem"));
		kitPreviewInventoryConfig.setEmptyKitItem(inventoryFile.getItem(
				"emptyKitItem"));

		kitPreviewInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		kitPreviewInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		kitPreviewInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));

		kitPreviewInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem"));

		kitPreviewInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());
	}

	private void loadKitPlayerGiveInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		kitPlayerGiveInventoryConfig = new EXGKitPlayerGiveInventoryConfig(title, rows, borderItem, borderSlots);

		kitPlayerGiveInventoryConfig.setPlayerItem(inventoryFile.getItem(
				"playerItem"));

		kitPlayerGiveInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		kitPlayerGiveInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		kitPlayerGiveInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));

		kitPlayerGiveInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem"));

		kitPlayerGiveInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());
	}

	private void loadKitEditingInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		kitEditingInventoryConfig = new EXGKitEditingInventoryConfig(title, rows, borderItem, borderSlots);

		kitEditingInventoryConfig.setPreviewKitItem(inventoryFile.getItem(
				"previewKitItem"));

		kitEditingInventoryConfig.setChangeDisplayNameItem(inventoryFile.getItem(
				"changeDisplayNameItem"));
		kitEditingInventoryConfig.setChangeIconItem(inventoryFile.getItem(
				"changeIconItem"));
		kitEditingInventoryConfig.setDeleteKitItem(inventoryFile.getItem(
				"deleteKitItem"));
		kitEditingInventoryConfig.setEditKitContentsItem(inventoryFile.getItem(
				"editKitContentsItem"));

		kitEditingInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem"));
	}


	private void loadKitEditorInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		kitEditorInventoryConfig = new EXGKitEditorInventoryConfig(title, rows, borderItem, borderSlots);

		kitEditorInventoryConfig.setSaveKitItem(inventoryFile.getItem(
				"saveKitItem"));
		kitEditorInventoryConfig.setCancelChangesItem(inventoryFile.getItem(
				"cancelChangesItem"));
	}


	//
	// WARPS INVENTORIES
	//


	private void loadWarpEditingInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		warpEditingInventoryConfig = new EXGWarpEditingInventoryConfig(title, rows, borderItem, borderSlots);

		warpEditingInventoryConfig.setPreviewWarpItem(inventoryFile.getItem(
				"previewWarpItem"));

		warpEditingInventoryConfig.setChangeDisplayNameItem(inventoryFile.getItem(
				"changeDisplayNameItem"));
		warpEditingInventoryConfig.setChangeIconItem(inventoryFile.getItem(
				"changeIconItem"));
		warpEditingInventoryConfig.setDeleteWarpItem(inventoryFile.getItem(
				"deleteWarpItem"));

		warpEditingInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem"));
	}

	private void loadWarpPlayerTeleportInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		warpPlayerTeleportInventoryConfig = new EXGWarpPlayerTeleportInventoryConfig(title, rows, borderItem, borderSlots);

		warpPlayerTeleportInventoryConfig.setPlayerItem(inventoryFile.getItem(
				"playerItem"));

		warpPlayerTeleportInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		warpPlayerTeleportInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		warpPlayerTeleportInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));

		warpPlayerTeleportInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem"));

		warpPlayerTeleportInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());
	}

	private void loadWarpsAdminViewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		warpsAdminViewInventoryConfig = new EXGWarpsAdminViewInventoryConfig(title, rows, borderItem, borderSlots);

		warpsAdminViewInventoryConfig.setWarpItem(inventoryFile.getItem(
				"warpItem"));
		warpsAdminViewInventoryConfig.setNoWarpsItem(inventoryFile.getItem(
				"noWarpsItem"));

		warpsAdminViewInventoryConfig.setCreateWarpItem(inventoryFile.getItem(
				"createWarpItem"));
		warpsAdminViewInventoryConfig.setSwitchToPlayerModeItem(inventoryFile.getItem(
				"switchToPlayerModeItem"));
		warpsAdminViewInventoryConfig.setSearchWarpItem(inventoryFile.getItem(
				"searchWarpItem"));
		warpsAdminViewInventoryConfig.setCancelSearchWarpItem(inventoryFile.getItem(
				"cancelSearchWarpItem"));
		warpsAdminViewInventoryConfig.setNoSearchWarpResultsItem(inventoryFile.getItem(
				"noSearchWarpResultsItem"));

		warpsAdminViewInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		warpsAdminViewInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		warpsAdminViewInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));

		warpsAdminViewInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem"));

		warpsAdminViewInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());
	}

	private void loadWarpsPlayerViewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		warpsPlayerViewInventoryConfig = new EXGWarpsPlayerViewInventoryConfig(title, rows, borderItem, borderSlots);

		warpsPlayerViewInventoryConfig.setWarpItem(inventoryFile.getItem(
				"warpItem"));
		warpsPlayerViewInventoryConfig.setNoWarpsItem(inventoryFile.getItem(
				"noWarpsItem"));

		warpsPlayerViewInventoryConfig.setSwitchToAdminModeItem(inventoryFile.getItem(
				"switchToAdminModeItem"));
		warpsPlayerViewInventoryConfig.setSearchWarpItem(inventoryFile.getItem(
				"searchWarpItem"));
		warpsPlayerViewInventoryConfig.setCancelSearchWarpItem(inventoryFile.getItem(
				"cancelSearchWarpItem"));
		warpsPlayerViewInventoryConfig.setNoSearchWarpResultsItem(inventoryFile.getItem(
				"noSearchWarpResultsItem"));

		warpsPlayerViewInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		warpsPlayerViewInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		warpsPlayerViewInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));

		warpsPlayerViewInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem"));

		warpsPlayerViewInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());
	}


	//
	// WHOIS INVENTORIES
	//


	private void loadWhoisPlayersInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		whoisPlayersInventoryConfig = new EXGWhoisPlayersInventoryConfig(title, rows, borderItem, borderSlots);

		whoisPlayersInventoryConfig.setPlayerItem(inventoryFile.getItem(
				"playerItem"));

		whoisPlayersInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		whoisPlayersInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		whoisPlayersInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));

		whoisPlayersInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem"));

		whoisPlayersInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());
	}

	private void loadWhoisViewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		whoisViewInventoryConfig = new EXGWhoisViewInventoryConfig(title, rows, borderItem, borderSlots);

		whoisViewInventoryConfig.setPlayerIdentificationItem(inventoryFile.getItem(
				"playerIdentificationItem"));
		whoisViewInventoryConfig.setPlayerStatisticsItem(inventoryFile.getItem(
				"playerStatisticsItem"));
		whoisViewInventoryConfig.setPlayerWorldItem(inventoryFile.getItem(
				"playerWorldItem"));
		whoisViewInventoryConfig.setPlayerServerDataItem(inventoryFile.getItem(
				"playerServerDataItem"));
		whoisViewInventoryConfig.setPlayerPunishmentsItem(inventoryFile.getItem(
				"playerPunishmentsItem"));

		whoisViewInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		whoisViewInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		whoisViewInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));

		whoisViewInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem"));

		whoisViewInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());

		whoisViewInventoryConfig.setOnlyUsePlaceholderAPI(inventoryFile.getYamlConfiguration()
				.getBoolean("onlyUsePlaceholderAPI"));
	}


	//
	// ECONOMY INVENTORIES
	//


	public void loadBalanceTopInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		balanceTopInventoryConfig = new EXGBalanceTopInventoryConfig(title, rows, borderItem, borderSlots);

		balanceTopInventoryConfig.setPlayerRankingItem(inventoryFile.getItem(
				"playerRankingItem"));
		balanceTopInventoryConfig.setForceUpdateItem(inventoryFile.getItem(
				"forceUpdateItem"));

		balanceTopInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem"));

		balanceTopInventoryConfig.setRankingItems(inventoryFile.getItemsSection(
				"rankingItems"));

		balanceTopInventoryConfig.setRankingRange(inventoryFile.getRankingRange());
	}


	public void loadWorthInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		worthInventoryConfig = new EXGWorthInventoryConfig(title, rows, borderItem, borderSlots);

		worthInventoryConfig.setAllItem(inventoryFile.getItem(
				"allItem"));
		worthInventoryConfig.setHandItem(inventoryFile.getItem(
				"handItem"));
		worthInventoryConfig.setInventoryItem(inventoryFile.getItem(
				"inventoryItem"));

		worthInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem"));
	}


	public void loadWorthAllInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		worthAllInventoryConfig = new EXGWorthAllInventoryConfig(title, rows, borderItem, borderSlots);

		worthAllInventoryConfig.setWorthItem(inventoryFile.getItem(
				"worthItem"));
		worthAllInventoryConfig.setNoWorthItem(inventoryFile.getItem(
				"noWorthItem"));

		worthAllInventoryConfig.setSearchWorthItem(inventoryFile.getItem(
				"searchWorthItem"));
		worthAllInventoryConfig.setCancelSearchWorthItem(inventoryFile.getItem(
				"cancelSearchWorthItem"));
		worthAllInventoryConfig.setNoSearchWorthResultsItem(inventoryFile.getItem(
				"noSearchWorthResultsItem"));

		worthAllInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		worthAllInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		worthAllInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));

		worthAllInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem"));

		worthAllInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());
	}


	public void loadWorthInventoryInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		worthInventoryInventoryConfig = new EXGWorthInventoryInventoryConfig(title, rows, borderItem, borderSlots);

		worthInventoryInventoryConfig.setWorthItem(inventoryFile.getItem(
				"worthItem"));
		worthInventoryInventoryConfig.setEmptyInventoryItem(inventoryFile.getItem(
				"emptyInventoryItem"));

		worthInventoryInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		worthInventoryInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		worthInventoryInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));

		worthInventoryInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem"));

		worthInventoryInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());
	}


	//
	// OTHERS INVENTORIES
	//


	public void loadDataEntryGUIInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		dataEntryGUIInventoryConfig = new EXGDataEntryGUInventoryConfig(title, rows, borderItem, borderSlots);

		dataEntryGUIInventoryConfig.setMaterialIconItem(
				inventoryFile.getItem("materialIconItem"));

		dataEntryGUIInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		dataEntryGUIInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		dataEntryGUIInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));

		dataEntryGUIInventoryConfig.setCancelItem(inventoryFile.getItem(
				"cancelItem"));

		dataEntryGUIInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());
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
