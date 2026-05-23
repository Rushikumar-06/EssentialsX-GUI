package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGPaginatedInventoryConfig;

public class EXGWorthInventoryInventoryConfig extends EXGPaginatedInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig worthItem;
	private EXGItemConfig emptyInventoryItem;

	private EXGItemConfig backItem;


	// -------------------------------------------------- //


	public EXGWorthInventoryInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getWorthItem() {
		return worthItem;
	}
	public EXGItemConfig getEmptyInventoryItem() {
		return emptyInventoryItem;
	}

	public EXGItemConfig getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //


	public void setWorthItem(EXGItemConfig worthItem) {
		this.worthItem = worthItem;
	}
	public void setEmptyInventoryItem(EXGItemConfig emptyInventoryItem) {
		this.emptyInventoryItem = emptyInventoryItem;
	}

	public void setBackItem(EXGItemConfig backItem) {
		this.backItem = backItem;
	}


	// -------------------------------------------------- //


	public EXGWorthInventoryInventoryConfig copy() {

		EXGWorthInventoryInventoryConfig copy = new EXGWorthInventoryInventoryConfig(
				this.getEXGTitle().getTitle(null),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setWorthItem(this.getWorthItem().duplicate());
		copy.setEmptyInventoryItem(this.getEmptyInventoryItem().duplicate());

		copy.setBackItem(this.getBackItem().duplicate());

		copy.copyPaginatedInventoryConfig(this);
		return copy;
	}


	// -------------------------------------------------- //
}
