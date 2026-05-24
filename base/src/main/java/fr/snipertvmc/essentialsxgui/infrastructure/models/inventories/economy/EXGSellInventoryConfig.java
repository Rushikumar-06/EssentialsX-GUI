package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;

public class EXGSellInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig confirmSellItem;
	private EXGItemConfig cancelSellItem;


	// -------------------------------------------------- //


	public EXGSellInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getConfirmSellItem() {
		return confirmSellItem;
	}
	public void setConfirmSellItem(EXGItemConfig confirmSellItem) {
		this.confirmSellItem = confirmSellItem;
	}


	// -------------------------------------------------- //


	public EXGItemConfig getCancelSellItem() {
		return cancelSellItem;
	}
	public void setCancelSellItem(EXGItemConfig cancelSellItem) {
		this.cancelSellItem = cancelSellItem;
	}


	// -------------------------------------------------- //


	public EXGSellInventoryConfig copy() {

		EXGSellInventoryConfig copy = new EXGSellInventoryConfig(
				this.getEXGTitle().duplicate().getTitle(null),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots());

		copy.setConfirmSellItem(this.getConfirmSellItem().duplicate());
		copy.setCancelSellItem(this.getCancelSellItem().duplicate());
		return copy;
	}


	// -------------------------------------------------- //
}
