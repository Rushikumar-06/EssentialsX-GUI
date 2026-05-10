package fr.snipertvmc.essentialsxgui.inventories.whois;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.whois.EXGWhoisPlayersInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class WhoisPlayersInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGWhoisPlayersInventoryConfig config = Main.getInstance().getInventoriesManager().getWhoisPlayersInventoryConfig().copy();


	// -------------------------------------------------- //


	public WhoisPlayersInventory(Player player) {
		super(
				Main.getInstance().getInventoriesManager().getWhoisPlayersInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getWhoisPlayersInventoryConfig().getEXGTitle()
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

			EXGItemConfig playerItem = config.getPlayerItem().duplicate();
			addContent(playerItem
					.updateVariables(Map.of(
							"targetName", onlinePlayer.getName()))
					.build(player), e -> {

				new WhoisViewInventory(player, onlinePlayer).open(player);
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
