package fr.snipertvmc.essentialsxgui.commands;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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


			// DEBUG ARGUMENT
			case "debug" -> {
				if (Main.getInstance().getConfiguration().hasEssentialsXGUICommandDebugArgument(player)) {
					showDebugMessage(player);
				} else {
					player.sendMessage(MessagesUtils.get(EXGMessage.NO_PERMISSION, null));
				}
			}


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


	public void showDebugMessage(CommandSender commandSender) {

		EXGPlayer exgPlayer = Main.getInstance().getPlayerManager().getPlayer(((Player) commandSender).getUniqueId().toString());
		Set<EXGHome> homes = exgPlayer.getHomes();
		Set<EXGKit> kits = Main.getInstance().getEXGServer().getKits();

		commandSender.sendMessage("");
		commandSender.sendMessage("  §dDebug information §7- §f" + commandSender.getName());
		commandSender.sendMessage("");
		commandSender.sendMessage("  §eHomes list §7- §6" + homes.size() + " home(s)");
		for (EXGHome home : homes) {
			commandSender.sendMessage("    §8■ §f" + home.getDisplayName() + " §7§o(" + home.getName() + ") §7- §f" + home.getMaterial());
		}
		commandSender.sendMessage("");
		commandSender.sendMessage("  §bKits list §7- §3" + homes.size() + " kit(s)");
		for (EXGKit kit : kits) {
			commandSender.sendMessage("    §8■ §f" + kit.getDisplayName() + " §7§o(" + kit.getName() + ") §7- §f" + kit.getMaterial());
		}
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
