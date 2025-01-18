package fr.snipertvmc.essentialsxgui.inventories.homes;

import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.InventoryScheme;
import fr.mrmicky.fastinv.ItemBuilder;
import fr.mrmicky.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class HomeEditingInventory extends FastInv {


	// -------------------------------------------------- //


	public HomeEditingInventory(Player player, EXGHome home) {
		super(54, "Editing '" + home.getName() + "' home");

		setItems(getBorders(), new ItemBuilder(Material.STAINED_GLASS_PANE).data(15).name(" ").build());


		setItem(22, new ItemBuilder(home.getMaterial()).name(home.getDisplayName()).lore("§7§oHome preview").build());


		setItem(30, new ItemBuilder(Material.NAME_TAG).name("§eChange name").build(), e -> {

			player.closeInventory();
			player.sendMessage("§ePlease enter the new display name in the chat. §7§oType 'cancel' to cancel the action.");
			Main.getInstance().getChatManager().addChat(player.getUniqueId(), newHomeName -> {

				if (newHomeName.equalsIgnoreCase("cancel")) {
					player.sendMessage("§cThe action has been canceled.");
					new HomeEditingInventory(player, home).open(player);
					return;
				}

				if (newHomeName.length() > 16) {
					player.sendMessage("§cThe display name of the home must be less than 16 characters.");
					new HomeEditingInventory(player, home).open(player);
					return;
				}

				home.setDisplayName(newHomeName);
				new HomeEditingInventory(player, home).open(player);

			}, 10);
		});


		setItem(32, new ItemBuilder(home.getMaterial()).name("§eChange icon").build(), e -> {

			player.closeInventory();
			player.sendMessage("§ePlease enter the new icon name in the chat. §7§oType 'cancel' to cancel the action.");
			Main.getInstance().getChatManager().addChat(player.getUniqueId(), materialName -> {

				if (materialName.equalsIgnoreCase("cancel")) {
					player.sendMessage("§cThe action has been canceled.");
					new HomeEditingInventory(player, home).open(player);
					return;
				}

				Material material = Material.matchMaterial(materialName);

				if (material == null) {
					player.sendMessage("§cThe material name is invalid.");
					new HomeEditingInventory(player, home).open(player);
					return;
				}

				home.setMaterial(material);
				new HomeEditingInventory(player, home).open(player);

			}, 10);
		});


		setItem(49, new ItemBuilder(Material.ARROW).name("§cBack").build(), e -> {
			new HomesInventory(player).open(player);
		});
	}


	// -------------------------------------------------- //
}
