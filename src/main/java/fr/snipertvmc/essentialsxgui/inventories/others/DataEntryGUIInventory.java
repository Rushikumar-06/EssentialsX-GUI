package fr.snipertvmc.essentialsxgui.inventories.others;

import com.cryptomorin.xseries.XMaterial;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryResult;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.others.EXGDataEntryGUInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.ItemBuilder;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.data.DataEntryUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DataEntryGUIInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGDataEntryGUInventoryConfig config = Main.getInstance().getInventoriesManager().getDataEntryGUIInventoryConfig().copy();


	// -------------------------------------------------- //


	public DataEntryGUIInventory(Player player, EXGEntrySettings entrySettings,
	                             Consumer<Pair<String, EXGEntryResult>> onSuccess,
	                             Consumer<Pair<String, EXGEntryResult>> onFailure) {
		super(
				Main.getInstance().getInventoriesManager().getDataEntryGUIInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getDataEntryGUIInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(
								Map.of("entryDisplayName", entrySettings.getEntryDisplayName()))
						.getTitle()
		);


		addMaterialItems(player, entrySettings, onSuccess, onFailure);
		initializeInventory(player, onFailure);
	}


	// -------------------------------------------------- //


	private void addMaterialItems(Player player, EXGEntrySettings entrySettings,
	                              Consumer<Pair<String, EXGEntryResult>> onSuccess,
	                              Consumer<Pair<String, EXGEntryResult>> onFailure) {


		List<Pair<XMaterial, Byte>> materialList = Main.getInstance().getConfiguration().getMaterialsList(entrySettings.getMaterialListPath());

		if (materialList.isEmpty()) {

			addContent(new ItemBuilder(XMaterial.BARRIER)
					.name("§4§lNo materials found")
					.lore(
							"§cPlease contact an administrator and inform them of the following details:",
							"",
							"§7No materials found in the config file for the entry type §f" + entrySettings.getType().name() + "§7.",
							"§7Path: §f" + entrySettings.getMaterialListPath())
					.build()
			);
			return;
		}

		for (Pair<XMaterial, Byte> materialPair : materialList) {

			EXGItemConfig materialIconItem = config.getMaterialIconItem().duplicate();
			materialIconItem.setMaterial(materialPair.getLeft().name());
			materialIconItem.setData(materialPair.getRight());

			addContent(materialIconItem
					.updateVariables(Map.of(
							"materialName", materialPair.getLeft().name()
					))
					.build(), e -> {

				String completeMaterial = materialPair.getLeft() + ":" + materialPair.getRight();
				Pair<Pair<XMaterial, Byte>, EXGEntryResult> result = DataEntryUtils.checkMaterialEntry(completeMaterial);

				if (result.getRight() == EXGEntryResult.SUCCESS) {
					onSuccess.accept(new Pair<>(completeMaterial, EXGEntryResult.SUCCESS));

				} else {
					onFailure.accept(new Pair<>(completeMaterial, result.getRight()));
					result.getRight().playResult(player);
				}
			});
		}
	}


	private void initializeInventory(Player player, Consumer<Pair<String, EXGEntryResult>> onFailure) {

		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem().build());
		}

		previousPageItem(config.getPreviousPageItem().getSlot(), p -> config.getPreviousPageItem().duplicate()
				.updateVariables(
						Map.of("currentPage", String.valueOf(p + 1),
								"previousPage", String.valueOf(p)))
				.build());


		nextPageItem(config.getNextPageItem().getSlot(), p -> config.getNextPageItem().duplicate()
				.updateVariables(
						Map.of("currentPage", String.valueOf(p - 1),
								"nextPage", String.valueOf(p)))
				.build());


		if (config.getCancelItem().isEnabled()) {
			setItem(config.getCancelItem().getSlot(), config.getCancelItem().build(), e -> {

				onFailure.accept(new Pair<>(null, EXGEntryResult.CANCELED));
				EXGEntryResult.CANCELED.playResult(player);
			});
		}


		config.getInventoryScheme().apply(this);
	}


	// -------------------------------------------------- //


	@Override
	protected void onPageChange(int page) {

		setItem(config.getCurrentPageItem().getSlot(), config.getCurrentPageItem().duplicate()
				.updateVariables(
						Map.of("currentPage", String.valueOf(page),
								"totalPages", String.valueOf(this.lastPage()),
								"previousPage", String.valueOf(page - 1),
								"nextPage", String.valueOf(page + 1)))
				.build());

		if (this.getInventory().getViewers().isEmpty()) {
			return;
		}

		Player player = this.getInventory().getViewers().isEmpty() ? null : (Player) this.getInventory().getViewers().get(0);
		SoundsUtils.playSound(player, EXGSound.GUI_PAGE_CHANGE);
	}


	// -------------------------------------------------- //
}
