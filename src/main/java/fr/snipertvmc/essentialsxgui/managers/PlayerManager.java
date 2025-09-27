package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;
import fr.snipertvmc.essentialsxgui.utilities.type.JsonUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager {


	// -------------------------------------------------- //


	private Set<EXGPlayer> players = new HashSet<>();


	// -------------------------------------------------- //


	public EXGPlayer initialize(Player player) {

		EXGPlayer exgPlayer = new EXGPlayer(player);
		players.add(exgPlayer);

		Map<String, Object> homesRaw;

		if (!Main.getInstance().getDatabaseManager().getPlayerHomesTableManager().isPlayerExists(exgPlayer.getName())) {
			homesRaw = Main.getInstance().getPlayerDataManager().generatePlayerHomes(exgPlayer);

			Main.getInstance().getDatabaseManager().getPlayerHomesTableManager().insertPlayer(exgPlayer.getName(), homesRaw);

		} else {
			homesRaw = Main.getInstance().getDatabaseManager().getPlayerHomesTableManager().fetchHomes(exgPlayer.getName());
		}

		exgPlayer.setHomesRaw(homesRaw);
		return exgPlayer;
	}


	public void save(EXGPlayer exgPlayer) {

		Map<String, Object> homesRaw = exgPlayer.getHomesRaw();
		Main.getInstance().getDatabaseManager().getPlayerHomesTableManager().updateHomes(exgPlayer.getName(), JsonUtils.mapToJson(homesRaw));
	}


	public void saveAll() {
		for (EXGPlayer exgPlayer : players) {
			if (exgPlayer != null) {
				save(exgPlayer);
			}
		}
	}


	// -------------------------------------------------- //


	public EXGPlayer getPlayer(Player player) {
		return players.stream()
				.filter(exgPlayer -> exgPlayer.getUuid().equals(player.getUniqueId()))
				.findFirst()
				.orElseGet(() -> {

					initialize(player);
					return getPlayer(player);
				});
	}


	public Set<EXGPlayer> getPlayers() {
		return players;
	}


	// -------------------------------------------------- //
}
