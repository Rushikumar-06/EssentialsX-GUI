package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGPaginatedInventoryConfig;

public class EXGWarpsAdminViewInventoryConfig extends EXGPaginatedInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig warpItem;
	private EXGItemConfig noWarpsItem;

	private EXGItemConfig switchToPlayerModeItem;
	private EXGItemConfig createWarpItem;
	private EXGItemConfig searchWarpItem;
	private EXGItemConfig cancelSearchWarpItem;
	private EXGItemConfig noSearchWarpResultsItem;

	private EXGItemConfig closeItem;


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

	public EXGItemConfig getCloseItem() {
		return closeItem;
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

	public void setCloseItem(EXGItemConfig closeItem) {
		this.closeItem = closeItem;
	}


	// -------------------------------------------------- //


	public EXGWarpsAdminViewInventoryConfig copy() {

		EXGWarpsAdminViewInventoryConfig copy = new EXGWarpsAdminViewInventoryConfig(
				this.getEXGTitle().getTitle(null),
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

		copy.setCloseItem(this.getCloseItem().duplicate());

		copy.copyPaginatedInventoryConfig(this);
		return copy;
	}


	// -------------------------------------------------- //
}
