package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure;

import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;

public class EXGPaginatedInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private InventoryScheme inventoryScheme;

	private EXGItemConfig nextPageItem;
	private EXGItemConfig previousPageItem;
	private EXGItemConfig currentPageItem;


	// -------------------------------------------------- //


	public EXGPaginatedInventoryConfig(String title, int rows, EXGItemConfig borderItem, int... borderSlots) {
		super(title, rows, borderItem, borderSlots);
	}


	// -------------------------------------------------- //


	public InventoryScheme getInventoryScheme() {
		return inventoryScheme;
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


	// -------------------------------------------------- //


	public void setInventoryScheme(InventoryScheme inventoryScheme) {
		this.inventoryScheme = inventoryScheme;
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


	// -------------------------------------------------- //


	public void copyPaginatedInventoryConfig(EXGPaginatedInventoryConfig source) {
		this.setInventoryScheme(source.getInventoryScheme());

		this.setNextPageItem(source.getNextPageItem().duplicate());
		this.setPreviousPageItem(source.getPreviousPageItem().duplicate());
		this.setCurrentPageItem(source.getCurrentPageItem().duplicate());
	}


	// -------------------------------------------------- //
}
