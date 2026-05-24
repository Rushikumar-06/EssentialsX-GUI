package fr.snipertvmc.essentialsxgui.v2_22_0;

import com.earth2me.essentials.Kit;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.User;
import com.earth2me.essentials.adventure.AdventureUtil;
import com.earth2me.essentials.craftbukkit.Inventories;
import com.earth2me.essentials.utils.NumberUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;
import fr.snipertvmc.essentialsxgui.managers.EssentialsManager;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import net.ess3.api.TranslatableException;
import net.ess3.api.events.UserBalanceUpdateEvent;
import net.ess3.provider.SerializationProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

public class Essentials_v2_22_0 implements EssentialsManager {


	// -------------------------------------------------- //


	public boolean canCreateHome(Player player) {
		int homeLimit = essentials.getSettings().getHomeLimit(essentials.getUser(player));
		EXGPlayer exgPlayer = Main.getInstance().getPlayerManager().getPlayer(player);

		if (player.hasPermission("essentials.sethome.multiple.unlimited")) {
			return true;
		}

		return homeLimit > exgPlayer.getHomes().size();
	}


	// -------------------------------------------------- //


	public void createKitWithPlayer(Player player, String kitName, long delay) {

		final ItemStack[] items = Inventories.getInventory(essentials.getUser(player).getBase(), true);
		final List<String> list = new ArrayList<>();

		final SerializationProvider serializationProvider = essentials.provider(SerializationProvider.class);
		final boolean useBetterKits = essentials.getSettings().isUseBetterKits() && serializationProvider != null;

		for (int i = 0; i < items.length; i++) {

			final ItemStack is = items[i];
			if (is == null || is.getType() == Material.AIR) {
				continue;
			}

			final String serialized;
			if (useBetterKits) {
				// Format Base64 (BetterKits / SerializationProvider)
				serialized = "slot:" + i + " @" + Base64Coder.encodeLines(serializationProvider.serializeItem(is));
			} else {
				// EssentialsX Classic Text Format
				serialized = "slot:" + i + " " + essentials.getItemDb().serialize(is);
			}
			list.add(serialized);
		}

		essentials.getKits().addKit(kitName, list, delay);
	}


	public List<ItemStack> getKitItems(String kitName) {

		final List<String> rawItems;
		try {
			final Kit kit = new Kit(kitName, essentials);
			rawItems = kit.getItems();

		}  catch (Exception e) {
			ConsoleLogger.warn("Unable to load the kit '" + kitName + "': " + e.getMessage());
			return new ArrayList<>();
		}

		final List<ItemStack> result = new ArrayList<>();
		final SerializationProvider serializationProvider = essentials.provider(SerializationProvider.class);

		for (final String rawItem : rawItems) {

			// Ignore any currency amounts (e.g., "$10")
			if (rawItem.startsWith(essentials.getSettings().getCurrencySymbol())) {
				continue;
			}

			try {
				// Remove the "slot:N" prefix if it is present
				final String itemStr = rawItem.replaceFirst("^slot:\\d+\\s+", "");
				ItemStack parsed;

				if (itemStr.startsWith("@") && serializationProvider != null) {
					// Désérialisation Base64 (BetterKits)
					final String base64 = itemStr.substring(1).trim();
					parsed = serializationProvider.deserializeItem(Base64Coder.decodeLines(base64));

				} else {
					// Standard text format: "diamond_sword 1 ..."
					final String[] parts = itemStr.split(" +");
					final int amount = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
					final ItemStack baseStack = essentials.getItemDb().get(parts[0], amount);

					final MetaItemStack metaStack = new MetaItemStack(baseStack);
					if (parts.length > 2) {
						metaStack.parseStringMeta(null, true, parts, 2, essentials);
					}
					parsed = metaStack.getItemStack();
				}

				if (parsed != null && parsed.getType() != Material.AIR) {
					result.add(parsed);
				}

			} catch (Exception e) {
				// We log in and keep going so we don't get stuck on a malformed item
				ConsoleLogger.warn("Unable to parse the kit item '" + kitName + "': " + rawItem + " — " + e.getMessage());
			}
		}

		return result;
	}


	// -------------------------------------------------- //


	public void createWarpWithPlayer(Player player, String warpName) throws Exception {
		essentials.getWarps().setWarp(warpName, player.getLocation());
	}


	// -------------------------------------------------- //


