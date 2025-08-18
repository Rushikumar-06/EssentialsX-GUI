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
	private EXGKitsPreviewInventoryConfig kitsPreviewInventoryConfig;
	private EXGKitsPlayerGiveInventoryConfig kitsPlayerGiveInventoryConfig;
	private EXGKitEditingInventoryConfig kitEditingInventoryConfig;


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
			case "kitsPreview" -> loadKitsPreviewInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "kitsPlayerGive" -> loadKitsPlayerGiveInventory(title, rows, inventoryName, borderItem, borderSlots);
			case "kitEditing" -> loadKitEditingInventory(title, rows, inventoryName, borderItem, borderSlots);
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

	private void loadKitsPreviewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		kitsPreviewInventoryConfig = new EXGKitsPreviewInventoryConfig(title, rows, borderItem, borderSlots);

		kitsPreviewInventoryConfig.setTitle(title);
		kitsPreviewInventoryConfig.setRows(rows);
		kitsPreviewInventoryConfig.setBorderItem(borderItem);
		kitsPreviewInventoryConfig.setBorderSlots(borderSlots);

		kitsPreviewInventoryConfig.setKitItem(inventoryFile.getItem(
				"kitItem"));
		kitsPreviewInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		kitsPreviewInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		kitsPreviewInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));
		kitsPreviewInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem"));

		kitsPreviewInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());
	}

	private void loadKitsPlayerGiveInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventory(inventoryName);

		kitsPlayerGiveInventoryConfig = new EXGKitsPlayerGiveInventoryConfig(title, rows, borderItem, borderSlots);

		kitsPlayerGiveInventoryConfig.setPlayerItem(inventoryFile.getItem(
				"playerItem"));
		kitsPlayerGiveInventoryConfig.setNextPageItem(inventoryFile.getItem(
				"nextPageItem"));
		kitsPlayerGiveInventoryConfig.setPreviousPageItem(inventoryFile.getItem(
				"previousPageItem"));
		kitsPlayerGiveInventoryConfig.setCurrentPageItem(inventoryFile.getItem(
				"currentPageItem"));
		kitsPlayerGiveInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem"));

		kitsPlayerGiveInventoryConfig.setInventoryScheme(inventoryFile.getInventoryScheme());
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
		kitEditingInventoryConfig.setBackItem(inventoryFile.getItem(
				"backItem"));
	}


	// -------------------------------------------------- //


	public List<String> getInventoryNames() {
		return List.of(
				"homes",
				"homeEditing",

				"kitsAdminView",
				"kitsPlayerView",
				"kitsPreview",
				"kitsPlayerGive",
				"kitEditing"
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
	public EXGKitsPreviewInventoryConfig getKitsPreviewInventoryConfig() {
		return kitsPreviewInventoryConfig;
	}
	public EXGKitsPlayerGiveInventoryConfig getKitsPlayerGiveInventoryConfig() {
		return kitsPlayerGiveInventoryConfig;
	}
	public EXGKitEditingInventoryConfig getKitEditingInventoryConfig() {
		return kitEditingInventoryConfig;
	}


	// -------------------------------------------------- //
}
