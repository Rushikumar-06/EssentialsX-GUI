package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.others;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;

public class EXGDataEntryGUInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig materialIconItem;

	private EXGItemConfig nextPageItem;
	private EXGItemConfig previousPageItem;
	private EXGItemConfig currentPageItem;

	private EXGItemConfig cancelItem;

	private InventoryScheme inventoryScheme;


	// -------------------------------------------------- //


	public EXGDataEntryGUInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getMaterialIconItem() {
		return materialIconItem;
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

	public EXGItemConfig getCancelItem() {
		return cancelItem;
	}

	public InventoryScheme getInventoryScheme() {
		return inventoryScheme;
	}


	// -------------------------------------------------- //


	public void setMaterialIconItem(EXGItemConfig materialIconItem) {
		this.materialIconItem = materialIconItem;
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

	public void setCancelItem(EXGItemConfig cancelItem) {
		this.cancelItem = cancelItem;
	}

	public void setInventoryScheme(InventoryScheme inventoryScheme) {
		this.inventoryScheme = inventoryScheme;
	}


	// -------------------------------------------------- //


	public EXGDataEntryGUInventoryConfig copy() {

		EXGDataEntryGUInventoryConfig copy = new EXGDataEntryGUInventoryConfig(
				this.getEXGTitle().getTitle(null),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setMaterialIconItem(this.getMaterialIconItem().duplicate());

		copy.setNextPageItem(this.getNextPageItem().duplicate());
		copy.setPreviousPageItem(this.getPreviousPageItem().duplicate());
		copy.setCurrentPageItem(this.getCurrentPageItem().duplicate());

		copy.setCancelItem(this.getCancelItem().duplicate());

		copy.setInventoryScheme(this.getInventoryScheme());
		return copy;
	}


	// -------------------------------------------------- //
}
