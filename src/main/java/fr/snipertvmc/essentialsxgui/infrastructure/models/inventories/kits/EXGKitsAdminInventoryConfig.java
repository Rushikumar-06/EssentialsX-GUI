package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits;

import fr.mrmicky.fastinv.InventoryScheme;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;

public class EXGKitsAdminInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig kitItem;

	private EXGItemConfig switchToPlayerModeItem;

	private EXGItemConfig nextPageItem;
	private EXGItemConfig previousPageItem;
	private EXGItemConfig currentPageItem;

	private EXGItemConfig closeItem;

	private InventoryScheme inventoryScheme;


	// -------------------------------------------------- //


	public EXGKitsAdminInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getKitItem() {
		return kitItem;
	}


	public EXGItemConfig getSwitchToPlayerModeItem() {
		return switchToPlayerModeItem;
	}


	public EXGItemConfig getNextPageItem() {
		return nextPageItem;
	}


	public EXGItemConfig getPreviousPageItem() {
		return previousPageItem;
	}


	public EXGItemConfig getCurrentPageItem() {
		return currentPageItem;
	}


	public EXGItemConfig getCloseItem() {
		return closeItem;
	}


	public InventoryScheme getInventoryScheme() {
		return inventoryScheme;
	}


	// -------------------------------------------------- //


	public void setKitItem(EXGItemConfig kitItem) {
		this.kitItem = kitItem;
	}


	public void setSwitchToPlayerModeItem(EXGItemConfig switchToPlayerModeItem) {
		this.switchToPlayerModeItem = switchToPlayerModeItem;
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


	public EXGKitsAdminInventoryConfig copy() {

		EXGKitsAdminInventoryConfig copy = new EXGKitsAdminInventoryConfig(
				this.getEXGTitle().getTitle(),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setKitItem(this.getKitItem().duplicate());
		copy.setSwitchToPlayerModeItem(this.getSwitchToPlayerModeItem().duplicate());
		copy.setNextPageItem(this.getNextPageItem().duplicate());
		copy.setPreviousPageItem(this.getPreviousPageItem().duplicate());
		copy.setCurrentPageItem(this.getCurrentPageItem().duplicate());
		copy.setCloseItem(this.getCloseItem().duplicate());

		copy.setInventoryScheme(this.getInventoryScheme());

		return copy;
	}


	// -------------------------------------------------- //
}
