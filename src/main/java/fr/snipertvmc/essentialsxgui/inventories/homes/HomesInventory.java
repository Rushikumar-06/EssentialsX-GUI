package fr.snipertvmc.essentialsxgui.inventories.homes;

import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.EXGHomesInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class HomesInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGHomesInventoryConfig config = Main.getInstance().getInventoriesManager().getHomesInventoryConfig().copy();


	// -------------------------------------------------- //


	public HomesInventory(Player player) {
		super(
				Main.getInstance().getInventoriesManager().getHomesInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getHomesInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(Map.of(
						"{player}", player.getName()))
						.getTitle()
		);


		Main.getInstance().getPlayerDataManager().cleanPlayerData(player.getUniqueId().toString());


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

			EXGItemConfig homeItem = config.getHomeItem().duplicate();
			homeItem.setMaterial(home.getMaterial().name());

			addContent(homeItem
					.updateVariables(
							Map.of("{homeDisplayName}", home.getDisplayName(),
									"{homeName}", home.getName()))
					.build(), e -> {

				if (e.getClick().isLeftClick()) {
					player.performCommand("essentials:home " + home.getName());

				} else if (e.getClick().isRightClick()) {
					new HomeEditingInventory(player, home).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				}

			});
		}

		if (homes.isEmpty()) {
			addContent(config.getNoHomesItem().build());
		}


		if (config.getCloseItem().isEnabled()) {
			setItem(config.getCloseItem().getSlot(), config.getCloseItem().build(), e -> {

				e.getWhoClicked().closeInventory();
				SoundsUtils.playSound(player, EXGSound.GUI_CLOSE);
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
