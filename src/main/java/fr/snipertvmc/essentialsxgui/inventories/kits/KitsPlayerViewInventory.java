package fr.snipertvmc.essentialsxgui.inventories.kits;

import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitsPlayerViewInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class KitsPlayerViewInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGKitsPlayerViewInventoryConfig config = Main.getInstance().getInventoriesManager().getKitsPlayerInventoryConfig().copy();


	// -------------------------------------------------- //


	public KitsPlayerViewInventory(Player player) {
		super(
				Main.getInstance().getInventoriesManager().getKitsPlayerInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getKitsPlayerInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(Map.of(
								"{player}", player.getName()))
						.getTitle()
		);


		Main.getInstance().getServerDataManager().cleanServerData();


		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem().build());
		}


		if (config.getSwitchToAdminModeItem().isEnabled() && Main.getInstance().getConfiguration().hasKitsAdminAccess(player)) {
			setItem(config.getSwitchToAdminModeItem().getSlot(), config.getSwitchToAdminModeItem().build(), e -> {

				new KitsAdminViewInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
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


		Set<EXGKit> kits = Main.getInstance().getEXGServer().getKits()
				.stream()
				.filter(kit -> player.hasPermission("essentials.kits." + kit.getName()))
				.sorted(Comparator.comparing(EXGKit::getName))
				.collect(Collectors.toCollection(LinkedHashSet::new));

		for (EXGKit kit : kits) {

			EXGItemConfig kitItem = config.getKitItem().duplicate();
			kitItem.setMaterial(kit.getMaterial().name());

			addContent(kitItem
					.updateVariables(
							Map.of("{kitDisplayName}", kit.getDisplayName(),
									"{kitName}", kit.getName()))
					.build(), e -> {

				if (e.getClick().isLeftClick()) {
					player.performCommand("essentials:kit " + kit.getName());
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);

				} else if (e.getClick().isRightClick()) {
					new KitsPreviewInventory(player, kit).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				}

			});
		}

		if (kits.isEmpty()) {
			addContent(config.getNoKitsItem().build());
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
