package fr.snipertvmc.essentialsxgui.inventories.economy;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.EXGSellInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellInventory extends FastInv {


	// -------------------------------------------------- //


	private final EXGSellInventoryConfig config = Main.getInstance().getInventoriesManager().getSellInventoryConfig().copy();

	private final List<Integer> reservedSlots = new ArrayList<>();


	// -------------------------------------------------- //


	public SellInventory(Player player) {
		super(
				Main.getInstance().getInventoriesManager().getSellInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getSellInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(
								Map.of("player", player.getName()))
						.getTitle(player)
		);


		InventoriesUtils.initializeBorderItem(player, config, this);


		if (config.getConfirmSellItem().isEnabled()) reservedSlots.add(config.getConfirmSellItem().getSlot());
		if (config.getCancelSellItem().isEnabled()) reservedSlots.add(config.getCancelSellItem().getSlot());
		for (int borderSlot : config.getBorderSlots()) {;
			reservedSlots.add(borderSlot);
		}


		if (config.getConfirmSellItem().isEnabled()) {
			setItem(config.getConfirmSellItem().getSlot(), config.getConfirmSellItem()
					.build(player), e -> {

				List<ItemStack> sellItems = new ArrayList<>();

				for (int i = 0; i < e.getInventory().getSize(); i++) {
					if (reservedSlots.contains(i)) continue;

					ItemStack itemStack = e.getInventory().getItem(i);
					if (itemStack != null && itemStack.getType() != Material.AIR) {
						sellItems.add(itemStack);
					}
				}

				sellItems(player, sellItems);
			});
		}


		if (config.getCancelSellItem().isEnabled()) {
			setItem(config.getCancelSellItem().getSlot(), config.getCancelSellItem()
					.build(player), e -> {

				List<ItemStack> sellItems = new ArrayList<>();

				for (int i = 0; i < e.getInventory().getSize(); i++) {
					if (reservedSlots.contains(i)) continue;

					ItemStack itemStack = e.getInventory().getItem(i);
					if (itemStack != null && itemStack.getType() != Material.AIR) {
						sellItems.add(itemStack);
					}
				}

				for (ItemStack sellItem : sellItems) {
					HashMap<Integer, ItemStack> dontFitItems = player.getInventory().addItem(sellItem);
					dontFitItems.forEach((integer, itemStack) -> player.getWorld().dropItem(player.getLocation(), itemStack));
				}

				player.closeInventory();
				SoundsUtils.playSound(player, EXGSound.ACTION_CANCELED);
			});
		}
	}


	// -------------------------------------------------- //


	private void sellItems(Player player, List<ItemStack> sellItems) {

		List<ItemStack> notSoldItems;
		try {
			notSoldItems = Main.getInstance().getEssentialsManager().sellItems(player, sellItems);

		} catch (Exception exception) {
			ConsoleLogger.error("An error occurred while selling items for player " + player.getName() + ": " + exception.getMessage());
			SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			HashMap<Integer, ItemStack> dontFitItems = player.getInventory().addItem(sellItems.toArray(new ItemStack[0]));
			dontFitItems.forEach((integer, itemStack) -> player.getWorld().dropItem(player.getLocation(), itemStack));
			return;
		}

		HashMap<Integer, ItemStack> dontFitItems = player.getInventory().addItem(notSoldItems.toArray(new ItemStack[0]));
		dontFitItems.forEach((integer, itemStack) -> player.getWorld().dropItem(player.getLocation(), itemStack));

		player.closeInventory();
		SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
	}


	// -------------------------------------------------- //


	@Override
	public void onClick(InventoryClickEvent event) {
		event.setCancelled(false);
		if (reservedSlots.contains(event.getRawSlot())) {
			event.setCancelled(true);
		}
	}


	// -------------------------------------------------- //
}
