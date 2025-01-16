package fr.snipertvmc.essentialsxgui.commands;

import fr.snipertvmc.essentialsxgui.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEssentialsXGUI implements CommandExecutor {


	// -------------------------------------------------- //


	@Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

    	if (!(commandSender instanceof Player player)) {
    		commandSender.sendMessage("§cVous ne pouvez pas utiliser cette commande dans la console.");
    		return true;
    	}

		player.sendMessage("");
		player.sendMessage("  §6EssentialsX-GUI §7- §fPlugin by §eSniper_TVmc");
		player.sendMessage("    §8■ §7§oEssentialsX-GUI is an addon for EssentialsX that adds some GUIs to the plugin.");
		player.sendMessage("");
		player.sendMessage("    §8■ §7Version: §b" + Main.getInstance().getDescription().getVersion());
		player.sendMessage("    §8■ §7Discord: §3discord.gg/§k8ZzQ6Q5");
		player.sendMessage("    §8■ §7Spigot: §6spigotmc.org/resources/§k100000");
		player.sendMessage("    §8■ §7GitHub: §fgithub.com/SniperTVmc/EssentialsX-GUI");
		player.sendMessage("");

    	return true;
    }


	// -------------------------------------------------- //
}
