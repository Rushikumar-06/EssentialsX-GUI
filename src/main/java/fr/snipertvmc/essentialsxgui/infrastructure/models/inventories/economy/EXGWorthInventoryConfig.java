package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;

import java.util.Set;

public class EXGWorthInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig allItem;
	private EXGItemConfig handItem;
	private EXGItemConfig inventoryItem;

	private EXGItemConfig closeItem;

	private Set<EXGItemConfig> rankingItems;

	private Pair<Integer, Integer> rankingRange;


	// -------------------------------------------------- //


	public EXGWorthInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getAllItem() {
		return allItem;
	}
	public EXGItemConfig getHandItem() {
		return handItem;
	}
	public EXGItemConfig getInventoryItem() {
		return inventoryItem;
	}

	public EXGItemConfig getCloseItem() {
		return closeItem;
	}

	public Set<EXGItemConfig> getRankingItems() {
		return rankingItems;
	}

	public Pair<Integer, Integer> getRankingRange() {
		return rankingRange;
	}


	// -------------------------------------------------- //


	public void setAllItem(EXGItemConfig allItem) {
		this.allItem = allItem;
	}
	public void setHandItem(EXGItemConfig handItem) {
		this.handItem = handItem;
	}
	public void setInventoryItem(EXGItemConfig inventoryItem) {
		this.inventoryItem = inventoryItem;
	}

	public void setCloseItem(EXGItemConfig closeItem) {
		this.closeItem = closeItem;
	}

	public void setRankingItems(Set<EXGItemConfig> rankingItems) {
		this.rankingItems = rankingItems;
	}

	public void setRankingRange(Pair<Integer, Integer> rankingRange) {
		this.rankingRange = rankingRange;
	}


	// -------------------------------------------------- //


	public EXGWorthInventoryConfig copy() {

		EXGWorthInventoryConfig copy = new EXGWorthInventoryConfig(
				this.getEXGTitle().getTitle(null),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setAllItem(this.getAllItem().duplicate());
		copy.setHandItem(this.getHandItem().duplicate());
		copy.setInventoryItem(this.getInventoryItem().duplicate());

		copy.setCloseItem(this.getCloseItem().duplicate());

		copy.setRankingItems(this.getRankingItems());

		copy.setRankingRange(this.getRankingRange());
		return copy;
	}


	// -------------------------------------------------- //
}
