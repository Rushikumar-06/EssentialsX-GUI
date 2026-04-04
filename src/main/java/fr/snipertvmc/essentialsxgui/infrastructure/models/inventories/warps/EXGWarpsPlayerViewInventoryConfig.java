package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGPaginatedInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;

public class EXGWarpsPlayerViewInventoryConfig extends EXGPaginatedInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig warpItem;
	private EXGItemConfig noWarpsItem;

	private EXGItemConfig switchToAdminModeItem;
	private EXGItemConfig searchWarpItem;
	private EXGItemConfig cancelSearchWarpItem;
	private EXGItemConfig noSearchWarpResultsItem;

	private EXGItemConfig nextPageItem;
	private EXGItemConfig previousPageItem;
	private EXGItemConfig currentPageItem;

	private EXGItemConfig closeItem;

	private InventoryScheme inventoryScheme;


	// -------------------------------------------------- //


	public EXGWarpsPlayerViewInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getWarpItem() {
		return warpItem;
	}
	public EXGItemConfig getNoWarpsItem() {
		return noWarpsItem;
	}

	public EXGItemConfig getSwitchToAdminModeItem() {
		return switchToAdminModeItem;
	}
	public EXGItemConfig getSearchWarpItem() {
		return searchWarpItem;
	}
	public EXGItemConfig getCancelSearchWarpItem() {
		return cancelSearchWarpItem;
	}
	public EXGItemConfig getNoSearchWarpResultsItem() {
		return noSearchWarpResultsItem;
	}

	public EXGItemConfig getCloseItem() {
		return closeItem;
	}

	public InventoryScheme getInventoryScheme() {
		return inventoryScheme;
	}


	// -------------------------------------------------- //


	public void setWarpItem(EXGItemConfig warpItem) {
		this.warpItem = warpItem;
	}
	public void setNoWarpsItem(EXGItemConfig noWarpsItem) {
		this.noWarpsItem = noWarpsItem;
	}

	public void setSwitchToAdminModeItem(EXGItemConfig switchToAdminModeItem) {
		this.switchToAdminModeItem = switchToAdminModeItem;
	}
	public void setSearchWarpItem(EXGItemConfig searchWarpItem) {
		this.searchWarpItem = searchWarpItem;
	}
	public void setCancelSearchWarpItem(EXGItemConfig cancelSearchWarpItem) {
		this.cancelSearchWarpItem = cancelSearchWarpItem;
	}
	public void setNoSearchWarpResultsItem(EXGItemConfig noSearchWarpResultsItem) {
		this.noSearchWarpResultsItem = noSearchWarpResultsItem;
	}

	public void setCloseItem(EXGItemConfig closeItem) {
		this.closeItem = closeItem;
	}

	public void setInventoryScheme(InventoryScheme inventoryScheme) {
		this.inventoryScheme = inventoryScheme;
	}


	// -------------------------------------------------- //


	public EXGWarpsPlayerViewInventoryConfig copy() {

		EXGWarpsPlayerViewInventoryConfig copy = new EXGWarpsPlayerViewInventoryConfig(
				this.getEXGTitle().getTitle(null),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setWarpItem(this.getWarpItem().duplicate());
		copy.setNoWarpsItem(this.getNoWarpsItem().duplicate());

		copy.setSwitchToAdminModeItem(this.getSwitchToAdminModeItem().duplicate());
		copy.setSearchWarpItem(this.getSearchWarpItem().duplicate());
		copy.setCancelSearchWarpItem(this.getCancelSearchWarpItem().duplicate());
		copy.setNoSearchWarpResultsItem(this.getNoSearchWarpResultsItem().duplicate());

		copy.setNextPageItem(this.getNextPageItem().duplicate());
		copy.setPreviousPageItem(this.getPreviousPageItem().duplicate());
		copy.setCurrentPageItem(this.getCurrentPageItem().duplicate());

		copy.setCloseItem(this.getCloseItem().duplicate());

		copy.setInventoryScheme(this.getInventoryScheme());

		return copy;
	}


	// -------------------------------------------------- //
}
