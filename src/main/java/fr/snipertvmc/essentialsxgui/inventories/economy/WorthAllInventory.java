package fr.snipertvmc.essentialsxgui.inventories.economy;

import com.earth2me.essentials.utils.NumberUtil;
import com.earth2me.essentials.utils.VersionUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.EXGWorthAllInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Map;

public class WorthAllInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGWorthAllInventoryConfig config = Main.getInstance().getInventoriesManager().getWorthAllInventoryConfig().copy();


	// -------------------------------------------------- //


	public WorthAllInventory(Player player) {
		super(
				Main.getInstance().getInventoriesManager().getWorthAllInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getWorthAllInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(
								Map.of("player", player.getName()))
						.getTitle(player)
		);


		InventoriesUtils.initializeBorderItem(player, config, this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		Map<String, BigDecimal> itemsWorth = Main.getInstance().getEXGServer().getWorth().getItemsWorth();

		// Without data
		if (VersionUtil.getServerBukkitVersion().isLowerThanOrEqualTo(VersionUtil.BukkitVersion.fromString("1.12.2-R0.1-SNAPSHOT"))) {

			for (Map.Entry<String, BigDecimal> entry : itemsWorth.entrySet()) {

				String materialName = entry.getKey().split(":")[0];
				String dataValue = entry.getKey().split(":")[1];

				Material material = Main.getInstance().getEXGServer().getWorth().getMaterialFromWorthName(materialName);
				byte data = !dataValue.equals("*") ? Byte.valueOf(dataValue) : 0;

				BigDecimal itemPrice = entry.getValue();
				String itemWorth = NumberUtil.displayCurrency(itemPrice, Main.getInstance().getEssentials());

				addContent(config.getWorthItem()
						.duplicate()
						.setMaterial(material.name())
						.setData(data)
						.updateVariables(Map.of(
								"worthItemMaterial", material.name(),
								"worthItemData", dataValue,
								"itemWorth", itemWorth))
						.build(player));
			}

		// Without data
		} else {

			for (Map.Entry<String, BigDecimal> entry : itemsWorth.entrySet()) {

				String materialName = entry.getKey();
				Material material = Main.getInstance().getEXGServer().getWorth().getMaterialFromWorthName(materialName);

				BigDecimal itemPrice = entry.getValue();
				String itemWorth = NumberUtil.displayCurrency(itemPrice, Main.getInstance().getEssentials());

				addContent(config.getWorthItem()
						.duplicate()
						.setMaterial(material.name())
						.updateVariables(Map.of(
								"worthItemMaterial", material.name(),
								"itemWorth", itemWorth))
						.build(player));
			}
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
