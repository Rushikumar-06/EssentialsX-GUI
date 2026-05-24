package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;

public class EXGEcoActionInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //

	private EXGItemConfig playerItem;

	private EXGItemConfig addBalanceItem;
	private EXGItemConfig takeBalanceItem;
	private EXGItemConfig setBalanceItem;
	private EXGItemConfig resetBalanceItem;

	private EXGItemConfig backItem;


	// -------------------------------------------------- //


	public EXGEcoActionInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getPlayerItem() {
		return playerItem;
	}

	public EXGItemConfig getAddBalanceItem() {
		return addBalanceItem;
	}
	public EXGItemConfig getTakeBalanceItem() {
		return takeBalanceItem;
	}
	public EXGItemConfig getSetBalanceItem() {
		return setBalanceItem;
	}
	public EXGItemConfig getResetBalanceItem() {
		return resetBalanceItem;
	}

	public EXGItemConfig getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //


	public void setPlayerItem(EXGItemConfig playerItem) {
		this.playerItem = playerItem;
	}

	public void setAddBalanceItem(EXGItemConfig addBalanceItem) {
		this.addBalanceItem = addBalanceItem;
	}
	public void setTakeBalanceItem(EXGItemConfig takeBalanceItem) {
		this.takeBalanceItem = takeBalanceItem;
	}
	public void setSetBalanceItem(EXGItemConfig setBalanceItem) {
		this.setBalanceItem = setBalanceItem;
	}
	public void setResetBalanceItem(EXGItemConfig resetBalanceItem) {
		this.resetBalanceItem = resetBalanceItem;
	}

	public void setBackItem(EXGItemConfig backItem) {
		this.backItem = backItem;
	}


	// -------------------------------------------------- //


	public EXGEcoActionInventoryConfig copy() {

		EXGEcoActionInventoryConfig copy = new EXGEcoActionInventoryConfig(
				this.getEXGTitle().getTitle(null),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setPlayerItem(this.getPlayerItem().duplicate());

		copy.setAddBalanceItem(this.getAddBalanceItem().duplicate());
		copy.setTakeBalanceItem(this.getTakeBalanceItem().duplicate());
		copy.setSetBalanceItem(this.getSetBalanceItem().duplicate());
		copy.setResetBalanceItem(this.getResetBalanceItem().duplicate());

		copy.setBackItem(this.getBackItem().duplicate());
		return copy;
	}


	// -------------------------------------------------- //
}
