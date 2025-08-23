package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits;

import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;

public class EXGKitsPlayerViewInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig kitItem;
	private EXGItemConfig noKitsItem;

	private EXGItemConfig switchToAdminModeItem;
	private EXGItemConfig searchKitItem;
	private EXGItemConfig cancelSearchKitItem;
	private EXGItemConfig noSearchKitResultsItem;

	private EXGItemConfig nextPageItem;
	private EXGItemConfig previousPageItem;
	private EXGItemConfig currentPageItem;

	private EXGItemConfig closeItem;

	private InventoryScheme inventoryScheme;


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


	public EXGKitsPlayerViewInventoryConfig copy() {

		EXGKitsPlayerViewInventoryConfig copy = new EXGKitsPlayerViewInventoryConfig(
				this.getEXGTitle().getTitle(),
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

		copy.setNextPageItem(this.getNextPageItem().duplicate());
		copy.setPreviousPageItem(this.getPreviousPageItem().duplicate());
		copy.setCurrentPageItem(this.getCurrentPageItem().duplicate());

		copy.setCloseItem(this.getCloseItem().duplicate());

		copy.setInventoryScheme(this.getInventoryScheme());

		return copy;
	}


	// -------------------------------------------------- //
}
