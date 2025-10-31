package fr.snipertvmc.essentialsxgui.inventories.warps;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGWarp;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps.EXGWarpEditingInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.DataEntryUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WarpEditingInventory extends FastInv {


	// -------------------------------------------------- //


	private final EXGWarpEditingInventoryConfig config = Main.getInstance().getInventoriesManager().getWarpEditingInventoryConfig().copy();


	// -------------------------------------------------- //


	public WarpEditingInventory(Player player, EXGWarp warp) {
		super(
				Main.getInstance().getInventoriesManager().getWarpEditingInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getWarpEditingInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(
								Map.of("player", player.getName(),
										"warpName", warp.getName(),
										"warpDisplayName", warp.getDisplayName()))
						.getTitle()
		);


		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem().build());
		}


		if (config.getPreviewWarpItem().isEnabled()) {

			EXGItemConfig previewWarpItem = config.getPreviewWarpItem().duplicate();
			previewWarpItem.setMaterial(warp.getMaterial().name());
			previewWarpItem.setData(warp.getData());

			ItemStack previewWarpItemStack;

			if (warp.getCustomItemStack() != null) {
				previewWarpItemStack = warp.getCustomItemStack().clone();
				ItemMeta meta = previewWarpItemStack.getItemMeta();

				meta.setDisplayName(previewWarpItem.getDisplayName()
						.replace("{warpDisplayName}", warp.getDisplayName())
						.replace("{warpName}", warp.getName())
						.replace("&", "§"));

				meta.setLore(previewWarpItem.getLore().stream()
						.map(line -> line
								.replace("{warpDisplayName}", warp.getDisplayName())
								.replace("{warpName}", warp.getName())
								.replace("&", "§"))
						.collect(Collectors.toList()));

				previewWarpItemStack.setItemMeta(meta);

			} else {
				previewWarpItemStack = previewWarpItem
						.updateVariables(
								Map.of("warpDisplayName", warp.getDisplayName(),
										"warpName", warp.getName()))
						.build();
			}

			setItem(config.getPreviewWarpItem().getSlot(), previewWarpItemStack);
		}


		if (config.getChangeDisplayNameItem().isEnabled()) {
			setItem(config.getChangeDisplayNameItem().getSlot(), config.getChangeDisplayNameItem()
					.updateVariables(
							Map.of("warpName", warp.getName(),
									"warpDisplayName", warp.getDisplayName()))
					.build(), e -> {

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				changeWarpDisplayName(player, warp);
			});
		}


		if (config.getChangeIconItem().isEnabled()) {
			setItem(config.getChangeIconItem().getSlot(), config.getChangeIconItem()
					.updateVariables(
							Map.of("warpName", warp.getName()))
					.build(), e -> {

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				changeWarpIcon(player, warp);
			});
		}

		if (config.getDeleteWarpItem().isEnabled()) {
			setItem(config.getDeleteWarpItem().getSlot(), config.getDeleteWarpItem()
					.updateVariables(
							Map.of("warpName", warp.getName(),
									"warpDisplayName", warp.getDisplayName()))
					.build(), e -> {

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				deleteWarp(player, warp);
			});
		}


		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(), e -> {

				new WarpsAdminViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //


	public void changeWarpDisplayName(Player player, EXGWarp warp) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getFilesManager().getConfiguration().getEntryType("warps", "changeWarpDisplayNameEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			player.sendMessage(MessagesUtils.get(EXGMessage.ENTER_NEW_DISPLAY_NAME_CHAT, null));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.get(EXGMessage.ENTER_NEW_DISPLAY_NAME, null))
				.setMinLength(Main.getInstance().getConfiguration().getMinNameLength())
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					warp.setDisplayName(result.getLeft());
					player.sendMessage(MessagesUtils.get(EXGMessage.DISPLAY_NAME_CHANGED,
							Map.of("newDisplayName", result.getLeft().replace("&", "§"))
					));
					new WarpEditingInventory(player, warp).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new WarpEditingInventory(player, warp).open(player)
		);
	}


	public void changeWarpIcon(Player player, EXGWarp warp) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getFilesManager().getConfiguration().getEntryType("warps", "changeWarpIconEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			player.sendMessage(MessagesUtils.get(EXGMessage.ENTER_NEW_ICON_NAME_CHAT, null));

		} else if (entryType == EXGEntryType.ITEM_IN_HAND) {

			ItemStack itemInHand = player.getInventory().getItem(player.getInventory().getHeldItemSlot());

			if (itemInHand == null || itemInHand.getType() == Material.AIR) {
				player.sendMessage(MessagesUtils.get(EXGMessage.ITEM_CANT_BE_AIR, null));
				new WarpEditingInventory(player, warp).open(player);
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
				return;
			}

			warp.setCustomItemStack(itemInHand);
			player.sendMessage(MessagesUtils.get(EXGMessage.ICON_CHANGED, Map.of("newIcon", itemInHand.getType().name())));
			new WarpEditingInventory(player, warp).open(player);
			SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
			return;
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL, EXGEntryType.GUI))
				.setEntryDisplayName(MessagesUtils.get(EXGMessage.ENTER_NEW_ICON_NAME, null))
				.setMaterialListPath("warps.changeWarpIconMaterialList")
				.setMinLength(Main.getInstance().getConfiguration().getMinNameLength())
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processMaterialEntry(player, entrySettings,

				result -> {

					warp.setCustomItemStack(null);
					warp.setMaterial(result.getLeft().getLeft());
					warp.setData(result.getLeft().getRight());

					player.sendMessage(MessagesUtils.get(EXGMessage.ICON_CHANGED, Map.of("newIcon", result.getLeft().getLeft().name())));
					new WarpEditingInventory(player, warp).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new WarpEditingInventory(player, warp).open(player)
		);
	}


	public void deleteWarp(Player player, EXGWarp warp) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getFilesManager().getConfiguration().getEntryType("warps", "deleteWarpEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			player.sendMessage(MessagesUtils.get(EXGMessage.CONFIRM_DELETE_WARP_CHAT, Map.of("warpName", warp.getName())));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.get(EXGMessage.CONFIRM_DELETE_WARP, null))
				.setEqualsToSomething("confirm");

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					try {
						Main.getInstance().getEssentials().getWarps().removeWarp(warp.getName());
						player.sendMessage(MessagesUtils.get(EXGMessage.WARP_DELETED, Map.of("warpName", warp.getName())));
						new WarpsAdminViewInventory(player, null, null).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

					} catch (Exception e) {
						player.sendMessage(MessagesUtils.get(EXGMessage.WARP_DELETE_ERROR, null));
						new WarpEditingInventory(player, warp).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					}

				}, entry -> new WarpEditingInventory(player, warp).open(player)
		);
	}


	// -------------------------------------------------- //
}
