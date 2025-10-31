package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;

public class EXGWarpsAdminViewInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig warpItem;
	private EXGItemConfig noWarpsItem;

	private EXGItemConfig switchToPlayerModeItem;
	private EXGItemConfig createWarpItem;
	private EXGItemConfig searchWarpItem;
	private EXGItemConfig cancelSearchWarpItem;
	private EXGItemConfig noSearchWarpResultsItem;

	private EXGItemConfig nextPageItem;
	private EXGItemConfig previousPageItem;
	private EXGItemConfig currentPageItem;

	private EXGItemConfig closeItem;

	private InventoryScheme inventoryScheme;


	// -------------------------------------------------- //


	public EXGWarpsAdminViewInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getWarpItem() {
		return warpItem;
	}
	public EXGItemConfig getNoWarpsItem() {
		return noWarpsItem;
	}

	public EXGItemConfig getSwitchToPlayerModeItem() {
		return switchToPlayerModeItem;
	}
	public EXGItemConfig getCreateWarpItem() {
		return createWarpItem;
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


	public void setWarpItem(EXGItemConfig warpItem) {
		this.warpItem = warpItem;
	}
	public void setNoWarpsItem(EXGItemConfig noWarpsItem) {
		this.noWarpsItem = noWarpsItem;
	}

	public void setSwitchToPlayerModeItem(EXGItemConfig switchToPlayerModeItem) {
		this.switchToPlayerModeItem = switchToPlayerModeItem;
	}
	public void setCreateWarpItem(EXGItemConfig createWarpItem) {
		this.createWarpItem = createWarpItem;
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


	public EXGWarpsAdminViewInventoryConfig copy() {

		EXGWarpsAdminViewInventoryConfig copy = new EXGWarpsAdminViewInventoryConfig(
				this.getEXGTitle().getTitle(),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setWarpItem(this.getWarpItem().duplicate());
		copy.setNoWarpsItem(this.getNoWarpsItem().duplicate());

		copy.setCreateWarpItem(this.getCreateWarpItem().duplicate());
		copy.setSwitchToPlayerModeItem(this.getSwitchToPlayerModeItem().duplicate());
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
