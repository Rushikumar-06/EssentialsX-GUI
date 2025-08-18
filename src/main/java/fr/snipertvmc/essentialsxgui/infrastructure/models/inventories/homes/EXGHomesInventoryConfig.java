package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes;

import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import org.apache.commons.lang3.tuple.ImmutablePair;

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

	private ImmutablePair<String, String> bedHomeItemOverworld;
	private ImmutablePair<String, String> bedHomeItemNether;
	private ImmutablePair<String, String> bedHomeItemNotSet;


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

	public void setBedHomeItemOverworldMaterial(String bedHomeItemOverworldMaterial, String bedHomeItemOverworldDisplayName) {
		this.bedHomeItemOverworld = ImmutablePair.of(bedHomeItemOverworldMaterial, bedHomeItemOverworldDisplayName);
	}
	public void setBedHomeItemNetherMaterial(String bedHomeItemNetherMaterial, String bedHomeItemNetherDisplayName) {
		this.bedHomeItemNether = ImmutablePair.of(bedHomeItemNetherMaterial, bedHomeItemNetherDisplayName);
	}
	public void setBedHomeItemNotSetMaterial(String bedHomeItemNotSetMaterial, String bedHomeItemNotSetDisplayName) {
		this.bedHomeItemNotSet = ImmutablePair.of(bedHomeItemNotSetMaterial, bedHomeItemNotSetDisplayName);
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

		copy.setBedHomeItemOverworldMaterial(this.getBedHomeItemOverworldMaterial(), this.getBedHomeItemOverworldDisplayName());
		copy.setBedHomeItemNetherMaterial(this.getBedHomeItemNetherMaterial(), this.getBedHomeItemNetherDisplayName());
		copy.setBedHomeItemNotSetMaterial(this.getBedHomeItemNotSetMaterial(), this.getBedHomeItemNotSetDisplayName());
		return copy;
	}


	// -------------------------------------------------- //
}
