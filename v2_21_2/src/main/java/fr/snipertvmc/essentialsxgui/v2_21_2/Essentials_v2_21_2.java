package fr.snipertvmc.essentialsxgui.v2_21_2;

import com.earth2me.essentials.Kit;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.craftbukkit.Inventories;
import com.earth2me.essentials.utils.AdventureUtil;
import com.earth2me.essentials.utils.NumberUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;
import fr.snipertvmc.essentialsxgui.managers.EssentialsManager;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import net.ess3.api.TranslatableException;
import net.ess3.api.events.UserBalanceUpdateEvent;
import net.ess3.provider.SerializationProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import static com.earth2me.essentials.I18n.tlLiteral;

public class Essentials_v2_21_2 implements EssentialsManager {


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
					parsed = serializationProvider.deserializeItem(Base64.getDecoder().decode(base64));

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

		final String[] args = new String[]{"all"};
		final String commandLabel = "sell";

		if (!user.isAuthorized("essentials.sell.bulk")) {
			throw new TranslatableException("sellBulkPermission");
		}

		int count = 0;

		final List<ItemStack> notSold = new ArrayList<>();
		final List<ItemStack> itemsToProcess = new ArrayList<>(items);

		for (ItemStack stack : itemsToProcess) {
			if (stack == null || stack.getType() == Material.AIR) {
				continue;
			}

			if (!essentials.getSettings().isAllowSellNamedItems()) {
				if (stack.getItemMeta() != null && stack.getItemMeta().hasDisplayName()) {
					notSold.add(stack);
					continue;
				}
			}

			try {
				if (stack.getAmount() > 0) {
					BigDecimal price = customSellItem(user, stack);

					totalWorth = totalWorth.add(price);
					ItemStack soldStack = stack.clone();
					count++;

					for (final ItemStack zeroStack : itemsToProcess) {
						if (zeroStack != null && zeroStack.isSimilar(soldStack)) {
							zeroStack.setAmount(0);
						}
					}
				}
			} catch (final Exception e) {
				notSold.add(stack);
			}
		}

		if (!notSold.isEmpty()) {
			final List<String> names = new ArrayList<>();
			for (final ItemStack stack : notSold) {
				if (stack.getItemMeta() != null && stack.getItemMeta().hasDisplayName()) {
					names.add(stack.getItemMeta().getDisplayName());
				}
			}
			if (!names.isEmpty()) {
				essentials.showError(user.getSource(), new TranslatableException("cannotSellTheseNamedItems", String.join(ChatColor.RESET + ", ", names)), commandLabel);
			}
		}

		if (count != 0) {
			final AdventureUtil.ParsedPlaceholder totalWorthStr = AdventureUtil.parsed(NumberUtil.displayCurrency(totalWorth, essentials));
			user.sendTl("totalWorthAll", totalWorthStr, totalWorthStr);
		}

		return notSold;
	}


	public BigDecimal customSellItem(final User user, final ItemStack is) throws Exception {
		final BigDecimal originalWorth = essentials.getWorth().getPrice(essentials, is);
		final BigDecimal worth = originalWorth == null ? null : originalWorth.multiply(essentials.getSettings().getMultiplier(user));

		if (worth == null) return BigDecimal.ZERO;

		final int amount = is.getAmount();
		if (amount <= 0) return BigDecimal.ZERO;

		final BigDecimal result = worth.multiply(BigDecimal.valueOf(amount));
		final ItemStack ris = is.clone();

		Trade.log("Command", "Sell", "Item", user.getName(),
				new com.earth2me.essentials.Trade(ris, essentials), user.getName(),
				new com.earth2me.essentials.Trade(result, essentials), user.getLocation(), user.getMoney(), essentials);

		user.giveMoney(result, null, UserBalanceUpdateEvent.Cause.COMMAND_SELL);

		final String typeName = is.getType().toString().toLowerCase(Locale.ENGLISH);
		final AdventureUtil.ParsedPlaceholder worthDisplay = AdventureUtil.parsed(NumberUtil.displayCurrency(worth, essentials));

		user.sendTl("itemSold", AdventureUtil.parsed(NumberUtil.displayCurrency(result, essentials)), amount, typeName, worthDisplay);

		essentials.getLogger().log(Level.INFO,
				AdventureUtil.miniToLegacy(tlLiteral("itemSoldConsole", user.getName(), typeName,
						AdventureUtil.miniToLegacy(NumberUtil.displayCurrency(result, essentials)), amount,
						AdventureUtil.miniToLegacy(worthDisplay.toString()), user.getDisplayName())));
		return result;
	}


	// -------------------------------------------------- //
}
