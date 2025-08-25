package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.EXGHomesInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.EXGHomeEditingInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.*;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;

import java.util.List;

public class InventoriesManager {


	// -------------------------------------------------- //


	private EXGHomesInventoryConfig homesInventoryConfig;
	private EXGHomeEditingInventoryConfig homeEditingInventoryConfig;

	private EXGKitsAdminViewInventoryConfig kitsAdminInventoryConfig;
	private EXGKitsPlayerViewInventoryConfig kitsPlayerInventoryConfig;
	private EXGKitPreviewInventoryConfig kitPreviewInventoryConfig;
	private EXGKitPlayerGiveInventoryConfig kitPlayerGiveInventoryConfig;
	private EXGKitEditingInventoryConfig kitEditingInventoryConfig;
	private EXGKitEditorInventoryConfig kitEditorInventoryConfig;


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
			case "kitPlayerGive" -> loadkitPlayerGiveInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "kitEditing" -> loadKitEditingInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "kitEditor" -> loadKitEditorInventory(title, rows, inventoryName, borderItem, borderSlots);

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

		kitsAdminInventoryConfig = new EXGKitsAdminViewInventoryConfig(title, rows, borderItem, borderSlots);

		kitsAdminInventoryConfig.setKitItem(inventoryFile.getItem(
				"kitItem"));
		kitsAdminInventoryConfig.setNoKitsItem(inventoryFile.getItem(
				"noKitsItem"));

		kitsAdminInventoryConfig.setCreateKitItem(inventoryFile.getItem(
				"createKitItem"));
		kitsAdminInventoryConfig.setSwitchToPlayerModeItem(inventoryFile.getItem(
				"switchToPlayerModeItem"));
		kitsAdminInventoryConfig.setSearchKitItem(inventoryFile.getItem(
				"searchKitItem"));
		kitsAdminInventoryConfig.setCancelSearchKitItem(inventoryFile.getItem(
				"cancelSearchKitItem"));
		kitsAdminInventoryConfig.setNoSearchKitResultsItem(inventoryFile.getItem(
				"noSearchKitResultsItem"));

		kitsAdminInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		kitsAdminInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		kitsAdminInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));

		kitsAdminInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem"));

		kitsAdminInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());
	}

	private void loadKitsPlayerViewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		kitsPlayerInventoryConfig = new EXGKitsPlayerViewInventoryConfig(title, rows, borderItem, borderSlots);

		kitsPlayerInventoryConfig.setKitItem(inventoryFile.getItem(
				"kitItem"));
		kitsPlayerInventoryConfig.setNoKitsItem(inventoryFile.getItem(
				"noKitsItem"));

		kitsPlayerInventoryConfig.setSwitchToAdminModeItem(inventoryFile.getItem(
				"switchToAdminModeItem"));
		kitsPlayerInventoryConfig.setSearchKitItem(inventoryFile.getItem(
				"searchKitItem"));
		kitsPlayerInventoryConfig.setCancelSearchKitItem(inventoryFile.getItem(
				"cancelSearchKitItem"));
		kitsPlayerInventoryConfig.setNoSearchKitResultsItem(inventoryFile.getItem(
				"noSearchKitResultsItem"));

		kitsPlayerInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		kitsPlayerInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		kitsPlayerInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));

		kitsPlayerInventoryConfig.setCloseItem(inventoryFile.getItem(
				"closeItem"));

		kitsPlayerInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());
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

	private void loadkitPlayerGiveInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

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
				"kitEditor"
		);
	}


	public EXGHomesInventoryConfig getHomesInventoryConfig() {
		return homesInventoryConfig;
	}
	public EXGHomeEditingInventoryConfig getHomeEditingInventoryConfig() {
		return homeEditingInventoryConfig;
	}

	public EXGKitsAdminViewInventoryConfig getKitsAdminInventoryConfig() {
		return kitsAdminInventoryConfig;
	}
	public EXGKitsPlayerViewInventoryConfig getKitsPlayerInventoryConfig() {
		return kitsPlayerInventoryConfig;
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



	// -------------------------------------------------- //
}
