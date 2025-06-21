package fr.snipertvmc.essentialsxgui.inventories.homes;

import fr.mrmicky.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.EXGHomeEditingInventoryConfig;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class HomeEditingInventory extends FastInv {


	// -------------------------------------------------- //


	private final EXGHomeEditingInventoryConfig config = Main.getInstance().getInventoriesManager().getHomeEditingInventoryConfig().copy();


	// -------------------------------------------------- //


	public HomeEditingInventory(Player player, EXGHome home) {
		super(
				Main.getInstance().getInventoriesManager().getHomeEditingInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getHomeEditingInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(Map.of(
								"{player}", player.getName(),
								"{homeName}", home.getName(),
								"{homeDisplayName}", home.getDisplayName()))
						.getTitle()
		);


		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem().build());
		}


		if (config.getPreviewHomeItem().isEnabled()) {
			setItem(config.getPreviewHomeItem().getSlot(), config.getPreviewHomeItem()
					.setMaterial(home.getMaterial().name())
					.updateVariables(
							Map.of("{homeName}", home.getName(),
									"{homeDisplayName}", home.getDisplayName(),
									"{homeMaterialName}", home.getMaterial().name()))
					.build());
		}


		if (config.getChangeDisplayNameItem().isEnabled()) {
			setItem(config.getChangeDisplayNameItem().getSlot(), config.getChangeDisplayNameItem()
					.updateVariables(
							Map.of("{homeName}", home.getName(),
									"{homeDisplayName}", home.getDisplayName()))
					.build(), e -> {

				player.closeInventory();
				player.sendMessage(MessagesUtils.get(EXGMessage.ENTER_NEW_DISPLAY_NAME, null));
				Main.getInstance().getChatManager().addChat(player.getUniqueId(), newHomeName -> {

					if (newHomeName.equalsIgnoreCase("cancel")) {
						player.sendMessage(MessagesUtils.get(EXGMessage.ACTION_CANCELED, null));
						new HomeEditingInventory(player, home).open(player);
						return;
					}

					if (newHomeName.length() > 16) {
						player.sendMessage(MessagesUtils.get(EXGMessage.CHARACTER_LIMIT, null));
						new HomeEditingInventory(player, home).open(player);
						return;
					}

					home.setDisplayName(newHomeName);
					player.sendMessage(MessagesUtils.get(EXGMessage.DISPLAY_NAME_CHANGED,
							Map.of("new_display_name", newHomeName.replace("&", "§"))
					));
					new HomeEditingInventory(player, home).open(player);

					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, 10);
			});
		}


		if (config.getChangeIconItem().isEnabled()) {
			setItem(config.getChangeIconItem().getSlot(), config.getChangeIconItem()
					.updateVariables(
							Map.of("{homeName}", home.getName(),
									"{homeMaterialName}", home.getMaterial().name()))
					.build(), e -> {

				player.closeInventory();
				player.sendMessage(MessagesUtils.get(EXGMessage.ENTER_NEW_ICON_NAME, null));
				Main.getInstance().getChatManager().addChat(player.getUniqueId(), materialName -> {

					if (materialName.equalsIgnoreCase("cancel")) {
						player.sendMessage(MessagesUtils.get(EXGMessage.ACTION_CANCELED, null));
						new HomeEditingInventory(player, home).open(player);
						return;
					}

					Material material = Material.matchMaterial(materialName);

					if (material == null) {
						player.sendMessage(MessagesUtils.get(EXGMessage.INVALID_MATERIAL, null));
						new HomeEditingInventory(player, home).open(player);
						return;
					}

					home.setMaterial(material);
					player.sendMessage(MessagesUtils.get(EXGMessage.ICON_CHANGED, Map.of("new_icon", material.name())));
					new HomeEditingInventory(player, home).open(player);

					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, 10);
			});
		}


		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(), e -> {

				new HomesInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //
}
