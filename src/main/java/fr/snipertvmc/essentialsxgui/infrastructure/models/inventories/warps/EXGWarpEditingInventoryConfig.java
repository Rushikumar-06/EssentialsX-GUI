package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;

public class EXGWarpEditingInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig previewWarpItem;

	private EXGItemConfig changeDisplayNameItem;
	private EXGItemConfig changeIconItem;
	private EXGItemConfig deleteWarpItem;

	private EXGItemConfig backItem;


	// -------------------------------------------------- //


	public EXGWarpEditingInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getPreviewWarpItem() {
		return previewWarpItem;
	}

	public EXGItemConfig getChangeDisplayNameItem() {
		return changeDisplayNameItem;
	}
	public EXGItemConfig getChangeIconItem() {
		return changeIconItem;
	}
	public EXGItemConfig getDeleteWarpItem() {
		return deleteWarpItem;
	}

	public EXGItemConfig getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //


	public void setPreviewWarpItem(EXGItemConfig previewWarpItem) {
		this.previewWarpItem = previewWarpItem;
	}

	public void setChangeDisplayNameItem(EXGItemConfig changeDisplayNameItem) {
		this.changeDisplayNameItem = changeDisplayNameItem;
	}
	public void setChangeIconItem(EXGItemConfig changeIconItem) {
		this.changeIconItem = changeIconItem;
	}
	public void setDeleteWarpItem(EXGItemConfig deleteWarpItem) {
		this.deleteWarpItem = deleteWarpItem;
	}

	public void setBackItem(EXGItemConfig backItem) {
		this.backItem = backItem;
	}


	// -------------------------------------------------- //


	public EXGWarpEditingInventoryConfig copy() {

		EXGWarpEditingInventoryConfig copy = new EXGWarpEditingInventoryConfig(
				this.getEXGTitle().duplicate().getTitle(),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots());

		copy.setPreviewWarpItem(this.getPreviewWarpItem().duplicate());

		copy.setChangeDisplayNameItem(this.getChangeDisplayNameItem().duplicate());
		copy.setChangeIconItem(this.getChangeIconItem().duplicate());
		copy.setDeleteWarpItem(this.getDeleteWarpItem().duplicate());

		copy.setBackItem(this.getBackItem().duplicate());
		return copy;
	}


	// -------------------------------------------------- //
}
