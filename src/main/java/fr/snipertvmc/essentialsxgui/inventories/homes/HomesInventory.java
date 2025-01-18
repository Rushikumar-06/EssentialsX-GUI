package fr.snipertvmc.essentialsxgui.inventories.homes;

import fr.mrmicky.fastinv.InventoryScheme;
import fr.mrmicky.fastinv.ItemBuilder;
import fr.mrmicky.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class HomesInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private static final InventoryScheme inventoryScheme = new InventoryScheme()
			.mask("         ")
			.mask("         ")
			.mask("  11111  ")
			.mask("  11111  ")
			.mask("         ")
			.mask("         ")
			.bindPagination('1');


	// -------------------------------------------------- //


	public HomesInventory(Player player) {
		super(54, player.getName() + "'s homes");

		setItems(getBorders(), new ItemBuilder(Material.STAINED_GLASS_PANE).data(15).name(" ").build());

		previousPageItem(47, p -> new ItemBuilder(Material.STONE_BUTTON).name("§bPrevious Page §7§o(" + p + "/" + lastPage() + ")").build());
		nextPageItem(51, p -> new ItemBuilder(Material.STONE_BUTTON).name("§bNext Page §7§o(" + p + "/" + lastPage() + ")").build());


		Set<EXGHome> homes = Main.getInstance().getPlayerManager().getPlayer(player.getUniqueId().toString()).getHomes()
				.stream()
				.sorted(Comparator.comparing(EXGHome::getName))
				.collect(Collectors.toCollection(LinkedHashSet::new));

		for (EXGHome home : homes) {

			String displayName = home.getDisplayName();
			Material material = home.getMaterial();

			List<String> homeLore = new ArrayList<>();
			homeLore.add("§7Right Click to teleport to this home.");
			homeLore.add("§7Left Click to edit this home.");

			if (!displayName.equals(home.getName())) {
				homeLore.add("§7§oId: §e§o" + home.getName());
				displayName = "§r" + displayName;
			} else {
				displayName = "§f§o" + displayName;
			}

			addContent(new ItemBuilder(material).name(displayName).lore(homeLore).build(), e -> {

				if (e.getClick().isLeftClick()) {
					player.performCommand("essentials:home " + home.getName());

				} else if (e.getClick().isRightClick()) {
					new HomeEditingInventory(player, home).open(player);
				}

			});
		}


		setItem(49, new ItemBuilder(Material.BARRIER).name("§cClose").build(), e -> {
			e.getWhoClicked().closeInventory();
		});


		inventoryScheme.apply(this);
	}


	// -------------------------------------------------- //


	@Override
	protected void onPageChange(int page) {
		setItem(4, new ItemBuilder(Material.PAPER).name("§fCurrent page: §e" + page).build());
	}


	// -------------------------------------------------- //
}
