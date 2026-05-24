package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;

import java.util.ArrayList;
import java.util.List;

public class EXGEcoAmountInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private List<EXGItemConfig> addItems;
	private List<EXGItemConfig> removeItems;

	private EXGItemConfig confirmActionItem;
	private EXGItemConfig cancelActionItem;


	// -------------------------------------------------- //


	public EXGEcoAmountInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public List<EXGItemConfig> getAddItems() {
		return addItems;
	}
	public List<EXGItemConfig> getRemoveItems() {
		return removeItems;
	}

	public EXGItemConfig getConfirmActionItem() {
		return confirmActionItem;
	}
	public EXGItemConfig getCancelActionItem() {
		return cancelActionItem;
	}


	// -------------------------------------------------- //


	public void setAddItems(List<EXGItemConfig> addItems) {
		this.addItems = addItems;
	}
	public void setRemoveItems(List<EXGItemConfig> removeItems) {
		this.removeItems = removeItems;
	}

	public void setConfirmActionItem(EXGItemConfig confirmActionItem) {
		this.confirmActionItem = confirmActionItem;
	}
	public void setCancelActionItem(EXGItemConfig cancelActionItem) {
		this.cancelActionItem = cancelActionItem;
	}


	// -------------------------------------------------- //


	public EXGEcoAmountInventoryConfig copy() {

		EXGEcoAmountInventoryConfig copy = new EXGEcoAmountInventoryConfig(
				this.getEXGTitle().getTitle(null),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setAddItems(new ArrayList<>(this.getAddItems()));
		copy.setRemoveItems(new ArrayList<>(this.getRemoveItems()));

		copy.setConfirmActionItem(this.getConfirmActionItem().duplicate());
		copy.setCancelActionItem(this.getCancelActionItem().duplicate());
		return copy;
	}


	// -------------------------------------------------- //
}
