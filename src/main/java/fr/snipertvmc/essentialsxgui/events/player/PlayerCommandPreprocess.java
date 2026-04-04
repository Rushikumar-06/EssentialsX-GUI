package fr.snipertvmc.essentialsxgui.events.player;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGPermission;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.inventories.homes.HomesInventory;
import fr.snipertvmc.essentialsxgui.inventories.kits.KitsAdminViewInventory;
import fr.snipertvmc.essentialsxgui.inventories.kits.KitsPlayerViewInventory;
import fr.snipertvmc.essentialsxgui.inventories.warps.WarpsAdminViewInventory;
import fr.snipertvmc.essentialsxgui.inventories.warps.WarpsPlayerViewInventory;
import fr.snipertvmc.essentialsxgui.inventories.whois.WhoisPlayersInventory;
import fr.snipertvmc.essentialsxgui.inventories.whois.WhoisViewInventory;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.Map;

public class PlayerCommandPreprocess implements Listener {


	// -------------------------------------------------- //



	private final List<String> commands = List.of(
			"home", "homes",
			"kit", "kits",
			"warp", "warps",
			"whois"
	);


	// -------------------------------------------------- //


	@EventHandler(ignoreCancelled = true)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

		Player player = event.getPlayer();
		String command = event.getMessage().split(" ")[0].replaceFirst("/", "").replace("essentials:", "").toLowerCase();
		String[] args = event.getMessage().split(" ");

		if (!commands.contains(command)) return;

		switch (command) {


			//
			// HOMES
			//

			case "home", "homes" -> {

				if (args.length > 1) return;
				if (!player.hasPermission("essentials.home")) return;

				if (!Main.getInstance().getConfiguration().isHomesModuleEnabled()) {
					return;
				}
				event.setCancelled(true);

				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_HOMES_INVENTORY, null));
				new HomesInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
			}


			//
			// KITS
			//

			case "kit", "kits" -> {

				if (args.length > 1) return;
				if (!player.hasPermission("essentials.kit")) return;

				if (!Main.getInstance().getConfiguration().isKitsModuleEnabled()) {
					return;
				}

				event.setCancelled(true);

				if (player.hasPermission(EXGPermission.KITS_ADMIN.get())
						&& Main.getInstance().getConfiguration().mustOpenKitAdminViewByDefault()) {

					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_ADMIN_KITS_INVENTORY, null));
					new KitsAdminViewInventory(player, null, null).open(player);

				} else {
					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_PLAYER_KITS_INVENTORY, null));
					new KitsPlayerViewInventory(player, null, null).open(player);
				}

				SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
			}


			//
			// WARPS
			//

			case "warp", "warps" -> {

				if (args.length > 1) return;
				if (!player.hasPermission("essentials.warp")) return;

				if (!Main.getInstance().getConfiguration().isWarpsModuleEnabled()) {
					return;
				}

				event.setCancelled(true);

				if (player.hasPermission(EXGPermission.WARPS_ADMIN.get())
						&& Main.getInstance().getConfiguration().mustOpenWarpAdminViewByDefault()) {

					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_ADMIN_KITS_INVENTORY, null));
					new WarpsAdminViewInventory(player, null, null).open(player);

				} else {
					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_PLAYER_KITS_INVENTORY, null));
					new WarpsPlayerViewInventory(player, null, null).open(player);
				}

				SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
			}


			//
 			// WHOIS
			//

			case "whois" -> {

				if (!player.hasPermission("essentials.whois")) return;

				event.setCancelled(true);

				if (args.length > 1) {
					String targetPlayerName = args[1];

					Player targetPlayer = Main.getInstance().getServer().getPlayerExact(targetPlayerName);
					if (targetPlayer == null) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.PLAYER_NOT_FOUND, Map.of("player", targetPlayerName)));
						return;
					}

					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_WHOIS_INVENTORY));
					new WhoisViewInventory(player, targetPlayer).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
					return;
				}

				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_WHOIS_INVENTORY));
				new WhoisPlayersInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
			}
		}

	}


	// -------------------------------------------------- //
}
