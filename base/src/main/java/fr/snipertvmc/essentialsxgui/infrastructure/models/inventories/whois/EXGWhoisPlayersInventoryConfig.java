package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.whois;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGPaginatedInventoryConfig;

public class EXGWhoisPlayersInventoryConfig extends EXGPaginatedInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig playerItem;

	private EXGItemConfig closeItem;


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


	// -------------------------------------------------- //


	public void setPlayerItem(EXGItemConfig playerItem) {
		this.playerItem = playerItem;
	}

	public void setCloseItem(EXGItemConfig closeItem) {
		this.closeItem = closeItem;
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

		copy.setCloseItem(this.getCloseItem().duplicate());

		copy.copyPaginatedInventoryConfig(this);
		return copy;
	}


	// -------------------------------------------------- //
}
