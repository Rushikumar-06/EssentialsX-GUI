package fr.snipertvmc.essentialsxgui.utilities;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGWarp;
import nl.kyllian.enums.Expire;
import nl.kyllian.models.Paste;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;

public class PluginDebugUtils {


	// -------------------------------------------------- //


	public static void sendDebugToPrivateBin(EXGPlayer exgPlayer) {


		// Define data to collect
		Set<EXGHome> homes = exgPlayer.getHomes();
		Set<EXGKit> kits = Main.getInstance().getEXGServer().getKits();
		Set<EXGWarp> warps = Main.getInstance().getEXGServer().getWarps();

		String currentLocalDataTime = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));
		String parisDateTime = ZonedDateTime.now(java.time.ZoneId.of("Europe/Paris")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));

		StringBuilder content = new StringBuilder();


		// Create debug content
		content.append("=== DEBUG INFORMATION ===\n\n");

		content.append("Server information:\n");
		content.append("Name: ").append(Main.getInstance().getServer().getName()).append("\n");
		content.append("Version: ").append(Main.getInstance().getServer().getVersion()).append("\n");
		content.append("Bukkit Version: ").append(Main.getInstance().getServer().getBukkitVersion()).append("\n");
		content.append("EssentialsX: ").append(Main.getInstance().getEssentials().getDescription().getVersion()).append("\n");
		content.append("Plugin Version: ").append(Main.getInstance().getDescription().getVersion()).append("\n");
		content.append("Local date: ").append(currentLocalDataTime).append("\n");
		content.append("Paris date: ").append(parisDateTime).append("\n\n");

		content.append("System information:\n");
		content.append("Java: ").append(System.getProperty("java.version")).append("\n");
		content.append("Java Vendor: ").append(System.getProperty("java.vendor")).append("\n");
		content.append("OS: ").append(System.getProperty("os.name")).append("\n");
		content.append("OS Version: ").append(System.getProperty("os.version")).append("\n");
		content.append("OS Architecture: ").append(System.getProperty("os.arch")).append("\n");
		content.append("CPU Cores: ").append(Runtime.getRuntime().availableProcessors()).append("\n");
		content.append("RAM (Total/Max/Free in MB): ")
				.append(Runtime.getRuntime().totalMemory() / 1048576L).append(" / ")
				.append(Runtime.getRuntime().maxMemory() / 1048576L).append(" / ")
				.append(Runtime.getRuntime().freeMemory() / 1048576L).append("\n");
		content.append("Disk (Total/Free in MB): ")
				.append(new File(".").getTotalSpace() / 1073741824L).append(" / ")
				.append(new File(".").getFreeSpace() / 1073741824L).append("\n");
		content.append("Uptime (ms/s/h): ")
				.append(Main.getInstance().getLoadingManager().getUptimeInMilliseconds()).append(" / ")
				.append(Main.getInstance().getLoadingManager().getUptimeInSeconds()).append(" / ")
				.append(Main.getInstance().getLoadingManager().getUptimeInHours()).append("\n\n");

		content.append("\nHomes (").append(homes.size()).append("):\n");
		for (EXGHome home : homes) {
			content.append("- ").append(home.getName()).append(" | ").append(home.getMaterial()).append(" ( ").append(home.getData()).append(")\n");
		}

		content.append("\nKits (").append(kits.size()).append("):\n");
		for (EXGKit kit : kits) {
			content.append("- ").append(kit.getName()).append(" | ").append(kit.getMaterial()).append(" ( ").append(kit.getData()).append(")\n");
		}

		content.append("\nWarps (").append(warps.size()).append("):\n");
		for (EXGWarp warp : warps) {
			content.append("- ").append(warp.getName()).append(" | ").append(warp.getMaterial()).append(" ( ").append(warp.getData()).append(")\n");
		}

		content.append("\n=== END OF DEBUG INFORMATION ===\n");


		// Upload debug to PrivateBin
		String password = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
		Paste paste = new Paste("https://privatebin.net/")
				.setMessage(content.toString())
				.setUserPassword(password)
				.setExpire(Expire.ONE_WEEK)
				.encrypt();


		// Send upload task asynchronously
		Main.getInstance().getServer().getScheduler().runTaskAsynchronously(

				Main.getInstance(),
				() -> {
					try {
						String pasteUrl = paste.send();
						if (pasteUrl != null) {

							exgPlayer.getPlayer().sendMessage("§a§l✔ Debug successfully uploaded!");
							exgPlayer.getPlayer().sendMessage("§7Link: §f" + pasteUrl);
							exgPlayer.getPlayer().sendMessage("§7Password: §c" + password);
							exgPlayer.getPlayer().sendMessage("§7§o(Expires in 1 month)");
							exgPlayer.getPlayer().sendMessage("§8→ §7Please share this link with EssentialsX-GUI developers to help you faster.");
							exgPlayer.getPlayer().sendMessage("§8→ §7The password is required to access the debug, don't share it publicly.");

						} else {
							exgPlayer.getPlayer().sendMessage("§cError uploading debug, please try again later.");
						}

					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
	}


	// -------------------------------------------------- //
}
