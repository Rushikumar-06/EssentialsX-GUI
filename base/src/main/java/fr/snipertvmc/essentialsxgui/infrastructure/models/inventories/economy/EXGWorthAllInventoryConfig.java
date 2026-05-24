package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGPaginatedInventoryConfig;

public class EXGWorthAllInventoryConfig extends EXGPaginatedInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig worthItem;
	private EXGItemConfig noWorthItem;

	private EXGItemConfig searchWorthItem;
	private EXGItemConfig cancelSearchWorthItem;
	private EXGItemConfig noSearchWorthResultsItem;

	private EXGItemConfig backItem;


	// -------------------------------------------------- //


	public EXGWorthAllInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getWorthItem() {
		return worthItem;
	}
	public EXGItemConfig getNoWorthItem() {
		return noWorthItem;
	}

	public EXGItemConfig getSearchWorthItem() {
		return searchWorthItem;
	}
	public EXGItemConfig getCancelSearchWorthItem() {
		return cancelSearchWorthItem;
	}
	public EXGItemConfig getNoSearchWorthItemsItem() {
		return noSearchWorthResultsItem;
	}

	public EXGItemConfig getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //


	public void setWorthItem(EXGItemConfig worthItem) {
		this.worthItem = worthItem;
	}
	public void setNoWorthItem(EXGItemConfig noWorthItem) {
		this.noWorthItem = noWorthItem;
	}

	public void setSearchWorthItem(EXGItemConfig searchWorthItem) {
		this.searchWorthItem = searchWorthItem;
	}
	public void setCancelSearchWorthItem(EXGItemConfig cancelSearchWorthItem) {
		this.cancelSearchWorthItem = cancelSearchWorthItem;
	}
	public void setNoSearchWorthResultsItem(EXGItemConfig noSearchWorthResultsItem) {
		this.noSearchWorthResultsItem = noSearchWorthResultsItem;
	}

	public void setBackItem(EXGItemConfig backItem) {
		this.backItem = backItem;
	}


	// -------------------------------------------------- //


	public EXGWorthAllInventoryConfig copy() {

		EXGWorthAllInventoryConfig copy = new EXGWorthAllInventoryConfig(
				this.getEXGTitle().getTitle(null),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setWorthItem(this.getWorthItem().duplicate());
		copy.setNoWorthItem(this.getNoWorthItem().duplicate());

		copy.setSearchWorthItem(this.getSearchWorthItem().duplicate());
		copy.setCancelSearchWorthItem(this.getCancelSearchWorthItem().duplicate());
		copy.setNoSearchWorthResultsItem(this.getNoSearchWorthItemsItem().duplicate());

		copy.setBackItem(this.getBackItem().duplicate());

		copy.copyPaginatedInventoryConfig(this);
		return copy;
	}


	// -------------------------------------------------- //
}
