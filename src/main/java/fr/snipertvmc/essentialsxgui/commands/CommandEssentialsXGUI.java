package fr.snipertvmc.essentialsxgui.commands;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import org.bukkit.Bukkit;
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

		if (!(commandSender instanceof Player player)) {
			sendAboutMessage(commandSender);
			return true;
		}

		if (args.length == 0 || !Main.getInstance().getConfiguration().hasEssentialsXGUICommand(player)) {
			sendAboutMessage(player);
			return true;
		}

		String firstArg = args[0].toLowerCase();

		switch (firstArg) {


			// RELOAD ARGUMENT
			case "reload" -> {
				player.sendMessage(MessagesUtils.get(EXGMessage.FILES_RELOADING, null));
				Main.getInstance().getFilesManager().reloadFiles();
				player.sendMessage(MessagesUtils.get(EXGMessage.FILES_RELOADED, null));
			}


			// ABOUT ARGUMENT
			case "about" -> sendAboutMessage(player);


			// NOT FOUND ARGUMENT
			default -> player.sendMessage(MessagesUtils.get(EXGMessage.ARGUMENT_NOT_FOUND, Map.of("argument", firstArg)));
		}

    	return true;
    }


	// -------------------------------------------------- //


	public void sendAboutMessage(CommandSender commandSender) {

		commandSender.sendMessage("");
		commandSender.sendMessage("  §6EssentialsX-GUI §7- §fPlugin by §eSniper_TVmc");
		commandSender.sendMessage("    §8■ §7§oEssentialsX-GUI is an addon for EssentialsX that adds some GUIs to the plugin.");
		commandSender.sendMessage("");
		commandSender.sendMessage("    §8■ §7Version: §b" + Main.getInstance().getDescription().getVersion());
		commandSender.sendMessage("    §8■ §7Discord: §3discord.gg/fSzK79TAYf");
		commandSender.sendMessage("    §8■ §7Spigot: §6spigotmc.org/resources/§k100000");
		commandSender.sendMessage("    §8■ §7GitHub: §fgithub.com/SniperTVmc/EssentialsX-GUI");
		commandSender.sendMessage("");
	}


	// -------------------------------------------------- //


	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {

		List<String> options = new ArrayList<>();

		if (args.length == 1) {

			List<String> firstArgs = List.of("about", "reload");
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
