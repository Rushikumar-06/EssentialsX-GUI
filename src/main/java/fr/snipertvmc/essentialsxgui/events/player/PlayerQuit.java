package fr.snipertvmc.essentialsxgui.events.player;

import fr.snipertvmc.essentialsxgui.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {


	// -------------------------------------------------- //


	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {

		Player player = event.getPlayer();

		Main.getInstance().getPlayerManager().save(player.getUniqueId().toString());
	}


	// -------------------------------------------------- //
}
