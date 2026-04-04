package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGPaginatedInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;

public class EXGHomesInventoryConfig extends EXGPaginatedInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig homeItem;
	private EXGItemConfig bedHomeItem;
	private EXGItemConfig noHomesItem;

	private EXGItemConfig createHomeItem;
	private EXGItemConfig searchHomeItem;
	private EXGItemConfig cancelSearchHomeItem;
	private EXGItemConfig noSearchHomeResultsItem;

	private EXGItemConfig closeItem;

	private InventoryScheme inventoryScheme;

	private Pair<String, String> bedHomeItemOverworld;
	private Pair<String, String> bedHomeItemNether;
	private Pair<String, String> bedHomeItemNotSet;


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
	public EXGItemConfig getSearchHomeItem() {
		return searchHomeItem;
	}
	public EXGItemConfig getCancelSearchHomeItem() {
		return cancelSearchHomeItem;
	}
	public EXGItemConfig getNoSearchHomeResultsItem() {
		return noSearchHomeResultsItem;
	}

	public EXGItemConfig getCloseItem() {
		return closeItem;
	}

	public InventoryScheme getInventoryScheme() {
		return inventoryScheme;
	}

	public String getBedHomeItemOverworldMaterial() {
		return bedHomeItemOverworld.getLeft();
	}
	public String getBedHomeItemNetherMaterial() {
		return bedHomeItemNether.getLeft();
	}
	public String getBedHomeItemNotSetMaterial() {
		return bedHomeItemNotSet.getLeft();
	}
	public String getBedHomeItemOverworldDisplayName() {
		return bedHomeItemOverworld.getRight();
	}
	public String getBedHomeItemNetherDisplayName() {
		return bedHomeItemNether.getRight();
	}
	public String getBedHomeItemNotSetDisplayName() {
		return bedHomeItemNotSet.getRight();
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
	public void setSearchHomeItem(EXGItemConfig searchHomeItem) {
		this.searchHomeItem = searchHomeItem;
	}
	public void setCancelSearchHomeItem(EXGItemConfig cancelSearchHomeItem) {
		this.cancelSearchHomeItem = cancelSearchHomeItem;
	}
	public void setNoSearchHomeResultsItem(EXGItemConfig noSearchHomeResultsItem) {
		this.noSearchHomeResultsItem = noSearchHomeResultsItem;
	}

	public void setCloseItem(EXGItemConfig closeItem) {
		this.closeItem = closeItem;
	}

	public void setInventoryScheme(InventoryScheme inventoryScheme) {
		this.inventoryScheme = inventoryScheme;
	}

	public void setBedHomeItemOverworldMaterial(String bedHomeItemOverworldMaterial, String bedHomeItemOverworldDisplayName) {
		this.bedHomeItemOverworld = Pair.of(bedHomeItemOverworldMaterial, bedHomeItemOverworldDisplayName);
	}
	public void setBedHomeItemNetherMaterial(String bedHomeItemNetherMaterial, String bedHomeItemNetherDisplayName) {
		this.bedHomeItemNether = Pair.of(bedHomeItemNetherMaterial, bedHomeItemNetherDisplayName);
	}
	public void setBedHomeItemNotSetMaterial(String bedHomeItemNotSetMaterial, String bedHomeItemNotSetDisplayName) {
		this.bedHomeItemNotSet = Pair.of(bedHomeItemNotSetMaterial, bedHomeItemNotSetDisplayName);
	}


	// -------------------------------------------------- //


	public EXGHomesInventoryConfig copy() {

		EXGHomesInventoryConfig copy = new EXGHomesInventoryConfig(
				this.getEXGTitle().getTitle(null),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setHomeItem(this.getHomeItem().duplicate());
		copy.setBedHomeItem(this.getBedHomeItem().duplicate());
		copy.setNoHomesItem(this.getNoHomesItem().duplicate());

		copy.setCreateHomeItem(this.getCreateHomeItem().duplicate());
		copy.setSearchHomeItem(this.getSearchHomeItem().duplicate());
		copy.setCancelSearchHomeItem(this.getCancelSearchHomeItem().duplicate());
		copy.setNoSearchHomeResultsItem(this.getNoSearchHomeResultsItem().duplicate());

		copy.setNextPageItem(this.getNextPageItem().duplicate());
		copy.setPreviousPageItem(this.getPreviousPageItem().duplicate());
		copy.setCurrentPageItem(this.getCurrentPageItem().duplicate());

		copy.setCloseItem(this.getCloseItem().duplicate());

		copy.setInventoryScheme(this.getInventoryScheme());

		copy.setBedHomeItemOverworldMaterial(this.getBedHomeItemOverworldMaterial(), this.getBedHomeItemOverworldDisplayName());
		copy.setBedHomeItemNetherMaterial(this.getBedHomeItemNetherMaterial(), this.getBedHomeItemNetherDisplayName());
		copy.setBedHomeItemNotSetMaterial(this.getBedHomeItemNotSetMaterial(), this.getBedHomeItemNotSetDisplayName());
		return copy;
	}


	// -------------------------------------------------- //
}
