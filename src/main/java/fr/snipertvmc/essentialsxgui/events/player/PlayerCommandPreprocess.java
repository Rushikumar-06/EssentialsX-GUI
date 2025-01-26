package fr.snipertvmc.essentialsxgui.events.player;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.inventories.homes.HomesInventory;
import fr.snipertvmc.essentialsxgui.inventories.kits.KitsAdminInventory;
import fr.snipertvmc.essentialsxgui.inventories.kits.KitsPlayerInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class PlayerCommandPreprocess implements Listener {


	// -------------------------------------------------- //


	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

		Player player = event.getPlayer();

		String command = event.getMessage()
				.split(" ")[0]
				.replaceFirst("/", "")
				.replace("essentials:", "")
				.toLowerCase();

		String[] args = event.getMessage()
				.split(" ");

		if (args.length > 1) {
			return;
		}

		List<String> commands = List.of(
				"home", "homes",
				"kit", "kits");
		if (!commands.contains(command)) {
			return;
		}

		event.setCancelled(true);

		switch (command) {

			//
			// HOMES
			//

			case "home", "homes" -> {

				player.sendMessage("§aOpening homes inventory...");
				new HomesInventory(player).open(player);
			}

			//
			// KITS
			//

			case "kit", "kits" -> {

				if (Main.getInstance().getConfiguration().hasKitsAdminAccess(player)) {
					player.sendMessage("§aOpening kits (admin mode) inventory...");
					new KitsAdminInventory(player).open(player);

				} else {
					player.sendMessage("§aOpening kits (player mode) inventory...");
					new KitsPlayerInventory(player).open(player);
				}
			}
		}

	}


	// -------------------------------------------------- //
}
