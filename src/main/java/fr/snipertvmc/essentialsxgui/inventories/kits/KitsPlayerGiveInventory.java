package fr.snipertvmc.essentialsxgui.inventories.kits;

import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitsPlayerGiveInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class KitsPlayerGiveInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGKitsPlayerGiveInventoryConfig config = Main.getInstance().getInventoriesManager().getKitsPlayerGiveInventoryConfig().copy();


	// -------------------------------------------------- //


	public KitsPlayerGiveInventory(Player player, EXGKit kit) {
		super(
				Main.getInstance().getInventoriesManager().getKitsPlayerGiveInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getKitsPlayerGiveInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(Map.of(
								"{kitName}", kit.getName(),
								"{kitDisplayName}", kit.getDisplayName()))
						.getTitle()
		);


		Main.getInstance().getServerDataManager().cleanServerData();


		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem().build());
		}


		previousPageItem(config.getPreviousPageItem().getSlot(), config.getPreviousPageItem()
				.updateVariables(Map.of(
						"{currentPage}", String.valueOf(this.currentPage()),
						"{previousPage}", String.valueOf(this.currentPage() - 1)))
				.build());


		nextPageItem(config.getNextPageItem().getSlot(), config.getNextPageItem()
				.updateVariables(Map.of(
						"{currentPage}", String.valueOf(this.currentPage()),
						"{nextPage}", String.valueOf(this.currentPage() + 1)))
				.build());

		List<Player> targets = Bukkit.getOnlinePlayers().stream()
				.map(p -> (Player) p)
				.sorted(Comparator.comparing(Player::getName))
				.toList();

		for (Player target : targets) {

			EXGItemConfig playerItem = config.getPlayerItem().duplicate();
			addContent(playerItem
					.updateVariables(Map.of(
							"{targetName}", target.getName(),
							"{kitName}", kit.getName(),
							"{kitDisplayName}", kit.getDisplayName()))
					.build(), e -> {

				player.performCommand("essentials:kit " + kit.getName() + " " + target.getName());
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}

		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(), e -> {

				new KitsAdminViewInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}


		config.getInventoryScheme().apply(this);
	}


	// -------------------------------------------------- //


	@Override
	protected void onPageChange(int page) {

		setItem(config.getCurrentPageItem().getSlot(), config.getCurrentPageItem()
				.updateVariables(
						Map.of("{currentPage}", String.valueOf(this.currentPage()),
								"{totalPages}", String.valueOf(this.lastPage()),
								"{previousPage}", String.valueOf(this.currentPage() - 1),
								"{nextPage}", String.valueOf(this.currentPage() + 1)))
				.build());
	}


	// -------------------------------------------------- //
}
