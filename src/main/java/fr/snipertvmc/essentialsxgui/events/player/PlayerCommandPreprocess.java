package fr.snipertvmc.essentialsxgui.events.player;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.inventories.homes.HomesInventory;
import fr.snipertvmc.essentialsxgui.inventories.kits.KitsAdminViewInventory;
import fr.snipertvmc.essentialsxgui.inventories.kits.KitsPlayerViewInventory;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
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

		switch (command) {

			//
			// HOMES
			//

			case "home", "homes" -> {

				if (!Main.getInstance().getConfiguration().isHomesModuleEnabled()) {
					return;
				}

				event.setCancelled(true);

				player.sendMessage(MessagesUtils.get(EXGMessage.OPENING_HOMES_INVENTORY, null));
				new HomesInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
			}

			//
			// KITS
			//

			case "kit", "kits" -> {

				if (!Main.getInstance().getConfiguration().isKitsModuleEnabled()) {
					return;
				}

				event.setCancelled(true);

				if (Main.getInstance().getConfiguration().hasKitsAdminAccess(player)) {
					player.sendMessage(MessagesUtils.get(EXGMessage.OPENING_ADMIN_KITS_INVENTORY, null));
					new KitsAdminViewInventory(player, null, null).open(player);

				} else {
					player.sendMessage(MessagesUtils.get(EXGMessage.OPENING_PLAYER_KITS_INVENTORY, null));
					new KitsPlayerViewInventory(player, null, null).open(player);
				}

				SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
			}
		}

	}


	// -------------------------------------------------- //
}
