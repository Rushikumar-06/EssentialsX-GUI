package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.others;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGPaginatedInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;

public class EXGDataEntryGUInventoryConfig extends EXGPaginatedInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig materialIconItem;

	private EXGItemConfig cancelItem;


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


	// -------------------------------------------------- //


	public void setMaterialIconItem(EXGItemConfig materialIconItem) {
		this.materialIconItem = materialIconItem;
	}

	public void setCancelItem(EXGItemConfig cancelItem) {
		this.cancelItem = cancelItem;
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

		copy.setCancelItem(this.getCancelItem().duplicate());

		copy.copyPaginatedInventoryConfig(this);
		return copy;
	}


	// -------------------------------------------------- //
}
