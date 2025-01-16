package fr.snipertvmc.essentialsxgui.events.player;

import fr.snipertvmc.essentialsxgui.inventories.homes.HomesInventory;
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

		List<String> commands = List.of("home", "homes");
		if (!commands.contains(command)) {
			return;
		}

		event.setCancelled(true);

		switch (command) {

			case "home", "homes" -> {
				player.sendMessage("§aOpening homes inventory...");
				new HomesInventory(player).open(player);
			}
		}

	}


	// -------------------------------------------------- //
}
