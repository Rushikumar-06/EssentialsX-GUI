package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;

public class EXGKitEditorInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig saveKitItem;
	private EXGItemConfig cancelChangesItem;


	// -------------------------------------------------- //


	public EXGKitEditorInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getSaveKitItem() {
		return saveKitItem;
	}
	public void setSaveKitItem(EXGItemConfig saveKitItem) {
		this.saveKitItem = saveKitItem;
	}


	// -------------------------------------------------- //


	public EXGItemConfig getCancelChangesItem() {
		return cancelChangesItem;
	}
	public void setCancelChangesItem(EXGItemConfig cancelChangesItem) {
		this.cancelChangesItem = cancelChangesItem;
	}


	// -------------------------------------------------- //


	public EXGKitEditorInventoryConfig copy() {

		EXGKitEditorInventoryConfig copy = new EXGKitEditorInventoryConfig(
				this.getEXGTitle().duplicate().getTitle(),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots());

		copy.setSaveKitItem(this.getSaveKitItem().duplicate());
		copy.setCancelChangesItem(this.getCancelChangesItem().duplicate());
		return copy;
	}


	// -------------------------------------------------- //
}
