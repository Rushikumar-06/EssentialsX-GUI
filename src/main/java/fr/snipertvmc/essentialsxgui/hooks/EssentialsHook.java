package fr.snipertvmc.essentialsxgui.hooks;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.craftbukkit.Inventories;
import com.earth2me.essentials.libs.snakeyaml.external.biz.base64Coder.Base64Coder;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.KeywordReplacer;
import com.earth2me.essentials.textreader.SimpleTextInput;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;
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

		return isVersionGreaterOrEqual(cleanVersion, minimumVersionRequired);
	}


	private boolean isVersionGreaterOrEqual(String current, String minimum) {
		String[] currentParts = current.split("\\.");
		String[] minimumParts = minimum.split("\\.");

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
		EXGPlayer exgPlayer = Main.getInstance().getPlayerManager().getPlayer(player.getUniqueId().toString());

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
		boolean useSerializationProvider = essentials.getSettings().isUseBetterKits();

		for (ItemStack is : items) {
			if (is != null && is.getType() != null && is.getType() != Material.AIR) {
				final String serialized;
				if (useSerializationProvider) {
					serialized = "@" + Base64Coder.encodeLines(serializationProvider.serializeItem(is));
				} else {
					serialized = essentials.getItemDb().serialize(is);
				}
				list.add(serialized);
			}
		}

		essentials.getKits().addKit(kitName, list, delay);
	}


	public List<ItemStack> getKitItems(Player player, String kitName) {

		final List<String> itemStringList = new ArrayList<>();
		try {
			final Object kitItems = essentials.getKits().getKit(kitName).get("items");
			if (kitItems instanceof List) {
				for (final Object item : (List) kitItems) {
					if (item instanceof String) {
						itemStringList.add(item.toString());
					}
				}
			}
		} catch (Exception e) {
			return new ArrayList<>();
		}

		final List<ItemStack> itemList = new ArrayList<>();
		final IText input = new SimpleTextInput(itemStringList);
		final IText output = new KeywordReplacer(input, essentials.getUser(player).getSource(), essentials, true, true);
		final boolean allowUnsafe = essentials.getSettings().allowUnsafeEnchantments();
		final SerializationProvider serializationProvider = essentials.provider(SerializationProvider.class);

		for (final String kitItem : output.getLines()) {
			// Ignore money and commands
			if (kitItem.startsWith("$") || kitItem.startsWith(essentials.getSettings().getCurrencySymbol())) {
				continue;
			}
			if (kitItem.startsWith("/")) {
				continue;
			}

			final ItemStack stack;

			if (kitItem.startsWith("@")) {
				stack = serializationProvider.deserializeItem(Base64Coder.decodeLines(kitItem.substring(1)));
			} else {
				final String[] parts = kitItem.split(" +");
				final ItemStack parseStack;
				try {
					parseStack = essentials.getItemDb().get(parts[0], parts.length > 1 ? Integer.parseInt(parts[1]) : 1);
				} catch (Exception ignored) {
					continue;
				}

				if (parseStack.getType() == Material.AIR) {
					continue;
				}

				final MetaItemStack metaStack = new MetaItemStack(parseStack);
				if (parts.length > 2) {
					try {
						metaStack.parseStringMeta(null, allowUnsafe, parts, 2, essentials);
					} catch (Exception ignored) {
						continue;
					}
				}
				stack = metaStack.getItemStack();
			}

			itemList.add(stack);
		}

		return itemList;
	}


	// -------------------------------------------------- //
}
