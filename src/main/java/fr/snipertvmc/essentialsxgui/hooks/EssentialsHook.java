package fr.snipertvmc.essentialsxgui.hooks;

import com.earth2me.essentials.Essentials;
import fr.snipertvmc.essentialsxgui.Main;
import org.bukkit.plugin.Plugin;

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
}
