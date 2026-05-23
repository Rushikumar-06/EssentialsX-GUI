package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGPaginatedInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;

public class EXGKitsAdminViewInventoryConfig extends EXGPaginatedInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig kitItem;
	private EXGItemConfig noKitsItem;

	private EXGItemConfig switchToPlayerModeItem;
	private EXGItemConfig createKitItem;
	private EXGItemConfig searchKitItem;
	private EXGItemConfig cancelSearchKitItem;
	private EXGItemConfig noSearchKitResultsItem;

	private EXGItemConfig closeItem;


	// -------------------------------------------------- //


	public EXGKitsAdminViewInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getKitItem() {
		return kitItem;
	}
	public EXGItemConfig getNoKitsItem() {
		return noKitsItem;
	}

	public EXGItemConfig getSwitchToPlayerModeItem() {
		return switchToPlayerModeItem;
	}
	public EXGItemConfig getCreateKitItem() {
		return createKitItem;
	}
	public EXGItemConfig getSearchKitItem() {
		return searchKitItem;
	}
	public EXGItemConfig getCancelSearchKitItem() {
		return cancelSearchKitItem;
	}
	public EXGItemConfig getNoSearchKitResultsItem() {
		return noSearchKitResultsItem;
	}

	public EXGItemConfig getCloseItem() {
		return closeItem;
	}


	// -------------------------------------------------- //


	public void setKitItem(EXGItemConfig kitItem) {
		this.kitItem = kitItem;
	}
	public void setNoKitsItem(EXGItemConfig noKitsItem) {
		this.noKitsItem = noKitsItem;
	}

	public void setSwitchToPlayerModeItem(EXGItemConfig switchToPlayerModeItem) {
		this.switchToPlayerModeItem = switchToPlayerModeItem;
	}
	public void setCreateKitItem(EXGItemConfig createKitItem) {
		this.createKitItem = createKitItem;
	}
	public void setSearchKitItem(EXGItemConfig searchKitItem) {
		this.searchKitItem = searchKitItem;
	}
	public void setCancelSearchKitItem(EXGItemConfig cancelSearchKitItem) {
		this.cancelSearchKitItem = cancelSearchKitItem;
	}
	public void setNoSearchKitResultsItem(EXGItemConfig noSearchKitResultsItem) {
		this.noSearchKitResultsItem = noSearchKitResultsItem;
	}

	public void setCloseItem(EXGItemConfig closeItem) {
		this.closeItem = closeItem;
	}


	// -------------------------------------------------- //


	public EXGKitsAdminViewInventoryConfig copy() {

		EXGKitsAdminViewInventoryConfig copy = new EXGKitsAdminViewInventoryConfig(
				this.getEXGTitle().getTitle(null),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setKitItem(this.getKitItem().duplicate());
		copy.setNoKitsItem(this.getNoKitsItem().duplicate());

		copy.setCreateKitItem(this.getCreateKitItem().duplicate());
		copy.setSwitchToPlayerModeItem(this.getSwitchToPlayerModeItem().duplicate());
		copy.setSearchKitItem(this.getSearchKitItem().duplicate());
		copy.setCancelSearchKitItem(this.getCancelSearchKitItem().duplicate());
		copy.setNoSearchKitResultsItem(this.getNoSearchKitResultsItem().duplicate());

		copy.setCloseItem(this.getCloseItem().duplicate());

		copy.copyPaginatedInventoryConfig(this);
		return copy;
	}


	// -------------------------------------------------- //
}
