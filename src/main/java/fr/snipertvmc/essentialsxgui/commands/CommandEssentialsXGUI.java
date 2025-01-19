package fr.snipertvmc.essentialsxgui.commands;

import fr.snipertvmc.essentialsxgui.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandEssentialsXGUI implements CommandExecutor {


	// -------------------------------------------------- //


	@Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

		commandSender.sendMessage("");
		commandSender.sendMessage("  §6EssentialsX-GUI §7- §fPlugin by §eSniper_TVmc");
		commandSender.sendMessage("    §8■ §7§oEssentialsX-GUI is an addon for EssentialsX that adds some GUIs to the plugin.");
		commandSender.sendMessage("");
		commandSender.sendMessage("    §8■ §7Version: §b" + Main.getInstance().getDescription().getVersion());
		commandSender.sendMessage("    §8■ §7Discord: §3discord.gg/§k8ZzQ6Q5");
		commandSender.sendMessage("    §8■ §7Spigot: §6spigotmc.org/resources/§k100000");
		commandSender.sendMessage("    §8■ §7GitHub: §fgithub.com/SniperTVmc/EssentialsX-GUI");
		commandSender.sendMessage("");

    	return true;
    }


	// -------------------------------------------------- //
}
