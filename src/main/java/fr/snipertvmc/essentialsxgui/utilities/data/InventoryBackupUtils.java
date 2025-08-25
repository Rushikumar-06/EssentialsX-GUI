package fr.snipertvmc.essentialsxgui.utilities.data;

import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayerInventoryData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryBackupUtils {


	// -------------------------------------------------- //


	public static EXGPlayerInventoryData createPlayerInventoryBackup(Player player) {

		Map<Integer, ItemStack> inventoryContents = new HashMap<>();

		for (int i = 0; i < player.getInventory().getSize(); i++) {
			ItemStack item = player.getInventory().getItem(i);
			if (item != null) {
				inventoryContents.put(i, item.clone());
			}
		}

		return new EXGPlayerInventoryData(player.getName(), inventoryContents);
    }


	// -------------------------------------------------- //


	public static void loadPlayerInventoryBackup(Player player, EXGPlayerInventoryData inventoryData) {

		player.getInventory().clear();

		for (Map.Entry<Integer, ItemStack> entry : inventoryData.getContents().entrySet()) {
            player.getInventory().setItem(entry.getKey(), entry.getValue());
        }
	}


	// -------------------------------------------------- //
}
