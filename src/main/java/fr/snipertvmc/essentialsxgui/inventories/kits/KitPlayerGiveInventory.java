package fr.snipertvmc.essentialsxgui.inventories.kits;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitPlayerGiveInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class KitPlayerGiveInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGKitPlayerGiveInventoryConfig config = Main.getInstance().getInventoriesManager().getKitPlayerGiveInventoryConfig().copy();


	// -------------------------------------------------- //


	public KitPlayerGiveInventory(Player player, EXGKit kit) {
		super(
				Main.getInstance().getInventoriesManager().getKitPlayerGiveInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getKitPlayerGiveInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(Map.of(
								"kitName", kit.getName(),
								"kitDisplayName", kit.getDisplayName()))
						.getTitle(player)
		);


		Main.getInstance().getServerDataManager().updateServerKits();


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
							"kitName", kit.getName(),
							"kitDisplayName", kit.getDisplayName()))
					.build(player), e -> {

				player.performCommand("essentials:kit " + kit.getName() + " " + target.getName());
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}

		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new KitsAdminViewInventory(player, null, null).open(player);
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
