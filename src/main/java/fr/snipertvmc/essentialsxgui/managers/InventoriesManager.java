package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.EXGHomesInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.EXGHomeEditingInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitsAdminViewInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitsPlayerGiveInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitsPlayerViewInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitsPreviewInventoryConfig;
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


	// -------------------------------------------------- //


	public void loadInventories() {

		for (String inventoryName : getInventoryNames()) {

			String title = Main.getInstance().getFilesManager().getInventory(inventoryName).getTitle(inventoryName);
			int rows = Main.getInstance().getFilesManager().getInventory(inventoryName).getRows(inventoryName);
			EXGItemConfig borderItem = Main.getInstance().getFilesManager().getInventory(inventoryName).getBorderItem(inventoryName);
			int[] borderSlots = Main.getInstance().getFilesManager().getInventory(inventoryName).getBorderSlots(inventoryName);

			switch (inventoryName) {
				case "homes" -> loadHomesInventory(title, rows, inventoryName, borderItem, borderSlots);
				case "homeEditing" -> loadHomeEditingInventory(title, rows, inventoryName, borderItem, borderSlots);

				case "kitsAdminView" -> loadKitsAdminViewInventory(title, rows, inventoryName, borderItem, borderSlots);
				case "kitsPlayerView" -> loadKitsPlayerViewInventory(title, rows, inventoryName, borderItem, borderSlots);
				case "kitsPreview" -> loadKitsPreviewInventory(title, rows, inventoryName, borderItem, borderSlots);
				case "kitsPlayerGive" -> loadKitsPlayerGiveInventory(title, rows, inventoryName, borderItem, borderSlots);
			}
		}
	}


	// -------------------------------------------------- //


	//
 	// HOMES INVENTORIES
	//


	private void loadHomesInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		homesInventoryConfig = new EXGHomesInventoryConfig(title, rows, borderItem, borderSlots);

		homesInventoryConfig.setHomeItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "homeItem"));
		homesInventoryConfig.setNextPageItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "nextPageItem"));
		homesInventoryConfig.setPreviousPageItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "previousPageItem"));
		homesInventoryConfig.setCurrentPageItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "currentPageItem"));
		homesInventoryConfig.setCloseItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "closeItem"));

		homesInventoryConfig.setInventoryScheme(Main.getInstance().getFilesManager().getInventory(inventoryName).getInventoryScheme(
				inventoryName));
	}

	private void loadHomeEditingInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		homeEditingInventoryConfig = new EXGHomeEditingInventoryConfig(title, rows, borderItem, borderSlots);

		homeEditingInventoryConfig.setPreviewHomeItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "previewHomeItem"));
		homeEditingInventoryConfig.setChangeDisplayNameItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "changeDisplayNameItem"));
		homeEditingInventoryConfig.setChangeIconItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "changeIconItem"));
		homeEditingInventoryConfig.setBackItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "backItem"));
	}


	//
	// KITS INVENTORIES
	//


	private void loadKitsAdminViewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		kitsAdminInventoryConfig = new EXGKitsAdminViewInventoryConfig(title, rows, borderItem, borderSlots);

		kitsAdminInventoryConfig.setKitItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "kitItem"));
		kitsAdminInventoryConfig.setSwitchToPlayerModeItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "switchToPlayerModeItem"));
		kitsAdminInventoryConfig.setNextPageItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "nextPageItem"));
		kitsAdminInventoryConfig.setPreviousPageItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "previousPageItem"));
		kitsAdminInventoryConfig.setCurrentPageItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "currentPageItem"));
		kitsAdminInventoryConfig.setCloseItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "closeItem"));

		kitsAdminInventoryConfig.setInventoryScheme(Main.getInstance().getFilesManager().getInventory(inventoryName).getInventoryScheme(
				inventoryName));
	}

	private void loadKitsPlayerViewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		kitsPlayerInventoryConfig = new EXGKitsPlayerViewInventoryConfig(title, rows, borderItem, borderSlots);

		kitsPlayerInventoryConfig.setKitItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "kitItem"));
		kitsPlayerInventoryConfig.setSwitchToAdminModeItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "switchToAdminModeItem"));
		kitsPlayerInventoryConfig.setNextPageItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "nextPageItem"));
		kitsPlayerInventoryConfig.setPreviousPageItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "previousPageItem"));
		kitsPlayerInventoryConfig.setCurrentPageItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "currentPageItem"));
		kitsPlayerInventoryConfig.setCloseItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "closeItem"));

		kitsPlayerInventoryConfig.setInventoryScheme(Main.getInstance().getFilesManager().getInventory(inventoryName).getInventoryScheme(
				inventoryName));
	}


	private void loadKitsPreviewInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		kitsPreviewInventoryConfig = new EXGKitsPreviewInventoryConfig(title, rows, borderItem, borderSlots);

		kitsPreviewInventoryConfig.setTitle(title);
		kitsPreviewInventoryConfig.setRows(rows);
		kitsPreviewInventoryConfig.setBorderItem(borderItem);
		kitsPreviewInventoryConfig.setBorderSlots(borderSlots);

		kitsPreviewInventoryConfig.setKitItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "kitItem"));
		kitsPreviewInventoryConfig.setNextPageItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "nextPageItem"));
		kitsPreviewInventoryConfig.setPreviousPageItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "previousPageItem"));
		kitsPreviewInventoryConfig.setCurrentPageItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "currentPageItem"));
		kitsPreviewInventoryConfig.setBackItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "backItem"));

		kitsPreviewInventoryConfig.setInventoryScheme(Main.getInstance().getFilesManager().getInventory(inventoryName).getInventoryScheme(
				inventoryName));
	}


	private void loadKitsPlayerGiveInventory(String title, int rows, String inventoryName, EXGItemConfig borderItem, int... borderSlots) {

		kitsPlayerGiveInventoryConfig = new EXGKitsPlayerGiveInventoryConfig(title, rows, borderItem, borderSlots);

		kitsPlayerGiveInventoryConfig.setPlayerItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "playerItem"));
		kitsPlayerGiveInventoryConfig.setNextPageItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "nextPageItem"));
		kitsPlayerGiveInventoryConfig.setPreviousPageItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "previousPageItem"));
		kitsPlayerGiveInventoryConfig.setCurrentPageItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "currentPageItem"));
		kitsPlayerGiveInventoryConfig.setBackItem(Main.getInstance().getFilesManager().getInventory(inventoryName).getItem(
				inventoryName, "backItem"));

		kitsPlayerGiveInventoryConfig.setInventoryScheme(Main.getInstance().getFilesManager().getInventory(inventoryName).getInventoryScheme(
				inventoryName));
	}


	// -------------------------------------------------- //


	public List<String> getInventoryNames() {
		return List.of(
				"homes",
				"homeEditing",

				"kitsAdminView",
				"kitsPlayerView",
				"kitsPreview",
				"kitsPlayerGive"
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


	// -------------------------------------------------- //
}
