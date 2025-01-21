package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.EXGHomesInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.EXGHomeEditingInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;

import java.util.List;

public class InventoriesManager {


	// -------------------------------------------------- //


	private List<String> inventoriesNames = List.of(
			"homes", "homeEditing");


	private EXGHomesInventoryConfig homesInventoryConfig;
	private EXGHomeEditingInventoryConfig homeEditingInventoryConfig;


	// -------------------------------------------------- //


	public void loadInventories() {

		for (String inventoryName : inventoriesNames) {

			String title = Main.getInstance().getFilesManager().getInventories().getTitle(inventoryName);
			int rows = Main.getInstance().getFilesManager().getInventories().getRows(inventoryName);
			EXGItemConfig borderItem = Main.getInstance().getFilesManager().getInventories().getBorderItem(inventoryName);
			int[] borderSlots = Main.getInstance().getFilesManager().getInventories().getBorderSlots(inventoryName);

			switch (inventoryName) {

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


	// -------------------------------------------------- //
}
