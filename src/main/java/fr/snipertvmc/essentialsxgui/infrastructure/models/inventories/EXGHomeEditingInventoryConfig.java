package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;

public class EXGHomeEditingInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig previewHomeItem;

	private EXGItemConfig changeDisplayNameItem;
	private EXGItemConfig changeIconItem;

	private EXGItemConfig backItem;


	// -------------------------------------------------- //


	public EXGHomeEditingInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getPreviewHomeItem() {
		return previewHomeItem;
	}


	public EXGItemConfig getChangeDisplayNameItem() {
		return changeDisplayNameItem;
	}


	public EXGItemConfig getChangeIconItem() {
		return changeIconItem;
	}


	public EXGItemConfig getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //


	public void setPreviewHomeItem(EXGItemConfig previewHomeItem) {
		this.previewHomeItem = previewHomeItem;
	}


	public void setChangeDisplayNameItem(EXGItemConfig changeDisplayNameItem) {
		this.changeDisplayNameItem = changeDisplayNameItem;
	}


	public void setChangeIconItem(EXGItemConfig changeIconItem) {
		this.changeIconItem = changeIconItem;
	}


	public void setBackItem(EXGItemConfig backItem) {
		this.backItem = backItem;
	}


	// -------------------------------------------------- //
}
