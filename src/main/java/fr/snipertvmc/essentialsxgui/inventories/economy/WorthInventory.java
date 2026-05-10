package fr.snipertvmc.essentialsxgui.inventories.economy;

import com.earth2me.essentials.utils.NumberUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.EXGWorthInventoryInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Map;

public class WorthInventory extends FastInv {


	// -------------------------------------------------- //


	private final EXGWorthInventoryInventoryConfig config = Main.getInstance().getInventoriesManager().getWorthInventoryConfig().copy();


	// -------------------------------------------------- //


	public WorthInventory(Player player) {
		super(
				Main.getInstance().getInventoriesManager().getWorthInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getWorthInventoryConfig().getEXGTitle()
						.duplicate()
						.getTitle(player)
		);


		InventoriesUtils.initializeInventoryWithClose(player, config, this, config.getCloseItem());


		if (config.getAllItem().isEnabled()) {
			setItem(config.getAllItem().getSlot(), config.getAllItem()
					.build(player));
		}


		boolean hasItemInHand = !player.getInventory().getItemInMainHand().getType().isAir();

		String handItemMaterial = hasItemInHand
				? player.getInventory().getItemInMainHand().getType().name()
				: "BARRIER";

		String handItemMaterialName = hasItemInHand
				? player.getInventory().getItemInMainHand().getType().name()
				: MessagesUtils.getString(EXGMessage.NO_WORTH_AVAILABLE);

		String handItemWorth = MessagesUtils.getString(EXGMessage.NO_WORTH_AVAILABLE);
		if (hasItemInHand) {

			BigDecimal handItemPrice = Main.getInstance().getEXGServer().getWorth().getPrice(player.getInventory().getItemInMainHand());

			if (handItemPrice != null) {
				handItemWorth = NumberUtil.displayCurrency(handItemPrice, Main.getInstance().getEssentials());
			}
		}

		if (config.getHandItem().isEnabled()) {
			setItem(config.getHandItem().getSlot(), config.getHandItem()
					.duplicate()
					.updateVariables(Map.of(
							"handItemMaterial", handItemMaterial,
							"handItemMaterialName", handItemMaterialName,
							"handItemWorth", handItemWorth))
					.build(player));
		}

//		Main.getInstance().getEssentials().getWorth().getPrice()
//		String inventoryWorth;


		if (config.getInventoryItem().isEnabled()) {
			setItem(config.getInventoryItem().getSlot(), config.getInventoryItem()
//					.updateVariables(Map.of(
//							"inventoryWorth", inventoryWorth))
					.build(player));
		}
	}


	// -------------------------------------------------- //
}
