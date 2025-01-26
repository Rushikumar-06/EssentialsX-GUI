package fr.snipertvmc.essentialsxgui.infrastructure.models;

import org.bukkit.Material;

public class EXGKit {


	// -------------------------------------------------- //


	private final String name;

	private String displayName;
	private Material material;


	// -------------------------------------------------- //


	public EXGKit(String name) {
		this.name = name;
		this.displayName = name;
		this.material = Material.GRASS;
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
