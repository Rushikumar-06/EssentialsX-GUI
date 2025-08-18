package fr.snipertvmc.essentialsxgui.inventories.kits;

import fr.snipertvmc.essentialsxgui.inventories.homes.HomeEditingInventory;
import fr.snipertvmc.essentialsxgui.inventories.homes.HomesInventory;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitEditingInventoryConfig;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.TypeUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

public class KitEditingInventory extends FastInv {


	// -------------------------------------------------- //


	private final EXGKitEditingInventoryConfig config = Main.getInstance().getInventoriesManager().getKitEditingInventoryConfig().copy();


	// -------------------------------------------------- //


	public KitEditingInventory(Player player, EXGKit kit) {
		super(
				Main.getInstance().getInventoriesManager().getKitEditingInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getKitEditingInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(
								Map.of("player", player.getName(),
										"kitName", kit.getName(),
										"kitDisplayName", kit.getDisplayName()))
						.getTitle()
		);


		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem().build());
		}


		if (config.getPreviewKitItem().isEnabled()) {
			setItem(config.getPreviewKitItem().getSlot(), config.getPreviewKitItem()
					.setMaterial(kit.getMaterial().name())
					.setData(kit.getData())
					.updateVariables(
							Map.of("kitName", kit.getName(),
									"kitDisplayName", kit.getDisplayName()))
					.build());
		}


		if (config.getChangeDisplayNameItem().isEnabled()) {
			setItem(config.getChangeDisplayNameItem().getSlot(), config.getChangeDisplayNameItem()
					.updateVariables(
							Map.of("kitName", kit.getName(),
									"kitDisplayName", kit.getDisplayName()))
					.build(), e -> changeKitDisplayName(player, kit));
		}


		if (config.getChangeIconItem().isEnabled()) {
			setItem(config.getChangeIconItem().getSlot(), config.getChangeIconItem()
					.updateVariables(
							Map.of("kitName", kit.getName()))
					.build(), e -> changeKitIcon(player, kit));
		}

		if (config.getDeleteKitItem().isEnabled()) {
			setItem(config.getDeleteKitItem().getSlot(), config.getDeleteKitItem()
					.updateVariables(
							Map.of("kitName", kit.getName(),
									"kitDisplayName", kit.getDisplayName()))
					.build(), e -> deleteKit(player, kit));
		}


		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(), e -> {

				new KitsAdminViewInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //


	public void changeKitDisplayName(Player player, EXGKit kit) {

		if (!Main.getInstance().getChatManager().canDoChat(player.getUniqueId())) {
			return;
		}

		player.closeInventory();
		player.sendMessage(MessagesUtils.get(EXGMessage.ENTER_NEW_DISPLAY_NAME, null));
		Main.getInstance().getChatManager().addChat(player.getUniqueId(), newKitName -> {

			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {

				if (newKitName.equalsIgnoreCase("cancel")) {
					player.sendMessage(MessagesUtils.get(EXGMessage.ACTION_CANCELED, null));
					new KitEditingInventory(player, kit).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_CANCELED);
					return;
				}

				if (newKitName.isEmpty() || newKitName.length() > 32) {
					player.sendMessage(MessagesUtils.get(EXGMessage.LENGTH_LIMIT, Map.of("min", "1", "max", "32")));
					new KitEditingInventory(player, kit).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				kit.setDisplayName(newKitName);
				player.sendMessage(MessagesUtils.get(EXGMessage.DISPLAY_NAME_CHANGED,
						Map.of("newDisplayName", newKitName.replace("&", "§"))
				));
				new KitEditingInventory(player, kit).open(player);
				SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

			});

		}, 10);
	}


	public void changeKitIcon(Player player, EXGKit kit) {

		if (!Main.getInstance().getChatManager().canDoChat(player.getUniqueId())) {
			return;
		}

		player.closeInventory();
		player.sendMessage(MessagesUtils.get(EXGMessage.ENTER_NEW_ICON_NAME, null));
		Main.getInstance().getChatManager().addChat(player.getUniqueId(), result -> {

			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {

				if (result.equalsIgnoreCase("cancel")) {
					player.sendMessage(MessagesUtils.get(EXGMessage.ACTION_CANCELED, null));
					new KitEditingInventory(player, kit).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_CANCELED);
					return;
				}

				Material material;
				byte data = 0;

				if (result.contains(":")) {

					String[] materialSplit = result.split(":");
					if (materialSplit.length != 2 || !TypeUtils.isByte(materialSplit[1])) {
						player.sendMessage(MessagesUtils.get(EXGMessage.INVALID_MATERIAL, null));
						new KitEditingInventory(player, kit).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					material = Material.matchMaterial(materialSplit[0]);
					data = Byte.parseByte(materialSplit[1]);

				} else {
					material = Material.matchMaterial(result);
				}

				if (material == null) {
					player.sendMessage(MessagesUtils.get(EXGMessage.INVALID_MATERIAL, null));
					new KitEditingInventory(player, kit).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					return;
				}

				kit.setMaterial(material);
				kit.setData(data);

				player.sendMessage(MessagesUtils.get(EXGMessage.ICON_CHANGED, Map.of("newIcon", material.name())));
				new KitEditingInventory(player, kit).open(player);
				SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

			});

		}, 10);
	}


	public void deleteKit(Player player, EXGKit kit) {

		if (!Main.getInstance().getChatManager().canDoChat(player.getUniqueId())) {
			return;
		}

		player.closeInventory();
		player.sendMessage(MessagesUtils.get(EXGMessage.CONFIRM_DELETE_KIT, Map.of("kitName", kit.getName())));
		Main.getInstance().getChatManager().addChat(player.getUniqueId(), result -> {

			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {

				if (result.equalsIgnoreCase("confirm")) {

					Main.getInstance().getEssentials().getKits().removeKit(kit.getName());
					player.sendMessage(MessagesUtils.get(EXGMessage.KIT_DELETED, Map.of("kitName", kit.getName())));
					new KitsAdminViewInventory(player).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				} else {
					player.sendMessage(MessagesUtils.get(EXGMessage.ACTION_CANCELED, null));
					new KitEditingInventory(player, kit).open(player);

					if (result.equalsIgnoreCase("cancel")) {
						SoundsUtils.playSound(player, EXGSound.ACTION_CANCELED);
					} else {
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					}
				}
			});

		}, 10);
	}


	// -------------------------------------------------- //
}
