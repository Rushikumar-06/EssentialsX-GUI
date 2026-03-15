package fr.snipertvmc.essentialsxgui.commands;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.PluginDebugUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
			case "debug" -> sendDebugLinkMessage(commandSender);

			// NOT FOUND ARGUMENT
			default -> TextUtils.sendComponentToCommandSender(commandSender, MessagesUtils.getComponent(EXGMessage.ARGUMENT_NOT_FOUND, Map.of("argument", firstArg)));
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
		commandSender.sendMessage("    §8■ §7/exg debug §7- §fGet a debug link to help you or developers.");
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


	public void sendDebugLinkMessage(CommandSender commandSender) {

		if (!(commandSender instanceof Player player)) {
			TextUtils.sendComponentToCommandSender(commandSender, MessagesUtils.getComponent(EXGMessage.ONLY_FOR_PLAYERS, null));
			return;
		}

		EXGPlayer exgPlayer = Main.getInstance().getPlayerManager().getPlayer(player);
		PluginDebugUtils.sendDebugToPrivateBin(exgPlayer);
	}


	// -------------------------------------------------- //


	public void reloadPlugin(CommandSender commandSender) {


		// FILES RELOADING
		TextUtils.sendComponentToCommandSender(commandSender, MessagesUtils.getComponent(EXGMessage.FILES_RELOADING, null));
		Main.getInstance().getFilesManager().reloadFiles();
		TextUtils.sendComponentToCommandSender(commandSender, MessagesUtils.getComponent(EXGMessage.FILES_RELOADED, null));


		// DATABASE RELOADING
		TextUtils.sendComponentToCommandSender(commandSender, MessagesUtils.getComponent(EXGMessage.DATABASE_RELOADING, null));

		Main.getInstance().getDatabaseManager().disconnectAllDatabases();
		Main.getInstance().getDatabaseManager().updateDatabaseStorage();
		Main.getInstance().getDatabaseManager().connectAllDatabases();

		String newStorageType = Main.getInstance().getConfiguration().getStorageType();
		TextUtils.sendComponentToCommandSender(commandSender, MessagesUtils.getComponent(EXGMessage.DATABASE_RELOADED, Map.of("newStorageType", newStorageType)));
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
