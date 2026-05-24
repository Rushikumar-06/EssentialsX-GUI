package fr.snipertvmc.essentialsxgui.inventories.kits;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitPreviewInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
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
						.getTitle(player)
		);


		Main.getInstance().getServerDataManager().updateServerKits();


		InventoriesUtils.initializeBorderItem(player, config, this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		List<ItemStack> items = Main.getInstance().getEssentialsManager().getKitItems(kit.getName());
		for (ItemStack item : items) {
			if (item != null && item.getType() != org.bukkit.Material.AIR) {
				addContent(item);
			}
		}

		if (items.isEmpty() && config.getEmptyKitItem().isEnabled()) {
			addContent(config.getEmptyKitItem().build(player));
		}

		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new KitsPlayerViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
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
