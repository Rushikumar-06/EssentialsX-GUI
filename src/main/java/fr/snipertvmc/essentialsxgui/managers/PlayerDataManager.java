package fr.snipertvmc.essentialsxgui.managers;

import com.earth2me.essentials.User;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;
import fr.snipertvmc.essentialsxgui.utilities.type.JsonUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class PlayerDataManager {


	// -------------------------------------------------- //


	public Map<String, Object> generatePlayerHomes(EXGPlayer player) {

		Map<String, Object> playerHomes = new HashMap<>();

		List<String> essentialsHomes = Main.getInstance().getEssentials().getUser(player.getName()).getHomes();

		for (String homeName : essentialsHomes) {

			String defaultMaterial = switch (Main.getInstance().getMCServerVersion()) {
				case v1_8_8, v1_9_4, v1_10_2, v1_11_2, v1_12_2 -> "GRASS";
				default -> "GRASS_BLOCK";
			};

			playerHomes.put(homeName, new HashMap<>() {{
				put("displayName", homeName);
				put("material", defaultMaterial);
				put("data", "0");
				put("customItemStack", null);
			}});
		}

		return playerHomes;
	}


	// -------------------------------------------------- //


	public void cleanPlayerHomes(EXGPlayer exgPlayer) {

		User user = Main.getInstance().getEssentials().getUser(exgPlayer.getName());


		// HOMES CLEANING
		List<String> essentialsHomes = user.getHomes();
		Set<EXGHome> playerHomes = exgPlayer.getHomes();

		Set<EXGHome> cleanedHomes = new HashSet<>();

		for (EXGHome home : playerHomes) {
			if (essentialsHomes.contains(home.getName())) {
				cleanedHomes.add(home);
			}
		}

		for (String homeName : essentialsHomes) {
			if (playerHomes.stream().noneMatch(home -> home.getName().equals(homeName))) {
				cleanedHomes.add(new EXGHome(homeName));
			}
		}

		exgPlayer.setHomes(cleanedHomes);
	}


	// -------------------------------------------------- //
}
