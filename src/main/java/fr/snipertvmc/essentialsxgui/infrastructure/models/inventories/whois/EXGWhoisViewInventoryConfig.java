package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.whois;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;

public class EXGWhoisViewInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig playerIdentificationItem;
	private EXGItemConfig playerStatisticsItem;
	private EXGItemConfig playerWorldItem;
	private EXGItemConfig playerServerDataItem;
	private EXGItemConfig playerPunishmentsItem;

	private EXGItemConfig nextPageItem;
	private EXGItemConfig previousPageItem;
	private EXGItemConfig currentPageItem;

	private EXGItemConfig backItem;

	private InventoryScheme inventoryScheme;


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


	public EXGWhoisViewInventoryConfig copy() {

		EXGWhoisViewInventoryConfig copy = new EXGWhoisViewInventoryConfig(
				this.getEXGTitle().getTitle(),
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

		return copy;
	}


	// -------------------------------------------------- //
}
