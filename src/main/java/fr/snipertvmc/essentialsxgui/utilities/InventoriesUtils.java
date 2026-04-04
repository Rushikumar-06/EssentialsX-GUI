package fr.snipertvmc.essentialsxgui.utilities;

import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGWarp;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGPaginatedInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.stream.Collectors;

public class InventoriesUtils {


	// -------------------------------------------------- //


	public static void initializePaginatedInventory(Player player, EXGPaginatedInventoryConfig config, PaginatedFastInv inv) {

		inv.previousPageItem(config.getPreviousPageItem().getSlot(), p -> config.getPreviousPageItem().duplicate()
				.updateVariables(
						Map.of("currentPage", String.valueOf(p + 1),
								"previousPage", String.valueOf(p)))
				.build(player));


		inv.nextPageItem(config.getNextPageItem().getSlot(), p -> config.getNextPageItem().duplicate()
				.updateVariables(
						Map.of("currentPage", String.valueOf(p - 1),
								"nextPage", String.valueOf(p)))
				.build(player));
	}


	// -------------------------------------------------- //


	public static ItemStack getHomeItemStack(EXGItemConfig homeItem, EXGHome home, Player player) {

		homeItem.setMaterial(home.getMaterial().name());
		homeItem.setData(home.getData());

		ItemStack homeItemStack;

		if (home.getCustomItemStack() != null) {
			homeItemStack = home.getCustomItemStack().clone();
			ItemMeta meta = homeItemStack.getItemMeta();

			meta.setDisplayName(homeItem.getDisplayName()
					.replace("{homeDisplayName}", home.getDisplayName())
					.replace("{homeName}", home.getName()));

			meta.setLore(homeItem.getLore().stream()
					.map(line -> line
							.replace("{homeDisplayName}", home.getDisplayName())
							.replace("{homeName}", home.getName()))
					.collect(Collectors.toList()));

			homeItemStack.setItemMeta(meta);
			return homeItemStack;

		} else {
			return homeItem
					.updateVariables(
							Map.of("homeDisplayName", home.getDisplayName(),
									"homeName", home.getName()))
					.build(player);
		}
	}


	public static ItemStack getKitItemStack(EXGItemConfig kitItem, EXGKit kit, Player player) {

		kitItem.setMaterial(kit.getMaterial().name());
		kitItem.setData(kit.getData());

		ItemStack kitItemStack;

		if (kit.getCustomItemStack() != null) {
			kitItemStack = kit.getCustomItemStack().clone();
			ItemMeta meta = kitItemStack.getItemMeta();

			meta.setDisplayName(kitItem.getDisplayName()
					.replace("{kitDisplayName}", kit.getDisplayName())
					.replace("{kitName}", kit.getName()));

			meta.setLore(kitItem.getLore().stream()
					.map(line -> line
							.replace("{kitDisplayName}", kit.getDisplayName())
							.replace("{kitName}", kit.getName()))
					.collect(Collectors.toList()));

			kitItemStack.setItemMeta(meta);
			return kitItemStack;

		} else {
			return kitItem
					.updateVariables(
							Map.of("kitDisplayName", kit.getDisplayName(),
									"kitName", kit.getName()))
					.build(player);
		}
	}


	public static ItemStack getWarpItemStack(EXGItemConfig warpItem, EXGWarp warp, Player player) {

		warpItem.setMaterial(warp.getMaterial().name());
		warpItem.setData(warp.getData());

		ItemStack warpItemStack;

		if (warp.getCustomItemStack() != null) {
			warpItemStack = warp.getCustomItemStack().clone();
			ItemMeta meta = warpItemStack.getItemMeta();

			meta.setDisplayName(warpItem.getDisplayName()
					.replace("{warpDisplayName}", warp.getDisplayName())
					.replace("{warpName}", warp.getName()));

			meta.setLore(warpItem.getLore().stream()
					.map(line -> line
							.replace("{warpDisplayName}", warp.getDisplayName())
							.replace("{warpName}", warp.getName()))
					.collect(Collectors.toList()));

			warpItemStack.setItemMeta(meta);
			return warpItemStack;

		} else {
			return warpItem
					.updateVariables(
							Map.of("warpDisplayName", warp.getDisplayName(),
									"warpName", warp.getName()))
					.build(player);
		}
	}


	// -------------------------------------------------- //
}
