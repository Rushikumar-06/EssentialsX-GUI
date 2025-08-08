package fr.snipertvmc.essentialsxgui.hooks;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.craftbukkit.Inventories;
import com.earth2me.essentials.libs.snakeyaml.external.biz.base64Coder.Base64Coder;
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


	public boolean canCreateHome(Player player) {
		int homeLimit = essentials.getSettings().getHomeLimit(essentials.getUser(player));
		EXGPlayer exgPlayer = Main.getInstance().getPlayerManager().getPlayer(player.getUniqueId().toString());

		if (player.hasPermission("essentials.sethome.multiple.unlimited")) {
			return true;
		}

		return homeLimit > exgPlayer.getHomes().size();
	}


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


	// -------------------------------------------------- //
}
