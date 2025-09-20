package fr.snipertvmc.essentialsxgui.infrastructure.models;

import fr.snipertvmc.essentialsxgui.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EXGHome {


	// -------------------------------------------------- //


	private final String name;

	private String displayName;
	private Material material;
	private byte data;

	private ItemStack customItemStack;


	// -------------------------------------------------- //


	public EXGHome(String name) {
		this.name = name;
		this.displayName = name;
		this.material = switch (Main.getInstance().getMCServerVersion()) {
			case v1_8_8, v1_9_4, v1_10_2, v1_11_2, v1_12_2 -> Material.matchMaterial("GRASS");
			default -> Material.matchMaterial("GRASS_BLOCK");
		};
		this.data = 0;
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
	public byte getData() {
		return data;
	}

	public ItemStack getCustomItemStack() {
		return customItemStack;
	}


	// -------------------------------------------------- //


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}
	public void setData(byte data) {
		this.data = data;
	}

	public void setCustomItemStack(ItemStack customItemStack) {
		this.customItemStack = customItemStack;
	}


	// -------------------------------------------------- //
}
