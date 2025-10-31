package fr.snipertvmc.essentialsxgui.commands;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGWarp;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandEssentialsXGUI implements CommandExecutor, TabCompleter {


	// -------------------------------------------------- //


	@Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

		if (args.length == 0 || (commandSender instanceof Player player && !Main.getInstance().getConfiguration().hasEssentialsXGUICommand(player))) {
			sendHelpMessage(commandSender);
			return true;
		}

		String firstArg = args[0].toLowerCase();
		switch (firstArg) {

			// HELP ARGUMENT
			case "help" -> sendHelpMessage(commandSender);

			// ABOUT ARGUMENT
			case "about" -> sendAboutMessage(commandSender);

			// RELOAD ARGUMENT
			case "reload" -> reloadPlugin(commandSender);

			// DEBUG ARGUMENT
			case "debug" -> showDebugMessage(commandSender);

			// NOT FOUND ARGUMENT
			default -> commandSender.sendMessage(MessagesUtils.get(EXGMessage.ARGUMENT_NOT_FOUND, Map.of("argument", firstArg)));
		}

    	return true;
    }


	// -------------------------------------------------- //


	public void sendHelpMessage(CommandSender commandSender) {

		commandSender.sendMessage("");
		commandSender.sendMessage("  §6EssentialsX-GUI §7- §fHelp");
		commandSender.sendMessage("");
		commandSender.sendMessage("    §8■ §7/exg help §7- §fDisplay this help message.");
		commandSender.sendMessage("    §8■ §7/exg about §7- §fDisplay information about the plugin.");
		commandSender.sendMessage("    §8■ §7/exg reload §7- §fReload the plugin files.");
		commandSender.sendMessage("    §8■ §7/exg debug §7- §fDisplay debug informations.");
		commandSender.sendMessage("");
	}


	public void sendAboutMessage(CommandSender commandSender) {

		commandSender.sendMessage("");
		commandSender.sendMessage("  §6EssentialsX-GUI §7- §fPlugin by §eSniper_TVmc");
		commandSender.sendMessage("    §8■ §7§oEssentialsX-GUI is an addon for EssentialsX that adds some GUIs to the plugin.");
		commandSender.sendMessage("");
		commandSender.sendMessage("    §8■ §7Version: §b" + Main.getInstance().getDescription().getVersion());
		commandSender.sendMessage("    §8■ §7Discord: §3discord.gg/fSzK79TAYf");
		commandSender.sendMessage("    §8■ §7Spigot: §6spigotmc.org/resources/127805");
		commandSender.sendMessage("    §8■ §7GitHub: §fgithub.com/SniperTVmc/EssentialsX-GUI");
		commandSender.sendMessage("");
	}


	public void showDebugMessage(CommandSender commandSender) {

		if (!(commandSender instanceof Player player)) {
			commandSender.sendMessage(MessagesUtils.get(EXGMessage.ONLY_FOR_PLAYERS, null));
			return;
		}

		EXGPlayer exgPlayer = Main.getInstance().getPlayerManager().getPlayer(player);
		Set<EXGHome> homes = exgPlayer.getHomes();
		Set<EXGKit> kits = Main.getInstance().getEXGServer().getKits();
		Set<EXGWarp> warps = Main.getInstance().getEXGServer().getWarps();

		String currentLocalDataTime = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));

		commandSender.sendMessage("");
		commandSender.sendMessage("  §d§lDebug information §7- §f" + commandSender.getName());
		commandSender.sendMessage("");
		commandSender.sendMessage("    §8■ §bServer informations");
		commandSender.sendMessage("      §8▢ §fName: §7" + Main.getInstance().getServer().getName());
		commandSender.sendMessage("      §8▢ §fVersion: §7" + Main.getInstance().getServer().getVersion());
		commandSender.sendMessage("      §8▢ §fBukkit Version: §7" + Main.getInstance().getServer().getBukkitVersion());
		commandSender.sendMessage("      §8▢ §fEssentialsX Version: §7" + Main.getInstance().getEssentials().getDescription().getVersion());
		commandSender.sendMessage("      §8▢ §fEssentialsX-GUI Version: §7" + Main.getInstance().getDescription().getVersion());
		commandSender.sendMessage("      §8▢ §fLocal date and time: §7" + currentLocalDataTime);
		commandSender.sendMessage("");
		commandSender.sendMessage("    §8■ §bHomes list §7- §3" + homes.size() + " home(s)");
		for (EXGHome home : homes) {
			commandSender.sendMessage("      §8▢ §f" + home.getDisplayName() + " §7§o(" + home.getName() + ") §7- §f" + home.getMaterial() + ":" + home.getData());
		}
		commandSender.sendMessage("");
		commandSender.sendMessage("    §8■ §bKits list §7- §3" + kits.size() + " kit(s)");
		for (EXGKit kit : kits) {
			commandSender.sendMessage("      §8▢ §f" + kit.getDisplayName() + " §7§o(" + kit.getName() + ") §7- §f" + kit.getMaterial() + ":" + kit.getData());
		}
		commandSender.sendMessage("");
		commandSender.sendMessage("    §8■ §bWarps list §7- §3" + warps.size() + " warp(s)");
		for (EXGWarp warp : warps) {
			commandSender.sendMessage("      §8▢ §f" + warp.getDisplayName() + " §7§o(" + warp.getName() + ") §7- §f" + warp.getMaterial() + ":" + warp.getData());
		}
		commandSender.sendMessage("");
	}


	// -------------------------------------------------- //


	public void reloadPlugin(CommandSender commandSender) {


		// FILES RELOADING
		commandSender.sendMessage(MessagesUtils.get(EXGMessage.FILES_RELOADING, null));
		Main.getInstance().getFilesManager().reloadFiles();
		commandSender.sendMessage(MessagesUtils.get(EXGMessage.FILES_RELOADED, null));


		// DATABASE RELOADING
		commandSender.sendMessage(MessagesUtils.get(EXGMessage.DATABASE_RELOADING, null));

		Main.getInstance().getDatabaseManager().disconnectAllDatabases();
		Main.getInstance().getDatabaseManager().updateDatabaseStorage();
		Main.getInstance().getDatabaseManager().connectAllDatabases();

		String newStorageType = Main.getInstance().getConfiguration().getStorageType();
		commandSender.sendMessage(MessagesUtils.get(EXGMessage.DATABASE_RELOADED, Map.of("newStorageType", newStorageType)));
	}


	// -------------------------------------------------- //


	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {

		List<String> options = new ArrayList<>();

		if (args.length == 1) {

			List<String> firstArgs = List.of("help", "about", "reload", "debug");
			String input = args[0];

			for (String option : firstArgs) {
				if (option.startsWith(input.toLowerCase()) || option.startsWith(input.toUpperCase())) {
					options.add(option);
				}
			}
		}

		return options;
	}


	// -------------------------------------------------- //
}
