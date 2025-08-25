package fr.snipertvmc.essentialsxgui.inventories.kits;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitPreviewInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.other.EssentialsParser;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class KitPreviewInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGKitPreviewInventoryConfig config = Main.getInstance().getInventoriesManager().getKitPreviewInventoryConfig().copy();


	// -------------------------------------------------- //


	public KitPreviewInventory(Player player, EXGKit kit) {
		super(
				Main.getInstance().getInventoriesManager().getKitPreviewInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getKitPreviewInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(Map.of(
								"kitName", kit.getName(),
								"kitDisplayName", kit.getDisplayName()))
						.getTitle()
		);


		Main.getInstance().getServerDataManager().cleanServerData();


		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem().build());
		}


		previousPageItem(config.getPreviousPageItem().getSlot(), config.getPreviousPageItem()
				.updateVariables(
						Map.of("currentPage", String.valueOf(this.currentPage()),
								"previousPage", String.valueOf(this.currentPage() - 1)))
				.build());


		nextPageItem(config.getNextPageItem().getSlot(), config.getNextPageItem()
				.updateVariables(
						Map.of("currentPage", String.valueOf(this.currentPage()),
								"nextPage", String.valueOf(this.currentPage() + 1)))
				.build());


		List<String> serializedItems = (List<String>) Main.getInstance().getEssentials().getKits().getKit(kit.getName()).get("items");
		List<ItemStack> items = EssentialsParser.deserializeKitItems(Main.getInstance().getEssentials(), serializedItems, player);
		for (ItemStack item : items) {
			if (item != null && item.getType() != org.bukkit.Material.AIR) {
				addContent(item);
			}
		}

		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(), e -> {

				new KitsPlayerViewInventory(player, null, null).open(player);
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
						Map.of("currentPage", String.valueOf(this.currentPage()),
								"totalPages", String.valueOf(this.lastPage()),
								"previousPage", String.valueOf(this.currentPage() - 1),
								"nextPage", String.valueOf(this.currentPage() + 1)))
				.build());
	}


	// -------------------------------------------------- //
}
