package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;

import java.util.HashSet;
import java.util.Set;

public class EXGBalanceTopInventoryConfig extends EXGInventoryConfig {


	// -------------------------------------------------- //


	private EXGItemConfig playerRankingItem;
	private EXGItemConfig forceUpdateItem;

	private EXGItemConfig closeItem;

	private Set<EXGItemConfig> rankingItems;

	private Pair<Integer, Integer> rankingRange;


	// -------------------------------------------------- //


	public EXGBalanceTopInventoryConfig(String title, int rows, EXGItemConfig borderItems, int... borderSlots) {
		super(title, rows, borderItems, borderSlots);
	}


	// -------------------------------------------------- //


	public EXGItemConfig getPlayerRankingItem() {
		return playerRankingItem;
	}
	public EXGItemConfig getForceUpdateItem() {
		return forceUpdateItem;
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


	public void setPlayerRankingItem(EXGItemConfig playerRankingItem) {
		this.playerRankingItem = playerRankingItem;
	}
	public void setForceUpdateItem(EXGItemConfig forceUpdateItem) {
		this.forceUpdateItem = forceUpdateItem;
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


	public EXGBalanceTopInventoryConfig copy() {

		EXGBalanceTopInventoryConfig copy = new EXGBalanceTopInventoryConfig(
				this.getEXGTitle().getTitle(null),
				this.getRows(),
				this.getBorderItem().duplicate(),
				this.getBorderSlots()
		);

		copy.setPlayerRankingItem(this.getPlayerRankingItem().duplicate());
		copy.setForceUpdateItem(this.getForceUpdateItem().duplicate());

		copy.setCloseItem(this.getCloseItem().duplicate());

		copy.setRankingItems(new HashSet<>(this.getRankingItems()));

		copy.setRankingRange(this.getRankingRange());
		return copy;
	}


	// -------------------------------------------------- //
}
