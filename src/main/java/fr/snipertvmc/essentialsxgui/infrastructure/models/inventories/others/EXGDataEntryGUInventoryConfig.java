package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.others;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGPaginatedInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;

public class EXGDataEntryGUInventoryConfig extends EXGPaginatedInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig materialIconItem;

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
