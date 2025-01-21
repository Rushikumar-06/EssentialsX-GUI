package fr.snipertvmc.essentialsxgui.inventories.homes;

import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class HomeEditingInventory extends FastInv {


	// -------------------------------------------------- //


	public HomeEditingInventory(Player player, EXGHome home) {
		super(54, "Editing '" + home.getName() + "' home");

		// MessagesUtils.get(EXGMessage.HOME_EDITING_TITLE, Map.of("{home_name}", home.getName()))

		setItems(getBorders(), new ItemBuilder(Material.STAINED_GLASS_PANE).data(15).name(" ").build());


		setItem(22, new ItemBuilder(home.getMaterial()).name(home.getDisplayName()).lore("§7§o'" + home.getName() + "' home preview").build());


		setItem(30, new ItemBuilder(Material.NAME_TAG).name("§eChange name").build(), e -> {

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
				player.sendMessage(MessagesUtils.get(EXGMessage.DISPLAY_NAME_CHANGED, Map.of("new_display_name", newHomeName)));
				new HomeEditingInventory(player, home).open(player);

			}, 10);
		});


		setItem(32, new ItemBuilder(home.getMaterial()).name("§eChange icon").build(), e -> {

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

			}, 10);
		});


		setItem(49, new ItemBuilder(Material.ARROW).name("§cBack").build(), e -> {
			new HomesInventory(player).open(player);
		});
	}


	// -------------------------------------------------- //
}
