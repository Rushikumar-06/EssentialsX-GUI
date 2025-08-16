package fr.snipertvmc.essentialsxgui.events.player;

import fr.snipertvmc.essentialsxgui.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {


	// -------------------------------------------------- //


	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {

		Player player = event.getPlayer();

		Main.getInstance().getPlayerManager().initialize(player.getUniqueId().toString());
		Main.getInstance().getPlayerDataManager().cleanPlayerData(player.getUniqueId().toString());

		Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
			Main.getInstance().getLoadingManager().alertPlayerForUpdate(player);
		}, 20L);
	}


	// -------------------------------------------------- //
}
