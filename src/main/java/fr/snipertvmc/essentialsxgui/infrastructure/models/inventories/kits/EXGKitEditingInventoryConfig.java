package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;

public class EXGKitEditingInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig previewKitItem;

	private EXGItemConfig changeDisplayNameItem;
	private EXGItemConfig changeIconItem;

	private EXGItemConfig backItem;


	// -------------------------------------------------- //


	public EXGKitEditingInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getPreviewKitItem() {
		return previewKitItem;
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


	public void setPreviewKitItem(EXGItemConfig previewKitItem) {
		this.previewKitItem = previewKitItem;
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


	public EXGKitEditingInventoryConfig copy() {

		EXGKitEditingInventoryConfig copy = new EXGKitEditingInventoryConfig(
				this.getEXGTitle().duplicate().getTitle(),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots());

		copy.setPreviewKitItem(this.getPreviewKitItem().duplicate());
		copy.setChangeDisplayNameItem(this.getChangeDisplayNameItem().duplicate());
		copy.setChangeIconItem(this.getChangeIconItem().duplicate());
		copy.setBackItem(this.getBackItem().duplicate());

		return copy;
	}


	// -------------------------------------------------- //
}
