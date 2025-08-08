package fr.snipertvmc.essentialsxgui.inventories.homes;

import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.EXGHomeEditingInventoryConfig;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Bukkit;
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
								"player", player.getName(),
								"homeName", home.getName(),
								"homeDisplayName", home.getDisplayName()))
						.getTitle()
		);


		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem().build());
		}


		if (config.getPreviewHomeItem().isEnabled()) {
			setItem(config.getPreviewHomeItem().getSlot(), config.getPreviewHomeItem()
					.setMaterial(home.getMaterial().name())
					.updateVariables(
							Map.of("homeName", home.getName(),
									"homeDisplayName", home.getDisplayName(),
									"homeMaterialName", home.getMaterial().name()))
					.build());
		}


		if (config.getChangeDisplayNameItem().isEnabled()) {
			setItem(config.getChangeDisplayNameItem().getSlot(), config.getChangeDisplayNameItem()
					.updateVariables(
							Map.of("homeName", home.getName(),
									"homeDisplayName", home.getDisplayName()))
					.build(), e -> changeHomeDisplayName(player, home));
		}


		if (config.getChangeIconItem().isEnabled()) {
			setItem(config.getChangeIconItem().getSlot(), config.getChangeIconItem()
					.updateVariables(
							Map.of("homeName", home.getName(),
									"homeMaterialName", home.getMaterial().name()))
					.build(), e -> changeHomeIcon(player, home));
		}

		if (config.getDeleteHomeItem().isEnabled()) {
			setItem(config.getDeleteHomeItem().getSlot(), config.getDeleteHomeItem()
					.updateVariables(
							Map.of("homeName", home.getName(),
									"homeDisplayName", home.getDisplayName()))
					.build(), e -> deleteHome(player, home));
		}


		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(), e -> {

				new HomesInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //


	public void changeHomeDisplayName(Player player, EXGHome home) {

		player.closeInventory();
		player.sendMessage(MessagesUtils.get(EXGMessage.ENTER_NEW_DISPLAY_NAME, null));

		Main.getInstance().getChatManager().addChat(player.getUniqueId(), newHomeName -> {

			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {

				if (newHomeName.equalsIgnoreCase("cancel")) {
					player.sendMessage(MessagesUtils.get(EXGMessage.ACTION_CANCELED, null));
					new HomeEditingInventory(player, home).open(player);
					return;
				}

				if (newHomeName.isEmpty() || newHomeName.length() > 32) {
					player.sendMessage(MessagesUtils.get(EXGMessage.LENGTH_LIMIT, Map.of("min", "1", "max", "32")));
					new HomeEditingInventory(player, home).open(player);
					return;
				}

				home.setDisplayName(newHomeName);
				player.sendMessage(MessagesUtils.get(EXGMessage.DISPLAY_NAME_CHANGED,
						Map.of("newDisplayName", newHomeName.replace("&", "§"))
				));
				new HomeEditingInventory(player, home).open(player);

				SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
			});

		}, 10);
	}


	public void changeHomeIcon(Player player, EXGHome home) {

		player.closeInventory();
		player.sendMessage(MessagesUtils.get(EXGMessage.ENTER_NEW_ICON_NAME, null));

		Main.getInstance().getChatManager().addChat(player.getUniqueId(), materialName -> {

			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {

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
				player.sendMessage(MessagesUtils.get(EXGMessage.ICON_CHANGED, Map.of("newIcon", material.name())));
				new HomeEditingInventory(player, home).open(player);

				SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
			});

		}, 10);
	}


	public void deleteHome(Player player, EXGHome home) {

		player.closeInventory();
		player.sendMessage(MessagesUtils.get(EXGMessage.CONFIRM_DELETE_HOME, Map.of("homeName", home.getName())));

		Main.getInstance().getChatManager().addChat(player.getUniqueId(), result -> {

			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {

				if (result.equalsIgnoreCase("confirm")) {

					try {
						Main.getInstance().getEssentials().getUser(player).delHome(home.getName());

					} catch (Exception ex) {
						player.sendMessage(MessagesUtils.get(EXGMessage.HOME_DELETE_ERROR, null));
						new HomesInventory(player).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					player.sendMessage(MessagesUtils.get(EXGMessage.HOME_DELETED, Map.of("homeName", home.getName())));
					new HomesInventory(player).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				} else {
					player.sendMessage(MessagesUtils.get(EXGMessage.ACTION_CANCELED, null));
					new HomeEditingInventory(player, home).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
				}
			});

		}, 10);
	}

	// -------------------------------------------------- //
}
