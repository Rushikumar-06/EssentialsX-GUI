package fr.snipertvmc.essentialsxgui.inventories.economy;

import com.earth2me.essentials.utils.NumberUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGBalanceTop;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.EXGBalanceTopInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.TimeUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalanceTopInventory extends FastInv {


	// -------------------------------------------------- //


	private final EXGBalanceTopInventoryConfig config = Main.getInstance().getInventoriesManager().getBalanceTopInventoryConfig().copy();


	// -------------------------------------------------- //


	public BalanceTopInventory(Player player) {
		super(
				Main.getInstance().getInventoriesManager().getBalanceTopInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getBalanceTopInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(
								Map.of("player", player.getName()))
						.getTitle(player)
		);


		initializeInventory(player);


		if (config.getPlayerRankingItem().isEnabled()) {

			EXGBalanceTop balanceTop = Main.getInstance().getEXGServer().getBalanceTop();
			int playerRank = balanceTop.getPlayerRank(player.getName());

			BigDecimal playerMoney = Main.getInstance().getEssentials().getUser(player).getMoney();
			boolean ecoEnabled = !Main.getInstance().getEssentials().getSettings().isEcoDisabled();
			String playerBalance = ecoEnabled
					? NumberUtil.displayCurrency(playerMoney, Main.getInstance().getEssentials())
					: MessagesUtils.getString(EXGMessage.DISABLED);

			setItem(config.getPlayerRankingItem().getSlot(), config.getPlayerRankingItem()
					.updateVariables(Map.of(
							"playerName", player.getName(),
							"playerRank", playerRank > 0 ? String.valueOf(playerRank) : MessagesUtils.getString(EXGMessage.NOT_RANKED),
							"playerBalance", playerBalance
					))
					.build(player));
		}

		if (config.getForceUpdateItem().isEnabled()) {

			if (config.getForceUpdateItem().hasUpdateItemInterval()) {
				long updateInterval = config.getForceUpdateItem().getUpdateItemInterval() * 20L;
				setDynamicItem(config.getForceUpdateItem().getSlot(), () -> getForceUpdateItem(player), updateInterval, e -> {

					if (player.hasPermission("essentials.balancetop.force")) {
						Main.getInstance().getEXGServer().getBalanceTop().forceUpdate();
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.BALANCE_TOP_DATA_UPDATED));
						SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

					} else {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.NO_PERMISSION));
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					}
				});

			} else {
				setItem(config.getForceUpdateItem().getSlot(), getForceUpdateItem(player));
			}
		}

		addRankingItems(player);
	}


	// -------------------------------------------------- //


	private void addRankingItems(Player player) {

		Map<String, String> placeholders = getBalanceTopPlaceholders();

		for (EXGItemConfig rankingItem : config.getRankingItems()) {
			rankingItem = rankingItem.duplicate();

			String displayName = rankingItem.getDisplayName();
			List<String> lore = rankingItem.getLore();

			for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
				if (displayName.contains(placeholder.getKey())) {
					displayName = displayName.replace(placeholder.getKey(), placeholder.getValue());
				}
			}

			for (int i = 0; i < lore.size(); i++) {
				String line = lore.get(i);
				for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
					if (line.contains(placeholder.getKey())) {
						line = line.replace(placeholder.getKey(), placeholder.getValue());
					}
				}
				lore.set(i, line);
			}

			rankingItem.setDisplayName(displayName);
			rankingItem.setLore(lore);
			setItem(rankingItem.getSlot(), rankingItem.build(player));
		}
	}


	private Map<String, String> getBalanceTopPlaceholders() {

		Map<String, String> placeholders = new HashMap<>();
		EXGBalanceTop balanceTop = Main.getInstance().getEXGServer().getBalanceTop();

		int start = config.getRankingRange().getLeft();
		int end = config.getRankingRange().getRight();

		boolean ecoEnabled = !Main.getInstance().getEssentials().getSettings().isEcoDisabled();
		String disabledMsg = MessagesUtils.getString(EXGMessage.DISABLED);
		String nobodyMsg = MessagesUtils.getString(EXGMessage.NOBODY);

		for (int i = start; i <= end; i++) {
			String name = nobodyMsg;
			String balance = "0";

			if (i <= balanceTop.getBalanceTopEntries().size()) {
				Pair<String, Double> entry = balanceTop.getBalanceTopEntries().get(i - 1);

				if (entry != null) {
					name = entry.getLeft();

					if (ecoEnabled) {
						BigDecimal amount = BigDecimal.valueOf(entry.getRight());
						balance = NumberUtil.displayCurrency(amount, Main.getInstance().getEssentials());
					} else {
						balance = disabledMsg;
					}
				}
			}

			placeholders.put("{playerName_" + i + "}", name);
			placeholders.put("{playerBalance_" + i + "}", balance);
		}
		return placeholders;
	}


	private ItemStack getForceUpdateItem(Player player) {

		EXGBalanceTop balanceTop = Main.getInstance().getEXGServer().getBalanceTop();
		long lastUpdateTimestamp = balanceTop.getLastUpdate();
		int secondsSinceLastUpdate = (int) ((System.currentTimeMillis() - lastUpdateTimestamp) / 1000);
		String lastUpdate = TimeUtils.formatAgoTime(secondsSinceLastUpdate);

		int balanceTopUpdateInterval = Main.getInstance().getConfiguration().getBalanceTopUpdateInterval();
		int secondesBeforeNextUpdate = balanceTopUpdateInterval - secondsSinceLastUpdate;
		String nextUpdate = TimeUtils.formatInTime(secondesBeforeNextUpdate);

		return config.getForceUpdateItem()
				.duplicate()
				.updateVariables(Map.of(
						"lastUpdate", lastUpdate,
						"nextUpdate", nextUpdate
				))
				.build(player);
	}


	private void initializeInventory(Player player) {

		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem().build(player));
		}


		if (config.getCloseItem().isEnabled()) {
			setItem(config.getCloseItem().getSlot(), config.getCloseItem().build(player), e -> {

				e.getWhoClicked().closeInventory();
				SoundsUtils.playSound(player, EXGSound.GUI_CLOSE);
			});
		}
	}


	// -------------------------------------------------- //
}
