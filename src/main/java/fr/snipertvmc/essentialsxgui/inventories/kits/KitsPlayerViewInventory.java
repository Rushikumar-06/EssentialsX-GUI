package fr.snipertvmc.essentialsxgui.inventories.kits;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.EXGKitsPlayerViewInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.DataEntryUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class KitsPlayerViewInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final EXGKitsPlayerViewInventoryConfig config = Main.getInstance().getInventoriesManager().getKitsPlayerInventoryConfig().copy();


	// -------------------------------------------------- //


	public KitsPlayerViewInventory(Player player, String kitSearch, Set<EXGKit> definedKits) {
		super(
				Main.getInstance().getInventoriesManager().getKitsPlayerInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getKitsPlayerInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(
								Map.of("player", player.getName()))
						.getTitle()
		);


		Main.getInstance().getServerDataManager().updateServerKits();

		Set<EXGKit> kits = kitSearch != null ? definedKits :

				Main.getInstance().getEXGServer().getKits()
						.stream()
						.filter(kit -> player.hasPermission("essentials.kits." + kit.getName()))
						.sorted(Comparator.comparing(EXGKit::getName))
						.collect(Collectors.toCollection(LinkedHashSet::new));


		initializeGeneralInventory(player);
		defineKitsItems(player, kitSearch, kits);
		defineSwitchToAdminModeItem(player);
		defineSearchKitItem(player, kitSearch, kits);
	}


	// -------------------------------------------------- //


	private void defineKitsItems(Player player, String kitSearch, Set<EXGKit> kits) {

		for (EXGKit kit : kits) {

			EXGItemConfig kitItem = config.getKitItem().duplicate();
			kitItem.setMaterial(kit.getMaterial().name());
			kitItem.setData(kit.getData());

			ItemStack kitItemStack;

			if (kit.getCustomItemStack() != null) {
				kitItemStack = kit.getCustomItemStack().clone();
				ItemMeta meta = kitItemStack.getItemMeta();

				meta.setDisplayName(kitItem.getDisplayName()
						.replace("{kitDisplayName}", kit.getDisplayName())
						.replace("{kitName}", kit.getName())
						.replace("&", "§"));

				meta.setLore(kitItem.getLore().stream()
						.map(line -> line
								.replace("{kitDisplayName}", kit.getDisplayName())
								.replace("{kitName}", kit.getName())
								.replace("&", "§"))
						.collect(Collectors.toList()));

				kitItemStack.setItemMeta(meta);

			} else {
				kitItemStack = kitItem
						.updateVariables(
								Map.of("kitDisplayName", kit.getDisplayName(),
										"kitName", kit.getName()))
						.build();
			}

			addContent(kitItemStack, e -> {

				if (kitItem.isCorrectClick(e.getClick(), "receiveKit")) {
					player.performCommand("essentials:kit " + kit.getName());
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);

				} else if (kitItem.isCorrectClick(e.getClick(), "previewKit")) {
					new KitPreviewInventory(player, kit).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				}

			});
		}

		if (kits.isEmpty()) {

			if (kitSearch == null) {
				addContent(config.getNoKitsItem().build());

			} else {
				addContent(config.getNoSearchKitResultsItem()
						.updateVariables(
								Map.of("kitSearch", kitSearch))
						.build());
			}
		}
	}


	private void defineSwitchToAdminModeItem(Player player) {

		if (config.getSwitchToAdminModeItem().isEnabled() && Main.getInstance().getConfiguration().hasKitsAdminAccess(player)) {
			setItem(config.getSwitchToAdminModeItem().getSlot(), config.getSwitchToAdminModeItem().build(), e -> {

				new KitsAdminViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}
	}


	private void defineSearchKitItem(Player player, String kitSearch, Set<EXGKit> kits) {

		if (kitSearch == null) {
			if (config.getSearchKitItem().isEnabled() && !kits.isEmpty()) {
				setItem(config.getSearchKitItem().getSlot(), config.getSearchKitItem()
						.build(), e -> {

					searchKit(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}

		} else {
			if (config.getCancelSearchKitItem().isEnabled()) {
				setItem(config.getCancelSearchKitItem().getSlot(), config.getCancelSearchKitItem()
						.build(), e -> {

					new KitsPlayerViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}
		}
	}


	private void initializeGeneralInventory(Player player) {

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


		if (config.getCloseItem().isEnabled()) {
			setItem(config.getCloseItem().getSlot(), config.getCloseItem().build(), e -> {

				e.getWhoClicked().closeInventory();
				SoundsUtils.playSound(player, EXGSound.GUI_CLOSE);
			});
		}


		config.getInventoryScheme().apply(this);
	}


	// -------------------------------------------------- //


	private void searchKit(Player player) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getFilesManager().getConfiguration().getEntryType("kits", "searchKitEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			player.sendMessage(MessagesUtils.get(EXGMessage.SEARCH_KIT_CHAT, null));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.get(EXGMessage.SEARCH_KIT, null))
				.setMinLength(1)
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					Set<EXGKit> searchKits = Main.getInstance().getEXGServer().getKits()
							.stream()
							.filter(kit -> player.hasPermission("essentials.kits." + kit.getName()))
							.filter(kit -> MessagesUtils.removeColorCodes(kit.getDisplayName()).toLowerCase().contains(result.getLeft().toLowerCase()) ||
									MessagesUtils.removeColorCodes(kit.getName()).toLowerCase().contains(result.getLeft().toLowerCase()))
							.sorted(Comparator.comparing(EXGKit::getName))
							.collect(Collectors.toCollection(LinkedHashSet::new));

					if (searchKits.isEmpty()) {
						player.sendMessage(MessagesUtils.get(EXGMessage.NO_KIT_FOUND, null));
						new KitsPlayerViewInventory(player, result.getLeft(), searchKits).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					new KitsPlayerViewInventory(player, result.getLeft(), searchKits).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new KitsPlayerViewInventory(player, null, null).open(player)
		);
	}


	// -------------------------------------------------- //


	@Override
	protected void onPageChange(int page) {

		setItem(config.getCurrentPageItem().getSlot(), config.getCurrentPageItem()
				.updateVariables(
						Map.of("currentPage", String.valueOf(this.currentPage()),
								"totalPages", String.valueOf(this.lastPage()),
								"previousPage", String.valueOf(this.currentPage() - 1),
								"nextPage", String.valueOf(this.currentPage() + 1)))
				.build());

		Player player = this.getInventory().getViewers().isEmpty() ? null : (Player) this.getInventory().getViewers().get(0);
		SoundsUtils.playSound(player, EXGSound.GUI_PAGE_CHANGE);
	}


	// -------------------------------------------------- //
}
