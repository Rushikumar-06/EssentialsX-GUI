package fr.snipertvmc.essentialsxgui.hooks;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Kit;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.craftbukkit.Inventories;
import com.earth2me.essentials.libs.snakeyaml.external.biz.base64Coder.Base64Coder;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import net.ess3.provider.SerializationProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class EssentialsHook {


	// -------------------------------------------------- //


	private Essentials essentials;


	public Essentials getEssentials() {

		if (essentials != null) {
			return essentials;
		}

		Plugin essentialsPlugin = Main.getInstance().getServer().getPluginManager().getPlugin("Essentials");

		if (essentialsPlugin instanceof Essentials) {
			return essentials = (Essentials) essentialsPlugin;

		} else {
			return null;
		}
	}


	// -------------------------------------------------- //


	private final String minimumVersionRequired = "2.21.2";


	public String getMinimumVersionRequired() {
		return minimumVersionRequired;
	}


	public boolean isEssentialsVersionSupported() {

		String version = essentials.getDescription().getVersion();
		String cleanVersion = version.split("-")[0];

		return isVersionGreaterOrEqual(cleanVersion);
	}


	private boolean isVersionGreaterOrEqual(String current) {
		String[] currentParts = current.split("\\.");
		String[] minimumParts = minimumVersionRequired.split("\\.");

		int length = Math.max(currentParts.length, minimumParts.length);

		for (int i = 0; i < length; i++) {
			int cur = (i < currentParts.length) ? Integer.parseInt(currentParts[i]) : 0;
			int min = (i < minimumParts.length) ? Integer.parseInt(minimumParts[i]) : 0;

			if (cur > min) return true;
			if (cur < min) return false;
		}
		return true;
	}


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
}
