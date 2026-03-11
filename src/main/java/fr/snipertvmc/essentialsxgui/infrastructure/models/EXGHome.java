package fr.snipertvmc.essentialsxgui.infrastructure.models;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;

public class EXGHome {


	// -------------------------------------------------- //


	private final String name;

	private String displayName;
	private XMaterial material;
	private byte data;

	private ItemStack customItemStack;


	// -------------------------------------------------- //


	public EXGHome(String name) {
		this.name = name;
		this.displayName = name;
		this.material = XMaterial.GRASS_BLOCK;
		this.data = 0;
	}


	// -------------------------------------------------- //


	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}
	public XMaterial getMaterial() {
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

	public void setMaterial(XMaterial material) {
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
