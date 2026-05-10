package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.whois;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGPaginatedInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;

public class EXGWhoisPlayersInventoryConfig extends EXGPaginatedInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig playerItem;

	private EXGItemConfig closeItem;

	private InventoryScheme inventoryScheme;


	// -------------------------------------------------- //


	public EXGWhoisPlayersInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getPlayerItem() {
		return playerItem;
	}

	public EXGItemConfig getCloseItem() {
		return closeItem;
	}

	public InventoryScheme getInventoryScheme() {
		return inventoryScheme;
	}


	// -------------------------------------------------- //


	public void setPlayerItem(EXGItemConfig playerItem) {
		this.playerItem = playerItem;
	}

	public void setCloseItem(EXGItemConfig closeItem) {
		this.closeItem = closeItem;
	}

	public void setInventoryScheme(InventoryScheme inventoryScheme) {
		this.inventoryScheme = inventoryScheme;
	}


	// -------------------------------------------------- //


	public EXGWhoisPlayersInventoryConfig copy() {

		EXGWhoisPlayersInventoryConfig copy = new EXGWhoisPlayersInventoryConfig(
				this.getEXGTitle().getTitle(null),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setPlayerItem(this.getPlayerItem().duplicate());

		copy.setNextPageItem(this.getNextPageItem().duplicate());
		copy.setPreviousPageItem(this.getPreviousPageItem().duplicate());
		copy.setCurrentPageItem(this.getCurrentPageItem().duplicate());

		copy.setCloseItem(this.getCloseItem().duplicate());

		copy.setInventoryScheme(this.getInventoryScheme());
		return copy;
	}


	// -------------------------------------------------- //
}
