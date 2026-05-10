package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.whois;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGPaginatedInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;

public class EXGWhoisViewInventoryConfig extends EXGPaginatedInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig playerIdentificationItem;
	private EXGItemConfig playerStatisticsItem;
	private EXGItemConfig playerWorldItem;
	private EXGItemConfig playerServerDataItem;
	private EXGItemConfig playerPunishmentsItem;

	private EXGItemConfig backItem;

	private InventoryScheme inventoryScheme;

	private boolean onlyUsePlaceholderAPI;


	// -------------------------------------------------- //


	public EXGWhoisViewInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getPlayerIdentificationItem() {
		return playerIdentificationItem;
	}
	public EXGItemConfig getPlayerStatisticsItem() {
		return playerStatisticsItem;
	}
	public EXGItemConfig getPlayerWorldItem() {
		return playerWorldItem;
	}
	public EXGItemConfig getPlayerServerDataItem() {
		return playerServerDataItem;
	}
	public EXGItemConfig getPlayerPunishmentsItem() {
		return playerPunishmentsItem;
	}

	public EXGItemConfig getBackItem() {
		return backItem;
	}

	public InventoryScheme getInventoryScheme() {
		return inventoryScheme;
	}

	public boolean isOnlyUsePlaceholderAPI() {
		return onlyUsePlaceholderAPI;
	}


	// -------------------------------------------------- //


	public void setPlayerIdentificationItem(EXGItemConfig playerIdentificationItem) {
		this.playerIdentificationItem = playerIdentificationItem;
	}
	public void setPlayerStatisticsItem(EXGItemConfig playerStatisticsItem) {
		this.playerStatisticsItem = playerStatisticsItem;
	}
	public void setPlayerWorldItem(EXGItemConfig playerWorldItem) {
		this.playerWorldItem = playerWorldItem;
	}
	public void setPlayerServerDataItem(EXGItemConfig playerServerDataItem) {
		this.playerServerDataItem = playerServerDataItem;
	}
	public void setPlayerPunishmentsItem(EXGItemConfig playerPunishmentsItem) {
		this.playerPunishmentsItem = playerPunishmentsItem;
	}

	public void setBackItem(EXGItemConfig backItem) {
		this.backItem = backItem;
	}

	public void setInventoryScheme(InventoryScheme inventoryScheme) {
		this.inventoryScheme = inventoryScheme;
	}

	public void setOnlyUsePlaceholderAPI(boolean onlyUsePlaceholderAPI) {
		this.onlyUsePlaceholderAPI = onlyUsePlaceholderAPI;
	}


	// -------------------------------------------------- //


	public EXGWhoisViewInventoryConfig copy() {

		EXGWhoisViewInventoryConfig copy = new EXGWhoisViewInventoryConfig(
				this.getEXGTitle().getTitle(null),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setPlayerIdentificationItem(this.getPlayerIdentificationItem().duplicate());
		copy.setPlayerStatisticsItem(this.getPlayerStatisticsItem().duplicate());
		copy.setPlayerWorldItem(this.getPlayerWorldItem().duplicate());
		copy.setPlayerServerDataItem(this.getPlayerServerDataItem().duplicate());
		copy.setPlayerPunishmentsItem(this.getPlayerPunishmentsItem().duplicate());

		copy.setNextPageItem(this.getNextPageItem().duplicate());
		copy.setPreviousPageItem(this.getPreviousPageItem().duplicate());
		copy.setCurrentPageItem(this.getCurrentPageItem().duplicate());

		copy.setBackItem(this.getBackItem().duplicate());

		copy.setInventoryScheme(this.getInventoryScheme());

		copy.setOnlyUsePlaceholderAPI(this.onlyUsePlaceholderAPI);

		return copy;
	}


	// -------------------------------------------------- //
}
