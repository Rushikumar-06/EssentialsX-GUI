package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure;

public class EXGInventoryConfig {


	// -------------------------------------------------- //


	private int configurationErrors = 0;


	private final EXGInventoryTitle title;
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


	public int getConfigurationErrors() {
		return configurationErrors;
	}

	public EXGInventoryTitle getEXGTitle() {
		return title;
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


	public void incrementConfigurationErrors() {
		this.configurationErrors++;
	}

	public void setTitle(String title) {
		this.title.setTitle(title);
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
