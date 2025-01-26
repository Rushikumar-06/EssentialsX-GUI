package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.EXGHomesInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.EXGHomeEditingInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitsAdminInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitsPlayerInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;

import java.util.List;

public class InventoriesManager {


	// -------------------------------------------------- //


	private List<String> inventoriesNames = List.of(
			"homes", "homeEditing",
			"kitsAdmin", "kitsPlayer");


	private EXGHomesInventoryConfig homesInventoryConfig;
	private EXGHomeEditingInventoryConfig homeEditingInventoryConfig;

	private EXGKitsAdminInventoryConfig kitsAdminInventoryConfig;
	private EXGKitsPlayerInventoryConfig kitsPlayerInventoryConfig;


	// -------------------------------------------------- //


	public void loadInventories() {

		for (String inventoryName : inventoriesNames) {

			String title = Main.getInstance().getFilesManager().getInventories().getTitle(inventoryName);
			int rows = Main.getInstance().getFilesManager().getInventories().getRows(inventoryName);
			EXGItemConfig borderItem = Main.getInstance().getFilesManager().getInventories().getBorderItem(inventoryName);
			int[] borderSlots = Main.getInstance().getFilesManager().getInventories().getBorderSlots(inventoryName);

			switch (inventoryName) {

				//
				// HOMES
				//

				case "homes" -> {

					homesInventoryConfig = new EXGHomesInventoryConfig(title, rows, borderItem, borderSlots);

					homesInventoryConfig.setHomeItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "homeItem"));
					homesInventoryConfig.setNextPageItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "nextPageItem"));
					homesInventoryConfig.setPreviousPageItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "previousPageItem"));
					homesInventoryConfig.setCurrentPageItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "currentPageItem"));
					homesInventoryConfig.setCloseItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "closeItem"));

					homesInventoryConfig.setInventoryScheme(Main.getInstance().getFilesManager().getInventories().getInventoryScheme(
							inventoryName));
				}

				case "homeEditing" -> {

					homeEditingInventoryConfig = new EXGHomeEditingInventoryConfig(title, rows, borderItem, borderSlots);

					homeEditingInventoryConfig.setPreviewHomeItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "previewHomeItem"));
					homeEditingInventoryConfig.setChangeDisplayNameItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "changeDisplayNameItem"));
					homeEditingInventoryConfig.setChangeIconItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "changeIconItem"));
					homeEditingInventoryConfig.setBackItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "backItem"));
				}

				//
				// KITS
				//

				case "kitsAdmin" -> {

					kitsAdminInventoryConfig = new EXGKitsAdminInventoryConfig(title, rows, borderItem, borderSlots);

					kitsAdminInventoryConfig.setKitItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "kitItem"));
					kitsAdminInventoryConfig.setSwitchToPlayerModeItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "switchToPlayerModeItem"));
					kitsAdminInventoryConfig.setNextPageItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "nextPageItem"));
					kitsAdminInventoryConfig.setPreviousPageItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "previousPageItem"));
					kitsAdminInventoryConfig.setCurrentPageItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "currentPageItem"));
					kitsAdminInventoryConfig.setCloseItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "closeItem"));

					kitsAdminInventoryConfig.setInventoryScheme(Main.getInstance().getFilesManager().getInventories().getInventoryScheme(
							inventoryName));
				}

				case "kitsPlayer" -> {

					kitsPlayerInventoryConfig = new EXGKitsPlayerInventoryConfig(title, rows, borderItem, borderSlots);

					kitsPlayerInventoryConfig.setKitItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "kitItem"));
					kitsPlayerInventoryConfig.setSwitchToAdminModeItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "switchToAdminModeItem"));
					kitsPlayerInventoryConfig.setNextPageItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "nextPageItem"));
					kitsPlayerInventoryConfig.setPreviousPageItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "previousPageItem"));
					kitsPlayerInventoryConfig.setCurrentPageItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "currentPageItem"));
					kitsPlayerInventoryConfig.setCloseItem(Main.getInstance().getFilesManager().getInventories().getItem(
							inventoryName, "closeItem"));

					kitsPlayerInventoryConfig.setInventoryScheme(Main.getInstance().getFilesManager().getInventories().getInventoryScheme(
							inventoryName));
				}
			}
		}
	}


	// -------------------------------------------------- //


	public EXGHomesInventoryConfig getHomesInventoryConfig() {
		return homesInventoryConfig;
	}
	public EXGHomeEditingInventoryConfig getHomeEditingInventoryConfig() {
		return homeEditingInventoryConfig;
	}

	public EXGKitsAdminInventoryConfig getKitsAdminInventoryConfig() {
		return kitsAdminInventoryConfig;
	}
	public EXGKitsPlayerInventoryConfig getKitsPlayerInventoryConfig() {
		return kitsPlayerInventoryConfig;
	}


	// -------------------------------------------------- //
}
