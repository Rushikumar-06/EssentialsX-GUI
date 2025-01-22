package fr.snipertvmc.essentialsxgui.infrastructure.models;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class EXGPlayer {


	// -------------------------------------------------- //


	private final UUID uuid;
	private final String name;

	private final Player player;


	private Set<EXGHome> homes = new HashSet<>();


	// -------------------------------------------------- //


	public EXGPlayer(String uuid) {
		this.uuid = UUID.fromString(uuid);

		this.player = Bukkit.getPlayer(this.uuid);
		this.name = player.getName();
	}


	// -------------------------------------------------- //


	public UUID getUuid() {
		return uuid;
	}
	public String getName() {
		return name;
	}

	public Player getPlayer() {
		return player;
	}


	// -------------------------------------------------- //


	public Set<EXGHome> getHomes() {
		return homes;
	}


	public EXGHome getHome(String homeName) {
		return homes.stream()
				.filter(home -> home.getName().equals(homeName))
				.findFirst()
				.orElse(null);
	}


	public void setHomes(Set<EXGHome> homes) {
		this.homes = homes;
	}


	// -------------------------------------------------- //


	public Map<String, Object> getHomesRaw() {

		Map<String, Object> homes = new HashMap<>();

		this.homes.forEach(home -> {
			homes.put(home.getName(), new HashMap<>() {{
				put("displayName", home.getDisplayName());
				put("material", home.getMaterial().toString());
			}});
		});

		return homes;
	}


	public void setHomesRaw(Map<String, Object> homes) {
		homes.forEach((homeName, homeData) -> {
			EXGHome home = new EXGHome(homeName);
			home.setDisplayName((String) ((Map<String, Object>) homeData).get("displayName"));
			home.setMaterial(Material.valueOf((String) ((Map<String, Object>) homeData).get("material")));
			this.homes.add(home);
		});
	}


	// -------------------------------------------------- //
}
