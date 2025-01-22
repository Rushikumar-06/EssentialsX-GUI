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
		return previewHomeItem.duplicate();
	}


	public EXGItemConfig getChangeDisplayNameItem() {
		return changeDisplayNameItem.duplicate();
	}


	public EXGItemConfig getChangeIconItem() {
		return changeIconItem.duplicate();
	}


	public EXGItemConfig getBackItem() {
		return backItem.duplicate();
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
