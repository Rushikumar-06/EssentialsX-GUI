package fr.snipertvmc.essentialsxgui.inventories.economy;

import com.earth2me.essentials.utils.NumberUtil;
import com.earth2me.essentials.utils.VersionUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.EXGWorthInventoryInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class WorthInventoryInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGWorthInventoryInventoryConfig config = Main.getInstance().getInventoriesManager().getWorthInventoryInventoryConfig().copy();


	// -------------------------------------------------- //


	public WorthInventoryInventory(Player player) {
		super(
				Main.getInstance().getInventoriesManager().getWorthInventoryInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getWorthInventoryInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(
								Map.of("player", player.getName()))
						.getTitle(player)
		);


		InventoriesUtils.initializeBorderItem(player, config, this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());

		for (ItemStack itemStack : player.getInventory().getContents()) {

			if (itemStack == null || itemStack.getType() == Material.AIR) continue;

			String materialName = itemStack.getType().name();

			BigDecimal itemUnitPrice = Main.getInstance().getEXGServer().getWorth().getUnitPrice(player, itemStack);
			BigDecimal itemTotalPrice = itemUnitPrice.multiply(BigDecimal.valueOf(itemStack.getAmount()));

			String itemUnitWorth = NumberUtil.displayCurrency(itemUnitPrice, Main.getInstance().getEssentials());
			String itemTotalWorth = NumberUtil.displayCurrency(itemTotalPrice, Main.getInstance().getEssentials());

			Map<String, String> variables = new HashMap<>() {{
				put("worthItemMaterial", materialName);
				put("itemUnitWorth", itemUnitWorth);
				put("itemTotalWorth", itemTotalWorth);
				put("itemAmount", String.valueOf(itemStack.getAmount()));
			}};

			if (VersionUtil.getServerBukkitVersion().isLowerThanOrEqualTo(VersionUtil.BukkitVersion.fromString("1.12.2-R0.1-SNAPSHOT"))) {
				variables.put("worthItemData", String.valueOf(itemStack.getDurability()));
			}

			addContent(config.getWorthItem()
					.duplicate()
					.setMaterial(materialName)
					.setAmount(itemStack.getAmount())
					.setData((byte) itemStack.getDurability())
					.updateVariables(variables)
					.build(player));
		}


		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new WorthInventory(player).open(player);
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
