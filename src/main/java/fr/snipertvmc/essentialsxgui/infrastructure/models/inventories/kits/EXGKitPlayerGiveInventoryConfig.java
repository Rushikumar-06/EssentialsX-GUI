package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;

public class EXGKitPlayerGiveInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig playerItem;

	private EXGItemConfig nextPageItem;
	private EXGItemConfig previousPageItem;
	private EXGItemConfig currentPageItem;

	private EXGItemConfig backItem;

	private InventoryScheme inventoryScheme;


	// -------------------------------------------------- //


	public EXGKitPlayerGiveInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getPlayerItem() {
		return playerItem;
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


	public EXGItemConfig getBackItem() {
		return backItem;
	}


	public InventoryScheme getInventoryScheme() {
		return inventoryScheme;
	}


	// -------------------------------------------------- //


	public void setPlayerItem(EXGItemConfig playerItem) {
		this.playerItem = playerItem;
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


	public void setBackItem(EXGItemConfig backItem) {
		this.backItem = backItem;
	}


	public void setInventoryScheme(InventoryScheme inventoryScheme) {
		this.inventoryScheme = inventoryScheme;
	}


	// -------------------------------------------------- //


	public EXGKitPlayerGiveInventoryConfig copy() {

		EXGKitPlayerGiveInventoryConfig copy = new EXGKitPlayerGiveInventoryConfig(
				this.getEXGTitle().getTitle(),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setPlayerItem(this.getPlayerItem().duplicate());
		copy.setNextPageItem(this.getNextPageItem().duplicate());
		copy.setPreviousPageItem(this.getPreviousPageItem().duplicate());
		copy.setCurrentPageItem(this.getCurrentPageItem().duplicate());
		copy.setBackItem(this.getBackItem().duplicate());

		copy.setInventoryScheme(this.getInventoryScheme());

		return copy;
	}


	// -------------------------------------------------- //
}
