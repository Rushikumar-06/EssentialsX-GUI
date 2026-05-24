package fr.snipertvmc.essentialsxgui.inventories.economy;

import com.earth2me.essentials.utils.NumberUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.EXGEcoPlayersInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class EcoPlayersInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGEcoPlayersInventoryConfig config = Main.getInstance().getInventoriesManager().getEcoPlayersInventoryConfig().copy();


	// -------------------------------------------------- //


	public EcoPlayersInventory(Player player) {
		super(
				Main.getInstance().getInventoriesManager().getEcoPlayersInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getEcoPlayersInventoryConfig().getEXGTitle()
						.duplicate()
						.getTitle(player)
		);


		InventoriesUtils.initializeInventoryWithClose(player, config, this, config.getCloseItem());
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		List<Player> onlinePlayers = Bukkit.getOnlinePlayers().stream()
				.map(p -> (Player) p)
				.sorted(Comparator.comparing(Player::getName))
				.toList();

		for (Player onlinePlayer : onlinePlayers) {

			BigDecimal targetBalanceValue = Main.getInstance().getEssentials().getUser(onlinePlayer).getMoney();
			String targetBalance = NumberUtil.displayCurrency(targetBalanceValue, Main.getInstance().getEssentials());

			addContent(config.getPlayerItem()
					.duplicate()
					.updateVariables(Map.of(
							"targetName", onlinePlayer.getName(),
							"targetBalance", targetBalance))
					.build(player), e -> {

				new EcoActionInventory(player, onlinePlayer).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}
	}


	// -------------------------------------------------- //


	@Override
	protected void onPageChange(int page) {
		Player player = this.getInventory().getViewers().isEmpty() ? null : (Player) this.getInventory().getViewers().get(0);
		InventoriesUtils.updateCurrentPageItem(player, config, this);
		SoundsUtils.playSound(player, EXGSound.GUI_PAGE_CHANGE);
	}


	// -------------------------------------------------- //
}
