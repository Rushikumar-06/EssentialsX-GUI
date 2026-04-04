package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure;

public class EXGPaginatedInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //

	private EXGItemConfig nextPageItem;
	private EXGItemConfig previousPageItem;
	private EXGItemConfig currentPageItem;


	// -------------------------------------------------- //


	public EXGPaginatedInventoryConfig(String title, int rows, EXGItemConfig borderItem, int... borderSlots) {
		super(title, rows, borderItem, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getNextPageItem() {
		return nextPageItem;
	}
	public EXGItemConfig getPreviousPageItem() {
		return previousPageItem;
	}
	public EXGItemConfig getCurrentPageItem() {
		return currentPageItem;
	}


	// -------------------------------------------------- //

	public void setNextPageItem(EXGItemConfig nextPageItem) {
		this.nextPageItem = nextPageItem;
	}
	public void setPreviousPageItem(EXGItemConfig previousPageItem) {
		this.previousPageItem = previousPageItem;
	}
	public void setCurrentPageItem(EXGItemConfig currentPageItem) {
		this.currentPageItem = currentPageItem;
	}


	// -------------------------------------------------- //
}