	public List<ItemStack> sellItems(Player player, List<ItemStack> items) throws Exception {
		BigDecimal totalWorth = BigDecimal.ZERO;
		final User user = essentials.getUser(player);
		final String commandLabel = "sell";

		if (!user.isAuthorized("essentials.sell.bulk")) {
			throw new TranslatableException("sellBulkPermission");
		}

		int count = 0;

		final List<ItemStack> notSold = new ArrayList<>();

		// On clone la liste pour pouvoir modifier les quantités (setAmount(0)) sans détruire les originaux
		final List<ItemStack> itemsToProcess = new ArrayList<>();
		for (ItemStack item : items) {
			if (item != null && item.getType() != Material.AIR) {
				itemsToProcess.add(item.clone());
			}
		}

		for (ItemStack stack : itemsToProcess) {
			if (stack.getAmount() <= 0) {
				continue;
			}

			if (!essentials.getSettings().isAllowSellNamedItems()) {
				if (stack.getItemMeta() != null && stack.getItemMeta().hasDisplayName()) {
					notSold.add(stack);
					continue;
				}
			}

			try {
				// Appel de notre méthode personnalisée compatible avec ta liste
				BigDecimal price = customSellItem(user, stack);

				if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
					totalWorth = totalWorth.add(price);
					count++;

					// On met à 0 les items similaires pour ne pas les vendre deux fois
					for (final ItemStack zeroStack : itemsToProcess) {
						if (zeroStack != null && zeroStack.isSimilar(stack)) {
							zeroStack.setAmount(0);
						}
					}
				} else {
					notSold.add(stack);
				}
			} catch (final Exception e) {
				notSold.add(stack);
			}
		}

		// Affichage des erreurs pour les items nommés restants
		if (!notSold.isEmpty()) {
			final List<String> names = new ArrayList<>();
			for (final ItemStack stack : notSold) {
				if (stack.getItemMeta() != null && stack.getItemMeta().hasDisplayName()) {
					names.add(stack.getItemMeta().getDisplayName());
				}
			}
			if (!names.isEmpty()) {
				essentials.showError(user.getSource(), new TranslatableException("cannotSellTheseNamedItems", String.join(org.bukkit.ChatColor.RESET + ", ", names)), commandLabel);
			}
		}

		// Message global de fin si au moins une vente a réussi
		if (count > 0) {
			final AdventureUtil.ParsedPlaceholder totalWorthStr = AdventureUtil.parsed(NumberUtil.displayCurrency(totalWorth, essentials));
			user.sendTl("totalWorthAll", totalWorthStr, totalWorthStr);
		}

		return notSold;
	}


	public BigDecimal customSellItem(final User user, final ItemStack is) throws Exception {
		// 1. Récupération du prix de base
		final BigDecimal originalWorth = essentials.getWorth().getPrice(essentials, is);

		// 2. Application du Multiplicateur de l'utilisateur (Prend en compte ses permissions de multiplicateurs)
		final BigDecimal worth = originalWorth == null ? null : originalWorth.multiply(essentials.getSettings().getMultiplier(user));

		if (worth == null) {
			return BigDecimal.ZERO; // Pas de valeur définie
		}

		final int amount = is.getAmount();
		if (amount <= 0) {
			return BigDecimal.ZERO;
		}

		// 3. Calcul du montant final (Prix avec multiplicateur * quantité)
		final BigDecimal result = worth.multiply(BigDecimal.valueOf(amount));

		// 4. Log Interne EssentialsX & Économie
		// On simule l'item vendu pour les logs sans toucher à l'inventaire réel de Spigot
		final ItemStack ris = is.clone();

		com.earth2me.essentials.Trade.log("Command", "Sell", "Item", user.getName(),
				new com.earth2me.essentials.Trade(ris, essentials), user.getName(),
				new com.earth2me.essentials.Trade(result, essentials), user.getLocation(), user.getMoney(), essentials);

		// On donne l'argent avec la cause d'EssentialsX
		user.giveMoney(result, null, UserBalanceUpdateEvent.Cause.COMMAND_SELL);

		// 5. Envoi des messages d'EssentialsX au joueur et dans la console (comme la commande de base)
		final String typeName = is.getType().toString().toLowerCase(Locale.ENGLISH);
		final AdventureUtil.ParsedPlaceholder worthDisplay = AdventureUtil.parsed(NumberUtil.displayCurrency(worth, essentials));

		user.sendTl("itemSold", AdventureUtil.parsed(NumberUtil.displayCurrency(result, essentials)), amount, typeName, worthDisplay);

		essentials.getLogger().log(Level.INFO, essentials.getAdventureFacet().miniToLegacy(
				com.earth2me.essentials.I18n.tlLiteral("itemSoldConsole", user.getName(), typeName,
						essentials.getAdventureFacet().miniToLegacy(NumberUtil.displayCurrency(result, essentials)),
						amount, essentials.getAdventureFacet().miniToLegacy(worthDisplay.toString()), user.getDisplayName())
		));

		return result;
	}


	// -------------------------------------------------- //
}
