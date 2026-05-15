package fr.snipertvmc.essentialsxgui.inventories.economy;

import com.earth2me.essentials.utils.NumberUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.EXGWorthInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Map;

public class WorthInventory extends FastInv {


	// -------------------------------------------------- //


	private final EXGWorthInventoryConfig config = Main.getInstance().getInventoriesManager().getWorthInventoryConfig().copy();


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
					.build(player), e -> {

				new WorthAllInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}


		ItemStack handItemStack = player.getInventory().getItemInMainHand();
		boolean hasItemInHand = !handItemStack.getType().isAir();

		String handItemMaterialName = hasItemInHand
				? handItemStack.getType().name()
				: "BARRIER";

		String handItemUnitWorth = MessagesUtils.getString(hasItemInHand
				? EXGMessage.NO_WORTH_AVAILABLE
				: EXGMessage.NO_ITEM_IN_HAND);

		String handItemTotalWorth = MessagesUtils.getString(hasItemInHand
				? EXGMessage.NO_WORTH_AVAILABLE
				: EXGMessage.NO_ITEM_IN_HAND);

		String handItemAmount = String.valueOf(handItemStack.getAmount());

		if (hasItemInHand) {

			BigDecimal handItemUnitPrice = Main.getInstance().getEXGServer().getWorth().getUnitPrice(player, handItemStack);
			if (handItemUnitPrice != null) {

				handItemUnitWorth = NumberUtil.displayCurrency(handItemUnitPrice, Main.getInstance().getEssentials());
				BigDecimal handItemTotalPrice = handItemUnitPrice.multiply(BigDecimal.valueOf(handItemStack.getAmount()));
				handItemTotalWorth = NumberUtil.displayCurrency(handItemTotalPrice, Main.getInstance().getEssentials());
			}
		}

		if (config.getHandItem().isEnabled()) {
			setItem(config.getHandItem().getSlot(), config.getHandItem()
					.duplicate()
					.setMaterial(handItemMaterialName)
					.setAmount(handItemStack.getAmount() > 0 ?  handItemStack.getAmount() : 1)
					.updateVariables(Map.of(
							"handItemMaterial", handItemMaterialName,
							"handItemUnitWorth", handItemUnitWorth,
							"handItemTotalWorth", handItemTotalWorth,
							"handItemAmount", handItemAmount))
					.build(player));
		}


		BigDecimal inventoryPrice = Main.getInstance().getEXGServer().getWorth().getInventoryPrice(player);
		String inventoryWorth = NumberUtil.displayCurrency(inventoryPrice, Main.getInstance().getEssentials());

		if (config.getInventoryItem().isEnabled()) {
			setItem(config.getInventoryItem().getSlot(), config.getInventoryItem()
					.updateVariables(Map.of(
							"inventoryWorth", inventoryWorth))
					.build(player), e -> {

				new WorthInventoryInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}
	}


	// -------------------------------------------------- //
}
