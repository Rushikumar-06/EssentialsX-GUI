package fr.snipertvmc.essentialsxgui.infrastructure.models;

import fr.snipertvmc.essentialsxgui.Main;
import org.bukkit.Material;

public class EXGHome {


	// -------------------------------------------------- //


	private final String name;

	private String displayName;
	private Material material;


	// -------------------------------------------------- //


	public EXGHome(String name) {
		this.name = name;
		this.displayName = name;
		this.material = switch (Main.getInstance().getMCServerVersion()) {
			case v1_8_8, v1_9_4, v1_10_2, v1_11_2, v1_12_2 -> Material.matchMaterial("GRASS");
			default -> Material.matchMaterial("GRASS_BLOCK");
		};
	}


	// -------------------------------------------------- //


	public String getName() {
		return name;
	}


	public String getDisplayName() {
		return displayName;
	}


	public Material getMaterial() {
		return material;
	}


	// -------------------------------------------------- //


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public void setMaterial(Material material) {
		this.material = material;
	}


	// -------------------------------------------------- //
}
