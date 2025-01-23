package fr.snipertvmc.essentialsxgui.inventories.homes;

import fr.mrmicky.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.EXGHomesInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class HomesInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGHomesInventoryConfig config = Main.getInstance().getInventoriesManager().getHomesInventoryConfig();


	// -------------------------------------------------- //


	public HomesInventory(Player player) {
		super(
				Main.getInstance().getInventoriesManager().getHomesInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getHomesInventoryConfig().getTitle()
						.updateVariables(Map.of(
						"{player}", player.getName()))
						.getTitle()
		);


		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem().build());
		}


		previousPageItem(config.getPreviousPageItem().getSlot(), config.getPreviousPageItem()
				.updateVariables(
						Map.of("{currentPage}", String.valueOf(this.currentPage()),
								"{previousPage}", String.valueOf(this.currentPage() - 1)))
				.build());


		nextPageItem(config.getNextPageItem().getSlot(), config.getNextPageItem()
				.updateVariables(
						Map.of("{currentPage}", String.valueOf(this.currentPage()),
								"{nextPage}", String.valueOf(this.currentPage() + 1)))
				.build());


		Set<EXGHome> homes = Main.getInstance().getPlayerManager().getPlayer(player.getUniqueId().toString()).getHomes()
				.stream()
				.sorted(Comparator.comparing(EXGHome::getName))
				.collect(Collectors.toCollection(LinkedHashSet::new));

		for (EXGHome home : homes) {

			EXGItemConfig homeItem = config.getHomeItem();
			homeItem.setMaterial(home.getMaterial().name());

			addContent(homeItem
					.updateVariables(
							Map.of("{displayName}", home.getDisplayName(),
									"{homeName}", home.getName()))
					.build(), e -> {

				if (e.getClick().isLeftClick()) {
					player.performCommand("essentials:home " + home.getName());

				} else if (e.getClick().isRightClick()) {
					new HomeEditingInventory(player, home).open(player);
				}

			});
		}


		if (config.getCloseItem().isEnabled()) {
			setItem(config.getCloseItem().getSlot(), config.getCloseItem().build(), e -> {
				e.getWhoClicked().closeInventory();
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
								"previousPage", String.valueOf(this.currentPage() - 1),
								"nextPage", String.valueOf(this.currentPage() + 1)))
				.build());
	}


	// -------------------------------------------------- //
}
