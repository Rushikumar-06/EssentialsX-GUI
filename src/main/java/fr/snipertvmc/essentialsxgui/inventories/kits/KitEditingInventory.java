package fr.snipertvmc.essentialsxgui.inventories.kits;

import fr.mrmicky.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitEditingInventoryConfig;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
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
						.updateVariables(Map.of(
								"{player}", player.getName(),
								"{kitName}", kit.getName(),
								"{kitDisplayName}", kit.getDisplayName()))
						.getTitle()
		);


		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem().build());
		}


		if (config.getPreviewKitItem().isEnabled()) {
			setItem(config.getPreviewKitItem().getSlot(), config.getPreviewKitItem()
					.setMaterial(kit.getMaterial().name())
					.updateVariables(
							Map.of("{kitName}", kit.getName(),
									"{kitDisplayName}", kit.getDisplayName(),
									"{kitMaterialName}", kit.getMaterial().name()))
					.build());
		}


		if (config.getChangeDisplayNameItem().isEnabled()) {
			setItem(config.getChangeDisplayNameItem().getSlot(), config.getChangeDisplayNameItem()
					.updateVariables(
							Map.of("{kitName}", kit.getName(),
									"{kitDisplayName}", kit.getDisplayName()))
					.build(), e -> {

				player.closeInventory();
				player.sendMessage(MessagesUtils.get(EXGMessage.ENTER_NEW_DISPLAY_NAME, null));
				Main.getInstance().getChatManager().addChat(player.getUniqueId(), newKitName -> {

					if (newKitName.equalsIgnoreCase("cancel")) {
						player.sendMessage(MessagesUtils.get(EXGMessage.ACTION_CANCELED, null));
						new KitEditingInventory(player, kit).open(player);
						return;
					}

					if (newKitName.length() > 16) {
						player.sendMessage(MessagesUtils.get(EXGMessage.CHARACTER_LIMIT, null));
						new KitEditingInventory(player, kit).open(player);
						return;
					}

					kit.setDisplayName(newKitName);
					player.sendMessage(MessagesUtils.get(EXGMessage.DISPLAY_NAME_CHANGED,
							Map.of("new_display_name", newKitName.replace("&", "§"))
					));
					new KitEditingInventory(player, kit).open(player);

				}, 10);
			});
		}


		if (config.getChangeIconItem().isEnabled()) {
			setItem(config.getChangeIconItem().getSlot(), config.getChangeIconItem()
					.updateVariables(
							Map.of("{kitName}", kit.getName(),
									"{kitMaterialName}", kit.getMaterial().name()))
					.build(), e -> {

				player.closeInventory();
				player.sendMessage(MessagesUtils.get(EXGMessage.ENTER_NEW_ICON_NAME, null));
				Main.getInstance().getChatManager().addChat(player.getUniqueId(), materialName -> {

					if (materialName.equalsIgnoreCase("cancel")) {
						player.sendMessage(MessagesUtils.get(EXGMessage.ACTION_CANCELED, null));
						new KitEditingInventory(player, kit).open(player);
						return;
					}

					Material material = Material.matchMaterial(materialName);

					if (material == null) {
						player.sendMessage(MessagesUtils.get(EXGMessage.INVALID_MATERIAL, null));
						new KitEditingInventory(player, kit).open(player);
						return;
					}

					kit.setMaterial(material);
					player.sendMessage(MessagesUtils.get(EXGMessage.ICON_CHANGED, Map.of("new_icon", material.name())));
					new KitEditingInventory(player, kit).open(player);

				}, 10);
			});
		}


		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(), e -> {
				new KitsAdminViewInventory(player).open(player);
			});
		}
	}


	// -------------------------------------------------- //
}
