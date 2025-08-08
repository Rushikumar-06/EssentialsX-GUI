package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits;

import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;

public class EXGKitsAdminViewInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig kitItem;
	private EXGItemConfig noKitsItem;

	private EXGItemConfig switchToPlayerModeItem;
	private EXGItemConfig createKitItem;

	private EXGItemConfig nextPageItem;
	private EXGItemConfig previousPageItem;
	private EXGItemConfig currentPageItem;

	private EXGItemConfig closeItem;

	private InventoryScheme inventoryScheme;


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

	public EXGItemConfig getCreateKitItem() {
		return createKitItem;
	}
	public EXGItemConfig getSwitchToPlayerModeItem() {
		return switchToPlayerModeItem;
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

	public void setCreateKitItem(EXGItemConfig createKitItem) {
		this.createKitItem = createKitItem;
	}
	public void setSwitchToPlayerModeItem(EXGItemConfig switchToPlayerModeItem) {
		this.switchToPlayerModeItem = switchToPlayerModeItem;
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


	public EXGKitsAdminViewInventoryConfig copy() {

		EXGKitsAdminViewInventoryConfig copy = new EXGKitsAdminViewInventoryConfig(
				this.getEXGTitle().getTitle(),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setKitItem(this.getKitItem().duplicate());
		copy.setNoKitsItem(this.getNoKitsItem().duplicate());

		copy.setCreateKitItem(this.getCreateKitItem().duplicate());
		copy.setSwitchToPlayerModeItem(this.getSwitchToPlayerModeItem().duplicate());

		copy.setNextPageItem(this.getNextPageItem().duplicate());
		copy.setPreviousPageItem(this.getPreviousPageItem().duplicate());
		copy.setCurrentPageItem(this.getCurrentPageItem().duplicate());

		copy.setCloseItem(this.getCloseItem().duplicate());

		copy.setInventoryScheme(this.getInventoryScheme());
		return copy;
	}


	// -------------------------------------------------- //
}
