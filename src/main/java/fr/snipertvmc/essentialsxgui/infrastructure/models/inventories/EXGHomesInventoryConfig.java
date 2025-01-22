package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories;

import fr.mrmicky.fastinv.InventoryScheme;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;

public class EXGHomesInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig homeItem;

	private EXGItemConfig nextPageItem;
	private EXGItemConfig previousPageItem;
	private EXGItemConfig currentPageItem;

	private EXGItemConfig closeItem;

	private InventoryScheme inventoryScheme;


	// -------------------------------------------------- //


	public EXGHomesInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getHomeItem() {
		return homeItem.duplicate();
	}


	public EXGItemConfig getNextPageItem() {
		return nextPageItem.duplicate();
	}


	public EXGItemConfig getPreviousPageItem() {
		return previousPageItem.duplicate();
	}


	public EXGItemConfig getCurrentPageItem() {
		return currentPageItem.duplicate();
	}


	public EXGItemConfig getCloseItem() {
		return closeItem.duplicate();
	}


	public InventoryScheme getInventoryScheme() {
		return inventoryScheme;
	}


	// -------------------------------------------------- //


	public void setHomeItem(EXGItemConfig homeItem) {
		this.homeItem = homeItem;
	}


	public void setNextPageItem(EXGItemConfig nextPageItem) {
		this.nextPageItem = nextPageItem;
	}


	public void setPreviousPageItem(EXGItemConfig previousPageItem) {
		this.previousPageItem = previousPageItem;
	}


	public void setCurrentPageItem(EXGItemConfig currentPageItem) {
		this.currentPageItem = currentPageItem;
	}


	public void setCloseItem(EXGItemConfig closeItem) {
		this.closeItem = closeItem;
	}


	public void setInventoryScheme(InventoryScheme inventoryScheme) {
		this.inventoryScheme = inventoryScheme;
	}


	// -------------------------------------------------- //
}
