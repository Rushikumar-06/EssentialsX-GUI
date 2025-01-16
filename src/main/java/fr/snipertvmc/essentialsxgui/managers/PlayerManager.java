package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;

import java.util.*;

public class PlayerManager {


	// -------------------------------------------------- //


	private Set<EXGPlayer> players = new HashSet<>();


	// -------------------------------------------------- //


	public void initialize(String uuid) {

		EXGPlayer exgPlayer = new EXGPlayer(uuid);
		players.add(exgPlayer);

		Map<String, Object> playerData = Main.getInstance().getPlayerDataManager().loadPlayerData(uuid);
		Map<String, Object> homes = (Map<String, Object>) playerData.get("homes");
		exgPlayer.setHomesRaw(homes);
	}


	public void save(String uuid) {

		EXGPlayer exgPlayer = getPlayer(uuid);

		Map<String, Object> homes = exgPlayer.getHomesRaw();
		Map<String, Object> playerData = new HashMap<>() {{
			put("homes", homes);
		}};
		Main.getInstance().getPlayerDataManager().savePlayerData(uuid, playerData);
	}


	// -------------------------------------------------- //


	public EXGPlayer getPlayer(String uuid) {
		return players.stream()
				.filter(exgPlayer -> exgPlayer.getUuid().equals(UUID.fromString(uuid)))
				.findFirst()
				.orElseGet(() -> {

					initialize(uuid);
					return getPlayer(uuid);
				});
	}


	public Set<EXGPlayer> getPlayers() {
		return players;
	}


	// -------------------------------------------------- //
}
