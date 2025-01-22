package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure;

public class EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGInventoryTitle title;
	private int rows;

	private EXGItemConfig borderItem;
	private int[] borderSlots;


	// -------------------------------------------------- //


	public EXGInventoryConfig(String title, int rows, EXGItemConfig borderItem, int... borderSlots) {
		this.title = new EXGInventoryTitle(title);
		this.rows = rows;
		this.borderItem = borderItem;
		this.borderSlots = borderSlots;
	}


	// -------------------------------------------------- //


	public EXGInventoryTitle getTitle() {
		return title.duplicate();
	}


	public int getRows() {
		return rows;
	}


	public EXGItemConfig getBorderItem() {
		return borderItem;
	}


	public int[] getBorderSlots() {
		return borderSlots;
	}


	// -------------------------------------------------- //


	public void setTitle(String title) {
		this.title = new EXGInventoryTitle(title);
	}


	public void setRows(int rows) {
		this.rows = rows;
	}


	public void setBorderItem(EXGItemConfig borderItem) {
		this.borderItem = borderItem;
	}


	public void setBorderSlots(int... borderSlots) {
		this.borderSlots = borderSlots;
	}


	// -------------------------------------------------- //
}
