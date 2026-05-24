package fr.snipertvmc.essentialsxgui.infrastructure.models;

import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EXGPlayerInventoryData {


	// -------------------------------------------------- //


	private String ownerName;
	private Map<Integer, ItemStack> contents;


	// -------------------------------------------------- //


	public EXGPlayerInventoryData(String ownerName, Map<Integer, ItemStack> contents) {
		this.ownerName = ownerName;
		this.contents = contents;
	}


	// -------------------------------------------------- //


	public String getOwnerName() {
        return ownerName;
    }

	public Map<Integer, ItemStack> getContents() {
        return contents;
    }


	// -------------------------------------------------- //
}
