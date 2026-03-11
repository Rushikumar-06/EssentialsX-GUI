package fr.snipertvmc.essentialsxgui.inventories.kits;

import com.cryptomorin.xseries.XItemFlag;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayerInventoryData;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitEditorInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.data.InventoryBackupUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class KitEditorInventory extends FastInv {


	// -------------------------------------------------- //


	private final EXGKitEditorInventoryConfig config = Main.getInstance().getInventoriesManager().getKitEditorInventoryConfig().copy();


	// -------------------------------------------------- //


	public KitEditorInventory(Player player, EXGKit kit) {
		super(
				Main.getInstance().getInventoriesManager().getKitEditorInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getKitEditorInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(
								Map.of("player", player.getName(),
										"kitName", kit.getName(),
										"kitDisplayName", kit.getDisplayName()))
						.getTitle()
		);


		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem()
					.setItemFlags(List.of(XItemFlag.HIDE_UNBREAKABLE))
					.build(), e -> e.setCancelled(true));
		}


		if (config.getSaveKitItem().isEnabled()) {
			setItem(config.getSaveKitItem().getSlot(), config.getSaveKitItem()
					.setItemFlags(List.of(XItemFlag.HIDE_UNBREAKABLE))
					.build(), e -> {

				e.setCancelled(true);

				ItemStack[] kitItems = new ItemStack[0];

				for (ItemStack itemStack : e.getInventory().getContents()) {
					if (itemStack != null && itemStack.getItemMeta() != null && !itemStack.getItemMeta().hasItemFlag(ItemFlag.HIDE_UNBREAKABLE)) {
						kitItems = Arrays.copyOf(kitItems, kitItems.length + 1);
						kitItems[kitItems.length - 1] = itemStack;
					}
				}

				saveKit(player, kit, kitItems);
			});
		}


		if (config.getCancelChangesItem().isEnabled()) {
			setItem(config.getCancelChangesItem().getSlot(), config.getCancelChangesItem()
					.setItemFlags(List.of(XItemFlag.HIDE_UNBREAKABLE))
					.build(), e -> {

				e.setCancelled(true);

				new KitEditingInventory(player, kit).open(player);
				SoundsUtils.playSound(player, EXGSound.ACTION_CANCELED);
			});
		}


		List<ItemStack> kitItems = Main.getInstance().getHookManager().getEssentialsHook().getKitItems(player, kit.getName());

		for (ItemStack item : kitItems) {
			if (item != null && item.getType() != Material.AIR) {
				addItem(item, e -> e.setCancelled(false));
			}
		}
	}


	// -------------------------------------------------- //


	private void saveKit(Player player, EXGKit kit, ItemStack... kitItems) {

		long delay = ((Number) Main.getInstance().getEssentials().getKits().getKit(kit.getName()).get("delay")).longValue();

		EXGPlayerInventoryData inventoryBackup = InventoryBackupUtils.createPlayerInventoryBackup(player);
		player.getInventory().clear();

		player.getInventory().setContents(kitItems);
		Main.getInstance().getHookManager().getEssentialsHook().createKitWithPlayer(player, kit.getName(), delay);

		InventoryBackupUtils.loadPlayerInventoryBackup(player, inventoryBackup);

		new KitEditingInventory(player, kit).open(player);
		SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
	}


	// -------------------------------------------------- //


	@Override
	public void onClick(InventoryClickEvent event) {
		event.setCancelled(false);
	}


	// -------------------------------------------------- //
}
