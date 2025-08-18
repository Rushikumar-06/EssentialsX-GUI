package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes;

import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;

public class EXGHomesInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig homeItem;
	private EXGItemConfig bedHomeItem;
	private EXGItemConfig noHomesItem;

	private EXGItemConfig createHomeItem;

	private EXGItemConfig nextPageItem;
	private EXGItemConfig previousPageItem;
	private EXGItemConfig currentPageItem;

	private EXGItemConfig closeItem;

	private InventoryScheme inventoryScheme;


	// -------------------------------------------------- //


	public EXGHomesInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getHomeItem() {
		return homeItem;
	}
	public EXGItemConfig getBedHomeItem() {
		return bedHomeItem;
	}
	public EXGItemConfig getNoHomesItem() {
		return noHomesItem;
	}

	public EXGItemConfig getCreateHomeItem() {
		return createHomeItem;
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


	public void setHomeItem(EXGItemConfig homeItem) {
		this.homeItem = homeItem;
	}
	public void setBedHomeItem(EXGItemConfig bedHomeItem) {
		this.bedHomeItem = bedHomeItem;
	}
	public void setNoHomesItem(EXGItemConfig noHomesItem) {
		this.noHomesItem = noHomesItem;
	}

	public void setCreateHomeItem(EXGItemConfig createHomeItem) {
		this.createHomeItem = createHomeItem;
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


	public EXGHomesInventoryConfig copy() {

		EXGHomesInventoryConfig copy = new EXGHomesInventoryConfig(
				this.getEXGTitle().getTitle(),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setHomeItem(this.getHomeItem().duplicate());
		copy.setBedHomeItem(this.getBedHomeItem().duplicate());
		copy.setNoHomesItem(this.getNoHomesItem().duplicate());

		copy.setCreateHomeItem(this.getCreateHomeItem().duplicate());

		copy.setNextPageItem(this.getNextPageItem().duplicate());
		copy.setPreviousPageItem(this.getPreviousPageItem().duplicate());
		copy.setCurrentPageItem(this.getCurrentPageItem().duplicate());

		copy.setCloseItem(this.getCloseItem().duplicate());

		copy.setInventoryScheme(this.getInventoryScheme());
		return copy;
	}


	// -------------------------------------------------- //
}
