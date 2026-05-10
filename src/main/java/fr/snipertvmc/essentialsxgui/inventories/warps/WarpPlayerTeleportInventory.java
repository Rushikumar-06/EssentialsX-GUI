package fr.snipertvmc.essentialsxgui.inventories.warps;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGWarp;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps.EXGWarpPlayerTeleportInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class WarpPlayerTeleportInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGWarpPlayerTeleportInventoryConfig config = Main.getInstance().getInventoriesManager().getWarpPlayerTeleportInventoryConfig().copy();


	// -------------------------------------------------- //


	public WarpPlayerTeleportInventory(Player player, EXGWarp warp) {
		super(
				Main.getInstance().getInventoriesManager().getWarpPlayerTeleportInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getWarpPlayerTeleportInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(Map.of(
								"warpName", warp.getName(),
								"warpDisplayName", warp.getDisplayName()))
						.getTitle(player)
		);


		Main.getInstance().getServerDataManager().updateServerWarps();


		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem().build(player));
		}


		InventoriesUtils.initializePaginatedInventory(player, config, this);


		List<Player> targets = Bukkit.getOnlinePlayers().stream()
				.map(p -> (Player) p)
				.sorted(Comparator.comparing(Player::getName))
				.toList();

		for (Player target : targets) {

			EXGItemConfig playerItem = config.getPlayerItem().duplicate();
			addContent(playerItem
					.updateVariables(Map.of(
							"targetName", target.getName(),
							"warpName", warp.getName(),
							"warpDisplayName", warp.getDisplayName()))
					.build(player), e -> {

				player.performCommand("essentials:warp " + warp.getName() + " " + target.getName());
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}

		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new WarpsAdminViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}


		config.getInventoryScheme().apply(this);
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
