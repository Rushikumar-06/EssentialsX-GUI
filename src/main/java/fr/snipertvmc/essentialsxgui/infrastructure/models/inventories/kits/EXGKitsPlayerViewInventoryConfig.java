package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGPaginatedInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;

public class EXGKitsPlayerViewInventoryConfig extends EXGPaginatedInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig kitItem;
	private EXGItemConfig noKitsItem;

	private EXGItemConfig switchToAdminModeItem;
	private EXGItemConfig searchKitItem;
	private EXGItemConfig cancelSearchKitItem;
	private EXGItemConfig noSearchKitResultsItem;

	private EXGItemConfig closeItem;


	// -------------------------------------------------- //


	public EXGKitsPlayerViewInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getKitItem() {
		return kitItem;
	}
	public EXGItemConfig getNoKitsItem() {
		return noKitsItem;
	}

	public EXGItemConfig getSwitchToAdminModeItem() {
		return switchToAdminModeItem;
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

	public void setSwitchToAdminModeItem(EXGItemConfig switchToAdminModeItem) {
		this.switchToAdminModeItem = switchToAdminModeItem;
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


	public EXGKitsPlayerViewInventoryConfig copy() {

		EXGKitsPlayerViewInventoryConfig copy = new EXGKitsPlayerViewInventoryConfig(
				this.getEXGTitle().getTitle(null),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setKitItem(this.getKitItem().duplicate());
		copy.setNoKitsItem(this.getNoKitsItem().duplicate());

		copy.setSwitchToAdminModeItem(this.getSwitchToAdminModeItem().duplicate());
		copy.setSearchKitItem(this.getSearchKitItem().duplicate());
		copy.setCancelSearchKitItem(this.getCancelSearchKitItem().duplicate());
		copy.setNoSearchKitResultsItem(this.getNoSearchKitResultsItem().duplicate());

		copy.setCloseItem(this.getCloseItem().duplicate());

		copy.copyPaginatedInventoryConfig(this);
		return copy;
	}


	// -------------------------------------------------- //
}
